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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("servicio_reserva")
public class ReservaService {

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

            List<ReservaEntity> listaPistasFiltradas = new ArrayList<>();


            reserva = prepararPeticionBusquedaReserva(reserva);

            //Primero validamos si los datos de entrada son correctos
            jsonResponse = validarPeticionBusquedaReserva(reserva);

            if(jsonResponse.getBoolean("success")){

                List<ReservaEntity> reservasEntity = repository.getAllBetweenDates(reserva.getHoraInicio(), reserva.getHoraFin());
                List<Reserva> reservasEncontradas = converter.convertirLista(reservasEntity);
                List<Pista> listaPistas = pistaService.obtenerPistas();


                if(reservasEncontradas.size() > 0){
                    List<ReservaEntity> finalListaPistasFiltradas = listaPistasFiltradas;
                    listaPistas.forEach(pista -> {
                        finalListaPistasFiltradas.add(pistaReservada(reservasEntity, pista.getId()));
                    });
                }

                if(listaPistasFiltradas != null){
                    return converter.convertirListaRespuestaDispo(listaPistasFiltradas);
                }

            }

        return null;
    }

    private ReservaEntity pistaReservada(List<ReservaEntity> pistasReservadas, int idPista){
        for(ReservaEntity reserva:pistasReservadas){
            if(idPista == reserva.getIdPista()){
                return reserva;
            }
        }
        return null;
    }

}
