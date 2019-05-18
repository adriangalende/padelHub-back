package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.UsuariosConverter;
import com.adriangalende.padelHub.entity.UsuariosEntity;
import com.adriangalende.padelHub.model.JwtAuthenticationToken;
import com.adriangalende.padelHub.model.JwtUser;
import com.adriangalende.padelHub.model.JwtUserDetails;
import com.adriangalende.padelHub.model.Usuarios;
import com.adriangalende.padelHub.repository.UsuariosRepository;
import com.adriangalende.padelHub.security.JwtGenerator;
import com.adriangalende.padelHub.security.JwtValidator;
import com.adriangalende.padelHub.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service("servicio_usuarios")
public class UsuariosService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("repositorio_usuarios")
    private UsuariosRepository repository;

    @Autowired
    @Qualifier("convertidor_usuarios")
    private UsuariosConverter converter;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private BCrypt bCrypt = new BCrypt();

    /**
     * Función que controla si existe un usuario dado de alta en la base de datos
     * con el mismo teléfono o email que el usuario que quiere darse de alta.
     *
     * Ambos campos deberían ser únicos.
     *
     * @param email
     * @param telefono
     * @return
     */
    private boolean existeUsuario(String email, String telefono){
        List<UsuariosEntity> usuarios = repository.findByEmailOrTelefono(email, telefono);
        
        if(usuarios.size() > 0){
            return true;
        }
        return false;
    }


    /**
     * Comprobamos que los datos de usuario sean correctos antes de
     * buscar si existe el usuario.
     *
     * @param usuario
     * @return
     */
    private JSONObject preparaUsuario(Usuarios usuario) throws JSONException {

        JSONObject jsonResponse = new JSONObject();

        if(usuario.getIdTiposUsuario() == 0){
            usuario.setIdTiposUsuario(1);
        }

        if(usuario.getIdClub() == 0){
            usuario.setIdClub(1);
        }

        if(StringUtils.isBlank(usuario.getTelefono()) ||  !(Utils.validar(usuario.getTelefono(), Utils.MOVIL_REGEX))){
            return Utils.jsonResponseSetter(false, "Error al validar el móvil");
        }


        if(StringUtils.isBlank(usuario.getEmail()) || !Utils.validar(usuario.getEmail(), Utils.EMAIL_REGEX)){
            return Utils.jsonResponseSetter(false, "Error al validar el email");
        }

        if(StringUtils.isNotBlank(usuario.getPassword())){
            usuario.setPassword(encoder.encode(usuario.getPassword()));
        } else {
            return Utils.jsonResponseSetter(false, "Error al validar la contraseña");
        }

        if(StringUtils.isBlank(usuario.getNombre())){
            return Utils.jsonResponseSetter(false, "El nombre de usuario no puede estar vacío");

        }

        return Utils.jsonResponseSetter(true, "OK");

    }

    /**
     * Método para dar de alta a los usuarios utilizando la api
     * @param usuario
     * @return
     * @throws JSONException
     */
    public JSONObject alta(Usuarios usuario) throws JSONException {
        //validamos datos del usuario antes de comprobar si existe
        JSONObject jsonResponse = preparaUsuario(usuario);

        if(jsonResponse.getBoolean("success")){
            //Buscamos si existe un usuario con los datos proporcionados
            jsonResponse = new JSONObject();
            if(!existeUsuario(usuario.getEmail(), usuario.getTelefono())){
                try {
                    repository.save(converter.convertirUsuarioEntidad(usuario));
                    jsonResponse = Utils.jsonResponseSetter(true, "OK");

                }catch (Exception ex){
                    jsonResponse = Utils.jsonResponseSetter(false, "Error al dar de alta al usuario: " + ex );
                    //throw ex;
                }
            } else {
                jsonResponse = Utils.jsonResponseSetter(false, "Ya existe un usuario con el mismo número o email");
            }
        }

        return jsonResponse;
    }

    public Usuarios obtenerUsuario(int id){
        Optional<UsuariosEntity> usuario = repository.findById(id);
        if(usuario.isPresent()){
            return converter.convertirUsuarioModelo(usuario.get());
        }
        return null;
    }

    public String obtenerTipoUsuario(int id){
        Optional<UsuariosEntity> usuario = repository.findById(id);
        if(usuario.isPresent()){
            return usuario.get().getTiposUsuarioByIdTiposUsuario().getNombre();
        }
        return "KO";
    }

    /**
     * Método que da de baja a un usuario
     * @param usuario
     * @return
     */
    public JSONObject baja(Usuarios usuario) {

        Optional<UsuariosEntity> usuarioOptional = repository.findById(usuario.getId());

        if(!usuarioOptional.isPresent()){
            return Utils.jsonResponseSetter(false, "No existe el usuario");
        }

        try{
            UsuariosEntity usuariosEntity = usuarioOptional.get();
            repository.delete(usuariosEntity);
            return Utils.jsonResponseSetter(true, "Usuario dado de baja");
        }catch (Exception ex){
            LOGGER.error("No se ha podido dar de baja al usuario", ex);
        }

        return Utils.jsonResponseSetter(false, "No se ha podido dar de baja al usuario");
    }

    public JSONObject login(Usuarios usuario) {
        Optional<UsuariosEntity> usuarioOptional = repository.buscarUsuario(usuario.getEmail(), usuario.getTelefono());
        UsuariosEntity usuarioEntity = null;
        JSONObject jsonObject = new JSONObject();

        if(!usuarioOptional.isPresent()){
            return Utils.jsonResponseSetter(false, "Credenciales incorrectas");
        }

        usuarioEntity = usuarioOptional.get();

        if(encoder.matches( usuario.getPassword(),usuarioEntity.getPassword())){
            try {
                usuario = converter.convertirUsuarioModelo(usuarioEntity);
                usuario.setPassword("");
                jsonObject.put("success", true);


                //Creamos el token con jwt
                JwtUser jwtUser = new JwtUser();
                jwtUser.setEmail(usuario.getEmail());
                jwtUser.setId(usuario.getId());
                jwtUser.setIdClub(usuario.getIdClub());
                jwtUser.setRole( usuarioEntity.getTiposUsuarioByIdTiposUsuario().getNombre());
                jwtUser.setUsername(usuario.getNombre());

                String token = new JwtGenerator().generate(jwtUser);

                jsonObject.put("token",token);
                usuarioEntity.setToken(token);

                repository.save(usuarioEntity);

                return jsonObject;

            } catch (JSONException e) {
                LOGGER.error("No se ha podido devolver la información del usuario", e);
            }

        } else {
            return Utils.jsonResponseSetter(false, "Credenciales incorrectas");
        }



        return Utils.jsonResponseSetter(false, "No ha sido posible iniciar sesión");
    }

    /**
     * Comprobamos los permisos del usuario que se ha logueado en el front para  conocer su rol
     * @param token
     * @return
     */
    public JSONObject permisos(String token) {

        JwtValidator validator = new JwtValidator();
        JSONObject respuesta = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();

        try {
            token = (String )mapper.readValue(token, HashMap.class).get("token");
        } catch (IOException e) {
            LOGGER.error("No ha sido posible parsear el json de entrada para comprobar permisos del token: ", e);
            return Utils.jsonResponseSetter(false, "No se ha podido comprobar el token");
        }

        try {
            respuesta.put("jwtUser",mapper.writeValueAsString(validator.validate(token)));
        } catch (JSONException e) {
            LOGGER.error("Error al generar el json object con los permisos de usuario", e);
            respuesta = Utils.jsonResponseSetter(false, "Algo ha salido mal");
        } catch (JsonProcessingException e) {
            LOGGER.error("Error al convertir la respuesta con datos de usuario a json:", e);
        }

        return respuesta;
    }
}
