package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.TiposReservaConverter;
import com.adriangalende.padelHub.entity.TiposReservaEntity;
import com.adriangalende.padelHub.model.TiposReserva;
import com.adriangalende.padelHub.repository.ReservaRepository;
import com.adriangalende.padelHub.repository.TiposReservaRepository;
import com.adriangalende.padelHub.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.util.List;
import java.util.Optional;

@Service("servicio_TiposReserva")
public class TiposReservaService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("repositorio_TiposReserva")
    private TiposReservaRepository repository;

    @Autowired
    @Qualifier("convertidor_TiposReserva")
    private TiposReservaConverter converter;

    public JSONObject obtenerTiposReservaClub(int idClub) {
        JSONObject respuesta = new JSONObject();

        Optional<List<TiposReservaEntity>> tiposReservaOptional = repository.obtenerTiposReservaClub(idClub);

        if(tiposReservaOptional.isPresent()){

            try {
                respuesta.put("success", true);
                respuesta.put("message", converter.convertirLista(tiposReservaOptional.get()));
                return respuesta;
            } catch (JSONException e) {
                LOGGER.error("No se ha podido generar la respuesta JSONOBJECT", e);
            }
        } else {
            return Utils.jsonResponseSetter(false, "No hemos podido obtener tipos de reserva para este club");
        }
        return Utils.jsonResponseSetter(false, "Error al obtener tipos de reserva para este club");
    }

    public String obtenerTipoReserva(int idTipoReserva){
        Optional<TiposReservaEntity> tiposReserva = repository.findById(idTipoReserva);
        if(tiposReserva.isPresent()){
            return tiposReserva.get().getDescripcion();
        }
        return null;
    }
}
