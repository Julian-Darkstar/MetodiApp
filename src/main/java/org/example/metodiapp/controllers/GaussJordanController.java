package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.example.metodiapp.services.Navigation;

import java.util.ArrayList;
import java.util.List;

public class GaussJordanController {

    @FXML private TextField filasTextField;
    @FXML private TextField columnasTextField;
    @FXML private Button crearMatrizButton;
    @FXML private GridPane matrizGridPane;
    @FXML private Button resolverButton;
    @FXML private Button reiniciarButton;
    @FXML private Button volverButton;
    @FXML private TextArea resultadoTextArea;

    private List<List<TextField>> matrizTextFields = new ArrayList<>();
    private double[][] matriz;

    @FXML
    protected void crearMatriz() {
        try {
            int filas = Integer.parseInt(filasTextField.getText());
            int columnas = Integer.parseInt(columnasTextField.getText());

            if (filas <= 0 || columnas <= 1) {
                mostrarAlerta("Error", "Las filas deben ser > 0 y las columnas > 1.");
                return;
            }

            matrizGridPane.getChildren().clear();
            matrizTextFields.clear();

            for (int i = 0; i < filas; i++) {
                List<TextField> filaDeTextFields = new ArrayList<>();
                for (int j = 0; j < columnas; j++) {
                    TextField textField = new TextField();
                    textField.setPrefWidth(70);
                    matrizGridPane.add(textField, j, i);
                    filaDeTextFields.add(textField);
                }
                matrizTextFields.add(filaDeTextFields);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese dimensiones numéricas válidas.");
        }
    }

    @FXML
    protected void resolverMatriz() {
        if (matrizTextFields.isEmpty()) {
            mostrarAlerta("Error", "Primero debe crear una matriz.");
            return;
        }

        try {
            int filas = matrizTextFields.size();
            int columnas = matrizTextFields.get(0).size();
            matriz = new double[filas][columnas];

            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    String valor = matrizTextFields.get(i).get(j).getText().trim();
                    if (valor.isEmpty()) {
                        mostrarAlerta("Error de Datos", "Por favor, complete todas las casillas de la matriz.");
                        return;
                    }
                    matriz[i][j] = Double.parseDouble(valor);
                }
            }

            resultadoTextArea.clear();
            resultadoTextArea.appendText("=== MATRIZ ORIGINAL ===\n");
            resultadoTextArea.appendText(matrizToString(matriz));

            resolverGaussJordan();

            resultadoTextArea.appendText("\n=== MATRIZ REDUCIDA ===\n");
            resultadoTextArea.appendText(matrizToString(matriz));

            resultadoTextArea.appendText("\n=== SOLUCIÓN DEL SISTEMA ===\n");
            resultadoTextArea.appendText(solucionToString());

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Datos", "Asegúrese de que todos los valores en la matriz sean números válidos.");
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error durante la resolución: " + e.getMessage());
        }
    }

    private void resolverGaussJordan() {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        resultadoTextArea.appendText("\n=== PROCESO DE ELIMINACIÓN ===\n");

        for (int pivote = 0; pivote < filas && pivote < columnas - 1; pivote++) {
            if (Math.abs(matriz[pivote][pivote]) < 1e-9) {
                int filaNoCero = -1;
                for (int i = pivote + 1; i < filas; i++) {
                    if (Math.abs(matriz[i][pivote]) > 1e-9) {
                        filaNoCero = i;
                        break;
                    }
                }

                if (filaNoCero != -1) {
                    double[] temp = matriz[pivote];
                    matriz[pivote] = matriz[filaNoCero];
                    matriz[filaNoCero] = temp;
                    resultadoTextArea.appendText("↔ Intercambio de Fila " + (pivote + 1) + " con Fila " + (filaNoCero + 1) + "\n");
                } else {
                    resultadoTextArea.appendText("⚠️ No se puede encontrar pivote no nulo en columna " + (pivote + 1) + ". Se omite.\n");
                    continue;
                }
            }

            double divisor = matriz[pivote][pivote];
            for (int j = pivote; j < columnas; j++) {
                matriz[pivote][j] /= divisor;
            }

            for (int i = 0; i < filas; i++) {
                if (i != pivote) {
                    double factor = matriz[i][pivote];
                    for (int j = pivote; j < columnas; j++) {
                        matriz[i][j] -= factor * matriz[pivote][j];
                    }
                }
            }
            resultadoTextArea.appendText("\nPaso " + (pivote + 1) + ":\n");
            resultadoTextArea.appendText(matrizToString(matriz));
        }
    }

    private String matrizToString(double[][] m) {
        StringBuilder sb = new StringBuilder();
        for (double[] fila : m) {
            sb.append("| ");
            for (int j = 0; j < fila.length; j++) {
                sb.append(String.format("%8.3f ", fila[j]));
                if (j == fila.length - 2) {
                    sb.append("| ");
                }
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    private String solucionToString() {
        StringBuilder sb = new StringBuilder();
        int filas = matriz.length;
        int vars = matriz[0].length - 1;

        for (int i = 0; i < filas; i++) {
            boolean esFilaCero = true;
            int pivoteCol = -1;
            for (int j = 0; j < vars; j++) {
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    esFilaCero = false;
                    pivoteCol = j;
                    break;
                }
            }

            if (esFilaCero) {
                if (Math.abs(matriz[i][vars]) > 1e-9) {
                    return "Sistema Inconsistente (0 = k).\n";
                }
            } else {
                 sb.append("x").append(pivoteCol + 1).append(" = ").append(String.format("%.4f", matriz[i][vars])).append("\n");
            }
        }
        if (vars > filas) {
             sb.append("\nSistema con infinitas soluciones (variables libres).\n");
        }

        return sb.toString();
    }

    @FXML
    protected void reiniciar() {
        filasTextField.clear();
        columnasTextField.clear();
        matrizGridPane.getChildren().clear();
        matrizTextFields.clear();
        resultadoTextArea.clear();
    }

    @FXML
    protected void volver() {
        Navigation.navigateTo("hello-view.fxml", volverButton);
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
