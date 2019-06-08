package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.PistaConverter;
import com.adriangalende.padelHub.converter.TiposReservaConverter;
import com.adriangalende.padelHub.entity.PistaEntity;
import com.adriangalende.padelHub.model.Pista;
import com.adriangalende.padelHub.model.TiposPista;
import com.adriangalende.padelHub.model.TiposReserva;
import com.adriangalende.padelHub.repository.PistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("servicio_pista")
public class PistaService {

    @Autowired
    @Qualifier("repositorio_pista")
    private PistaRepository repository;

    @Autowired
    @Qualifier("convertidor_pista")
    private PistaConverter converter;


    public List<Pista> obtenerPistas(){
        return converter.convertirListaEntities(repository.findAll());
    }

    public Optional<PistaEntity> datosPista(int idPista){
        return repository.findById(idPista);
    }


    public List<Pista> obtenerPistasClub(Integer idClub) {
        return converter.convertirListaEntities(repository.buscarPistas(idClub));
    }

}
