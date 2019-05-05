package com.adriangalende.padelHub.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern MOVIL_REGEX = Pattern.compile("(\\+34|0034|34)?[ -]*(6|7)([0-9]){2}[ -]?(([0-9]){2}[ -]?([0-9]){2}[ -]?([0-9]){2}|([0-9]){3}[ -]?([0-9]){3})", Pattern.CASE_INSENSITIVE);
    public static final String DEFAULT_TIMEZONE = "Europe/Madrid";
    public static final int DEFAULT_TIPO_RESERVA = 1;


    public static boolean validar(String elemento, Pattern pattern){
        Matcher matcher = pattern.matcher(elemento);
        return matcher.find();
    }

    public static JSONObject jsonResponseSetter(Boolean succes, String message) {
        JSONObject jsonResponse = new JSONObject();
        try {
            jsonResponse.put("success", succes);
            jsonResponse.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

}
