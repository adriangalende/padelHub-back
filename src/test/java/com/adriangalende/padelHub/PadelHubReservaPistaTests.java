package com.adriangalende.padelHub;

import com.adriangalende.padelHub.model.Reserva;
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

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PadelHubReservaPistaTests {
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
    public void conversorTest() throws IOException {
        jsonPeticion = "{\n" +
                "    \"idPista\": \"5\",\n" +
                "    \"idUsuario\":\"25\",\n" +
                "    \"nombre\": \"Pista5 Test\",\n" +
                "    \"idClub\": \"1\",\n" +
                "    \"tipoPista\": \"aire libre\",\n" +
                "    \"rutaImagenes\": \"\",\n" +
                "    \"precio\": \"0.0\",\n" +
                "    \"precioLuz\": \"\",\n" +
                "    \"horaInicio\": \"04/05/2019 08:00\",\n" +
                "    \"duracion\": \"90\"\n" +
                "}";

        Reserva reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
        Assert.assertEquals(5, reserva.getIdPista());

    }

    @Test
    public void reservaPistaMalIdClubTest() throws IOException, JSONException {
        jsonPeticion = "{\n" +
                "    \"idPista\": \"5\",\n" +
                "    \"idUsuario\":\"25\",\n" +
                "    \"nombre\": \"Pista5 Test\",\n" +
                "    \"tipoPista\": \"aire libre\",\n" +
                "    \"rutaImagenes\": \"\",\n" +
                "    \"precio\": \"0.0\",\n" +
                "    \"precioLuz\": \"\",\n" +
                "    \"horaInicio\": \"04/05/2019 12:00\",\n" +
                "    \"duracion\": \"90\"\n" +
                "}";

        Reserva reserva = null;
        reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
        JSONObject jsonObject =  new JSONObject();
        jsonObject = reservaService.reservar(reserva);
        Assert.assertEquals(false, jsonObject.getBoolean("success"));
    }

    @Test
    public void reservaPistaMalHoraTest() throws JSONException, IOException {
        jsonPeticion = "{\n" +
                "    \"idPista\": \"5\",\n" +
                "    \"idUsuario\":\"25\",\n" +
                "    \"nombre\": \"Pista5 Test\",\n" +
                "    \"idClub\": \"1\",\n" +
                "    \"tipoPista\": \"aire libre\",\n" +
                "    \"rutaImagenes\": \"\",\n" +
                "    \"precio\": \"0.0\",\n" +
                "    \"precioLuz\": \"\",\n" +
                "    \"horaInicio\": \"04/05/2019 08:00\",\n" +
                "    \"duracion\": \"90\"\n" +
                "}";

        Reserva reserva = null;

        reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
        JSONObject jsonObject =  new JSONObject();
        jsonObject = reservaService.reservar(reserva);
        System.out.println(jsonObject.toString());
        Assert.assertEquals(false, jsonObject.getBoolean("success"));
        Assert.assertEquals("Las horas de reserva son de 9:00 a 22:00", jsonObject.getString("message"));
    }


    @Test
    public void reservaPistaMalHoraPasadoTest() throws JSONException, IOException {
        jsonPeticion = "{\n" +
                "    \"idPista\": \"5\",\n" +
                "    \"idUsuario\":\"25\",\n" +
                "    \"nombre\": \"Pista5 Test\",\n" +
                "    \"idClub\": \"1\",\n" +
                "    \"tipoPista\": \"aire libre\",\n" +
                "    \"rutaImagenes\": \"\",\n" +
                "    \"precio\": \"0.0\",\n" +
                "    \"precioLuz\": \"\",\n" +
                "    \"horaInicio\": \"04/05/2019 23:00\",\n" +
                "    \"duracion\": \"90\"\n" +
                "}";

        Reserva reserva = null;

        reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
        JSONObject jsonObject =  new JSONObject();
        jsonObject = reservaService.reservar(reserva);
        System.out.println(jsonObject.toString());
        Assert.assertEquals(false, jsonObject.getBoolean("success"));
        Assert.assertEquals("Las horas de reserva son de 9:00 a 22:00", jsonObject.getString("message"));
    }

    @Test
    public void reservaPistaMalYaReservadaTest() throws JSONException, IOException {
        jsonPeticion = "{\n" +
                "    \"idPista\": \"5\",\n" +
                "    \"idUsuario\":\"25\",\n" +
                "    \"nombre\": \"Pista5 Test\",\n" +
                "    \"idClub\": \"1\",\n" +
                "    \"tipoPista\": \"aire libre\",\n" +
                "    \"rutaImagenes\": \"\",\n" +
                "    \"precio\": \"0.0\",\n" +
                "    \"precioLuz\": \"\",\n" +
                "    \"horaInicio\": \"04/05/2019 12:00\",\n" +
                "    \"duracion\": \"90\"\n" +
                "}";

        Reserva reserva = null;

        reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
        JSONObject jsonObject =  new JSONObject();
        jsonObject = reservaService.reservar(reserva);
        System.out.println(jsonObject.toString());
        Assert.assertEquals(false, jsonObject.getBoolean("success"));
        Assert.assertEquals("Ups! parece que alguien se te ha adelantado!", jsonObject.getString("message"));
    }

    @Test
    public void reservaPistaOkTest() throws JSONException, IOException {
        jsonPeticion = "{\n" +
                "    \"idPista\": \"5\",\n" +
                "    \"idUsuario\":\"25\",\n" +
                "    \"nombre\": \"Pista5 Test\",\n" +
                "    \"idClub\": \"1\",\n" +
                "    \"tipoPista\": \"aire libre\",\n" +
                "    \"rutaImagenes\": \"\",\n" +
                "    \"precio\": \"0.0\",\n" +
                "    \"precioLuz\": \"\",\n" +
                "    \"horaInicio\": \"04/05/2019 14:00\",\n" +
                "    \"duracion\": \"90\"\n" +
                "}";

        Reserva reserva = null;

        reserva = objectMapper.readValue(jsonPeticion, Reserva.class);
        JSONObject jsonObject =  new JSONObject();
        jsonObject = reservaService.reservar(reserva);
        System.out.println(jsonObject.toString());
        Assert.assertEquals(true, jsonObject.getBoolean("success"));
        Assert.assertNotEquals(0.0, ((Reserva)jsonObject.get("message")).getPrecio());
    }

}
