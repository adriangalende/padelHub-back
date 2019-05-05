package com.adriangalende.padelHub.controller;


import com.adriangalende.padelHub.service.PreciosService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ws")
public class PreciosController {
    @Autowired
    PreciosService service;
    ObjectMapper mapper;

    @RequestMapping(value="/precios")
    public String obtenerPistas(){
        JSONObject precios = new JSONObject();
        mapper = new ObjectMapper();
        try {
            precios = service.obtenerTodosPrecios();
            return mapper.writeValueAsString(precios.get("message"));
        } catch (JsonProcessingException e) {
           return "Error al pasar lista de precios";
        } catch (JSONException e) {
            return "Error al pasar lista de precios";
        }
    }
}
