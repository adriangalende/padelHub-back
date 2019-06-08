package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.PeticionCancelarPista;
import com.adriangalende.padelHub.model.Pista;
import com.adriangalende.padelHub.model.Reserva;
import com.adriangalende.padelHub.model.RespuestaDisponibilidadPista;
import com.adriangalende.padelHub.service.ReservaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class ReservaController {
    @Autowired
    ReservaService service;
    ObjectMapper mapper;
    JSONObject jsonObject;

    @RequestMapping(value="/ws/buscar")
    public String buscarPistas(@RequestBody @Valid Reserva reservas){
        jsonObject = null;
        mapper = new ObjectMapper();
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

        return jsonObject.toString();
    }


    @RequestMapping(value="/wss/reservar")
    public String reservarPista(@RequestBody @Valid Reserva reserva, @RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try {
            jsonObject = service.reservar(reserva, token);
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        } catch (JSONException e) {
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            return (String)jsonObject.get("message");
        } catch (JSONException e) {
            return "El formato json no es correcto";
        }
    }

    @RequestMapping(value="/wss/cancelar")
    public String cancelarPista(@RequestBody @Valid PeticionCancelarPista peticion,  @RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try{
            jsonObject = service.cancelar(peticion, token);
            if(jsonObject.getBoolean("success")){
                return jsonObject.toString();
            }
        }catch (JSONException e){
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return jsonObject.toString();
    }

    @RequestMapping(value="/wss/misReservas")
    public String obtener(@RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try{
            jsonObject = service.obtenerReservas(token);
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        }catch (JSONException e){
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

            return jsonObject.toString();
    }

    @RequestMapping(value="/wss/reservas")
    public String obtenerReservasClub(@RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try{
            jsonObject = service.obtenerReservasClub(token);
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        }catch (JSONException e){
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="/wss/treservas")
    public String obtenerTodasReservasClub(@RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try{
            jsonObject = service.obtenerTodasReservasClub(token);
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        }catch (JSONException e){
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="/wss/checkIn")
    public String checkIn(@RequestBody @Valid PeticionCancelarPista peticion,  @RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try{
            jsonObject = service.checkIn(peticion, token.split(" ")[1]);
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        }catch (JSONException e){
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @RequestMapping(value="/wss/noShow")
    public String noShow(@RequestBody @Valid PeticionCancelarPista peticion,  @RequestHeader("Authorization") String token){
        jsonObject = null;
        mapper = new ObjectMapper();

        try{
            jsonObject = service.noShow(peticion, token.split(" ")[1]);
            if(jsonObject.getBoolean("success")){
                return mapper.writeValueAsString(jsonObject.get("message"));
            }
        }catch (JSONException e){
            return "El formato json no es correcto";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    
}
