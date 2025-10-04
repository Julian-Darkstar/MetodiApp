package org.example.metodiapp.services;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Clase de utilidad para manejar la lógica de graficación de funciones.
 */
public class GraphingService {

    /**
     * Dibuja una función en un LineChart de JavaFX.
     *
     * @param chart El componente LineChart donde se dibujará la función.
     * @param functionStr La expresión matemática de la función (ej. "x^2 - 1").
     * @param xMin El valor mínimo del eje X para el ploteo.
     * @param xMax El valor máximo del eje X para el ploteo.
     * @param numPoints El número de puntos a calcular para la gráfica (mayor número = más suave).
     */
    public static void plotFunction(LineChart<Number, Number> chart, String functionStr, double xMin, double xMax, int numPoints) {
        // 1. Prepara la expresión matemática
        Expression expression = new ExpressionBuilder(functionStr).variable("x").build();

        // 2. Crea una nueva serie de datos
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(functionStr); // El nombre de la serie será la propia función

        // 3. Calcula los puntos
        double step = (xMax - xMin) / numPoints;
        for (double x = xMin; x <= xMax; x += step) {
            try {
                double y = expression.setVariable("x", x).evaluate();
                series.getData().add(new XYChart.Data<>(x, y));
            } catch (Exception e) {
                // Si un punto falla, simplemente no se añade y se continúa.
                System.err.println("No se pudo evaluar el punto x=" + x + ": " + e.getMessage());
            }
        }

        // 4. Limpia la gráfica anterior y dibuja la nueva serie
        chart.getData().clear();
        chart.getData().add(series);
        chart.setTitle("Gráfico de la Función");
    }
}
