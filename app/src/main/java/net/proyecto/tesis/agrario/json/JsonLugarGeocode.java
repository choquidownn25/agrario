package net.proyecto.tesis.agrario.json;
//Paquete
/**
 * Created by choqu_000 on 06/04/2015.
 * Clase json para json geo codigo
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonLugarGeocode {

    /** Recibe el objeto Json y lo retorna a la lista */
    public List<HashMap<String,String>> analizar(JSONObject jObject){

        JSONArray jLugars = null;
        try {
            /** Recibe todos los elementos lugares del array */
            jLugars = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoca objeto del lugar
         * Donde CAda objeto invoca lugar
         */
        return getLugars(jLugars);
    }

    /** Objeto lista que trae los parametros */
    private List<HashMap<String, String>> getLugars(JSONArray jLugars){
        int lugarsCount = jLugars.length();
        List<HashMap<String, String>> lugarsList = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> lugar = null;
        /** Tomando cada lugar, analiza y añade a la lista de objetos */
        for(int i=0; i<lugarsCount;i++){
            try {
                /** Objeto Json Analiza lugar */
                lugar = getLugar((JSONObject)jLugars.get(i));
                lugarsList.add(lugar);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return lugarsList;
    }

    /** Analizando el Objeto Json */
    private HashMap<String, String> getLugar(JSONObject jLugar){

        HashMap<String, String> lugar = new HashMap<String, String>();
        String formatted_address = "-NA-";
        String lat="";
        String lng="";


        try {
            // Estrae wl nombre del lugar
            if(!jLugar.isNull("formatted_address")){
                formatted_address = jLugar.getString("formatted_address");
            }
            // geometria de puntos
            lat = jLugar.getJSONObject("geometry").getJSONObject("location").getString("lat");
            lng = jLugar.getJSONObject("geometry").getJSONObject("location").getString("lng");


            lugar.put("formatted_address", formatted_address);
            lugar.put("lat", lat);
            lugar.put("lng", lng);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lugar;
    }


}
