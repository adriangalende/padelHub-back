package com.adriangalende.padelHub.controller;

import com.adriangalende.padelHub.model.Usuarios;
import com.adriangalende.padelHub.service.UsuariosService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/api")
public class UsuariosController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("servicio_usuarios")
    UsuariosService service;

    @RequestMapping(value="/registro")
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

}
