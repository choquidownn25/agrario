package net.proyecto.tesis.agrario.vista;

/**
 * Created by choqu_000 on 06/04/2015.
 * Se crea clase para la vista del list view a mustrar en nuestos mapas
 *
 */
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

//import com.example.jose_antonio_sarria.slidenavigation.MainActivity;
import net.proyecto.tesis.agrario.MainActivity;
//import com.example.jose_antonio_sarria.slidenavigation.R;
//import com.example.choqu_000.googlemaps.R;
import net.proyecto.tesis.agrario.listas.Lista_adaptador;
import net.proyecto.tesis.agrario.listas.Lista_entrada;
//import com.example.choqu_000.googlemaps.listas.Lista_adaptador;
import net.proyecto.tesis.agrario.mapa.MapaAnalisisdelLugar;
import net.proyecto.tesis.agrario.mapa.MapaDireccionlocalizacion;
import net.proyecto.tesis.agrario.mapa.MapaJsonLugarGeocode;
import net.proyecto.tesis.agrario.mapa.MapaPrueba;
//import com.example.jose_antonio_sarria.slidenavigation.mapa.MapaJsonLugares;
//import com.example.jose_antonio_sarria.slidenavigation.modelo.MiAdatadorExpandible;


import net.proyecto.tesis.agrario.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import com.example.jose_antonio_sarria.slidenavigation.serviciogoogle.DetalleLugarActivity;

//public class View_Lista  extends ExpandableListActivity implements OnClickListener {
public class View_Lista extends Activity implements OnClickListener {

    //Atributo
    private ListView lista;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //llama la cista
        setContentView(R.layout.listado);

        final ArrayList<Lista_entrada> datos = new ArrayList<Lista_entrada>();

        datos.add(new Lista_entrada(R.drawable.imagendecolombia, "Google Maps Version 2", "Google Maps Vercion 2."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia2, "Mapa Base", "Mapa base del sistema."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia3, "Servicio de Busqueda", "Servicio de Busqueda."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia44, "Ruta", "Ruta mas corta."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia55, "Aguas Subterraneas", "Mapa de Aguas Subterraneas."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia6, "Biodiversidad", "Mapa de Biodiversidad."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia8, "Suelo", "Mapa de Suelos Subterraneos."));
        datos.add(new Lista_entrada(R.drawable.imagencolombia7, "Vientos", "Mapa de Vientos."));
        //lo castiamos para listview
        lista = (ListView) findViewById(R.id.ListView_listado);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                    if (texto_superior_entrada != null)
                        texto_superior_entrada.setText(((Lista_entrada) entrada).getTextoEncima());

                    TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior);
                    if (texto_inferior_entrada != null)
                        texto_inferior_entrada.setText(((Lista_entrada) entrada).getTextoDebajo());

                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
                    //if (imagen_entrada != null)
                    //    imagen_entrada.setImageResource(((Lista_entrada) entrada).getIdImagen());
                }

            }
        });

        lista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion);

                switch (posicion) {
                    case 1 :
                        onClick(view);
                        break;
                    case 2:
                        subMapaDireccionLocalizacion();
                        break;

                    case 3:
                        subMapaDireccioneslocalizadas();
                        break;
                    default:
                        //si no esta la opcion mostrara un toast y nos mandara a Home
                        CharSequence texto = "Seleccionado: " + elegido.getTextoDebajo();
                        Toast toast = Toast.makeText(View_Lista.this, texto, Toast.LENGTH_LONG);
                        toast.show();

                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(View_Lista.this, MapaPrueba.class);
        startActivity(intent);
    }

    public void subMapaDireccionLocalizacion(){
        Intent intent = new Intent(View_Lista.this, MapaJsonLugarGeocode.class);
        startActivity(intent);
    }

    public void subMapaDireccioneslocalizadas(){
        Intent intent = new Intent(View_Lista.this, MapaDireccionlocalizacion.class);
        startActivity(intent);
    }

}