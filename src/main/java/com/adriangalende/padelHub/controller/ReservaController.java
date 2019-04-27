package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.Pista;
import com.adriangalende.padelHub.model.Reserva;
import com.adriangalende.padelHub.model.RespuestaDisponibilidadPista;
import com.adriangalende.padelHub.service.ReservaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="/ws")
public class ReservaController {
    @Autowired
    ReservaService service;

    @RequestMapping(value="/buscar")
    public String buscarPistas(@RequestBody @Valid Reserva reservas){
        List<RespuestaDisponibilidadPista> pistas = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            pistas = service.buscar(reservas);

            if(pistas != null){
                try {
                    return mapper.writeValueAsString(pistas);
                } catch (JsonProcessingException e) {
                    return "Error al intentar obtener los tipos de club en formato json";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Error al intentar obtener los tipos de club en formato json";
    }
}
