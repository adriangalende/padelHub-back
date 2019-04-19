package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reserva", schema = "padelhub", catalog = "")
public class ReservaEntity {
    private int id;
    private int idUsuario;
    private int idClub;
    private int idPista;
    private String horaInicio;
    private String horaFin;
    private String fecha;
    private int idTipoReserva;
    private Double precio;
    private UsuariosEntity usuariosByIdUsuario;
    private ClubEntity clubByIdClub;
    private PistaEntity pistaByIdPista;
    private TiposReservaEntity tiposReservaByIdTipoReserva;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "id_usuario")
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Basic
    @Column(name = "id_club")
    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    @Basic
    @Column(name = "id_pista")
    public int getIdPista() {
        return idPista;
    }

    public void setIdPista(int idPista) {
        this.idPista = idPista;
    }

    @Basic
    @Column(name = "hora_inicio")
    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    @Basic
    @Column(name = "hora_fin")
    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    @Basic
    @Column(name = "fecha")
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "id_tipo_reserva")
    public int getIdTipoReserva() {
        return idTipoReserva;
    }

    public void setIdTipoReserva(int idTipoReserva) {
        this.idTipoReserva = idTipoReserva;
    }

    @Basic
    @Column(name = "precio")
    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservaEntity that = (ReservaEntity) o;
        return id == that.id &&
                idUsuario == that.idUsuario &&
                idClub == that.idClub &&
                idPista == that.idPista &&
                idTipoReserva == that.idTipoReserva &&
                Objects.equals(horaInicio, that.horaInicio) &&
                Objects.equals(horaFin, that.horaFin) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(precio, that.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idUsuario, idClub, idPista, horaInicio, horaFin, fecha, idTipoReserva, precio);
    }

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public UsuariosEntity getUsuariosByIdUsuario() {
        return usuariosByIdUsuario;
    }

    public void setUsuariosByIdUsuario(UsuariosEntity usuariosByIdUsuario) {
        this.usuariosByIdUsuario = usuariosByIdUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "id_club", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public ClubEntity getClubByIdClub() {
        return clubByIdClub;
    }

    public void setClubByIdClub(ClubEntity clubByIdClub) {
        this.clubByIdClub = clubByIdClub;
    }

    @ManyToOne
    @JoinColumn(name = "id_pista", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public PistaEntity getPistaByIdPista() {
        return pistaByIdPista;
    }

    public void setPistaByIdPista(PistaEntity pistaByIdPista) {
        this.pistaByIdPista = pistaByIdPista;
    }

    @ManyToOne
    @JoinColumn(name = "id_tipo_reserva", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public TiposReservaEntity getTiposReservaByIdTipoReserva() {
        return tiposReservaByIdTipoReserva;
    }

    public void setTiposReservaByIdTipoReserva(TiposReservaEntity tiposReservaByIdTipoReserva) {
        this.tiposReservaByIdTipoReserva = tiposReservaByIdTipoReserva;
    }
}
