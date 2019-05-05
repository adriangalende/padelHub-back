package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.PreciosConverter;
import com.adriangalende.padelHub.entity.PreciosEntity;
import com.adriangalende.padelHub.model.Precios;
import com.adriangalende.padelHub.repository.PreciosRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("servicio_precios")
public class PreciosService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("repositorio_precios")
    private PreciosRepository repository;

    @Autowired
    @Qualifier("convertidor_precios")
    private PreciosConverter converter;

    public Precios obtenerPrecios(int idPista, int idTiposUsuario, Date horaInicio){
        String franjaHoraria = obtenerFranjaHoraria(horaInicio);
        PreciosEntity preciosEntity = repository.recuperaPrecio(idPista, idTiposUsuario, franjaHoraria);
        if(preciosEntity != null){
            return converter.convertirEntity(preciosEntity);
        } else {
            return new Precios();
        }
    }

    public JSONObject obtenerTodosPrecios(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("message", ((List<Precios>)converter.convertirListaEntities(repository.todos())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    /**
     * Obtenemos la franja horaria según la hora de inicio
     * @param horaInicio
     * @return
     */
    private String obtenerFranjaHoraria(Date horaInicio) {
        if(horaInicio.getHours() <= 12){
            return "mañana";
        }
        return "tarde";
    }

}
