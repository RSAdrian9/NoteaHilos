package org.example;

import javafx.application.Platform;

/**
 * La clase `Tarea` representa una tarea en la aplicación de Lista de Tareas.
 * Extiende la clase `Thread` para ejecutarse como un hilo independiente.
 */
public class Tarea extends Thread {
    private String descripcion;
    private boolean completada;
    private double tiempo;
    private double tiempoTranscurrido;
    private long tiempoInicio;
    private MainApp mainApp;

    /**
     * Constructor de la clase `Tarea`.
     *
     * @param descripcion La descripción de la tarea.
     * @param mainApp     La instancia de la clase `MainApp` que contiene la tarea.
     */
    public Tarea(String descripcion, MainApp mainApp) {
        this.descripcion = descripcion;
        this.completada = false;
        this.mainApp = mainApp;
        this.tiempo = generarTiempoAleatorio();
        this.tiempoInicio = System.currentTimeMillis();
    }

    /**
     * Método que se ejecuta cuando el hilo de la tarea inicia.
     * Realiza la espera del tiempo asignado a la tarea y luego notifica a la
     * aplicación principal sobre la finalización de la tarea.
     */
    @Override
    public void run() {
        try {
            // Simulación de la tarea: pausa el hilo según el tiempo estimado
            Thread.sleep((long) (tiempo * 1000));
        } catch (InterruptedException e) {
            // La tarea fue interrumpida antes de completarse
            e.printStackTrace();
        }

        // La tarea se ha completado
        completada = true;
        // Actualización de la interfaz gráfica a través de JavaFX
        Platform.runLater(() -> mainApp.actualizarTabla());

        // Registro de la tarea completada en un archivo
        long tiempoFinal = System.currentTimeMillis();
        tiempoTranscurrido = (tiempoFinal - tiempoInicio) / 1000.0;
        mainApp.registrarTareaCompletada(descripcion, tiempoTranscurrido);
    }

    /**
     * Método que devuelve la descripción de la tarea.
     *
     * @return La descripción de la tarea.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método que indica si la tarea ha sido completada.
     *
     * @return `true` si la tarea está completada, `false` de lo contrario.
     */
    public boolean isCompletada() {
        return completada;
    }

    /**
     * Método que devuelve el tiempo estimado para la tarea.
     *
     * @return El tiempo estimado en segundos.
     */
    public double getTiempo() {
        return tiempo;
    }

    /**
     * Método que devuelve el tiempo transcurrido durante la ejecución de la tarea.
     *
     * @return El tiempo transcurrido en segundos.
     */
    public double getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }


    /**
     * Método privado que genera un tiempo aleatorio para simular la duración de la tarea.
     *
     * @return El tiempo aleatorio en segundos.
     */
    private double generarTiempoAleatorio() {
        int[] opciones = {3, 5, 10, 30}; // opciones en segundos
        int indiceAleatorio = (int) (Math.random() * opciones.length);
        return opciones[indiceAleatorio];
    }
}
