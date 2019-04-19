package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.TiposUsuarioEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TiposUsuario {
    private int id;
    private String nombre;

    public TiposUsuario(){}

    public TiposUsuario(TiposUsuarioEntity tiposUsuarioEntity){
        this.id = tiposUsuarioEntity.getId();
        this.nombre = tiposUsuarioEntity.getNombre();
    }
}
