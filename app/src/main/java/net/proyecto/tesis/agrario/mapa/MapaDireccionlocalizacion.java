package net.proyecto.tesis.agrario.mapa;

/**
 * Created by choqu_000 on 11/04/2015.
 * Clase mapa para la direccion y localizacion de un punto en la polilinea
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import net.proyecto.tesis.agrario.R;
import net.proyecto.tesis.agrario.json.JsonAnalizarDireccion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapaDireccionlocalizacion extends FragmentActivity implements LocationListener {

    GoogleMap mGoogleMap;
    ArrayList<LatLng> mMarkerPoints;
    double mLatitude=0;
    double mLongitude=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapadireccionesdelocalizaciones);

        // Disponibilidad de Google play
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status!=ConnectionResult.SUCCESS){ // Si el servicio google play no esta disponible

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Servicio Google Play Disponible

            // Inicializa Array
            mMarkerPoints = new ArrayList<LatLng>();

            // Obtención de referencia a SupportMapFragment de la activity_main
            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

            // Obtener mapas para el SupportMapFragment
            mGoogleMap = fm.getMap();

            // Activar mi ubicación del botón en el mapa
            mGoogleMap.setMyLocationEnabled(true);

            // Conseguir objeto LocationManager de Sistema de Servicio LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creación de un objeto de criterios para recuperar proveedor
            Criteria criteria = new Criteria();

            // Obtener el nombre de los mejores proveedores
            String provider = locationManager.getBestProvider(criteria, true);

            // Obtener la ubicación actual En GPS
            Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);

            // Configuración onclick detector de eventos para el mapa
            mGoogleMap.setOnMapClickListener(new OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already map contain destination location
                    if(mMarkerPoints.size()>1){

                        FragmentManager fm = getSupportFragmentManager();
                        mMarkerPoints.clear();
                        mGoogleMap.clear();
                        LatLng startPoint = new LatLng(mLatitude, mLongitude);

                        // draw the marker at the current position
                        drawMarker(startPoint);
                    }

                    // dibuja el marcador en el lugar actualmente tocado
                    drawMarker(point);

                    // Cheques, si ubicaciones de inicio y fin son capturados
                    if(mMarkerPoints.size() >= 2){
                        LatLng origin = mMarkerPoints.get(0);
                        LatLng dest = mMarkerPoints.get(1);

                        // Obtener URL para el Google Directions API
                        String url = getDirectionsUrl(origin, dest);

                        DownloadTask downloadTask = new DownloadTask();

                        // Iniciar la descarga de datos JSON de Google Directions API
                        downloadTask.execute(url);
                    }
                }
            });
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origen de la ruta
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destino de la Ruta
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor Activado
        String sensor = "sensor=false";

        // La construcción de los parámetros para el servicio web
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // formato de salida
        String output = "json";

        // La construcción de la URL al servicio Web
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** Un método para descargar datos JSON de url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            //  Creación de una conexión HTTP para comunicarse con url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Conexión a url
            urlConnection.connect();

            // Lectura de datos de url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** Una clase para descargar datos de Google Direcciones URL */
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // La descarga de datos en el hilo no ui
        @Override
        protected String doInBackground(String... url) {

            // Para el almacenamiento de datos de servicio web
            String data = "";

            try{
                // Extrayendo los datos de servicio web
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Ejecutar en hilo de interfaz de usuario, tras la ejecución de
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** Una clase para analizar las Direcciones de Google en formato JSON */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Analizando los datos de la rosca no ui
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                JsonAnalizarDireccion parser = new JsonAnalizarDireccion();

                // Inicia datos analizar
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Ejecutar en hilo de interfaz de usuario, después de que el proceso de análisis
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            //Atravesando por toda la ruta
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Obtención de la ruta ruta
                List<HashMap<String, String>> path = result.get(i);

                // Obtención de todos los puntos de ruta de orden i
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adición de todos los puntos de la ruta a Opciones de línea
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Dibujo polilínea en el mapa de Google para la ruta de orden i
            mGoogleMap.addPolyline(lineOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto agrega elementos a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void drawMarker(LatLng point){
        mMarkerPoints.add(point);

        // Crear MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Ajuste de la posición del marcador
        options.position(point);

        /**
         *   Para conocer la ubicación de inicio, el color del marcador es verde y
         * Para la ubicación final, el color del marcador es ROJO.
         */
        if(mMarkerPoints.size()==1){
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }else if(mMarkerPoints.size()==2){
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Añadir nuevo marcador para la API V2 Google Map Android
        mGoogleMap.addMarker(options);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Dibujar el marcador, si no se establece la ubicación de destino
        if(mMarkerPoints.size() < 2){

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            LatLng point = new LatLng(mLatitude, mLongitude);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            drawMarker(point);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}

