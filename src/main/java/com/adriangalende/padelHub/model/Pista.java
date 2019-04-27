package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.PistaEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pista {
    private int id;
    private String nombre;
    private int idClub;
    private int idTipoPista;
    private String carpetaImagenes;

    public Pista(){}

    public Pista(PistaEntity pistaEntity){
        this.id = pistaEntity.getId();
        this.nombre = pistaEntity.getNombre();
        this.idClub = pistaEntity.getIdClub();
        this.idTipoPista = pistaEntity.getIdTipoPista();
        this.carpetaImagenes = pistaEntity.getCarpetaImagenes();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public int getIdTipoPista() {
        return idTipoPista;
    }

    public void setIdTipoPista(int idTipoPista) {
        this.idTipoPista = idTipoPista;
    }

    public String getCarpetaImagenes() {
        return carpetaImagenes;
    }

    public void setCarpetaImagenes(String carpetaImagenes) {
        this.carpetaImagenes = carpetaImagenes;
    }
}
