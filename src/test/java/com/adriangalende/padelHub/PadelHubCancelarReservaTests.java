package com.adriangalende.padelHub;

import com.adriangalende.padelHub.model.PeticionCancelarPista;
import com.adriangalende.padelHub.service.ReservaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PadelHubCancelarReservaTests {
    private ObjectMapper objectMapper = new ObjectMapper();
    private String jsonPeticion = "";

    @Autowired
    ReservaService service;

    public void contextLoads(){
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    }

    public PeticionCancelarPista crearPeticion(String peticion) throws IOException {
        return  objectMapper.readValue(jsonPeticion, PeticionCancelarPista.class);
    }

    @Test
    public void conversorTest() throws IOException {
        jsonPeticion = "{\n" +
                "    \"idReserva\": \"6\",\n" +
                "    \"idUsuario\":\"28\",\n" +
                "    \"idClub\": \"1\"\n" +
                "}";

       PeticionCancelarPista peticionCancelarPista = crearPeticion(jsonPeticion);
        Assert.assertNotNull(peticionCancelarPista);
        Assert.assertEquals(6, peticionCancelarPista.getIdReserva());
        Assert.assertEquals(28, peticionCancelarPista.getIdUsuario());
        Assert.assertEquals(1, peticionCancelarPista.getIdClub());
    }

    @Test
    public void CancelarReservaSinIdUsuarioTest() throws IOException, JSONException {
        jsonPeticion = "{\n" +
                "    \"idReserva\": \"6\",\n" +
                "    \"idClub\": \"1\"\n" +
                "}";

        PeticionCancelarPista peticionCancelarPista = crearPeticion(jsonPeticion);
        Assert.assertEquals(0, peticionCancelarPista.getIdUsuario());
        JSONObject cancelacion = service.cancelar(peticionCancelarPista);
        Assert.assertEquals(false, cancelacion.getBoolean("success"));
    }

    @Test
    /**
     * Estas reservas son las que tienen activada la columna checkin o noshow
     */
    public void CancelarReservaPasadaTest() throws IOException, JSONException {
        jsonPeticion = "{\n" +
                "    \"idReserva\": \"6\",\n" +
                "    \"idUsuario\":\"28\",\n" +
                "    \"idClub\": \"1\"\n" +
                "}";

        PeticionCancelarPista peticionCancelarPista = crearPeticion(jsonPeticion);
        Assert.assertEquals(6, peticionCancelarPista.getIdReserva());
        JSONObject cancelacion = service.cancelar(peticionCancelarPista);
        Assert.assertEquals(false, cancelacion.getBoolean("success"));
    }

    @Test
    /**
     * Usuario que no ha realizado la reserva intenta cancelarla
     * Esta es 2x1 ya que el usuario tiene tipoUsuario club, y se comprueba que no pueda
     * eliminar reservas que corresponden a otro club que no sea el suyo
     */
    public void CancelarReservaUsuarioDistintoTest() throws IOException, JSONException {
        jsonPeticion = "{\n" +
                "    \"idReserva\": \"4\",\n" +
                "    \"idUsuario\":\"28\""+
                "}";

        PeticionCancelarPista peticion = crearPeticion(jsonPeticion);
        JSONObject cancelacion = service.cancelar(peticion);
        Assert.assertEquals(false, cancelacion.getBoolean("success"));
    }

    @Test
    @Transactional
    public void CancelarReservaMismoUsuarioTest() throws IOException, JSONException {
        jsonPeticion = "{\n" +
                "    \"idReserva\": \"9\",\n" +
                "    \"idUsuario\":\"25\"" +
                "}";

        PeticionCancelarPista peticion = crearPeticion(jsonPeticion);
        JSONObject cancelacion = service.cancelar(peticion);
        Assert.assertEquals(true, cancelacion.getBoolean("success"));
    }

    @Test
    @Transactional
    public void CancelarReservaUsuarioClubTest() throws JSONException, IOException {
        jsonPeticion = "{\n" +
                "    \"idReserva\": \"9\",\n" +
                "    \"idUsuario\":\"1\"" +
                "}";

        PeticionCancelarPista peticion = crearPeticion(jsonPeticion);
        JSONObject cancelacion = service.cancelar(peticion);
        Assert.assertEquals(true, cancelacion.getBoolean("success"));
    }

}
