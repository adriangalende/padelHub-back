package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.TiposClubConverter;
import com.adriangalende.padelHub.model.TiposClub;
import com.adriangalende.padelHub.repository.TiposClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("servicio_TiposClub")
public class TiposClubService {

    @Autowired
    @Qualifier("repositorio_tiposClub")
    private TiposClubRepository repository;

    @Autowired
    @Qualifier("convertidor_TiposClub")
    private TiposClubConverter converter;

    public List<TiposClub> listar(){
        return converter.convertirLista(repository.findAll());
    }

}
