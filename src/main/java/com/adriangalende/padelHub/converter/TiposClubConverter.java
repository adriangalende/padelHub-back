package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.TiposClubEntity;
import com.adriangalende.padelHub.model.TiposClub;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("convertidor_TiposClub")
public class TiposClubConverter {

    /**
     *
     * @param tiposClubEntities
     * @return
     */
    public List<TiposClub> convertirLista(List<TiposClubEntity> tiposClubEntities){
        List<TiposClub> listaTiposClub = new ArrayList<>();
        tiposClubEntities.forEach( tipoClub -> listaTiposClub.add(new TiposClub(tipoClub)) );
        return listaTiposClub;
    }

}
