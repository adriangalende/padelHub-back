package com.adriangalende.padelHub.service;

import com.adriangalende.padelHub.converter.UsuariosConverter;
import com.adriangalende.padelHub.entity.UsuariosEntity;
import com.adriangalende.padelHub.model.Usuarios;
import com.adriangalende.padelHub.repository.UsuariosRepository;
import com.adriangalende.padelHub.utils.Utils;
import freemarker.template.utility.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.rmi.CORBA.Util;
import java.util.List;


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
        return converter.convertirUsuarioModelo(repository.findById(id).get());
    }

}
