package net.proyecto.tesis.agrario.sensor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import net.proyecto.tesis.agrario.R;

/**
 * Created by choqu_000 on 22/08/2015.
 * Clase donde creamos el web service
 *
 */
public class LenturaSensorRasbian extends Activity {

    //Atributos
    private TextView anno;

    //Creamos la Vista
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.service_sensor_rasberrypi);

        //
        anno = (TextView)findViewById(R.id.textViewanno);
    }
}
