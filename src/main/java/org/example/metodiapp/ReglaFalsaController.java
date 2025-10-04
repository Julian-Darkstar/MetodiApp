package org.example.metodiapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ReglaFalsaController {

    // --- Campos de la Interfaz ---
    @FXML private TextField funcionTextField;
    @FXML private TextField aTextField;
    @FXML private TextField bTextField;
    @FXML private TextField toleranciaTextField;
    @FXML private TableView<IteracionReglaFalsa> tablaResultados;
    @FXML private Label raizLabel;
    @FXML private Button calcularButton; // Se usará como nodo de contexto para la navegación

    // --- Campos para el gráfico (implementación existente) ---
    @FXML private LineChart<Number, Number> funcionChart;
    @FXML private TextField xMinField;
    @FXML private TextField xMaxField;

    /**
     * Método existente para graficar la función.
     */
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
            System.err.println("Error en el rango de graficación: " + e.getMessage());
        } catch (Exception e) {
            funcionChart.setTitle("Error al graficar la función.");
            System.err.println("Error inesperado al graficar: " + e.getMessage());
        }
    }

    private double f(Expression expr, double x) {
        return expr.setVariable("x", x).evaluate();
    }

    @FXML
    protected void calcular() {
        try {
            String funcionStr = funcionTextField.getText();
            double a = Double.parseDouble(aTextField.getText());
            double b = Double.parseDouble(bTextField.getText());
            double tolerancia = Double.parseDouble(toleranciaTextField.getText());

            Expression expr = new ExpressionBuilder(funcionStr).variable("x").build();

            if (f(expr, a) * f(expr, b) >= 0) {
                raizLabel.setText("Error: La función no es válida en el intervalo o no cumple f(a) * f(b) < 0.");
                tablaResultados.setItems(null);
                return;
            }

            ObservableList<IteracionReglaFalsa> iteraciones = FXCollections.observableArrayList();
            tablaResultados.setItems(iteraciones);
            raizLabel.setText("Raíz: ...");

            double c = a;
            int iter = 0;
            double error = Double.MAX_VALUE;

            while (error > tolerancia && iter < 100) {
                iter++;
                double fa = f(expr, a);
                double fb = f(expr, b);
                double c_anterior = c;
                c = (a * fb - b * fa) / (fb - fa);
                double fc = f(expr, c);
                error = Math.abs(c - c_anterior);

                iteraciones.add(new IteracionReglaFalsa(iter, a, b, c, fc, error));

                if (fc == 0.0) {
                    break;
                } else if (fa * fc < 0) {
                    b = c;
                } else {
                    a = c;
                }
            }

            raizLabel.setText(String.format("Raíz aproximada encontrada: %.8f", c));

        } catch (NumberFormatException e) {
            raizLabel.setText("Error: Asegúrate de que a, b y tolerancia sean números válidos.");
            tablaResultados.setItems(null);
        } catch (Exception e) {
            raizLabel.setText("Error en la función o cálculo: " + e.getMessage());
            tablaResultados.setItems(null);
        }
    }

    /**
     * Vuelve a la pantalla de bienvenida utilizando la clase de navegación centralizada.
     */
    @FXML
    protected void volver() {
        Navigation.navigateTo("hello-view.fxml", calcularButton);
    }
}
