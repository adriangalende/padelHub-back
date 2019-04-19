package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tipos_reserva", schema = "padelhub", catalog = "")
public class TiposReservaEntity {
    private int id;
    private String descripcion;
    private int idClub;
    private Collection<ReservaEntity> reservasById;
    private ClubEntity clubByIdClub;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "id_club")
    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TiposReservaEntity that = (TiposReservaEntity) o;
        return id == that.id &&
                idClub == that.idClub &&
                Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, idClub);
    }

    @OneToMany(mappedBy = "tiposReservaByIdTipoReserva")
    public Collection<ReservaEntity> getReservasById() {
        return reservasById;
    }

    public void setReservasById(Collection<ReservaEntity> reservasById) {
        this.reservasById = reservasById;
    }

    @ManyToOne
    @JoinColumn(name = "id_club", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public ClubEntity getClubByIdClub() {
        return clubByIdClub;
    }

    public void setClubByIdClub(ClubEntity clubByIdClub) {
        this.clubByIdClub = clubByIdClub;
    }
}
