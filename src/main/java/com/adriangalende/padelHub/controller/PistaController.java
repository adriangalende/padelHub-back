package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.Pista;
import com.adriangalende.padelHub.service.PistaService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/ws")
public class PistaController {
    @Autowired
    PistaService service;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value="/pistas")
    public List<Pista> obtenerPistas(){
        return service.obtenerPistas();
    }
}
