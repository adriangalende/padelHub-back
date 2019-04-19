package com.adriangalende.padelHub.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PreciosEntityPK implements Serializable {
    private int idPista;
    private String franjaHoraria;

    @Column(name = "id_pista")
    @Id
    public int getIdPista() {
        return idPista;
    }

    public void setIdPista(int idPista) {
        this.idPista = idPista;
    }

    @Column(name = "franja_horaria")
    @Id
    public String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreciosEntityPK that = (PreciosEntityPK) o;
        return idPista == that.idPista &&
                Objects.equals(franjaHoraria, that.franjaHoraria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPista, franjaHoraria);
    }
}
