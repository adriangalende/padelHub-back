package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "pista", schema = "padelhub", catalog = "")
public class PistaEntity {
    private int id;
    private String nombre;
    private int idClub;
    private int idTipoPista;
    private String carpetaImagenes;
    private ClubEntity clubByIdClub;
    private TiposPistaEntity tiposPistaByIdTipoPista;
    private Collection<PreciosEntity> preciosById;
    private Collection<ReservaEntity> reservasById;

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
    @Column(name = "id_club")
    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    @Basic
    @Column(name = "id_tipo_pista")
    public int getIdTipoPista() {
        return idTipoPista;
    }

    public void setIdTipoPista(int idTipoPista) {
        this.idTipoPista = idTipoPista;
    }

    @Basic
    @Column(name = "carpeta_imagenes")
    public String getCarpetaImagenes() {
        return carpetaImagenes;
    }

    public void setCarpetaImagenes(String carpetaImagenes) {
        this.carpetaImagenes = carpetaImagenes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PistaEntity that = (PistaEntity) o;
        return id == that.id &&
                idClub == that.idClub &&
                idTipoPista == that.idTipoPista &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(carpetaImagenes, that.carpetaImagenes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, idClub, idTipoPista, carpetaImagenes);
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
    @JoinColumn(name = "id_tipo_pista", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public TiposPistaEntity getTiposPistaByIdTipoPista() {
        return tiposPistaByIdTipoPista;
    }

    public void setTiposPistaByIdTipoPista(TiposPistaEntity tiposPistaByIdTipoPista) {
        this.tiposPistaByIdTipoPista = tiposPistaByIdTipoPista;
    }

    @OneToMany(mappedBy = "pistaByIdPista")
    public Collection<PreciosEntity> getPreciosById() {
        return preciosById;
    }

    public void setPreciosById(Collection<PreciosEntity> preciosById) {
        this.preciosById = preciosById;
    }

    @OneToMany(mappedBy = "pistaByIdPista")
    public Collection<ReservaEntity> getReservasById() {
        return reservasById;
    }

    public void setReservasById(Collection<ReservaEntity> reservasById) {
        this.reservasById = reservasById;
    }
}
