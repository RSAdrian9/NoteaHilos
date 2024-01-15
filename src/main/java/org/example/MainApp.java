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

public class MainApp extends Application {
    private List<Tarea> tareas = new ArrayList<>();
    private TableView<Tarea> tablaTareas;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplicación de Lista de Tareas");

        BorderPane borderPane = new BorderPane();

        borderPane.setStyle("-fx-background-color: #2E7D32;");

        // Crear tabla de tareas
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


        borderPane.setCenter(tablaTareas);
        borderPane.setBottom(btnAgregar);
        borderPane.setRight(btnCompletar);
        borderPane.setLeft(btnEliminar);

        Scene scene = new Scene(borderPane, 880, 500);
        scene.setFill(Color.web("#2E7D32"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void agregarTarea() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Tarea");
        dialog.setHeaderText(null);
        dialog.setContentText("Ingrese la descripción de la tarea:");

        dialog.showAndWait().ifPresent(descripcion -> {
            Tarea tarea = new Tarea(descripcion, this);
            tareas.add(tarea);
            tarea.start();
            actualizarTabla();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información de Tarea");
            alert.setHeaderText(null);
            alert.setContentText("Tarea agregada. Tiempo estimado: " + tarea.getTiempo() + " s");
            alert.showAndWait();
        });
    }

    private void completarTareaSeleccionada() {
        Tarea tareaSeleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null && !tareaSeleccionada.isCompletada()) {
            tareaSeleccionada.interrupt();
        }
    }

    private void eliminarTareaSeleccionada() {
        Tarea tareaSeleccionada = tablaTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            tareas.remove(tareaSeleccionada);
            actualizarTabla();
        }
    }

    public void actualizarTabla() {
        tablaTareas.getItems().setAll(tareas);
    }

    public void registrarTareaCompletada(String descripcion, double tiempo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("registro_tareas.txt", true))) {
            writer.println("Tarea completada: " + descripcion + " - Tiempo: " + tiempo + " s");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        for (Tarea tarea : tareas) {
            tarea.interrupt();
        }
        Platform.exit();
    }
}
