package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.ReservaEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reserva {
    private int id;
    private int idUsuario;
    private int idClub;
    private int idPista;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss", timezone = "+02:00")
    private Date horaInicio;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private Date horaFin;
    private String fecha;
    private int idTipoReserva;
    private Double precio;
    private int duracion;
    private int flexibilidad;
    private int checkin;
    private int noShow;
    private String nombreClub;
    private String descripcion;

    public Reserva() {
    }

    public Reserva(ReservaEntity reservaEntity) {
        this.id = reservaEntity.getId();
        this.descripcion = reservaEntity.getDescripcion();
        this.idUsuario = reservaEntity.getIdUsuario();
        this.idClub = reservaEntity.getIdClub();
        this.idPista = reservaEntity.getIdPista();
        this.horaInicio = reservaEntity.getHoraInicio();
        this.horaFin = reservaEntity.getHoraFin();
        this.fecha = reservaEntity.getFecha();
        this.idTipoReserva = reservaEntity.getIdTipoReserva();
        this.precio = reservaEntity.getPrecio();
        this.duracion = reservaEntity.getDuracion();
        this.checkin = reservaEntity.getCheckIn();
        this.noShow = reservaEntity.getNoShow();
        this.nombreClub = reservaEntity.getClubByIdClub() != null ? reservaEntity.getClubByIdClub().getNombre() : null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public int getIdPista() {
        return idPista;
    }

    public void setIdPista(int idPista) {
        this.idPista = idPista;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getIdTipoReserva() {
        return idTipoReserva;
    }

    public void setIdTipoReserva(int idTipoReserva) {
        this.idTipoReserva = idTipoReserva;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getFlexibilidad() {
        return flexibilidad;
    }

    public void setFlexibilidad(int flexibilidad) {
        this.flexibilidad = flexibilidad;
    }

    public int getCheckin() {
        return checkin;
    }

    public void setCheckin(int checkin) {
        this.checkin = checkin;
    }

    public int getNoShow() {
        return noShow;
    }

    public void setNoShow(int noShow) {
        this.noShow = noShow;
    }

    public String getNombreClub() {
        return nombreClub;
    }

    public void setNombreClub(String nombreClub) {
        this.nombreClub = nombreClub;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
