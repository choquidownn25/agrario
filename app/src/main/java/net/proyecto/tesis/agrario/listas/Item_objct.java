package net.proyecto.tesis.agrario.listas;

/**
 * Created by choqu_000 on 06/04/2015.
 * Clase para l texto y la imagen del list view
 */

public class Item_objct {
    private String titulo;
    private int icono;
    public Item_objct(String title, int icon) {
        this.titulo = title;
        this.icono = icon;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public int getIcono() {
        return icono;
    }
    public void setIcono(int icono) {
        this.icono = icono;
    }
}