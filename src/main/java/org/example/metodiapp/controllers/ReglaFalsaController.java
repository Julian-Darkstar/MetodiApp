package org.example.metodiapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
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
    @FXML private TableColumn<IteracionReglaFalsa, Number> colFa; // Nueva columna
    @FXML private TableColumn<IteracionReglaFalsa, Number> colFb; // Nueva columna
    @FXML private TableColumn<IteracionReglaFalsa, Number> colXr; // Renombrada
    @FXML private TableColumn<IteracionReglaFalsa, Number> colFxr; // Renombrada
    @FXML private TableColumn<IteracionReglaFalsa, Number> colError;

    private ObservableList<IteracionReglaFalsa> iteraciones;

    @FXML
    public void initialize() {
        // Conexión manual y explícita de las columnas a las propiedades del modelo
        colIteracion.setCellValueFactory(cellData -> cellData.getValue().iteracionProperty());
        colA.setCellValueFactory(cellData -> cellData.getValue().aProperty());
        colB.setCellValueFactory(cellData -> cellData.getValue().bProperty());
        colFa.setCellValueFactory(cellData -> cellData.getValue().faProperty()); // Conexión nueva
        colFb.setCellValueFactory(cellData -> cellData.getValue().fbProperty()); // Conexión nueva
        colXr.setCellValueFactory(cellData -> cellData.getValue().xrProperty()); // Conexión actualizada
        colFxr.setCellValueFactory(cellData -> cellData.getValue().fxrProperty()); // Conexión actualizada
        colError.setCellValueFactory(cellData -> cellData.getValue().errorProperty());

        // Vincula la lista de datos a la tabla
        iteraciones = FXCollections.observableArrayList();
        tablaResultados.setItems(iteraciones);
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

            double xr = a; // Renombrada de 'c'
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

                // Pasa los nuevos valores (fa, fb) al crear el objeto de la iteración
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
