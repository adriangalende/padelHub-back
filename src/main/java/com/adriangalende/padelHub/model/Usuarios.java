package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.UsuariosEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuarios {
    private int id;
    private String nombre;
    private String telefono;
    private int idTiposUsuario;
    private int idClub;
    private String password;

    public Usuarios(){}

    public Usuarios(UsuariosEntity usuariosEntity) {
        this.id = usuariosEntity.getId();
        this.nombre = usuariosEntity.getNombre();
        this.telefono = usuariosEntity.getTelefono();
        this.idTiposUsuario = usuariosEntity.getIdTiposUsuario();
        this.idClub = usuariosEntity.getIdClub();
        this.password = usuariosEntity.getPassword();
    }
}
