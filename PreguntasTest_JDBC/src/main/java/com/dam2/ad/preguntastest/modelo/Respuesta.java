package com.dam2.ad.preguntastest.modelo;

public class Respuesta {
    private int numPreg;
    private int numResp;
    private String textoResp;
    private boolean correcta;
    private String rutaFoto;
    private int vecesRespondida;

    public Respuesta() {
    }

    public Respuesta(int numPreg, int numResp, String textoResp, boolean correcta, String rutaFoto, int vecesRespondida) {
        this.numPreg = numPreg;
        this.numResp = numResp;
        this.textoResp = textoResp;
        this.correcta = correcta;
        this.rutaFoto = rutaFoto;
        this.vecesRespondida = vecesRespondida;
    }

    public int getNumPreg() {
        return numPreg;
    }

    public void setNumPreg(int numPreg) {
        this.numPreg = numPreg;
    }

    public int getNumResp() {
        return numResp;
    }

    public void setNumResp(int numResp) {
        this.numResp = numResp;
    }

    public String getTextoResp() {
        return textoResp;
    }

    public void setTextoResp(String textoResp) {
        this.textoResp = textoResp;
    }

    public boolean isCorrecta() {
        return correcta;
    }

    public void setCorrecta(boolean correcta) {
        this.correcta = correcta;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public int getVecesRespondida() {
        return vecesRespondida;
    }

    public void setVecesRespondida(int vecesRespondida) {
        this.vecesRespondida = vecesRespondida;
    }

    @Override
    public String toString() {
        return numResp + ". " + textoResp;
    }
}
