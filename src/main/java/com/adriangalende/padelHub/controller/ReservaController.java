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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/ws")
public class ReservaController {
    @Autowired
    ReservaService service;

    @RequestMapping(value="/buscar")
    public String buscarPistas(@RequestBody @Valid Reserva reservas){
        JSONObject jsonObject = null;
        ObjectMapper mapper = new ObjectMapper();
        List<RespuestaDisponibilidadPista> pistas = new ArrayList<>();
        try {
            jsonObject = service.buscar(reservas);

            if(jsonObject != null && jsonObject.getBoolean("success")){
                try {
                    pistas.addAll((List<RespuestaDisponibilidadPista>)jsonObject.get("message"));
                    return mapper.writeValueAsString(pistas);
                } catch (JsonProcessingException e) {
                    return "Error al convertir la clase a json para ver las pistas disponibles";
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return (String)jsonObject.get("message");
        }catch (JSONException e) {
            return "El formato json no es correcto";
        }
    }
}
