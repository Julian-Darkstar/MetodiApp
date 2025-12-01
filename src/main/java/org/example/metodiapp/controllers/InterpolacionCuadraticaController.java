package org.example.metodiapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.metodiapp.Navigation;
import org.example.metodiapp.models.InterpolacionCuadratica;

public class InterpolacionCuadraticaController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField x0Field;
    @FXML
    private TextField y0Field;
    @FXML
    private TextField x1Field;
    @FXML
    private TextField y1Field;
    @FXML
    private TextField x2Field;
    @FXML
    private TextField y2Field;
    @FXML
    private TextField xField;
    @FXML
    private Label resultadoLabel;

    @FXML
    public void calcular(ActionEvent event) {
        try {
            double x0 = Double.parseDouble(x0Field.getText());
            double y0 = Double.parseDouble(y0Field.getText());
            double x1 = Double.parseDouble(x1Field.getText());
            double y1 = Double.parseDouble(y1Field.getText());
            double x2 = Double.parseDouble(x2Field.getText());
            double y2 = Double.parseDouble(y2Field.getText());
            double x = Double.parseDouble(xField.getText());

            double resultado = InterpolacionCuadratica.interpolar(x0, y0, x1, y1, x2, y2, x);

            resultadoLabel.setText(String.format("Resultado: %.6f", resultado));

        } catch (NumberFormatException e) {
            resultadoLabel.setText("Error: Ingrese valores numéricos válidos.");
        } catch (Exception e) {
            resultadoLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void limpiar() {
        x0Field.clear();
        y0Field.clear();
        x1Field.clear();
        y1Field.clear();
        x2Field.clear();
        y2Field.clear();
        xField.clear();
        resultadoLabel.setText("Resultado:");
    }

    @FXML
    public void volverAlMenu() {
        Navigation.navigateTo("hello-view.fxml", rootPane);
    }

    @FXML
    public void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Interpolación Cuadrática");
        alert.setHeaderText("Instrucciones para usar el método de Interpolación Cuadrática");
        alert.setContentText(
                "1. Ingrese las coordenadas de tres puntos conocidos (x0, y0), (x1, y1) y (x2, y2).\n" +
                "2. Ingrese el valor de 'x' en el punto donde desea estimar el valor de 'y'.\n" +
                "3. Haga clic en 'Calcular' para ver el resultado de la interpolación.\n" +
                "4. Use 'Limpiar' para reiniciar los campos y 'Volver al Menú' para regresar a la pantalla principal."
        );
        alert.showAndWait();
    }
}
