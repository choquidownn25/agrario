package net.proyecto.tesis.agrario.datos;

/**
 * Created by choqu_000 on 03/09/2015.
 */
public class Modelo {

    //Atributos
    private int CodAforo;

    private int DPR;

    private double Perimetro_Mojado;

    private double Radio_Hidraulico;

    private double Rdossobretres;

    private double isobren;

    private double Revoluciones;

    private double Pt;

    private double  Anchos;

    private double Areas;

    private double  Caudales;

    private double  Velocidadmediaprpmedia;

    private double  Vmv;

    private double  porcentajeprofundidadmedia;

    private double  numerodetalleafro;

    public Modelo(){

    }

    //Encapsulamiento
    public int getCodAforo() {
        return CodAforo;
    }

    public void setCodAforo(int codAforo) {
        CodAforo = codAforo;
    }

    public int getDPR() {
        return DPR;
    }

    public void setDPR(int DPR) {
        this.DPR = DPR;
    }

    public double getPerimetro_Mojado() {
        return Perimetro_Mojado;
    }

    public void setPerimetro_Mojado(double perimetro_Mojado) {
        Perimetro_Mojado = perimetro_Mojado;
    }

    public double getRadio_Hidraulico() {
        return Radio_Hidraulico;
    }

    public void setRadio_Hidraulico(double radio_Hidraulico) {
        Radio_Hidraulico = radio_Hidraulico;
    }

    public double getRdossobretres() {
        return Rdossobretres;
    }

    public void setRdossobretres(double rdossobretres) {
        Rdossobretres = rdossobretres;
    }

    public double getIsobren() {
        return isobren;
    }

    public void setIsobren(double isobren) {
        this.isobren = isobren;
    }

    public double getRevoluciones() {
        return Revoluciones;
    }

    public void setRevoluciones(double revoluciones) {
        Revoluciones = revoluciones;
    }

    public double getPt() {
        return Pt;
    }

    public void setPt(double pt) {
        Pt = pt;
    }

    public double getAnchos() {
        return Anchos;
    }

    public void setAnchos(double anchos) {
        Anchos = anchos;
    }

    public double getAreas() {
        return Areas;
    }

    public void setAreas(double areas) {
        Areas = areas;
    }

    public double getCaudales() {
        return Caudales;
    }

    public void setCaudales(double caudales) {
        Caudales = caudales;
    }

    public double getVelocidadmediaprpmedia() {
        return Velocidadmediaprpmedia;
    }

    public void setVelocidadmediaprpmedia(double velocidadmediaprpmedia) {
        Velocidadmediaprpmedia = velocidadmediaprpmedia;
    }

    public double getVmv() {
        return Vmv;
    }

    public void setVmv(double vmv) {
        Vmv = vmv;
    }

    public double getPorcentajeprofundidadmedia() {
        return porcentajeprofundidadmedia;
    }

    public void setPorcentajeprofundidadmedia(double porcentajeprofundidadmedia) {
        this.porcentajeprofundidadmedia = porcentajeprofundidadmedia;
    }

    public double getNumerodetalleafro() {
        return numerodetalleafro;
    }

    public void setNumerodetalleafro(double numerodetalleafro) {
        this.numerodetalleafro = numerodetalleafro;
    }

}
