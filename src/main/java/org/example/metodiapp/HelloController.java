package org.example.metodiapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloController {

    @FXML
    private BorderPane rootPane; // Se conectará con el panel principal de la vista

    /**
     * Carga y muestra la vista del método de la Regla Falsa.
     */
    @FXML
    protected void abrirReglaFalsa() {
        try {
            // Carga el nuevo archivo FXML desde los recursos
            Parent reglaFalsaRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/example/metodiapp/regla-falsa-view.fxml")));

            // Obtiene la ventana (Stage) actual a través del panel principal
            Stage stage = (Stage) rootPane.getScene().getWindow();

            // Reemplaza la escena actual por la nueva
            stage.setScene(new Scene(reglaFalsaRoot));

        } catch (IOException e) {
            System.err.println("Error al cargar la vista de Regla Falsa:");
            e.printStackTrace();
        }
    }
}
