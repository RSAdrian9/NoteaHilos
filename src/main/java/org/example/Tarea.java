package org.example;

import javafx.application.Platform;

public class Tarea extends Thread {
    private String descripcion;
    private boolean completada;
    private double tiempo;
    private double tiempoTranscurrido;
    private long tiempoInicio;
    private MainApp mainApp;

    public Tarea(String descripcion, MainApp mainApp) {
        this.descripcion = descripcion;
        this.completada = false;
        this.mainApp = mainApp;
        this.tiempo = generarTiempoAleatorio();
        this.tiempoInicio = System.currentTimeMillis();
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (tiempo * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        completada = true;
        Platform.runLater(() -> mainApp.actualizarTabla());


        long tiempoFinal = System.currentTimeMillis();
        tiempoTranscurrido = (tiempoFinal - tiempoInicio) / 1000.0;

        mainApp.registrarTareaCompletada(descripcion, tiempoTranscurrido);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public double getTiempo() {
        return tiempo;
    }

    public double getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }


    private double generarTiempoAleatorio() {
        int[] opciones = {3, 5, 10, 30}; // opciones en segundos
        int indiceAleatorio = (int) (Math.random() * opciones.length);
        return opciones[indiceAleatorio];
    }
}
