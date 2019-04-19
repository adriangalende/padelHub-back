package com.adriangalende.padelHub.model;


import com.adriangalende.padelHub.entity.TiposPistaEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TiposPista {
    private int id;
    private String descripcion;

    public TiposPista (){

    }

    public TiposPista(TiposPistaEntity tiposPistaEntity){
        this.id = tiposPistaEntity.getId();
        this.descripcion = tiposPistaEntity.getDescripcion();
    }
}
