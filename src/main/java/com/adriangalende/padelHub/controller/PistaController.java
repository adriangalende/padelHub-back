package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.Pista;
import com.adriangalende.padelHub.service.PistaService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value="/ws")
@CrossOrigin
public class PistaController {
    @Autowired
    PistaService service;


    @RequestMapping(value="/pistas")
    public List<Pista> obtenerPistas(){
        return service.obtenerPistas();
    }

    @RequestMapping(value="/pistasClub/{idClub}")
    public List<Pista> obtenerPistasClub(@PathVariable("idClub") String idClub){
        return service.obtenerPistasClub(Integer.parseInt(idClub));
    }


}
