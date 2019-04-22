package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.UsuariosEntity;

public class Usuarios {
    private int id;
    private String nombre;
    private String telefono;
    private int idTiposUsuario;
    private int idClub;
    private String password;
    private String email;

    public Usuarios(){}

    public Usuarios(UsuariosEntity usuariosEntity) {
        this.id = usuariosEntity.getId();
        this.nombre = usuariosEntity.getNombre();
        this.telefono = usuariosEntity.getTelefono();
        this.idTiposUsuario = usuariosEntity.getIdTiposUsuario();
        this.idClub = usuariosEntity.getIdClub();
        this.password = usuariosEntity.getPassword();
        this.email = usuariosEntity.getEmail();
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdTiposUsuario() {
        return idTiposUsuario;
    }

    public void setIdTiposUsuario(int idTiposUsuario) {
        this.idTiposUsuario = idTiposUsuario;
    }

    public int getIdClub() {
        return idClub;
    }

    public void setIdClub(int idClub) {
        this.idClub = idClub;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
