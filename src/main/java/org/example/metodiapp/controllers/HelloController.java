package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.metodiapp.services.Navigation;

import java.io.InputStream;

public class HelloController {

    @FXML
    private BorderPane rootPane; // Necesario para obtener el contexto de la escena

    @FXML
    private VBox logoContainer; // El VBox vacío que preparamos en el FXML

    @FXML
    public void initialize() {
        // Carga la imagen de forma programática, que es más robusto que hacerlo en FXML.
        try (InputStream logoStream = getClass().getResourceAsStream("/org/example/metodiapp/images/logoMetodiApp.png")) {
            if (logoStream != null) {
                Image logo = new Image(logoStream);
                ImageView logoView = new ImageView(logo);
                logoView.setFitHeight(150);
                logoView.setFitWidth(150);
                logoView.setPreserveRatio(true);
                logoContainer.getChildren().add(logoView);
            } else {
                System.err.println("ADVERTENCIA: No se pudo cargar el logo en la vista principal. Recurso no encontrado.");
            }
        } catch (Exception e) {
            System.err.println("ADVERTENCIA: Ocurrió un error al procesar el logo de la vista principal.");
            e.printStackTrace();
        }
    }

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

    @FXML
    protected void abrirNewtonMulti() {
        Navigation.navigateTo("newton-multi-view.fxml", rootPane);
    }

    @FXML
    protected void abrirJacobi() {
        Navigation.navigateTo("Jacobi-view.fxml", rootPane);
    }
}
