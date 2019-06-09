package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.ClubConverter;
import com.adriangalende.padelHub.entity.ClubEntity;
import com.adriangalende.padelHub.repository.ClubRepository;
import com.adriangalende.padelHub.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("servicio_club")
public class ClubService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("repositorio_club")
    private ClubRepository repository;

    @Autowired
    @Qualifier("convertidor_club")
    private ClubConverter converter;

    public int obtenerIdClub(String nombreClub){
        return repository.findAllByNombre(nombreClub);
    }

    public JSONObject obtenerInfoClub(int idClub) {
        Optional<ClubEntity> clubEntity = repository.findById(idClub);
        JSONObject respuesta = new JSONObject();
        if(clubEntity.isPresent()){
            try {
                respuesta.put("success", true);
                respuesta.put("message", converter.convertirEntity(clubEntity.get()));
            } catch (JSONException e) {
                LOGGER.error("Error al convertir a jsonObject: ", e);
                return Utils.jsonResponseSetter(false, "Error al recuperar información del club");
            }
        } else {
            return Utils.jsonResponseSetter(false, "No hemos encontrado información para esta id");
        }
        return respuesta;
    }
}
