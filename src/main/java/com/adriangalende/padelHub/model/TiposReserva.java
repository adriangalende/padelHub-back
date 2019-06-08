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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }
}
