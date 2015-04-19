package net.proyecto.tesis.agrario.dispositivos.webcam;

/**
 * Created by choqu_000 on 17/04/2015.
 * clase que me controla la web cam desde mis dispositivo smarphone
 */


import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import net.proyecto.tesis.agrario.R;

public class WebCam extends Activity{

    //Atributos
    private ImageView webCamViewer;
    private Button refreshButton;
    private static String IMAGE_URL = "http://itwebcammh.fullerton.edu/axis-cgi/jpg/image.cgi?resolution=480x320";
    private static Bitmap BROKEN_IMAGE;//Acceso a la imagen

    /** Se llama cuando se creó por primera vez la actividad. */

    public void onCreate(Bundle saveInstanceStated){
        super.onCreate(saveInstanceStated);
        setContentView(R.layout.camara);

        // inicializar imagen de error que se muestra
        BROKEN_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.dlerror);
        //inicializa imagen de los controles
        InicializaControles();
    }

    //Metodo para inicializar controles
    private void InicializaControles() {

        // referencia a las vistas en el diseño principal
        refreshButton = (Button) findViewById(R.id.refreshButton);
        //intaciar la imagen
        webCamViewer = (ImageView) findViewById(R.id.webCamImage);

        //Registrar una devolución de llamada que se invoca cuando se hace clic en este punto de vista.


        webCamViewer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNuevaImagen();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getNuevaImagen();
            }
        });
    }

    public void getNuevaImagen() {
        // obtener la primera imagen
        new DownloadImagesTask().execute(webCamViewer);
    }

    public class DownloadImagesTask extends AsyncTask<ImageView, Void, Bitmap> {
        //Atributo
        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageView... imageViews) {
            this.imageView = imageViews[0];
            Bitmap img = null;

            img = download_Image(IMAGE_URL);

            // mapas de bits de carga de la red puede ser un poco lento a veces.
            // así que tratamos de descargarlo de nuevo ..
            if (img == null) {
                img = download_Image(IMAGE_URL);

            }


            //si la imagen aún no se ha cargado establecimos imagen a nuestra imagen rota
            if (img == null) {
                img = BROKEN_IMAGE;
            }

            return img;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                imageView.setImageBitmap(result);
            } catch (Exception ex) {
                imageView.setImageBitmap(result);
            }
        }

        //metodo para cargar la imagen
        private Bitmap download_Image(String url) {
            try {
                //declarar la nueva url
                URL imageURL = new URL(url);

                //declara una coneccion
                URLConnection conn;

                //abre la conecion
                conn = imageURL.openConnection();

                //connect
                conn.connect();

                //instancia al stream
                final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

                final Bitmap bitmap = BitmapFactory.decodeStream(bis);//objetos que crea viaras fuentes de imgen y mapas

                return bitmap;

            } catch (Exception ex) {
                Log.e("download_Image", ex.getMessage());
            }
            return null;
        }
    }
}
