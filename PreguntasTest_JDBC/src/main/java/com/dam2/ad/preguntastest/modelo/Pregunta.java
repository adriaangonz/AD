package com.dam2.ad.preguntastest.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private int numPreg;
    private String enunciado;
    private String codCategoria;
    private String nombreCategoria;
    private int nivel;
    private int tipo;
    private String rutaFoto;
    private String rutaAudio;
    private int vecesFormulada;
    private int vecesAcertada;
    private LocalDate fechaCanc;
    private List<Respuesta> respuestas;

    public Pregunta() {
        this.respuestas = new ArrayList<>();
    }

    public Pregunta(int numPreg, String enunciado, String codCategoria, String nombreCategoria,
                    int nivel, int tipo, String rutaFoto, String rutaAudio,
                    int vecesFormulada, int vecesAcertada, LocalDate fechaCanc) {
        this.numPreg = numPreg;
        this.enunciado = enunciado;
        this.codCategoria = codCategoria;
        this.nombreCategoria = nombreCategoria;
        this.nivel = nivel;
        this.tipo = tipo;
        this.rutaFoto = rutaFoto;
        this.rutaAudio = rutaAudio;
        this.vecesFormulada = vecesFormulada;
        this.vecesAcertada = vecesAcertada;
        this.fechaCanc = fechaCanc;
        this.respuestas = new ArrayList<>();
    }

    public int getNumPreg() {
        return numPreg;
    }

    public void setNumPreg(int numPreg) {
        this.numPreg = numPreg;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }

    public void setRutaAudio(String rutaAudio) {
        this.rutaAudio = rutaAudio;
    }

    public int getVecesFormulada() {
        return vecesFormulada;
    }

    public void setVecesFormulada(int vecesFormulada) {
        this.vecesFormulada = vecesFormulada;
    }

    public int getVecesAcertada() {
        return vecesAcertada;
    }

    public void setVecesAcertada(int vecesAcertada) {
        this.vecesAcertada = vecesAcertada;
    }

    public LocalDate getFechaCanc() {
        return fechaCanc;
    }

    public void setFechaCanc(LocalDate fechaCanc) {
        this.fechaCanc = fechaCanc;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public void agregarRespuesta(Respuesta r) {
        this.respuestas.add(r);
    }

    public boolean estaCancelada() {
        return fechaCanc != null;
    }

    @Override
    public String toString() {
        String cat = nombreCategoria != null ? nombreCategoria : (codCategoria != null ? codCategoria : "General");
        return String.format("Pregunta %d [%s · nivel %d]%n%s", numPreg, cat, nivel, enunciado);
    }
}
