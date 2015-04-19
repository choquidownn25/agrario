package net.proyecto.tesis.agrario.listas;

/**
 * Created by choqu_000 on 06/04/2015.
 * Clase en donde colocamos la posistions
 */

public class Lista_entrada {
    //Atributos
    private int idImagen; //Imagen
    private String textoEncima; //Texto arriba
    private String textoDebajo; //Texto Abajo

    //Poliformismo
    public Lista_entrada(int idImagen, String textoEncima, String textoDebajo){
        //Parametros
        this.setIdImagen(idImagen);
        this.setTextoEncima(textoEncima);
        this.setTextoDebajo(textoDebajo);
    }

    /*Encapsulamiento */
    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getTextoEncima() {
        return textoEncima;
    }

    public void setTextoEncima(String textoEncima) {
        this.textoEncima = textoEncima;
    }

    public String getTextoDebajo() {
        return textoDebajo;
    }

    public void setTextoDebajo(String textoDebajo) {
        this.textoDebajo = textoDebajo;
    }
}