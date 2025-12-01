package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.metodiapp.Navigation;
import org.example.metodiapp.models.MRegresion;

public class MultipleRegressionController {

    @FXML
    private TextField numObservationsField;
    @FXML
    private TextField numVariablesField;
    @FXML
    private GridPane xGrid;
    @FXML
    private VBox yVBox;
    @FXML
    private Label coefficientsLabel;
    @FXML
    private Button volverButton;

    private TextField[][] xFields;
    private TextField[] yFields;

    @FXML
    public void generateFields() {
        xGrid.getChildren().clear();
        yVBox.getChildren().clear();

        int numObservations;
        int numVariables;
        try {
            numObservations = Integer.parseInt(numObservationsField.getText());
            numVariables = Integer.parseInt(numVariablesField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese números válidos para observaciones y variables.");
            return;
        }

        xFields = new TextField[numObservations][numVariables + 1];
        yFields = new TextField[numObservations];

        for (int i = 0; i < numObservations; i++) {
            for (int j = 1; j <= numVariables; j++) {
                xFields[i][j] = new TextField();
                xFields[i][j].setPromptText("Obs " + (i + 1) + ", Var " + j);
                xGrid.add(xFields[i][j], j - 1, i);
            }
        }

        for (int i = 0; i < numObservations; i++) {
            yFields[i] = new TextField();
            yFields[i].setPromptText("Y " + (i + 1));
            yVBox.getChildren().add(yFields[i]);
        }
    }

    @FXML
    public void calculateRegression() {
        int numObservations;
        int numVariables;
        try {
            numObservations = Integer.parseInt(numObservationsField.getText());
            numVariables = Integer.parseInt(numVariablesField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese números válidos para observaciones y variables.");
            return;
        }


        double[][] x = new double[numObservations][numVariables + 1];
        double[] y = new double[numObservations];

        try {
            for (int i = 0; i < numObservations; i++) {
                x[i][0] = 1.0; // Intercepto
                for (int j = 1; j <= numVariables; j++) {
                    x[i][j] = Double.parseDouble(xFields[i][j].getText());
                }
                y[i] = Double.parseDouble(yFields[i].getText());
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese números válidos en todos los campos.");
            return;
        }

        MRegresion regresion = new MRegresion();
        regresion.calculateRegression(x, y);
        double[] coefficients = regresion.getCoefficients();

        if (coefficients != null) {
            StringBuilder sb = new StringBuilder("Coeficientes de Regresión:\n");
            sb.append(String.format("b0 (Intercepto): %.4f%n", coefficients[0]));
            for (int i = 1; i < coefficients.length; i++) {
                sb.append(String.format("b%d: %.4f%n", i, coefficients[i]));
            }
            coefficientsLabel.setText(sb.toString());
        } else {
            coefficientsLabel.setText("La regresión aún no ha sido calculada.");
        }
    }

    @FXML
    public void mostrarAyuda() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Ayuda - Regresión Múltiple");
        a.setHeaderText("Descripción del Método");
        a.setContentText("La regresión múltiple es un método estadístico que permite modelar la relación entre una variable dependiente (Y) y múltiples variables independientes (X).\n\n" +
                "Cómo usar:\n" +
                "1. Ingrese el número de observaciones y de variables independientes.\n" +
                "2. Presione 'Generar Campos' para crear las casillas de entrada.\n" +
                "3. Llene los valores para cada variable independiente (X) y dependiente (Y).\n" +
                "4. Presione 'Calcular' para obtener los coeficientes de la regresión.");
        a.showAndWait();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulo);
a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    protected void volver() {
        Navigation.navigateTo("hello-view.fxml", volverButton);
    }
}