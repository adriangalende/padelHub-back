package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.UsuariosEntity;
import com.adriangalende.padelHub.model.Usuarios;
import org.springframework.stereotype.Component;

@Component("convertidor_usuarios")
public class UsuariosConverter {

    public Usuarios convertirUsuarioModelo(UsuariosEntity usuariosEntity){
        return new Usuarios(usuariosEntity);
    }

    public UsuariosEntity convertirUsuarioEntidad(Usuarios usuarios){
        return new UsuariosEntity(usuarios);
    }



}
