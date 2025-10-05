package org.example.metodiapp.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.example.metodiapp.services.Navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GaussJordanController {

    @FXML private TextField filasTextField;
    @FXML private TextField columnasTextField;
    @FXML private GridPane headerGridPane;
    @FXML private GridPane matrizGridPane;
    @FXML private Button volverButton;
    @FXML private TextArea resultadoTextArea;

    private List<List<TextField>> matrizTextFields = new ArrayList<>();
    private double[][] matriz;

    @FXML
    protected void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Matriz Aumentada");
        alert.setHeaderText("Cómo ingresar un sistema de ecuaciones");

        String ayudaTexto = "Este método resuelve sistemas de ecuaciones lineales (ej: 2x + y = 5).\n\n" +
                "1. Represente su sistema como una 'Matriz Aumentada'.\n" +
                "   Las columnas de la izquierda son los coeficientes de las variables (x, y, z, ...).\n" +
                "   La última columna de la derecha es el resultado de cada ecuación.\n\n" +
                "Ejemplo para el sistema:\n" +
                "   2x + 1y = 8\n" +
                "   1x - 3y = -1\n\n" +
                "Dimensiones a ingresar:\n" +
                "   - Filas (m): 2 (porque hay 2 ecuaciones)\n" +
                "   - Columnas (n): 3 (2 variables + 1 columna de resultados)\n\n" +
                "Matriz a llenar en la cuadrícula:\n" +
                "   | 2.0   1.0  |  8.0 |\n" +
                "   | 1.0  -3.0  | -1.0 |\n";

        TextArea textArea = new TextArea(ayudaTexto);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    @FXML
    protected void crearMatriz() {
        try {
            int filas = Integer.parseInt(filasTextField.getText());
            int columnas = Integer.parseInt(columnasTextField.getText());

            if (filas <= 0 || columnas <= 1) {
                mostrarAlerta("Error", "Las filas deben ser > 0 y las columnas > 1.");
                return;
            }

            headerGridPane.getChildren().clear();
            matrizGridPane.getChildren().clear();
            matrizTextFields.clear();

            for (int j = 0; j < columnas; j++) {
                String headerText = (j < columnas - 1) ? "x" + (j + 1) : "Resultado";
                Label headerLabel = new Label(headerText);
                headerLabel.setStyle("-fx-font-weight: bold;");
                headerLabel.setAlignment(Pos.CENTER);
                headerLabel.setPrefWidth(70);
                headerGridPane.add(headerLabel, j, 0);
            }

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
            resultadoTextArea.appendText(matrizToString(matriz, null, null));

            resolverGaussJordan();

            resultadoTextArea.appendText("\n=== MATRIZ REDUCIDA ===\n");
            resultadoTextArea.appendText(matrizToString(matriz, null, null));

            resultadoTextArea.appendText("\n=== SOLUCIÓN DEL SISTEMA ===\n");
            resultadoTextArea.appendText(solucionToString());

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Datos", "Asegúrese de que todos los valores en la matriz sean números válidos.");
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error durante la resolución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resolverGaussJordan() {
        int filas = matriz.length;
        int columnas = matriz[0].length;
        resultadoTextArea.appendText("\n=== PROCESO DE ELIMINACIÓN ===\n");

        for (int pivote = 0; pivote < filas && pivote < columnas - 1; pivote++) {
            Set<Integer> filasModificadas = new HashSet<>();
            List<String> operaciones = new ArrayList<>();

            if (Math.abs(matriz[pivote][pivote]) < 1e-9) {
                 int filaNoCero = -1;
                for (int i = pivote + 1; i < filas; i++) {
                    if (Math.abs(matriz[i][pivote]) > 1e-9) {
                        filaNoCero = i;
                        break;
                    }
                }
                if(filaNoCero != -1) {
                    double[] temp = matriz[pivote];
                    matriz[pivote] = matriz[filaNoCero];
                    matriz[filaNoCero] = temp;
                    operaciones.add(String.format("Intercambio R%d ↔ R%d", pivote + 1, filaNoCero + 1));
                    filasModificadas.add(pivote);
                    filasModificadas.add(filaNoCero);
                }
            }

            double divisor = matriz[pivote][pivote];
            if (Math.abs(divisor) < 1e-9) continue;

            operaciones.add(String.format("R%d → R%d / %.3f", pivote + 1, pivote + 1, divisor));
            filasModificadas.add(pivote);
            for (int j = pivote; j < columnas; j++) {
                matriz[pivote][j] /= divisor;
            }

            for (int i = 0; i < filas; i++) {
                if (i != pivote) {
                    double factor = matriz[i][pivote];
                    if (Math.abs(factor) > 1e-9) {
                        operaciones.add(String.format("R%d → R%d - (%.3f * R%d)", i + 1, i + 1, factor, pivote + 1));
                        filasModificadas.add(i);
                        for (int j = pivote; j < columnas; j++) {
                            matriz[i][j] -= factor * matriz[pivote][j];
                        }
                    }
                }
            }

            resultadoTextArea.appendText("\nPaso " + (pivote + 1) + ":\n");
            resultadoTextArea.appendText(matrizToString(matriz, filasModificadas, operaciones));
        }
    }

    private String matrizToString(double[][] m, Set<Integer> filasModificadas, List<String> operaciones) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.length; i++) {
            boolean esModificada = filasModificadas != null && filasModificadas.contains(i);
            sb.append(esModificada ? "→ | " : "  | ");

            for (int j = 0; j < m[0].length; j++) {
                sb.append(String.format("%8.3f ", m[i][j]));
                if (j == m[0].length - 2) {
                    sb.append("| ");
                }
            }
            sb.append("|\n");
        }
        if (operaciones != null && !operaciones.isEmpty()) {
            sb.append("  Operaciones:\n");
            for (String op : operaciones) {
                sb.append("    - ").append(op).append("\n");
            }
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
        headerGridPane.getChildren().clear();
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
