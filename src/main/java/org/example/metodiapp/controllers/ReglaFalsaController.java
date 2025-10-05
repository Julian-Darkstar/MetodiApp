package org.example.metodiapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.example.metodiapp.models.IteracionReglaFalsa;
import org.example.metodiapp.services.GraphingService;
import org.example.metodiapp.services.Navigation;

public class ReglaFalsaController {

    // --- Campos de la Interfaz (Inputs) ---
    @FXML private TextField funcionTextField;
    @FXML private TextField aTextField;
    @FXML private TextField bTextField;
    @FXML private TextField toleranciaTextField;
    @FXML private Button calcularButton;

    // --- Campos de la Interfaz (Gráfico) ---
    @FXML private LineChart<Number, Number> funcionChart;
    @FXML private TextField xMinField;
    @FXML private TextField xMaxField;

    // --- Campos de la Interfaz (Resultados) ---
    @FXML private Label raizLabel;
    @FXML private TableView<IteracionReglaFalsa> tablaResultados;

    // --- Columnas de la Tabla ---
    @FXML private TableColumn<IteracionReglaFalsa, Number> colIteracion;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colA;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colB;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colFa;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colFb;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colXr;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colFxr;
    @FXML private TableColumn<IteracionReglaFalsa, Number> colError;

    private ObservableList<IteracionReglaFalsa> iteraciones;

    @FXML
    public void initialize() {
        colIteracion.setCellValueFactory(cellData -> cellData.getValue().iteracionProperty());
        colA.setCellValueFactory(cellData -> cellData.getValue().aProperty());
        colB.setCellValueFactory(cellData -> cellData.getValue().bProperty());
        colFa.setCellValueFactory(cellData -> cellData.getValue().faProperty());
        colFb.setCellValueFactory(cellData -> cellData.getValue().fbProperty());
        colXr.setCellValueFactory(cellData -> cellData.getValue().xrProperty());
        colFxr.setCellValueFactory(cellData -> cellData.getValue().fxrProperty());
        colError.setCellValueFactory(cellData -> cellData.getValue().errorProperty());

        iteraciones = FXCollections.observableArrayList();
        tablaResultados.setItems(iteraciones);
    }

    @FXML
    protected void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Sintaxis de Funciones");
        alert.setHeaderText("Cómo escribir funciones matemáticas");

        String ayudaTexto = "Use 'x' como la variable.\n\n" +
                "Operadores Aritméticos:\n" +
                "  +  (suma)\n" +
                "  -  (resta)\n" +
                "  *  (multiplicación)\n" +
                "  /  (división)\n" +
                "  ^  (potencia, ej: x^2)\n\n" +
                "Funciones Comunes:\n" +
                "  sin(x), cos(x), tan(x)\n" +
                "  asin(x), acos(x), atan(x)\n" +
                "  sqrt(x)  (raíz cuadrada)\n" +
                "  exp(x)  (e elevado a la x)\n\n" +
                "Logaritmos:\n" +
                "  ln(x)    (logaritmo natural)\n" +
                "  log10(x) (logaritmo base 10)\n" +
                "  log(base, x) (logaritmo en cualquier base)\n\n" +
                "Constantes:\n" +
                "  pi\n" +
                "  e";

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
    protected void graficar() {
        try {
            String funcionStr = funcionTextField.getText();
            double xMin = Double.parseDouble(xMinField.getText());
            double xMax = Double.parseDouble(xMaxField.getText());
            if (funcionStr.isEmpty()) {
                funcionChart.setTitle("Por favor, ingrese una función.");
                return;
            }
            GraphingService.plotFunction(funcionChart, funcionStr, xMin, xMax, 500);
        } catch (NumberFormatException e) {
            funcionChart.setTitle("Error: El rango de X debe ser numérico.");
        } catch (Exception e) {
            funcionChart.setTitle("Error al graficar la función.");
        }
    }

    private double f(Expression expr, double x) {
        return expr.setVariable("x", x).evaluate();
    }

    @FXML
    protected void calcular() {
        iteraciones.clear();
        raizLabel.setText("Calculando...");
        try {
            String funcionStr = funcionTextField.getText();
            double a = Double.parseDouble(aTextField.getText());
            double b = Double.parseDouble(bTextField.getText());
            double tolerancia = Double.parseDouble(toleranciaTextField.getText());
            Expression expr = new ExpressionBuilder(funcionStr).variable("x").build();

            if (f(expr, a) * f(expr, b) >= 0) {
                raizLabel.setText("Error: La función no es válida en el intervalo o no cumple f(a) * f(b) < 0.");
                return;
            }

            double xr = a;
            int iter = 0;
            double error = Double.MAX_VALUE;

            while (error > tolerancia && iter < 100) {
                iter++;
                double fa = f(expr, a);
                double fb = f(expr, b);
                double xr_anterior = xr;
                xr = (a * fb - b * fa) / (fb - fa);
                double fxr = f(expr, xr);
                error = Math.abs(xr - xr_anterior);

                iteraciones.add(new IteracionReglaFalsa(iter, a, b, fa, fb, xr, fxr, error));

                if (fxr == 0.0) break;
                else if (fa * fxr < 0) b = xr;
                else a = xr;
            }
            raizLabel.setText(String.format("Raíz aproximada encontrada: %.8f", xr));
        } catch (NumberFormatException e) {
            raizLabel.setText("Error: Asegúrate de que a, b y tolerancia sean números válidos.");
        } catch (Exception e) {
            raizLabel.setText("Error en la función o cálculo: " + e.getMessage());
        }
    }

    @FXML
    protected void volver() {
        Navigation.navigateTo("hello-view.fxml", calcularButton);
    }
}
