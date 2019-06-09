package com.adriangalende.padelHub.service;


import com.adriangalende.padelHub.converter.ReservaConverter;
import com.adriangalende.padelHub.converter.TiposReservaConverter;
import com.adriangalende.padelHub.entity.ReservaEntity;
import com.adriangalende.padelHub.entity.UsuariosEntity;
import com.adriangalende.padelHub.model.*;
import com.adriangalende.padelHub.repository.ClubRepository;
import com.adriangalende.padelHub.repository.ReservaRepository;
import com.adriangalende.padelHub.security.JwtValidator;
import com.adriangalende.padelHub.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;


import javax.rmi.CORBA.Util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("servicio_reserva")
public class ReservaService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("repositorio_reserva")
    private ReservaRepository repository;

    @Autowired
    @Qualifier("servicio_precios")
    private PreciosService preciosService;

    @Autowired
    @Qualifier("servicio_pista")
    private PistaService pistaService;

    @Autowired
    @Qualifier("servicio_usuarios")
    private UsuariosService usuariosService;

    @Autowired
    @Qualifier("servicio_club")
    private ClubService clubService;

    @Autowired
    @Qualifier("convertidor_reserva")
    private ReservaConverter converter;

    @Autowired
    @Qualifier("servicio_TiposReserva")
    private TiposReservaService tiposReservaService;


    /**
     * Comprobamos que:
     * - no vengan elementos necesarios vacíos:
     * + idClub
     * + fechaInicio
     * + fechaFin
     *
     * @param reserva
     * @return
     */
    private JSONObject validarPeticionBusquedaReserva(Reserva reserva, boolean initial) {

        if ((reserva.getHoraInicio() == null || reserva.getHoraFin() == null) && !initial) {
            return Utils.jsonResponseSetter(false, "Las horas de inicio y final no son correctas");
        } else if (reserva.getHoraInicio() == null && initial) {
            return Utils.jsonResponseSetter(false, "La hora de inicio no puede enviarse vacía");
        }


        int hora = reserva.getHoraInicio().getHours();
        int minutos = reserva.getHoraInicio().getMinutes();

        if ((hora < 9 && minutos >= 0) || (hora >= 22 && minutos > 0 || (hora > 22 && minutos >= 0))) {
            return Utils.jsonResponseSetter(false, "Las horas de reserva son de 9:00 a 22:00");
        }

        //Si la fecha de reserva es inferior a ahora
        if (reserva.getHoraInicio().before(new Date())) {
            return Utils.jsonResponseSetter(false, "No puedes reservar pistas en fechas pasadas");
        }

        if (reserva.getDuracion() == 0) {
            return Utils.jsonResponseSetter(false, "La duración de la partida no puede enviarse vacía");
        }


        return Utils.jsonResponseSetter(true, "OK");
    }

    /**
     * La hora de finalización debe coincidir con la hora de inicio + la duración de la partida
     * que por norma general suelen ser 90 minutos, pero podrían ser 60 minutos o incluso 120 minutos.
     *
     * @param reserva
     * @return
     */
    private Reserva prepararPeticionBusquedaReserva(Reserva reserva) {

        Date horaFin = (Date) reserva.getHoraInicio().clone();
        horaFin.setMinutes(horaFin.getMinutes() + reserva.getDuracion());
        reserva.setHoraFin(horaFin);

        //Si no tenemos información del usuario le asignamos el usuario por defecto
        if (reserva.getIdUsuario() == 0) {
            reserva.setIdUsuario(1);
        }

        return reserva;
    }

    /**
     * Buscamos en la base de datos si hay reservas que coincidan con los parámetros
     * que hemos elegido :
     * <p>
     * Fecha para la partida
     * Hora inicio
     * duración partida
     *
     * @param reserva
     * @return
     */
    public JSONObject buscar(Reserva reserva) throws JSONException {
        JSONObject jsonResponse = new JSONObject();

        List<ReservaEntity> listaDisponibilidadPistasReserva = new ArrayList<>();

        reserva.setFlexibilidad(controlFlexibilidad(reserva.getFlexibilidad()));

        jsonResponse = validarPeticionBusquedaReserva(reserva, true);

        Date horaInicio = null;
        if (reserva.getHoraInicio() != null) {
            horaInicio = (Date) reserva.getHoraInicio().clone();
        }

        if (horaInicio != null && jsonResponse.getBoolean("success")) {
            /**
             *    tantas peticiones como flexibilidad se haya elegido:
             *    i = 0 -> Sin flexibilidad, queremos pista únicamente a esa hora
             *    i > 0 -> flexibilidad = i -> +1 hora, +2 horas.
             */
            for (int i = 0; i <= reserva.getFlexibilidad(); i++) {
                Calendar nuevaHoraInicio = Calendar.getInstance(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE));
                nuevaHoraInicio.setTime(horaInicio);
                nuevaHoraInicio.add(Calendar.HOUR_OF_DAY, i);
                reserva.setHoraInicio(nuevaHoraInicio.getTime());

                reserva = prepararPeticionBusquedaReserva(reserva);

                //Primero validamos si los datos de entrada son correctos
                jsonResponse = validarPeticionBusquedaReserva(reserva, false);

                if (jsonResponse.getBoolean("success")) {
                    //Reservas encontradas con los parámetros de búsqueda.
                    List<ReservaEntity> reservasEntity = repository.getAllBetweenDates(reserva.getHoraInicio(), reserva.getHoraFin());
                    List<Reserva> reservasEncontradas = converter.convertirLista(reservasEntity);
                    List<Pista> listaPistas = pistaService.obtenerPistas();
                    List<Pista> listaPistasFiltradas = new ArrayList<>();

                    listaPistasFiltradas.addAll(filtrarListaPistas(reservasEntity, listaPistas));
                    if (listaPistasFiltradas.size() == 0) {
                        listaPistasFiltradas.addAll(listaPistas);
                    }

                    listaDisponibilidadPistasReserva.addAll(generarListaPistasDisponiblesReserva(reserva, listaPistasFiltradas));

                }

            }

            if (listaDisponibilidadPistasReserva != null) {
                jsonResponse.put("message", converter.convertirListaRespuestaDispo(listaDisponibilidadPistasReserva));
                return jsonResponse;
            } else {
                return Utils.jsonResponseSetter(false, "No hay pistas disponibles para tu búsqueda");
            }
        }

        return jsonResponse;
    }

    /**
     * Ajustamos flexibilidad por si llega un parámetro no deseado.
     * la flexibilidad horaria permitida es únicamente +1 o +2
     *
     * @param flexibilidad
     * @return
     */
    private int controlFlexibilidad(int flexibilidad) {
        if (flexibilidad < 0) {
            return 0;
        } else if (flexibilidad > 2) {
            return 2;
        } else {
            return flexibilidad;
        }
    }

    /**
     * Comparamos la pista en cada una de las reservas encontradas con TODAS las ids de pistas que
     * tenemos en la base de datos.
     * <p>
     * Si no encontramos coincidencia, añadimos una reserva con la id de pista ( de la tabla pista ), porque significa
     * que esa pista está disponible para reservar.
     *
     * @param reservaEntities
     * @param pistaList
     * @return
     */
    private List<Pista> filtrarListaPistas(List<ReservaEntity> reservaEntities, List<Pista> pistaList) {

        List<Pista> resultadoPistasDisponibles = new ArrayList<>();

        for (Pista pista : pistaList) {
            if (!pistaReservada(reservaEntities, pista.getId())) {
                resultadoPistasDisponibles.add(pista);
            }
        }

        return resultadoPistasDisponibles;
    }

    /**
     * @param reserva
     * @param listaPistas
     * @return
     */
    private List<ReservaEntity> generarListaPistasDisponiblesReserva(Reserva reserva, List<Pista> listaPistas) {
        List<ReservaEntity> pistasDisponiblesReserva = new ArrayList<>();

        for (Pista pista : listaPistas) {

            ReservaEntity reservaAuxiliar = new ReservaEntity();
            BeanUtils.copyProperties(reserva, reservaAuxiliar);

            reservaAuxiliar.setIdPista(pista.getId());
            reservaAuxiliar.setPistaByIdPista(pistaService.datosPista(pista.getId()).get());

            Usuarios usuario = usuariosService.obtenerUsuario(reserva.getIdUsuario());
            Precios precio = preciosService.obtenerPrecios(pista.getId(), usuario.getIdTiposUsuario(), reservaAuxiliar.getHoraInicio());
            if (precio.getSuplementoLuz() == null) {
                precio.setPrecio(0.0);
                precio.setSuplementoLuz(0.0);
            }

            reservaAuxiliar.setPrecio(precio.getPrecio() + precio.getSuplementoLuz());
            reservaAuxiliar.setIdClub(pista.getIdClub());
            pistasDisponiblesReserva.add(reservaAuxiliar);


        }

        return pistasDisponiblesReserva;
    }


    /**
     * Busca la id de la pista en la lista de reservas
     *
     * @param reservaEntities
     * @param idPista
     * @return boolean
     */
    private boolean pistaReservada(List<ReservaEntity> reservaEntities, int idPista) {

        for (ReservaEntity reservaEntity : reservaEntities) {
            if (idPista == reservaEntity.getIdPista()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprobamos que el precio de la pista corresponde al que está
     * en la base de datos
     *
     * @param reserva
     * @return
     */
    private Reserva controlPrecio(Reserva reserva) {

        Usuarios usuario = usuariosService.obtenerUsuario(reserva.getIdUsuario());
        int tipoUsuario;
        if (usuario.getIdTiposUsuario() == 1 || usuario.getIdTiposUsuario() == 2) {
            tipoUsuario = 1;
        } else {
            tipoUsuario = usuario.getIdTiposUsuario();
        }

        Precios precio = preciosService.obtenerPrecios(reserva.getIdPista(), tipoUsuario, reserva.getHoraInicio());

        //Tenemos que obtener la id que tiene el club para el tipoReserva bloquea, si tiene.
        JSONObject tiposReservaResponse = tiposReservaService.obtenerTiposReservaClub(reserva.getIdClub());
        final int[] idBloqueoClub = {0};

        try {
            if (tiposReservaResponse.get("success") != null && (boolean) tiposReservaResponse.get("success")) {
                List<TiposReserva> tiposReservaList = (List<TiposReserva>) tiposReservaResponse.get("message");

                tiposReservaList.forEach(tiposReserva -> {
                    if (StringUtils.equalsIgnoreCase("bloqueo", tiposReserva.getDescripcion())) {
                        idBloqueoClub[0] = tiposReserva.getId();
                    }
                });

            }
        } catch (JSONException e) {
            LOGGER.error("No se ha podido obtener el contenido del JSONObject tiposReservaResponse ", e);
        }

        //CUANDO LA RESERVA SEA UN BLOQUEO IGNORAR EL CÁLCULO DE PRECIO
        if (reserva.getIdTipoReserva() != idBloqueoClub[0]) {
            if (precio.getSuplementoLuz() == null) {
                precio.setSuplementoLuz(0.0);
            }
            //El precio y el suplemento de luz son por hora
            Double horas = (reserva.getDuracion() / 60.0);
            Double precioFinal = (precio.getPrecio() + precio.getSuplementoLuz()) * horas;
            reserva.setPrecio(precioFinal);
        }


        return reserva;
    }

    /**
     * Comprobamos que varios parámetros lleguen informados porque son necesarios
     * para realizar la reserva correctamente.
     */
    private JSONObject prepararPeticionReserva(Reserva reserva) {

        if (reserva.getIdClub() == 0) {
            return Utils.jsonResponseSetter(false, "La id del club debe venir informada");
        }

        if (reserva.getIdUsuario() == 0) {
            return Utils.jsonResponseSetter(false, "La id de usuario debe venir informada");
        }

        if (reserva.getIdTipoReserva() == 0) {
            return Utils.jsonResponseSetter(true, "DEFAULT_TIPORESERVA");
        }

        return Utils.jsonResponseSetter(true, "OK");

    }


    /**
     * Método que reserva la pista del usuario (Para test)
     * 1. Comprobar que los datos que obtenemos de la reserva son correctos
     * 2. Comprobar que ESA PISTA no está reservada en esa franja horaria
     * 3. Mostrar resumen de la reserva
     *
     * @param reserva
     * @return jsonObject {"success":ture/false, "message":String/Object}
     */
    public JSONObject reservar(Reserva reserva) throws JSONException {
        //Aprovechamos la función que hicimos para validar los datos en la búsqueda de pistas
        JSONObject jsonObject = validarPeticionBusquedaReserva(reserva, true);


        //Hemos comprobado si la hora de reserva y la duración son correctas
        if (jsonObject.getBoolean("success")) {

            //Le asignamos la hora de finalización basándonos en la hora de inicio y la duración de la partida
            reserva = prepararPeticionBusquedaReserva(reserva);

            //comprobar idPista e idUsuario viene informado
            jsonObject = prepararPeticionReserva(reserva);

            //No llega el tipo de reserva informado, así que le asignamos el valor por defecto
            if (StringUtils.equalsAnyIgnoreCase("DEFAULT_TIPORESERVA", jsonObject.getString("message"))) {
                reserva.setIdTipoReserva(Utils.DEFAULT_TIPO_RESERVA);
            }

            //Volvemos a comprobar el precio por si ha habido algún cambio inesperado
            reserva = controlPrecio(reserva);

            if (jsonObject.getBoolean("success") && repository.recuperarReserva(reserva.getIdPista(), reserva.getHoraInicio(), reserva.getHoraFin()).size() == 0) {
                try {
                    //Añadimos la fecha de la reserva
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE));
                    Date hoy = Calendar.getInstance(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE)).getTime();
                    reserva.setFecha(dateFormat.format(hoy));

                    ReservaEntity reservaEntity = converter.convertirReservaEntity(reserva);

                    reservaEntity = repository.saveAndFlush(reservaEntity);
                    jsonObject.put("message", converter.convertirReservaModelo(reservaEntity));

                    return jsonObject;
                } catch (Exception e) {
                    LOGGER.error("Ha ocurrido un error al intentar guardar la reserva", e);
                    return Utils.jsonResponseSetter(false, "No se ha podido realizar la reserva");
                }
            } else {
                return Utils.jsonResponseSetter(false, "Ups! parece que alguien se te ha adelantado!");
            }
        }

        return jsonObject;
    }


    /**
     * Comprobamos si los datos que llegan por petición coinciden
     * con la información que nos llega desde el token JWT
     * Campos que comprobamos
     *
     * @param reserva
     * @param jwtUser
     * @return
     */
    private JSONObject controlUsuario(Reserva reserva, JwtUser jwtUser) {

        if (reserva.getIdUsuario() != jwtUser.getId()) {
            LOGGER.error("Se ha detectado una posible manipulación del token o de la id de usuario");
            return Utils.jsonResponseSetter(false, "No se ha podido realizar la reserva, contacta con el administrador");
        }

        return Utils.jsonResponseSetter(true, "OK");
    }

    /**
     * Método que reserva la pista del usuario (Mismo método pero con sobrecarga)
     * 1. Comprobar que los datos que obtenemos de la reserva son correctos
     * 2. Comprobar que ESA PISTA no está reservada en esa franja horaria
     * 3. Mostrar resumen de la reserva
     *
     * @param reserva
     * @param token
     * @return
     * @throws JSONException
     */
    public JSONObject reservar(Reserva reserva, String token) throws JSONException {
        //Aprovechamos la función que hicimos para validar los datos en la búsqueda de pistas
        JSONObject jsonObject = validarPeticionBusquedaReserva(reserva, true);

        if (!jsonObject.getBoolean("success")) {
            return jsonObject;
        }

        //Comprobamos que el usuario es quien dice ser
        token = token.split(" ")[1];
        JwtUser jwtUser = new JwtUser();
        if (StringUtils.isNotBlank(token)) {
            jwtUser = new JwtValidator().validate(token);
            if (jwtUser != null) {
                //Si no llega la id de usuario por la petición la añadimos cogiéndola del token
                if (reserva.getIdUsuario() == 0) {
                    reserva.setIdUsuario((int) (long) jwtUser.getId());
                }

                jsonObject = controlUsuario(reserva, jwtUser);
            } else {
                jsonObject = Utils.jsonResponseSetter(false, "No se ha podido realizar la reserva, contacta con el administrador");
            }
        }

        //Hemos comprobado si la hora de reserva y la duración son correctas
        if (jsonObject.getBoolean("success")) {

            //Le asignamos la hora de finalización basándonos en la hora de inicio y la duración de la partida
            reserva = prepararPeticionBusquedaReserva(reserva);

            //comprobar idPista e idUsuario viene informado
            jsonObject = prepararPeticionReserva(reserva);

            //No llega el tipo de reserva informado, así que le asignamos el valor por defecto
            if (StringUtils.equalsAnyIgnoreCase("DEFAULT_TIPORESERVA", jsonObject.getString("message"))) {
                reserva.setIdTipoReserva(Utils.DEFAULT_TIPO_RESERVA);
            }

            //Volvemos a comprobar el precio por si ha habido algún cambio inesperado
            reserva = controlPrecio(reserva);


            //TAG SI LA RESERVA ES UN BLOQUEO Y EL USUARIO ES DE TIPO CLUB PARA EL CLUB DONDE SE REALIA LA RESERVA
            //TAG ACTIVAR MODO DESTROYER!!!!!
            boolean esBloqueo = false;
            String tipoReserva = tiposReservaService.obtenerTipoReserva(reserva.getIdTipoReserva());

            if (tipoReserva != null) {
                if (StringUtils.equalsIgnoreCase("bloqueo", tipoReserva)) {
                    esBloqueo = true;

                    //limpiamos TODAS las reservas que se hayan hecho este día para
                    limpiarReservas(reserva.getIdClub(), reserva.getHoraInicio(), reserva.getIdPista());

                    //Ponemos las horas de inicio a las 09:00 y la hora de fin a las 22:00
                    reserva.setHoraInicio(modificarHoraReserva(reserva.getHoraInicio(), 9).getTime());
                    reserva.setHoraFin(modificarHoraReserva(reserva.getHoraFin(), 23).getTime());
                }
            }

            //Si es un bloqueo debería ignorar la sentencia anterior, aunque en realidad no habría reservas en esa pista para esa hora
            // en el club, ya que anteriormente las hemos borrado.
            if ((jsonObject.getBoolean("success") && repository.recuperarReserva(reserva.getIdPista(), reserva.getHoraInicio(), reserva.getHoraFin()).size() == 0) || esBloqueo) {
                try {
                    //Añadimos la fecha de la reserva
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE));
                    Date hoy = Calendar.getInstance(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE)).getTime();
                    reserva.setFecha(dateFormat.format(hoy));

                    if (reserva.getHoraInicio().before(hoy)) {
                        return Utils.jsonResponseSetter(false, "No se puede reservar una pista del pasado, a menos que llames al Dr. Emmett Brown ");
                    }


                    ReservaEntity reservaEntity = converter.convertirReservaEntity(reserva);

                    reservaEntity = repository.saveAndFlush(reservaEntity);
                    jsonObject.put("message", converter.convertirReservaModelo(reservaEntity));

                    return jsonObject;
                } catch (Exception e) {
                    LOGGER.error("Ha ocurrido un error al intentar guardar la reserva", e);
                    return Utils.jsonResponseSetter(false, "No se ha podido realizar la reserva");
                }
            } else {
                return Utils.jsonResponseSetter(false, "Ups! parece que alguien se te ha adelantado!");
            }
        }

        return jsonObject;
    }

    /**
     * Limpiamos todas las reservas (de una pista) que se hayan realizado en un club para una fecha determinada
     * Esto suele darse cuando hay algún torneo y el club bloquea las pistas para que los usuarios no puedan reservar
     *
     * @param idClub
     * @param horaInicio
     * @param idPista
     */
    private void limpiarReservas(int idClub, Date horaInicio, int idPista) {
        Optional<List<ReservaEntity>> listaReservas = repository.reservasClubFecha(idClub, horaInicio, idPista);

        if (listaReservas.isPresent()) {
            listaReservas.get().forEach(reserva -> {
                repository.delete(reserva);
            });
        }
    }

    /**
     * Método para cancelar pista
     * 1- Comprobamos que el usuario cumpla 1 de estos requisitos:
     * 1.1 - Tipousuario = club (admin)
     * 1.2 - Ser el mismo usuario que realizó la reserva.
     * <p>
     * 2- La reserva no puede haberse validado ( checkin, noshow )
     *
     * @param peticion
     * @return
     */
    public JSONObject cancelar(PeticionCancelarPista peticion, String token) throws JsonProcessingException {
        JSONObject jsonObject = null;
        Optional<ReservaEntity> reserva = repository.findById(peticion.getIdReserva());
        ReservaEntity reservaEntity = new ReservaEntity();

        //Obtenemos el usuario a partir del jwt token
        JwtUser jwtUser = new JwtValidator().validate(token.split(" ")[1]);

        if (jwtUser == null) {
            LOGGER.error("La información del token no es correcta, posible manipulación de los datos.");
            LOGGER.error("Token: " + token);
            LOGGER.error("peticion: " + new ObjectMapper().writeValueAsString(peticion));
            return Utils.jsonResponseSetter(false, "No se han podido comprobar las crecenciales del usuario");
        }

        Usuarios usuario = usuariosService.obtenerUsuario((int) (long) jwtUser.getId());
        if (usuario == null || usuario.getId() == 0) {
            return Utils.jsonResponseSetter(false, "El usuario no existe o no tiene permisos");
        }

        if (reserva.isPresent()) {
            reservaEntity = reserva.get();
            Reserva reservaAux = converter.convertirReservaModelo(reservaEntity);

            //Si la reserva que se quiere cancelar ya ha sido validada ( checkin = 1 o noshow = 1 )
            if (reservaEntity.getCheckIn() == 1 || reservaEntity.getNoShow() == 1) {
                return Utils.jsonResponseSetter(false, "No se pueden borrar reservas pasadas.");
            }

            /** Comprobamos que el usuario sea el mismo que realizó la reserva o su
             *  tipo_usuario sea club ( admin ) y la reserva esté hecha en ese club
             ***/

            if (usuario.getId() == reservaEntity.getIdUsuario() ||
                    (StringUtils.equalsIgnoreCase(usuariosService.obtenerTipoUsuario(usuario.getId()), "club") && usuario.getIdClub() == reservaEntity.getIdClub())) {
                repository.delete(reservaEntity);
                return Utils.jsonResponseSetter(true, "Se ha borrado la reserva correctamente: " + reservaAux.getId());

            } else {
                return Utils.jsonResponseSetter(false, "El usuario no puede cancelar la reserva");
            }
        }

        return Utils.jsonResponseSetter(false, "No se ha podido cancelar la reserva");
    }


    public JSONObject obtenerReservas(String token) {
        JSONObject jsonObject = new JSONObject();
        //Token = Bearer tokenString.
        JwtUser jwtUser = new JwtValidator().validate(token.split(" ")[1]);

        if (jwtUser == null) {
            return Utils.jsonResponseSetter(false, "No hemos podido recuperar tus reservas");
        }

        Optional<List<ReservaEntity>> listaReservas = repository.obtenerReservasUsuario((int) (long) jwtUser.getId());

        if (listaReservas.isPresent()) {
            try {
                jsonObject.put("success", true);
                jsonObject.put("message", converter.convertirLista(listaReservas.get()));
            } catch (JSONException e) {
                LOGGER.error("Error al agregar la lista de reservas en el objeto jsonObject", e);
                jsonObject = Utils.jsonResponseSetter(false, "No hemos podido recuperar tus reservas");
            }
        } else {
            return Utils.jsonResponseSetter(false, "Todavía no tienes reservas hechas");
        }

        return jsonObject;
    }


    public JSONObject obtenerReservasClub(String token) {
        JSONObject jsonObject = new JSONObject();
        //Token = Bearer tokenString.
        JwtUser jwtUser = new JwtValidator().validate(token.split(" ")[1]);

        if (jwtUser == null || StringUtils.equalsIgnoreCase(jwtUser.getRole(), "usuario")) {
            return Utils.jsonResponseSetter(false, "No hemos podido recuperar tus reservas");
        }

        Optional<List<ReservaEntity>> listaReservas = repository.obtenerReservasClub((int) jwtUser.getIdClub());

        if (listaReservas.isPresent()) {
            try {

                jsonObject.put("success", true);
                jsonObject.put("message", completarListaReserva(listaReservas.get()));
            } catch (JSONException e) {
                LOGGER.error("Error al agregar la lista de reservas en el objeto jsonObject", e);
                jsonObject = Utils.jsonResponseSetter(false, "No hemos podido recuperar tus reservas");
            }
        } else {
            return Utils.jsonResponseSetter(false, "Todavía no tienes reservas hechas");
        }

        return jsonObject;
    }


    public JSONObject obtenerTodasReservasClub(String token) {
        JSONObject jsonObject = new JSONObject();
        //Token = Bearer tokenString.
        JwtUser jwtUser = new JwtValidator().validate(token.split(" ")[1]);

        if (jwtUser == null || StringUtils.equalsIgnoreCase(jwtUser.getRole(), "usuario")) {
            return Utils.jsonResponseSetter(false, "No hemos podido recuperar tus reservas");
        }

        Optional<List<ReservaEntity>> listaReservas = repository.obtenerTodasReservasClub((int) jwtUser.getIdClub());

        if (listaReservas.isPresent()) {
            try {

                jsonObject.put("success", true);
                jsonObject.put("message", completarListaReserva(listaReservas.get()));
            } catch (JSONException e) {
                LOGGER.error("Error al agregar la lista de reservas en el objeto jsonObject", e);
                jsonObject = Utils.jsonResponseSetter(false, "No hemos podido recuperar tus reservas");
            }
        } else {
            return Utils.jsonResponseSetter(false, "Todavía no tienes reservas hechas");
        }

        return jsonObject;
    }

    private List<ReservaCompleta> completarListaReserva(List<ReservaEntity> listaReservasEntity) {
        List<ReservaCompleta> listaReservasCompleta = new ArrayList<>();

        for (ReservaEntity reservaEntity : listaReservasEntity) {

            ReservaCompleta reservaCompleta = converter.convertirReservaCompletaModelo(reservaEntity);

            //Obtenemos datos extra para completar
            Usuarios usuarioReserva = usuariosService.obtenerUsuario(reservaCompleta.getIdUsuario());
            reservaCompleta.setNombreUsuario(usuarioReserva.getNombre());
            reservaCompleta.setTelefonoUsuario(usuarioReserva.getTelefono());
            reservaCompleta.setTipoUsuario(usuariosService.obtenerTipoUsuario(usuarioReserva.getId()));
            reservaCompleta.setNombrePista(pistaService.datosPista(reservaCompleta.getIdPista()).get().getNombre());
            listaReservasCompleta.add(reservaCompleta);

        }

        return listaReservasCompleta;
    }

    /**
     * Modificamos la hora de una fecha poniendo la hora que deseemos.
     *
     * @param horaInicio
     * @param hora
     * @return
     */
    private Calendar modificarHoraReserva(Date horaInicio, int hora) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(horaInicio);
        calendar.set(Calendar.HOUR, hora);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar;
    }

    /**
     * Los usuarios se han presentado a la partida y han abonado la cantidad de la reserva
     * el checkIn solo lo podrá hacer:
     * -un usuario de tipo club
     * -que su club sea el mismo que la reserva
     *
     * @param peticion
     * @param token
     * @return
     */
    public JSONObject checkIn(PeticionCancelarPista peticion, String token) {

        JSONObject respuesta;

        //Obtenemos el usuario que corresponde al token
        JwtUser jwtUser = new JwtValidator().validate(token);
        Usuarios usuario = usuariosService.obtenerUsuario((int)jwtUser.getId());

        if ( usuario.getIdClub() != peticion.getIdClub() && !StringUtils.equalsIgnoreCase("club", jwtUser.getRole())) {
            return Utils.jsonResponseSetter(false, "No tienes permisos para realizar esta acción");
        } else { // Se puede realizar el checkIn

            //recuperamos la reserva
            Optional<ReservaEntity> reservaOptional = repository.findById(peticion.getIdReserva());

            if(reservaOptional.isPresent()){
                ReservaEntity reserva = reservaOptional.get();
                reserva.setCheckIn(1);
                repository.saveAndFlush(reserva);
                return Utils.jsonResponseSetter(true, "La operación de checkIn se ha realizado correctamente");

            } else {
                return Utils.jsonResponseSetter(false, "No hemos podido realizar la acción de checkIn en esta reserva");
            }

        }

    }

    public JSONObject noShow(PeticionCancelarPista peticion, String token) {
        JSONObject respuesta;

        //Obtenemos el usuario que corresponde al token
        JwtUser jwtUser = new JwtValidator().validate(token);
        Usuarios usuario = usuariosService.obtenerUsuario((int)jwtUser.getId());

        if ( usuario.getIdClub() != peticion.getIdClub() && !StringUtils.equalsIgnoreCase("club", jwtUser.getRole())) {
            return Utils.jsonResponseSetter(false, "No tienes permisos para realizar esta acción");
        } else { // Se puede realizar el checkIn

            //recuperamos la reserva
            Optional<ReservaEntity> reservaOptional = repository.findById(peticion.getIdReserva());

            if(reservaOptional.isPresent()){
                ReservaEntity reserva = reservaOptional.get();
                reserva.setNoShow(1);
                repository.saveAndFlush(reserva);
                return Utils.jsonResponseSetter(true, "La operación de noShow se ha realizado correctamente");

            } else {
                return Utils.jsonResponseSetter(false, "No hemos podido realizar la acción de noShow en esta reserva");
            }

        }
    }
}
