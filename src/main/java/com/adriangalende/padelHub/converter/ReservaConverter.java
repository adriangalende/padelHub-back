package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.ReservaEntity;
import com.adriangalende.padelHub.model.Reserva;
import com.adriangalende.padelHub.model.ReservaCompleta;
import com.adriangalende.padelHub.model.RespuestaDisponibilidadPista;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("convertidor_reserva")
public class ReservaConverter {

    public Reserva convertirReservaModelo(ReservaEntity reservaEntity){ return new Reserva(reservaEntity);}

    public ReservaCompleta convertirReservaCompletaModelo(ReservaEntity reservaEntity){ return new ReservaCompleta(reservaEntity);}

    public ReservaEntity convertirReservaEntity(Reserva reserva){ return new ReservaEntity(reserva);}

    public List<Reserva> convertirLista(List<ReservaEntity> reservaEntityList){
        List<Reserva> listaReservas = new ArrayList<>();
        reservaEntityList.forEach( reserva -> listaReservas.add(new Reserva(reserva)) );
        return listaReservas;
    }

    public List<ReservaCompleta> convertirListaReservaCompleta(List<ReservaEntity> reservaEntityList){
        List<ReservaCompleta> listaReservas = new ArrayList<>();
        reservaEntityList.forEach( reserva -> listaReservas.add(new ReservaCompleta(reserva)) );
        return listaReservas;
    }

    public List<RespuestaDisponibilidadPista> convertirListaRespuestaDispo(List<ReservaEntity> reservaEntities){
        List<RespuestaDisponibilidadPista> listaReservas = new ArrayList<>();
        reservaEntities.forEach( reserva -> {
            if(reserva != null) {
                RespuestaDisponibilidadPista respuestaDisponibilidadPista = new RespuestaDisponibilidadPista();
                respuestaDisponibilidadPista.setIdPista(String.valueOf(reserva.getIdPista()));
                respuestaDisponibilidadPista.setNombre(reserva.getPistaByIdPista().getNombre());
                respuestaDisponibilidadPista.setClub(reserva.getPistaByIdPista().getClubByIdClub().getNombre());
                respuestaDisponibilidadPista.setIdClub(String.valueOf(reserva.getIdClub()));
                respuestaDisponibilidadPista.setTipoPista(reserva.getPistaByIdPista().getTiposPistaByIdTipoPista().getDescripcion());
                respuestaDisponibilidadPista.setRutaImagenes("");
                respuestaDisponibilidadPista.setPrecio(reserva.getPrecio().toString());
                respuestaDisponibilidadPista.setPrecioLuz("");
                respuestaDisponibilidadPista.setHoraInicio(reserva.getHoraInicio().toString());
                respuestaDisponibilidadPista.setDuracion(String.valueOf(reserva.getDuracion()));
                respuestaDisponibilidadPista.setDescripcion(reserva.getDescripcion());
                listaReservas.add(respuestaDisponibilidadPista);
            }

        } );
        return listaReservas;
    }

}
