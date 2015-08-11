package net.proyecto.tesis.agrario.validacion;

/**
 * Created by choqu_000 on 05/07/2015.
 */

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.proyecto.tesis.agrario.MainActivity;
import net.proyecto.tesis.agrario.R;


public class Loguin extends ActionBarActivity{

    //Atributos

    Button BotonEntrada; //Butom
    private Button update;
    EditText txtetUserName,txtpass; //Textobox
    TextView tv;
    HttpPost httppost; //Http Post de
    StringBuffer buffer; //Creacion de Cadena
    HttpResponse response; //sistema de archivos para que puedan ser reutilizados
    HttpClient httpclient; //Hilo de seguridad clientes HTTP depende de la implementación y configuración del cliente específico.
    List<NameValuePair> nameValuePairs; //parametro de valor
    ProgressDialog dialog = null; //MEnsaje de Dialogo

    //Creacion de la actividad
    protected void onCreator(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_loguin);

        //Campos de entrada de configuración
        BotonEntrada = (Button)findViewById(R.id.login);
        txtetUserName = (EditText)findViewById(R.id.username);
        txtpass= (EditText)findViewById(R.id.password);
        tv = (TextView)findViewById(R.id.tv);

        BotonEntrada.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(Loguin.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        login();
                    }
                }).start();
            }
        });

    }

    //Metodo validar loguin
    void login(){
        try{

            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://192.168.1.124/webserviceloguin/check.php"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Utilice siempre el mismo nombre de variable por publicar es decir,
            // el nombre de la variable lado androide y lado php nombre de la variable debe ser similar,
            nameValuePairs.add(new BasicNameValuePair("username",txtetUserName.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password",txtpass.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            // encapsula el proceso de generación de un objeto de respuesta de un HttpResponse
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    tv.setText("Respuesta de PHP : " + response);
                    dialog.dismiss();
                }
            });

            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Loguin.this,"Login Success", Toast.LENGTH_SHORT).show();
                    }
                });

                //Inten para formulario
                Intent i = new Intent(Loguin.this, MainActivity.class);
                startActivity(i);
            }else{
                showAlert();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    //Metodo para mostrar el metodo o mensaje de ingresado o autulizado
    public void showAlert(){
        Loguin.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Loguin.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
