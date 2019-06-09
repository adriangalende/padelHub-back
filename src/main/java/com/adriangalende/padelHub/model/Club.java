package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.ClubEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Club {
    private int id;
    private String nombre;
    private String gps;
    private String ciudad;
    private String telefono;
    private int idTipoClub;

    public Club(){}

    public Club(ClubEntity clubEntity){
        this.id = clubEntity.getId();
        this.nombre = clubEntity.getNombre();
        this.gps = clubEntity.getGps();
        this.ciudad = clubEntity.getCiudad();
        this.telefono = clubEntity.getTelefono();
        this.idTipoClub = clubEntity.getIdTipoClub();
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

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getIdTipoClub() {
        return idTipoClub;
    }

    public void setIdTipoClub(int idTipoClub) {
        this.idTipoClub = idTipoClub;
    }
}
