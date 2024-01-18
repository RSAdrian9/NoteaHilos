package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase `MainApp` es la clase principal de la aplicación de Lista de Tareas.
 * Extiende la clase `Application` de JavaFX, proporcionando el método `start`
 * para inicializar la interfaz gráfica de usuario.
 */
public class MainApp extends Application {
    private List<Tarea> tareas = new ArrayList<>();
    private TableView<Tarea> tablaTareas;

    /**
     * Método principal que lanza la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no utilizado).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método que se llama al iniciar la aplicación. Configura y muestra la interfaz
     * gráfica de usuario, incluida la tabla de tareas y los botones de acción.
     *
     * @param primaryStage El escenario principal de la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        // Configuración del título y la apariencia del escenario principal
        primaryStage.setTitle("Aplicación de Lista de Tareas");

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #2E7D32;");

        // Creación y configuración de la tabla de tareas
        tablaTareas = new TableView<>();
        tablaTareas.setStyle("-fx-background-color: #81C784;");
        TableColumn<Tarea, String> descripcionColumna = new TableColumn<>("Descripción");
        descripcionColumna.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        descripcionColumna.setMinWidth(150); // Ajusta el ancho mínimo de la columna

        TableColumn<Tarea, Boolean> completadaColumna = new TableColumn<>("Completada");
        completadaColumna.setCellValueFactory(new PropertyValueFactory<>("completada"));
        completadaColumna.setMinWidth(100); // Ajusta el ancho mínimo de la columna

        TableColumn<Tarea, Double> tiempoColumna = new TableColumn<>("Tiempo (s)");
        tiempoColumna.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        tiempoColumna.setMinWidth(100); // Ajusta el ancho mínimo de la columna

        TableColumn<Tarea, Double> tiempoTranscurridoColumna = new TableColumn<>("Tiempo Transcurrido (s)");
        tiempoTranscurridoColumna.setCellValueFactory(new PropertyValueFactory<>("tiempoTranscurrido"));
        tiempoTranscurridoColumna.setMinWidth(202); // Ajusta el ancho mínimo de la columna

        tablaTareas.getColumns().addAll(descripcionColumna, completadaColumna, tiempoColumna, tiempoTranscurridoColumna);

        // Creación y configuración de los botones de acción
        // Configuración de la acción al hacer clic en el botón "Agregar Tarea"
        Button btnAgregar = new Button("Agregar Tarea");
        btnAgregar.setStyle(
                "-fx-background-color: #0090ff;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-border-radius: 5px;"
        );
        btnAgregar.setOnAction(e -> agregarTarea());

        // Configuración de la acción al hacer clic en el botón "Completar Tarea"
        Button btnCompletar = new Button("Completar Tarea");
        btnCompletar.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-border-radius: 5px;"
        );
        btnCompletar.setOnAction(e -> completarTareaSeleccionada());

        // Configuración de la acción al hacer clic en el botón "Eliminar Tarea"
        Button btnEliminar = new Button("Eliminar Tarea");
        btnEliminar.setStyle(
                "-fx-background-color: #e81818;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-border-radius: 5px;"
        );
        btnEliminar.setOnAction(e -> eliminarTareaSeleccionada());


        Button btnTareasPorTurnos = new Button("Tareas por turnos");
        btnTareasPorTurnos.setStyle(
                "-fx-background-color: #FFA000;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-border-radius: 5px;"
        );
        btnTareasPorTurnos.setOnAction(e -> agregarTareasPorTurnos());


        // Configuración del diseño de la interfaz y visualización
        borderPane.setCenter(tablaTareas);
        borderPane.setBottom(btnAgregar);
        borderPane.setRight(btnCompletar);
        borderPane.setLeft(btnEliminar);
        borderPane.setTop(btnTareasPorTurnos);




        Scene scene = new Scene(borderPane, 880, 500);
        scene.setFill(Color.web("#2E7D32"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Método privado que se llama al hacer clic en el botón "Agregar Tarea".
     * Muestra un cuadro de diálogo para ingresar la descripción de la tarea y
     * agrega la tarea a la lista de tareas.
     */
    private void agregarTarea() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Tarea");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese la descripción de la tarea:");

        // Cuadro de diálogo para preguntar el número de hilos
        TextInputDialog numHilosDialog = new TextInputDialog();
        numHilosDialog.setTitle("Número de personas");
        numHilosDialog.setHeaderText(null);
        numHilosDialog.setContentText("Ingrese el número de personas para realizar la tarea:");

        dialog.showAndWait().ifPresent(descripcion -> {
            // Obtener la descripción de la tarea
            int numHilos = 1; // Valor por defecto

            // Mostrar el cuadro de diálogo para el número de hilos y obtener la respuesta
            Optional<String> result = numHilosDialog.showAndWait();
            if (result.isPresent()) {
                try {
                    numHilos = Integer.parseInt(result.get());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Crear y agregar la tarea con el número de hilos especificado
            for (int i = 0; i < numHilos; i++) {
                Tarea tarea = new Tarea(descripcion, this);
                tareas.add(tarea);
                tarea.start();
            }

            // Actualizar la tabla
            actualizarTabla();

            // Mostrar información sobre la tarea agregada
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información de Tarea");
            alert.setHeaderText(null);
            alert.setContentText("Tarea agregada. Tiempo estimado: " + tareas.get(tareas.size() - 1).getTiempo() + " s");
            alert.showAndWait();
        });
    }


    /**
     * Método privado que se llama al hacer clic en el botón "Tareas por turnos".
     * Muestra un cuadro de diálogo para ingresar la descripción de la tarea y
     * el número de trabajadores, luego agrega tareas por turnos a la lista y
     * actualiza la tabla con la información detallada.
     */
    private void agregarTareasPorTurnos() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Tareas por turnos");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese la descripción de la tarea:");

        TextInputDialog numTrabajadoresDialog = new TextInputDialog();
        numTrabajadoresDialog.setTitle("Número de trabajadores");
        numTrabajadoresDialog.setHeaderText(null);
        numTrabajadoresDialog.setContentText("Ingrese el número de trabajadores:");

        dialog.showAndWait().ifPresent(descripcion -> {
            // Obtener la descripción de la tarea
            int numTrabajadores = 1; // Valor por defecto

            // Mostrar el cuadro de diálogo para el número de trabajadores y obtener la respuesta
            Optional<String> result = numTrabajadoresDialog.showAndWait();
            if (result.isPresent()) {
                try {
                    numTrabajadores = Integer.parseInt(result.get());

                    // Crear una variable final para usar en la expresión lambda
                    final int numTrabajadoresFinal = numTrabajadores;

                    // Crear un hilo para agregar y sincronizar las tareas por turnos
                    Thread tareaTurnosThread = new Thread(() -> {
                        try {
                            double tiempoTotal = 0;

                            // Crear y agregar las tareas por turnos
                            for (int i = 0; i < numTrabajadoresFinal; i++) {
                                String nombreTrabajador = "Trabajador " + (i + 1); // Nombre único para cada trabajador
                                Tarea tarea = new Tarea(descripcion + " - " + nombreTrabajador, this);
                                tareas.add(tarea);
                                tarea.start();

                                // Esperar a que la tarea actual termine antes de continuar
                                tarea.join();

                                // Sumar el tiempo estimado de la tarea actual al tiempo total
                                tiempoTotal += tarea.getTiempo();

                                // Esperar un tiempo antes de iniciar la próxima tarea
                                Thread.sleep(1000); // Puedes ajustar el tiempo de espera según tus necesidades
                            }

                            // Actualizar la tabla
                            Platform.runLater(() -> actualizarTabla());

                            // Mostrar información sobre las tareas por turnos
                            double finalTiempoTotal = tiempoTotal;
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Información de Tareas por turnos");
                                alert.setHeaderText(null);

                                // Mostrar información detallada de cada trabajador
                                for (int i = 0; i < numTrabajadoresFinal; i++) {
                                    alert.setContentText(alert.getContentText() +
                                            "Trabajador " + (i + 1) + ": " +
                                            tareas.get(i).getDescripcion() + " - Tiempo: " +
                                            tareas.get(i).getTiempo() + " s\n");
                                }

                                // Mostrar el tiempo total estimado
                                alert.setContentText(alert.getContentText() +
                                        "Tiempo total estimado: " + finalTiempoTotal + " s\n");

                                alert.showAndWait();
                            });
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    });

                    tareaTurnosThread.start(); // Iniciar el hilo

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    /**
     * Método privado que se llama al hacer clic en el botón "Completar Tarea".
     * Interrumpe la ejecución de la tarea seleccionada si no ha sido completada.
     */
    private void completarTareaSeleccionada() {
        Tarea tareaSeleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null && !tareaSeleccionada.isCompletada()) {
            tareaSeleccionada.interrupt();
        }
    }

    /**
     * Método privado que se llama al hacer clic en el botón "Eliminar Tarea".
     * Elimina la tarea seleccionada de la lista de tareas y actualiza la tabla.
     */
    private void eliminarTareaSeleccionada() {
        Tarea tareaSeleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            tareas.remove(tareaSeleccionada);
            actualizarTabla();
        }
    }

    /**
     * Método que actualiza la tabla de tareas con la lista actualizada.
     * Sincroniza el acceso a la lista de tareas y actualiza la interfaz gráfica.
     */
    public synchronized void actualizarTabla() {
        tablaTareas.getItems().setAll(tareas);
    }


    /**
     * Método que registra la información de una tarea completada en un archivo de texto.
     * @param descripcion La descripción de la tarea completada.
     * @param tiempo El tiempo transcurrido en la tarea.
     */
    public void registrarTareaCompletada(String descripcion, double tiempo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("registro_tareas.txt", true))) {
            writer.println("Tarea completada: " + descripcion + " - Tiempo: " + tiempo + " s");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Método que se llama al cerrar la aplicación. Interrumpe la ejecución de todas
     * las tareas y cierra la aplicación.
     */
    @Override
    public void stop() {
        for (Tarea tarea : tareas) {
            tarea.interrupt();
        }
        Platform.exit();
    }
}
