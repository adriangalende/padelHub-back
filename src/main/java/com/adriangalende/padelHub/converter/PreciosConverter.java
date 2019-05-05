package com.adriangalende.padelHub.converter;

import com.adriangalende.padelHub.entity.PreciosEntity;
import com.adriangalende.padelHub.model.Precios;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("convertidor_precios")
public class PreciosConverter {

    public Precios convertirEntity(PreciosEntity preciosEntity){
        return new Precios(preciosEntity);
    }

    public List<Precios> convertirListaEntities(List<PreciosEntity> preciosEntityList){
        List<Precios> listaPrecios = new ArrayList<>();
        preciosEntityList.forEach(precio -> listaPrecios.add(new Precios(precio)));
        return listaPrecios;
    }
}
