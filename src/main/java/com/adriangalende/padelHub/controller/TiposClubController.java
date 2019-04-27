package com.adriangalende.padelHub.controller;


import com.adriangalende.padelHub.model.TiposClub;
import com.adriangalende.padelHub.service.TiposClubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/ws")
public class TiposClubController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("servicio_TiposClub")
    TiposClubService service;

    @RequestMapping(value="/tiposClub")
    @ResponseBody
    public String obtenerTiposClub() {
        ObjectMapper mapper = new ObjectMapper();

        List<TiposClub> listaTiposClub = service.listar();
        try {
            return mapper.writeValueAsString(listaTiposClub);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error al intentar obtener los tipos de club en formato json: "+ e);
            return "Error al intentar obtener los tipos de club en formato json";
        }
    }
}
