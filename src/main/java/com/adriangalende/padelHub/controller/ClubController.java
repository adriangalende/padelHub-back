package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.service.ClubService;
import com.adriangalende.padelHub.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ClubController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ClubService service;


    @RequestMapping(value="/ws/club/{idClub}")
    public String infoClub(@PathVariable("idClub") String idClub){
        JSONObject jsonObject = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonObject = service.obtenerInfoClub(Integer.parseInt(idClub));
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        } catch (JSONException e) {
            LOGGER.error("Error al obtener json ");
        } catch (JsonProcessingException e) {
            LOGGER.error("Error al mapear objeto a  json ");
        }
        return jsonObject.toString();
    }


    @RequestMapping(value="/ws/nclub/{idClub}")
    public String nombreClub(@PathVariable("idClub") String idClub){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre",service.obtenerNombreClub(Integer.parseInt(idClub)));
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Utils.jsonResponseSetter(false, "No se ha podido recuperar el nombre del club").toString();
    }
}
