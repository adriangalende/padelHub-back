package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.TiposReservaEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TiposReserva {
    private int id;
    private String descripcion;
    private int idClub;

    public TiposReserva(){ }

    public TiposReserva(TiposReservaEntity tiposReservaEntity){
        this.id = tiposReservaEntity.getId();
        this.descripcion = tiposReservaEntity.getDescripcion();
        this.idClub = tiposReservaEntity.getIdClub();
    }
}
