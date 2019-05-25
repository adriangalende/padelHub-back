package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.Usuarios;
import com.adriangalende.padelHub.service.UsuariosService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value="/ws/usuarios")
public class UsuariosController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("servicio_usuarios")
    UsuariosService service;

    @RequestMapping(value="/alta")
    @ResponseBody
    public String registrar(@RequestBody @Valid Usuarios usuario){
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse = service.alta(usuario);
        } catch (JSONException e) {
            LOGGER.error("Error json: " + e);
        }
        return jsonResponse.toString();
    }

    @RequestMapping(value="/baja")
    @ResponseBody
    public String baja(@RequestBody @Valid Usuarios usuario){
        JSONObject jsonResponse = new JSONObject();
        jsonResponse = service.baja(usuario);
        return jsonResponse.toString();
    }


    @RequestMapping(value="/login")
    @ResponseBody
    public String login(@RequestBody @Valid Usuarios usuario){
        JSONObject jsonResponse = new JSONObject();
        jsonResponse = service.login(usuario);
        return jsonResponse.toString();
    }

    @RequestMapping(value="/permisos")
    @ResponseBody
    public String permisos(@RequestBody @Valid String token){
        JSONObject jsonResponse = new JSONObject();
        jsonResponse = service.permisos(token);
        return jsonResponse.toString();
    }

}
