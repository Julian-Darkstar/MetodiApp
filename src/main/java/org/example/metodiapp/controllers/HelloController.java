package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.example.metodiapp.services.Navigation;

public class HelloController {

    @FXML
    private BorderPane rootPane; // Necesario para obtener el contexto de la escena

    /**
     * Carga y muestra la vista del método de la Regla Falsa utilizando la clase de navegación.
     */
    @FXML
    protected void abrirReglaFalsa() {
        Navigation.navigateTo("regla-falsa-view.fxml", rootPane);
    }
}
