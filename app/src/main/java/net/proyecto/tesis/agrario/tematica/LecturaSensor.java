package net.proyecto.tesis.agrario.tematica;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.proyecto.tesis.agrario.R;
import net.proyecto.tesis.agrario.datos.Sensor;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by choqu_000 on 30/08/2015.
 * Clase para capturar servicios web service
 *
 */
public class LecturaSensor extends Activity  {
    //Atributos
    private String url = "http://192.168.1.124/ServiceTesis/transmicionsensorestacion.php";
    //private String url = "http://informaticaintegral.co/ServiceTesis/transmicionsensorestacion.php";
    private TextView numero_sensor_estacion;
    private TextView anno;
    private TextView mes;
    private TextView dia;
    private TextView hora;
    private TextView humedad;
    private TextView temperatura;
    private String jsonResult;
    private Sensor sensorRasberry;
    private int posicion=0;
    private List<Sensor> listaSensorRasberry;
    private ImageButton mas;
    private ImageButton menos;
    private TextView evento;
    //Atributos para la lista
    List<String> list = new ArrayList<String>();

    //Creacion de la vista
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.service_sensor_rasberrypi);

        //Casting para los servicios
        numero_sensor_estacion=(TextView)findViewById(R.id.textnumero_sensor_estacion);
        anno = (TextView)findViewById(R.id.textViewanno);
        mes = (TextView)findViewById(R.id.textmes);
        dia = (TextView)findViewById(R.id.textViewdia);
        hora = (TextView)findViewById(R.id.textViewhora);
        humedad = (TextView)findViewById(R.id.textViewHumedad);
        temperatura = (TextView)findViewById(R.id.textViewTemperatura);
        //lista
        listaSensorRasberry = new ArrayList<Sensor>();

        accessWebService();

        mas=(ImageButton)findViewById(R.id.mas);
        //Evenco del botom
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si lista sensor es diferente a la lista con valor boolean
                if (!listaSensorRasberry.isEmpty()){
                    //Si la lista posicion mayor o igual
                    if(posicion>=listaSensorRasberry.size()-1){
                        posicion=listaSensorRasberry.size()-1;
                        mostrarDato(posicion);
                    }else {
                        posicion ++;
                        mostrarDato(posicion);
                    }
                }
            }

        });

        menos = (ImageButton)findViewById(R.id.menos);
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si lista sensor es diferente a la lista con valor boolean
                if (!listaSensorRasberry.isEmpty()){
                    //Si la lista posicion mayor o igual
                    if(posicion<=0){
                        posicion=0;
                        mostrarDato(posicion);
                    }else {
                        posicion --;
                        mostrarDato(posicion);
                    }
                }

            }
        });
    }

    //MEtodo de acceso al web serive
    public void accessWebService(){
        Mostrar task = new Mostrar();
        task.execute(new String []{
                url

        });
    }

    //Metodo del recorrido del web service
    private String mostrar(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://192.168.1.124/ServiceTesis/transmicionsensorestacion.php");
        //HttpPost httppost = new HttpPost("http://informaticaintegral.co/ServiceTesis/transmicionsensorestacion.php");
        String resultado="";
        HttpResponse response;
        try {
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            resultado= convertStreamToString(instream);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultado;
    }

    //Medoto para la escritura del web service
    private String convertStreamToString(InputStream instream) throws IOException {

        if (instream != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(instream, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e){
             e.printStackTrace();
            } finally {
                instream.close();
            }
            return sb.toString();
        } else {
            return "";
        }

    }

    //Mostrar las personas
    protected void mostrarDato(final int posicion){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Sensor personas = listaSensorRasberry.get(posicion);
                int Dato_Numero_sensor_estacion = personas.getNumero_sensor_estacion();
                numero_sensor_estacion.setText(String.valueOf(Dato_Numero_sensor_estacion));
               // numero_sensor_estacion.setText(sensorRasberry.getNumero_sensor_estacion());
                anno.setText(sensorRasberry.getAnno());
                mes.setText(sensorRasberry.getMes());
                dia.setText(sensorRasberry.getDia());
                hora.setText(sensorRasberry.getHora());
                humedad.setText(sensorRasberry.getHumedad());
                temperatura.setText(sensorRasberry.getTemperatura());
            }
        });
    }

    private boolean filtrarDatos() {
        listaSensorRasberry.clear();
        String data=mostrar();
        if (!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("geometria");
                for (int i = 0; i < jsonArray.length(); i++) {
                    sensorRasberry=new Sensor();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                    //Sensor personas = listaSensorRasberry.get(posicion);
                    //numero_sensor_estacion.setText(sensorRasberry.getNumero_sensor_estacion());
                    //int Datonumero_sensor_estacion = Integer.parseInt(personas.getNumero_sensor_estacion());
                    //numero_sensor_estacion.setText(sensorRasberry.getNumero_sensor_estacion());

                    sensorRasberry.setNumero_sensor_estacion(jsonArrayChild.optInt("numero_sensor_estacion"));
                    sensorRasberry.setAnno(jsonArrayChild.optString("anno"));
                    sensorRasberry.setMes(jsonArrayChild.optString("mes"));
                    sensorRasberry.setDia(jsonArrayChild.optString("dia"));
                    sensorRasberry.setHora(jsonArrayChild.optString("hora"));
                    sensorRasberry.setHumedad(jsonArrayChild.optString("humedad"));
                    sensorRasberry.setTemperatura(jsonArrayChild.optString("temperatura"));

                    listaSensorRasberry.add(sensorRasberry);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    //Clase Json
    class Mostrar extends AsyncTask <String, String, String>{

        @Override
        protected String doInBackground(String... params) {

            //LogicAa
            if (filtrarDatos())
                mostrarDato(posicion);
            return null;
        }
    }


}