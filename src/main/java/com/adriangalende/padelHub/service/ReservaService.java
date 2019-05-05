package com.adriangalende.padelHub.service;


import com.adriangalende.padelHub.converter.ReservaConverter;
import com.adriangalende.padelHub.entity.ReservaEntity;
import com.adriangalende.padelHub.model.*;
import com.adriangalende.padelHub.repository.ReservaRepository;
import com.adriangalende.padelHub.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


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
    @Qualifier("convertidor_reserva")
    private ReservaConverter converter;



    /**
     * Comprobamos que:
     * - no vengan elementos necesarios vacíos:
     *  + idClub
     *  + fechaInicio
     *  + fechaFin
     *
     * @param reserva
     * @return
     */
    private JSONObject validarPeticionBusquedaReserva(Reserva reserva, boolean initial){

        if((reserva.getHoraInicio() == null || reserva.getHoraFin() == null) && !initial){
            return Utils.jsonResponseSetter(false, "Las horas de inicio y final no son correctas");
        } else if (reserva.getHoraInicio() == null && initial){
            return Utils.jsonResponseSetter(false, "La hora de inicio no puede enviarse vacía");
        }


        int hora = reserva.getHoraInicio().getHours();
        int minutos = reserva.getHoraInicio().getMinutes();

        if( (hora < 9 && minutos >= 0)  || ( hora >= 22 && minutos > 0 ||  (hora > 22 && minutos >= 0))){
            return Utils.jsonResponseSetter(false, "Las horas de reserva son de 9:00 a 22:00");
        }

        if(reserva.getDuracion() == 0){
            return Utils.jsonResponseSetter(false, "La duración de la partida no puede enviarse vacía");
        }


        return Utils.jsonResponseSetter(true, "OK");
    }

    /**
     * La hora de finalización debe coincidir con la hora de inicio + la duración de la partida
     * que por norma general suelen ser 90 minutos, pero podrían ser 60 minutos o incluso 120 minutos.
     * @param reserva
     * @return
     */
    private Reserva prepararPeticionBusquedaReserva(Reserva reserva){

        Date horaFin = (Date) reserva.getHoraInicio().clone();
        horaFin.setMinutes(horaFin.getMinutes()+reserva.getDuracion());
        reserva.setHoraFin(horaFin);

        return reserva;
    }

    /**
     * Buscamos en la base de datos si hay reservas que coincidan con los parámetros
     * que hemos elegido :
     *
     *  Fecha para la partida
     *  Hora inicio
     *  duración partida
     *
     *
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
    private int controlFlexibilidad(int flexibilidad){
        if(flexibilidad < 0){
            return 0;
        } else if (flexibilidad > 2){
            return 2;
        } else {
            return flexibilidad;
        }
    }

    /**
     * Comparamos la pista en cada una de las reservas encontradas con TODAS las ids de pistas que
     * tenemos en la base de datos.
     *
     * Si no encontramos coincidencia, añadimos una reserva con la id de pista ( de la tabla pista ), porque significa
     * que esa pista está disponible para reservar.
     *
     * @param reservaEntities
     * @param pistaList
     * @return
     */
    private List<Pista> filtrarListaPistas(List<ReservaEntity> reservaEntities, List<Pista> pistaList){

        List<Pista> resultadoPistasDisponibles = new ArrayList<>();

        for(Pista pista : pistaList){
            if(!pistaReservada(reservaEntities, pista.getId())){
                resultadoPistasDisponibles.add(pista);
            }
        }

        return resultadoPistasDisponibles;
    }

    /**
     *
     * @param reserva
     * @param listaPistas
     * @return
     */
    private List<ReservaEntity> generarListaPistasDisponiblesReserva(Reserva reserva, List<Pista> listaPistas){
        List<ReservaEntity> pistasDisponiblesReserva = new ArrayList<>();

        for(Pista pista:listaPistas){

            ReservaEntity reservaAuxiliar = new ReservaEntity();
            BeanUtils.copyProperties(reserva, reservaAuxiliar);

            reservaAuxiliar.setIdPista(pista.getId());
            reservaAuxiliar.setPistaByIdPista(pistaService.datosPista(pista.getId()).get());

            Usuarios usuario = usuariosService.obtenerUsuario(reserva.getIdUsuario());
            Precios precio = preciosService.obtenerPrecios(pista.getId(), usuario.getIdTiposUsuario(), reservaAuxiliar.getHoraInicio());
            if(precio.getSuplementoLuz() == null) {
                precio.setPrecio(0.0);
                precio.setSuplementoLuz(0.0);
            }

            reservaAuxiliar.setPrecio(precio.getPrecio() + precio.getSuplementoLuz());

            pistasDisponiblesReserva.add(reservaAuxiliar);


        }

        return pistasDisponiblesReserva;
    }


    /**
     * Busca la id de la pista en la lista de reservas
     * @param reservaEntities
     * @param idPista
     * @return boolean
     */
    private boolean pistaReservada(List<ReservaEntity> reservaEntities, int idPista){

        for(ReservaEntity reservaEntity:reservaEntities){
            if(idPista == reservaEntity.getIdPista()){
                return true;
            }
        }
        return false;
    }


    /**
     * Método que reserva la pista del usuario
     * 1. Comprobar que los datos que obtenemos de la reserva son correctos
     * 2. Comprobar que ESA PISTA no está reservada en esa franja horaria
     * 3. Mostrar resumen de la reserva
     * @param reserva
     * @return jsonObject {"success":ture/false, "message":String/Object}
     *
     */
    public JSONObject reservar(Reserva reserva) throws JSONException {
        //Aprovechamos la función que hicimos para validar los datos en la búsqueda de pistas
        JSONObject jsonObject = validarPeticionBusquedaReserva(reserva, true);

        //Hemos comprobado si la hora de reserva y la duración son correctas
        if(jsonObject.getBoolean("success")){

            //Le asignamos la hora de finalización basándonos en la hora de inicio y la duración de la partida
            reserva = prepararPeticionBusquedaReserva(reserva);

            //comprobar idPista e idUsuario viene informado
            jsonObject = prepararPeticionReserva(reserva);

            //No llega el tipo de reserva informado, así que le asignamos el valor por defecto
            if(StringUtils.equalsAnyIgnoreCase("DEFAULT_TIPORESERVA", jsonObject.getString("message"))){
                reserva.setIdTipoReserva(Utils.DEFAULT_TIPO_RESERVA);
            }

            //Volvemos a comprobar el precio por si ha habido algún cambio inesperado
            reserva = controlPrecio(reserva);

            if(jsonObject.getBoolean("success")&& repository.recuperarReserva(reserva.getIdPista(), reserva.getHoraInicio(), reserva.getHoraFin()).size() == 0){
                try{
                    //Añadimos la fecha de la reserva
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE));
                    Date hoy = Calendar.getInstance(TimeZone.getTimeZone(Utils.DEFAULT_TIMEZONE)).getTime();
                    reserva.setFecha(dateFormat.format(hoy));

                    ReservaEntity reservaEntity = converter.convertirReservaEntity(reserva);

                    reservaEntity = repository.saveAndFlush(reservaEntity);
                    jsonObject.put("message", converter.convertirReservaModelo(reservaEntity));

                    return  jsonObject;
                }catch (Exception e){
                 LOGGER.error("Ha ocurrido un error al intentar guardar la reserva", e);
                 return Utils.jsonResponseSetter(false,"No se ha podido realizar la reserva");
                }
            } else {
                return Utils.jsonResponseSetter(false, "Ups! parece que alguien se te ha adelantado!");
            }
        }

        return  jsonObject;
    }

    /**
     * Comprobamos que el precio de la pista corresponde al que está
     * en la base de datos
     * @param reserva
     * @return
     */
    private Reserva controlPrecio(Reserva reserva) {
        Usuarios usuario = usuariosService.obtenerUsuario(reserva.getIdUsuario());
        Precios precio = preciosService.obtenerPrecios(reserva.getIdPista(), usuario.getIdTiposUsuario(), reserva.getHoraInicio());

        //TODO CUANDO LA RESERVA SEA UN BLOQUEO IGNORAR EL CÁLCULO DE PRECIO
        if(reserva.getIdTipoReserva() != 3) {
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
        if(reserva.getIdClub() == 0){
           return Utils.jsonResponseSetter(false,"La id del club debe venir informada");
        }

        if(reserva.getIdUsuario() == 0){
            return Utils.jsonResponseSetter(false, "La id de usuario debe venir informada");
        }

        if(reserva.getIdTipoReserva() == 0){
            return Utils.jsonResponseSetter(true,"DEFAULT_TIPORESERVA");
        }

        return Utils.jsonResponseSetter(true,"OK");

    }

}
