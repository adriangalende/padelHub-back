package com.adriangalende.padelHub;

import com.adriangalende.padelHub.model.Reserva;
import com.adriangalende.padelHub.model.RespuestaDisponibilidadPista;
import com.adriangalende.padelHub.service.ReservaService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PadelHubBusquedaPistaTests {

    private ObjectMapper objectMapper = new ObjectMapper();
    private  String jsonPeticion = "";

    @Autowired
    ReservaService reservaService;

    @Test
    public void contextLoads(){
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
    }

    @Test
    public void conversorTest(){
        jsonPeticion ="{\n" +
                "\t\"idUsuario\": 1,\n" +
                "\t\"idClub\": 1,\n" +
                "\t\"idPista\": 1,\n" +
                "\t\"horaInicio\":\"23/04/2019 20:00\",\n" +
                "\t\"duracion\":90,\n" +
                "\t\"flexibilidad\":2\n" +
                "}";

        try {
            Reserva reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
            Assert.assertNotEquals(null, reserva);
            Assert.assertEquals(2, reserva.getFlexibilidad());
            Assert.assertEquals(1, reserva.getIdClub());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    /**
     * Enviamos una petición de búsqueda sin informar de la hora de inicio
     */
    public void busquedaDatosMalTest(){
        jsonPeticion = "{\n" +
                "\t\"idUsuario\": 1,\n" +
                "\t\"idClub\": 1,\n" +
                "\t\"idPista\": 1,\n" +
                "\t\"horaInicio\":\"\",\n" +
                "\t\"duracion\":90,\n" +
                "\t\"flexibilidad\":2\n" +
                "}";

        Reserva reserva = null;
        try {
            reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
            Assert.assertEquals(false , reservaService.buscar(reserva).getBoolean("success"));
            Assert.assertEquals("La hora de inicio no puede enviarse vacía" , reservaService.buscar(reserva).getString("message"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * La buúsqueda de pista debe de realizarse entre las 9:00 y las 22:00 de cada día
     */
    public void busquedaDatosMalTestHora(){
        jsonPeticion ="{\n" +
                "\t\"idUsuario\": 1,\n" +
                "\t\"idClub\": 1,\n" +
                "\t\"idPista\": 1,\n" +
                "\t\"horaInicio\":\"23/04/2019 08:59\",\n" +
                "\t\"duracion\":90,\n" +
                "\t\"flexibilidad\":2\n" +
                "}";

        Reserva reserva = null;

        try {
            reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
            Assert.assertEquals(false , reservaService.buscar(reserva).getBoolean("success"));
            Assert.assertEquals("Las horas de reserva son de 9:00 a 22:00" , reservaService.buscar(reserva).getString("message"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void busquedaDatosMalDuracionTest(){
        jsonPeticion ="{\n" +
                "\t\"idUsuario\": 1,\n" +
                "\t\"idClub\": 1,\n" +
                "\t\"idPista\": 1,\n" +
                "\t\"horaInicio\":\"23/04/2019 10:00\",\n" +
                "\t\"flexibilidad\":2\n" +
                "}";

        Reserva reserva = null;

        try {
            reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
            Assert.assertEquals(false , reservaService.buscar(reserva).getBoolean("success"));
            Assert.assertEquals("La duración de la partida no puede enviarse vacía" , reservaService.buscar(reserva).getString("message"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void busquedaDatosOkTest(){
        jsonPeticion ="{\n" +
                "\t\"idUsuario\": 1,\n" +
                "\t\"idClub\": 1,\n" +
                "\t\"idPista\": 1,\n" +
                "\t\"horaInicio\":\"23/04/2019 20:00\",\n" +
                "\t\"duracion\":90,\n" +
                "\t\"flexibilidad\":2\n" +
                "}";

        Reserva reserva = null;

        try {
            reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
            Assert.assertEquals(true , reservaService.buscar(reserva).getBoolean("success"));
            Assert.assertEquals(true , ((List<RespuestaDisponibilidadPista>)reservaService.buscar(reserva).get("message")).size() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
