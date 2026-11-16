package com.dam2.ad.preguntastest.modelo;

public class Categoria {
    private String codCat;
    private String nombreCat;

    public Categoria() {
    }

    public Categoria(String codCat, String nombreCat) {
        this.codCat = codCat;
        this.nombreCat = nombreCat;
    }

    public String getCodCat() {
        return codCat;
    }

    public void setCodCat(String codCat) {
        this.codCat = codCat;
    }

    public String getNombreCat() {
        return nombreCat;
    }

    public void setNombreCat(String nombreCat) {
        this.nombreCat = nombreCat;
    }

    @Override
    public String toString() {
        return nombreCat != null ? nombreCat : codCat;
    }
}
