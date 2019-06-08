package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.TiposReservaEntity;
import com.adriangalende.padelHub.model.TiposReserva;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("convertidor_TiposReserva")
public class TiposReservaConverter {

    public List<TiposReserva> convertirLista(List<TiposReservaEntity> tiposReservaEntityList){
        List<TiposReserva> listaTiposReserva = new ArrayList<>();
        tiposReservaEntityList.forEach(tipoReserva -> listaTiposReserva.add(new TiposReserva(tipoReserva)));
        return listaTiposReserva;
    }

}
