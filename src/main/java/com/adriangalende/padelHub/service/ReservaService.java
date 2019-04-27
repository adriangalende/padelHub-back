package com.adriangalende.padelHub.service;


import com.adriangalende.padelHub.converter.ReservaConverter;
import com.adriangalende.padelHub.entity.ReservaEntity;
import com.adriangalende.padelHub.model.Pista;
import com.adriangalende.padelHub.model.Reserva;
import com.adriangalende.padelHub.model.RespuestaDisponibilidadPista;
import com.adriangalende.padelHub.repository.ReservaRepository;
import com.adriangalende.padelHub.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("servicio_reserva")
public class ReservaService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("repositorio_reserva")
    private ReservaRepository repository;

    @Autowired
    @Qualifier("servicio_pista")
    PistaService pistaService;

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
    private JSONObject validarPeticionBusquedaReserva(Reserva reserva){

        if(reserva.getHoraInicio() == null || reserva.getHoraFin() == null){
            return Utils.jsonResponseSetter(false, "Las horas de inicio y final no son correctas");
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
    public List<RespuestaDisponibilidadPista> buscar(Reserva reserva) throws JSONException {
        JSONObject jsonResponse = new JSONObject();

            List<ReservaEntity> listaDisponibilidadPistasReserva = new ArrayList<>();

            reserva.setFlexibilidad(controlFlexibilidad(reserva.getFlexibilidad()));

            Date horaInicio = (Date) reserva.getHoraInicio().clone();

        /**
         *    tantas peticiones como flexibilidad se haya elegido:
         *    i = 0 -> Sin flexibilidad, queremos pista únicamente a esa hora
         *    i > 0 -> flexibilidad = i -> +1 hora, +2 horas.
         */
            for(int i=0 ; i <= reserva.getFlexibilidad() ; i++){
                Calendar nuevaHoraInicio = Calendar.getInstance();
                nuevaHoraInicio.setTime(horaInicio);
                nuevaHoraInicio.add(Calendar.HOUR_OF_DAY, i);
                reserva.setHoraInicio(nuevaHoraInicio.getTime());

                reserva = prepararPeticionBusquedaReserva(reserva);

                //Primero validamos si los datos de entrada son correctos
                jsonResponse = validarPeticionBusquedaReserva(reserva);

                if(jsonResponse.getBoolean("success")){
                    //Reservas encontradas con los parámetros de búsqueda.
                    List<ReservaEntity> reservasEntity = repository.getAllBetweenDates(reserva.getHoraInicio(), reserva.getHoraFin());
                    List<Reserva> reservasEncontradas = converter.convertirLista(reservasEntity);
                    List<Pista> listaPistas = pistaService.obtenerPistas();
                    List<Pista> listaPistasFiltradas = new ArrayList<>();

                    listaPistasFiltradas.addAll(filtrarListaPistas(reservasEntity, listaPistas));
                    if(listaPistasFiltradas.size() == 0){
                        listaPistasFiltradas.addAll(listaPistas);
                    }

                    listaDisponibilidadPistasReserva.addAll(generarListaPistasDisponiblesReserva(reserva, listaPistasFiltradas));

                }

            }

        if(listaDisponibilidadPistasReserva != null){
            return converter.convertirListaRespuestaDispo(listaDisponibilidadPistasReserva);
        }

        return null;
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
            //TODO modificar precio reserva según la id de la pista
            reservaAuxiliar.setPrecio(0.0);
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

}
