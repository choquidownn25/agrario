package net.proyecto.tesis.agrario.datos;

/**
 * Created by choqu_000 on 30/08/2015.
 */
public class Sensor {
    //Atributos
    private int numero_sensor_estacion;
    private String anno;
    private String mes;
    private String dia;
    private String hora;
    private String temperatura;
    private String humedad;

    public Sensor(){}

    //Propiedad Encasulamiento

    public int getNumero_sensor_estacion() {
        return numero_sensor_estacion;
    }

    public void setNumero_sensor_estacion(int numero_sensor_estacion) {
        this.numero_sensor_estacion = numero_sensor_estacion;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

}
