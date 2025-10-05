package org.example.metodiapp.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Clase que representa una fila de datos (una iteración) para la tabla del método de Newton-Raphson.
 */
public class IteracionNewton {
    private final IntegerProperty iteracion;
    private final DoubleProperty xi;
    private final DoubleProperty fxi;
    private final DoubleProperty dfxi; // Derivada de f(xi)
    private final DoubleProperty xi1;  // xi+1
    private final DoubleProperty error;

    public IteracionNewton(int iteracion, double xi, double fxi, double dfxi, double xi1, double error) {
        this.iteracion = new SimpleIntegerProperty(iteracion);
        this.xi = new SimpleDoubleProperty(xi);
        this.fxi = new SimpleDoubleProperty(fxi);
        this.dfxi = new SimpleDoubleProperty(dfxi);
        this.xi1 = new SimpleDoubleProperty(xi1);
        this.error = new SimpleDoubleProperty(error);
    }

    // --- Getters para las Propiedades de JavaFX (necesarios para la TableView) ---
    public IntegerProperty iteracionProperty() {
        return iteracion;
    }

    public DoubleProperty xiProperty() {
        return xi;
    }

    public DoubleProperty fxiProperty() {
        return fxi;
    }

    public DoubleProperty dfxiProperty() {
        return dfxi;
    }

    public DoubleProperty xi1Property() {
        return xi1;
    }

    public DoubleProperty errorProperty() {
        return error;
    }
}
