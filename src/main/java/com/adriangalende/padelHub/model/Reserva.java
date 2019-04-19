package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.ReservaEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reserva {
    private int id;
    private int idUsuario;
    private int idClub;
    private int idPista;
    private String horaInicio;
    private String horaFin;
    private String fecha;
    private int idTipoReserva;
    private Double precio;

    public Reserva() {
    }

    public Reserva(ReservaEntity reservaEntity) {
        this.id = reservaEntity.getId();
        this.idUsuario = reservaEntity.getIdUsuario();
        this.idClub = reservaEntity.getIdClub();
        this.idPista = reservaEntity.getIdPista();
        this.horaInicio = reservaEntity.getHoraInicio();
        this.horaFin = reservaEntity.getHoraFin();
        this.fecha = reservaEntity.getFecha();
        this.idTipoReserva = reservaEntity.getIdTipoReserva();
        this.precio = reservaEntity.getPrecio();
    }
}
