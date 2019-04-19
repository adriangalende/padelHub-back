package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.TiposClubEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TiposClub {
    private int id;
    private String descripcion;

    public TiposClub(){

    }

    public TiposClub(TiposClubEntity tiposClubEntity){
        this.id = tiposClubEntity.getId();
        this.descripcion = tiposClubEntity.getDescripcion();
    }
}
