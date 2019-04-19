package com.adriangalende.padelHub.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "precios", schema = "padelhub", catalog = "")
@IdClass(PreciosEntityPK.class)
public class PreciosEntity {
    private int idPista;
    private String franjaHoraria;
    private double precio;
    private Double suplementoLuz;
    private PistaEntity pistaByIdPista;

    @Id
    @Column(name = "id_pista")
    public int getIdPista() {
        return idPista;
    }

    public void setIdPista(int idPista) {
        this.idPista = idPista;
    }

    @Id
    @Column(name = "franja_horaria")
    public String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    @Basic
    @Column(name = "precio")
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Basic
    @Column(name = "suplemento_luz")
    public Double getSuplementoLuz() {
        return suplementoLuz;
    }

    public void setSuplementoLuz(Double suplementoLuz) {
        this.suplementoLuz = suplementoLuz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreciosEntity that = (PreciosEntity) o;
        return idPista == that.idPista &&
                Double.compare(that.precio, precio) == 0 &&
                Objects.equals(franjaHoraria, that.franjaHoraria) &&
                Objects.equals(suplementoLuz, that.suplementoLuz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPista, franjaHoraria, precio, suplementoLuz);
    }

    @ManyToOne
    @JoinColumn(name = "id_pista", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public PistaEntity getPistaByIdPista() {
        return pistaByIdPista;
    }

    public void setPistaByIdPista(PistaEntity pistaByIdPista) {
        this.pistaByIdPista = pistaByIdPista;
    }
}
