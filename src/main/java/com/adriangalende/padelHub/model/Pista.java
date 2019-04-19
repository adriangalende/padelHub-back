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

    public Pista(){}

    public Pista(PistaEntity pistaEntity){
        this.id = pistaEntity.getId();
        this.nombre = pistaEntity.getNombre();
        this.idClub = pistaEntity.getIdClub();
        this.idTipoPista = pistaEntity.getIdTipoPista();
    }
}
