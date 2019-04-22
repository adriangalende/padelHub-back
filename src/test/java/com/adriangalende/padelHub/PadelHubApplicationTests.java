package com.adriangalende.padelHub;

import com.adriangalende.padelHub.model.Usuarios;
import com.adriangalende.padelHub.service.UsuariosService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
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
public class PadelHubApplicationTests {

	private ObjectMapper objectMapper = new ObjectMapper();
	private  String jsonUsuario = "";

	@Autowired
	UsuariosService usuariosService;


	@Test
	public void contextLoads() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
	}

	@Test
	public void registroTestDatosOK(){
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Rafa Sánchez\",\n" +
				"\t\"telefono\":\"696162444\",\n" +
				"\t\"password\":\"rafa\",\n" +
				"\t\"email\":\"rafaSanchez@gmail.com\"\n" +
				"}";

		try {
			Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
			Assert.assertNotEquals(null, usuario);
			Assert.assertEquals("Rafa Sánchez", usuario.getNombre());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void registroTestDatosVacios() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Rafa Sánchez\",\n" +
				"\t\"telefono\":\"696162444\",\n" +
				"\t\"password\":\"rafa\",\n" +
				"\t\"email\":\"rafaSanchez.com\"\n" +
				"}";

		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		Assert.assertNotEquals(null, usuario);
		Assert.assertEquals("Rafa Sánchez", usuario.getNombre());

		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertNotEquals(null, respuesta);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
	}

	@Test
	public void registroTestEmailMal() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Rafa Sánchez\",\n" +
				"\t\"telefono\":\"696162444\",\n" +
				"\t\"password\":\"rafa\",\n" +
				"\t\"email\":\"\"\n" +
				"}";

		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertNotEquals(null, respuesta);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
		Assert.assertEquals("Error al validar el email", respuesta.getString("message"));

	}

	@Test
	public void registroTestMovilMal() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Rafa Sánchez\",\n" +
				"\t\"telefono\":\"69616\",\n" +
				"\t\"password\":\"rafa\",\n" +
				"\t\"email\":\"rafaSanchez@gmail.com\"\n" +
				"}";
		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertNotEquals(null, respuesta);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
		Assert.assertEquals("Error al validar el móvil", respuesta.getString("message"));
	}

	@Test
	public void registroTestUsuarioVacio() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"\",\n" +
				"\t\"telefono\":\"696162444\",\n" +
				"\t\"password\":\"rafa\",\n" +
				"\t\"email\":\"rafaSanchez@gmail.com\"\n" +
				"}";
		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertNotEquals(null, respuesta);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
		Assert.assertEquals("El nombre de usuario no puede estar vacío", respuesta.getString("message"));
	}


	@Test
	public void registroTestUsuarioExisteMovil() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Pepito Pérez\",\n" +
				"\t\"telefono\":\"696162444\",\n" +
				"\t\"password\":\"pepito\",\n" +
				"\t\"email\":\"pepito@gmail.com\"\n" +
				"}";
		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertNotEquals(null, respuesta);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
		Assert.assertEquals("Ya existe un usuario con el mismo número o email", respuesta.getString("message"));
	}

	@Test
	public void registroTestUsuarioExisteEmail() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Pepito Pérez\",\n" +
				"\t\"telefono\":\"696162412\",\n" +
				"\t\"password\":\"pepito\",\n" +
				"\t\"email\":\"rafaSanchez@gmail.com\"\n" +
				"}";
		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertNotEquals(null, respuesta);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
		Assert.assertEquals("Ya existe un usuario con el mismo número o email", respuesta.getString("message"));
	}

	@Test
	public void registroTestUsuarioDatosMal() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Nombre Priape\",\n" +
				"\t\"telefono\":\"696162422\",\n" +
				"\t\"password\":\"pepito\",\n" +
				"\t\"idClub\":\"10\",\n" +
				"\t\"email\":\"nomPriape@gmail.com\"\n" +
				"}";
		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertEquals(false, respuesta.getBoolean("success"));
	}

	@Test
	public void registroTestUsuarioDatosOk() throws IOException, JSONException {
		jsonUsuario = "{\n" +
				"\t\"nombre\": \"Nombre Priape\",\n" +
				"\t\"telefono\":\"696162488\",\n" +
				"\t\"password\":\"pepito\",\n" +
				"\t\"email\":\"nomPriape@gmail.com\"\n" +
				"}";
		Usuarios usuario = objectMapper.readValue(jsonUsuario, Usuarios.class);
		JSONObject respuesta = usuariosService.alta(usuario);
		Assert.assertEquals(true, respuesta.getBoolean("success"));
		Assert.assertEquals("OK", respuesta.getString("message"));
	}

}
