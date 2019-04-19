package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "club", schema = "padelhub", catalog = "")
public class ClubEntity {
    private int id;
    private String nombre;
    private String gps;
    private String ciudad;
    private String telefono;
    private int idTipoClub;
    private TiposClubEntity tiposClubById;
    private Collection<PistaEntity> pistasById;
    private Collection<ReservaEntity> reservasById;
    private Collection<TiposReservaEntity> tiposReservasById;
    private Collection<UsuariosEntity> usuariosById;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "nombre")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "gps")
    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @Basic
    @Column(name = "ciudad")
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Basic
    @Column(name = "telefono")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Basic
    @Column(name = "id_tipo_club")
    public int getIdTipoClub() {
        return idTipoClub;
    }

    public void setIdTipoClub(int idTipoClub) {
        this.idTipoClub = idTipoClub;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubEntity that = (ClubEntity) o;
        return id == that.id &&
                idTipoClub == that.idTipoClub &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(gps, that.gps) &&
                Objects.equals(ciudad, that.ciudad) &&
                Objects.equals(telefono, that.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, gps, ciudad, telefono, idTipoClub);
    }

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false)
    public TiposClubEntity getTiposClubById() {
        return tiposClubById;
    }

    public void setTiposClubById(TiposClubEntity tiposClubById) {
        this.tiposClubById = tiposClubById;
    }

    @OneToMany(mappedBy = "clubByIdClub")
    public Collection<PistaEntity> getPistasById() {
        return pistasById;
    }

    public void setPistasById(Collection<PistaEntity> pistasById) {
        this.pistasById = pistasById;
    }

    @OneToMany(mappedBy = "clubByIdClub")
    public Collection<ReservaEntity> getReservasById() {
        return reservasById;
    }

    public void setReservasById(Collection<ReservaEntity> reservasById) {
        this.reservasById = reservasById;
    }

    @OneToMany(mappedBy = "clubByIdClub")
    public Collection<TiposReservaEntity> getTiposReservasById() {
        return tiposReservasById;
    }

    public void setTiposReservasById(Collection<TiposReservaEntity> tiposReservasById) {
        this.tiposReservasById = tiposReservasById;
    }

    @OneToMany(mappedBy = "clubByIdClub")
    public Collection<UsuariosEntity> getUsuariosById() {
        return usuariosById;
    }

    public void setUsuariosById(Collection<UsuariosEntity> usuariosById) {
        this.usuariosById = usuariosById;
    }
}
