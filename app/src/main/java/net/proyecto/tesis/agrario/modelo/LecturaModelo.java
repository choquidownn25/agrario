package net.proyecto.tesis.agrario.modelo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import net.proyecto.tesis.agrario.R;
import net.proyecto.tesis.agrario.datos.Modelo;

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
 * Created by choqu_000 on 03/09/2015.
 */
public class LecturaModelo extends ActionBarActivity {

    //Atributos
    private String url = "http://192.168.1.124/ServiceTesis/CaracteristicasGeometricas.php";

    private TextView numerodetalleafro;
    private TextView CodAforo;
    private TextView DPR;
    private TextView Perimetro_Mojado;
    private TextView Radio_Hidraulico;
    private TextView Rdossobretres;
    private TextView isobren;
    private TextView Pt;
    private TextView Anchos;
    private TextView Areas;
    private TextView Caudales;
    private TextView Velocidadmediaprpmedia;
    private TextView Vmv;
    private TextView porcentajeprofundidadmedia;
    private String jsonResult;
    private Modelo modeloRasberry;
    private int posicion=0;
    private List<Modelo> listaModeloRasberry;
    private ImageButton mas;
    private ImageButton menos;
    private TextView evento;
    //Atributos para la lista
    List<String> list = new ArrayList<String>();

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_modelo);

        //CAsting servicios
        numerodetalleafro = (TextView)findViewById(R.id.textViewnumerodetalleafro);
        CodAforo=(TextView)findViewById(R.id.textViewCodAforo);
        DPR = (TextView)findViewById(R.id.textViewDPR);
        Perimetro_Mojado = (TextView)findViewById(R.id.textViewPerimetro_Mojado);
        Radio_Hidraulico =(TextView)findViewById(R.id.textViewRadio_Hidraulico);
        Rdossobretres = (TextView)findViewById(R.id.textViewRdossobretres);
        isobren = (TextView)findViewById(R.id.textViewisobren);
        Pt = (TextView)findViewById(R.id.textViewPt);
        Anchos = (TextView)findViewById(R.id.textViewAnchos);
        Areas = (TextView) findViewById(R.id.textViewAreas);
        Caudales = (TextView) findViewById(R.id.textViewCaudales);
        Vmv = (TextView) findViewById(R.id.textViewVmv);
        Velocidadmediaprpmedia = (TextView) findViewById(R.id.textViewporcentajeprofundidadmedia);
        porcentajeprofundidadmedia = (TextView)findViewById(R.id.textViewporcentajeprofundidadmedia);
        //lista
        listaModeloRasberry = new ArrayList<Modelo>();
        WebService();

        mas=(ImageButton)findViewById(R.id.mas);
        //Evenco del botom
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Si lista sensor es diferente a la lista con valor boolean
                if (!listaModeloRasberry.isEmpty()){
                    //Si la lista posicion mayor o igual
                    if(posicion>=listaModeloRasberry.size()-1){
                        posicion=listaModeloRasberry.size()-1;
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
                if (!listaModeloRasberry.isEmpty()) {
                    //Si la lista posicion mayor o igual
                    if (posicion <= 0) {
                        posicion = 0;
                        mostrarDato(posicion);
                    } else {
                        posicion--;
                        mostrarDato(posicion);
                    }
                }

            }
        });
    }
    //Metodo de acceso webservice
    public void WebService(){
        JsonModelo task = new JsonModelo();
        task.execute(new String []{
                url
        });
    }

    //Metodo del recorrido del web service
    private String mostrar(){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://192.168.1.124/ServiceTesis/CaracteristicasGeometricas.php");
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
    protected void mostrarDato( final int posicion){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Modelo personas = listaModeloRasberry.get(posicion);
                double Dato_Numero_sensor_estacion = personas.getNumerodetalleafro();
                //numero_sensor_estacion.setText(String.valueOf(Dato_Numero_sensor_estacion));
                numerodetalleafro.setText(String.valueOf(Dato_Numero_sensor_estacion));
                int Dato_CodAforo=personas.getCodAforo();
                //Casting   anno.setText(sensorRasberry.getAnno());
                CodAforo.setText(String.valueOf(Dato_CodAforo));
                int Dato_DPR = personas.getDPR();
                DPR.setText(String.valueOf(Dato_DPR));
                double Dato_Perimetro_Mojado = personas.getPerimetro_Mojado();
                Perimetro_Mojado.setText(String.valueOf(Dato_Perimetro_Mojado));
                double Dato_Radio_Hidraulico = personas.getRadio_Hidraulico();
                Radio_Hidraulico.setText(String.valueOf(Dato_Radio_Hidraulico));
                double Dato_Rdossobretres = personas.getRdossobretres();
                Rdossobretres.setText(String.valueOf(Dato_Rdossobretres));
                double Dato_isobren = personas.getIsobren();
                isobren.setText(String.valueOf(Dato_isobren));
                double Dato_Pt = personas.getPt();
                Pt.setText(String.valueOf(Dato_Pt));
                double Dato_Anchos = personas.getAnchos();
                Anchos.setText(String.valueOf(Dato_Anchos));
                double Dato_Areas = personas.getAreas();
                Areas.setText(String.valueOf(Dato_Areas));
                double Dato_Caudales = personas.getCaudales();
                Caudales.setText(String.valueOf(Dato_CodAforo));
                double Dato_Velocidadmediaprpmedia = personas.getVelocidadmediaprpmedia();
                Velocidadmediaprpmedia.setText(String.valueOf(Dato_Velocidadmediaprpmedia));
                double Dato_Vmv = personas.getVmv();
                Vmv.setText(String.valueOf(Dato_Vmv));
                double Dato_porcentajeprofundidadmedia = personas.getPorcentajeprofundidadmedia();
                porcentajeprofundidadmedia.setText(String.valueOf(Dato_porcentajeprofundidadmedia));
            }
        });
    }

    protected boolean filtrarDatos() {
        listaModeloRasberry.clear();
        String data=mostrar();
        if (!data.equalsIgnoreCase("")){
            JSONObject json;
            try {
                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("geometria");
                 for (int i = 0; i < jsonArray.length(); i++) {
                    modeloRasberry=new Modelo();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                    //Sensor personas = listaSensorRasberry.get(posicion);
                    //numero_sensor_estacion.setText(sensorRasberry.getNumero_sensor_estacion());
                    //int Datonumero_sensor_estacion = Integer.parseInt(personas.getNumero_sensor_estacion());
                    //numero_sensor_estacion.setText(sensorRasberry.getNumero_sensor_estacion());

                    modeloRasberry.setNumerodetalleafro(jsonArrayChild.optInt("numerodetalleafro"));
                    modeloRasberry.setCodAforo(jsonArrayChild.optInt("CodAforo"));
                    modeloRasberry.setDPR(jsonArrayChild.optInt("DPR"));
                    modeloRasberry.setPerimetro_Mojado(jsonArrayChild.optDouble("Perimetro_Mojado"));
                    modeloRasberry.setRadio_Hidraulico(jsonArrayChild.optDouble("Radio_Hidraulico"));
                    modeloRasberry.setRdossobretres(jsonArrayChild.optDouble("Rdossobretres"));
                    modeloRasberry.setIsobren(jsonArrayChild.optDouble("isobren"));
                    modeloRasberry.setPt(jsonArrayChild.optDouble("Pt"));

                    modeloRasberry.setAnchos(jsonArrayChild.optDouble("Anchos"));
                    modeloRasberry.setAreas(jsonArrayChild.optDouble("Areas"));
                    modeloRasberry.setCaudales(jsonArrayChild.optDouble("Caudales"));
                    modeloRasberry.setVelocidadmediaprpmedia(jsonArrayChild.optDouble("Velocidadmediaprpmedia"));
                    modeloRasberry.setVmv(jsonArrayChild.optDouble("Vmv"));
                    modeloRasberry.setPorcentajeprofundidadmedia(jsonArrayChild.optDouble("porcentajeprofundidadmedia"));
                    //modeloRasberry.setPt(jsonArrayChild.optDouble("Pt"));


                    listaModeloRasberry.add(modeloRasberry);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


    /**
     * Created by choqu_000 on 12/09/2015.
     * Clase json para capturar el modelo de datos en web
     */
    public class JsonModelo extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            if(filtrarDatos()){
               mostrarDato(posicion );
            }
            return null;
        }
    }
}
