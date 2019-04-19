package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.PreciosEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Precios {
    private int idPista;
    private String franjaHoraria;
    private double precio;
    private Double suplementoLuz;

    public Precios(){}

    public Precios(PreciosEntity preciosEntity) {
        this.idPista = preciosEntity.getIdPista();
        this.franjaHoraria = preciosEntity.getFranjaHoraria();
        this.precio = preciosEntity.getPrecio();
        this.suplementoLuz = preciosEntity.getSuplementoLuz();
    }
}
