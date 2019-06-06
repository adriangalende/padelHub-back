package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("servicio_club")
public class ClubService {

    @Autowired
    @Qualifier("repositorio_club")

    private ClubRepository repository;

    public int obtenerIdClub(String nombreClub){
        return repository.findAllByNombre(nombreClub);
    }
}
