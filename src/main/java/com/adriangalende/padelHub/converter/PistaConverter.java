package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.PistaEntity;
import com.adriangalende.padelHub.model.Pista;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("convertidor_pista")
public class PistaConverter {

    public List<Pista> convertirListaEntities(List<PistaEntity> pistaEntityList){
        List<Pista> listaPistas = new ArrayList<>();
        pistaEntityList.forEach( reserva -> listaPistas.add(new Pista(reserva)) );
        return listaPistas;
    }

}
