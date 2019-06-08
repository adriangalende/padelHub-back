package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.TiposReserva;
import com.adriangalende.padelHub.service.TiposReservaService;
import com.adriangalende.padelHub.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/ws")
@CrossOrigin
public class TiposReservaController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("servicio_TiposReserva")
    TiposReservaService service;

    @Autowired
    ObjectMapper mapper;

    @RequestMapping(value="/tiposReservaClub/{idClub}")
    @ResponseBody
    public String obtenerTiposReservaClub(@PathVariable("idClub") String idClub){
        JSONObject respuesta = new JSONObject();
        mapper = new ObjectMapper();
        try {
            respuesta = service.obtenerTiposReservaClub(Integer.parseInt(idClub));
            return mapper.writeValueAsString(respuesta.get("message"));
        } catch (JsonProcessingException e) {
            LOGGER.error("Error al convertir el objeto obtener la lista de tipos de reserva por club", e);
        } catch (JSONException e) {
            LOGGER.error("Error al intentar obtener el json con la lista de tipos de reserva", e);
        }
        return Utils.jsonResponseSetter(false, "No se han podido recuperar los tipos de reserva para este club").toString();
    }

}
