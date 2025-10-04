package org.example.metodiapp;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * Un servicio de utilidad para graficar funciones matemáticas en un LineChart.
 */
public class GraphingService {

    /**
     * Grafica una función matemática en un LineChart dado.
     *
     * @param chart El LineChart donde se dibujará la función.
     * @param functionStr La expresión matemática de la función (ej. "x^2 - 4").
     * @param xMin El valor mínimo de x para el rango de graficación.
     * @param xMax El valor máximo de x para el rango de graficación.
     * @param steps El número de puntos a calcular y dibujar.
     */
    public static void plotFunction(LineChart<Number, Number> chart, String functionStr, double xMin, double xMax, int steps) {
        // 1. Limpiar cualquier gráfico anterior
        chart.getData().clear();

        // 2. Crear una nueva serie de datos para la función
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(functionStr); // El nombre de la serie será la propia función

        try {
            // 3. Preparar la expresión matemática con exp4j
            Expression expr = new ExpressionBuilder(functionStr).variable("x").build();

            // 4. Calcular los puntos
            double stepSize = (xMax - xMin) / steps;
            for (int i = 0; i <= steps; i++) {
                double x = xMin + i * stepSize;
                double y = expr.setVariable("x", x).evaluate();

                // Añadir el punto (x, y) a la serie
                series.getData().add(new XYChart.Data<>(x, y));
            }

            // 5. Añadir la serie de datos al gráfico
            chart.getData().add(series);

        } catch (Exception e) {
            // Si la función es inválida, no se grafica nada y se muestra un error en la consola.
            System.err.println("Error al graficar la función: " + e.getMessage());
            chart.setTitle("Error en la función: " + functionStr);
        }
    }
}
