package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.example.metodiapp.Navigation;

import java.io.InputStream;

public class HelloController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        try (InputStream logoStream = getClass().getResourceAsStream("/org/example/metodiapp/images/logoMetodiApp.png")) {
            if (logoStream != null) {
                Image logo = new Image(logoStream);
                logoImageView.setImage(logo);
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
    @FXML
    protected void abrirIDPN() {
        Navigation.navigateTo("NewtonInterpolationVIew.fxml", rootPane);
    }

    @FXML
    protected void abrirInterpolacionCuadratica() {
        Navigation.navigateTo("interpolacion-cuadratica-view.fxml", rootPane);
    }

    @FXML
    protected void abrirMultipleRegression() {
        Navigation.navigateTo("multiple-regression-view.fxml", rootPane);
    }
}