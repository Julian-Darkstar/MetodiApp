package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.example.metodiapp.services.Navigation;

public class HelloController {

    @FXML
    private BorderPane rootPane; // Necesario para obtener el contexto de la escena

    @FXML
    protected void abrirReglaFalsa() {
        Navigation.navigateTo("regla-falsa-view.fxml", rootPane);
    }

    @FXML
    protected void abrirGaussJordan() {
        Navigation.navigateTo("gauss-jordan-view.fxml", rootPane);
    }

    @FXML
    protected void abrirNewtonRaphson() {
        Navigation.navigateTo("newton-raphson-view.fxml", rootPane);
    }

    /**
     * Carga y muestra la vista del método de Jacobi utilizando la clase de navegación.
     */
    @FXML
    protected void abrirJacobi() {
        Navigation.navigateTo("Jacobi-view.fxml", rootPane);
    }
}
