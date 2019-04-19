package com.adriangalende.padelHub.model;

import com.adriangalende.padelHub.entity.ClubEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
        this.nombre = clubEntity.getGps();
        this.ciudad = clubEntity.getCiudad();
        this.telefono = clubEntity.getTelefono();
        this.idTipoClub = clubEntity.getIdTipoClub();
    }

}
