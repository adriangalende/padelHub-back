package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.PreciosEntity;

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

    public int getIdPista() {
        return idPista;
    }

    public void setIdPista(int idPista) {
        this.idPista = idPista;
    }

    public String getFranjaHoraria() {
        return franjaHoraria;
    }

    public void setFranjaHoraria(String franjaHoraria) {
        this.franjaHoraria = franjaHoraria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Double getSuplementoLuz() {
        return suplementoLuz;
    }

    public void setSuplementoLuz(Double suplementoLuz) {
        this.suplementoLuz = suplementoLuz;
    }
}
