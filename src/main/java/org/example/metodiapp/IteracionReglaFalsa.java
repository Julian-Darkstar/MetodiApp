package org.example.metodiapp;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Clase que representa una fila de datos (una iteración) para la tabla del método de la Regla Falsa.
 * JavaFX utiliza estas propiedades para poblar las celdas de la tabla.
 */
public class IteracionReglaFalsa {
    private final SimpleIntegerProperty iteracion;
    private final SimpleDoubleProperty a;
    private final SimpleDoubleProperty b;
    private final SimpleDoubleProperty c;
    private final SimpleDoubleProperty fc;
    private final SimpleDoubleProperty error;

    public IteracionReglaFalsa(int iteracion, double a, double b, double c, double fc, double error) {
        this.iteracion = new SimpleIntegerProperty(iteracion);
        this.a = new SimpleDoubleProperty(a);
        this.b = new SimpleDoubleProperty(b);
        this.c = new SimpleDoubleProperty(c);
        this.fc = new SimpleDoubleProperty(fc);
        this.error = new SimpleDoubleProperty(error);
    }

    // Getters (necesarios para que JavaFX pueda acceder a los valores)
    public int getIteracion() {
        return iteracion.get();
    }

    public double getA() {
        return a.get();
    }

    public double getB() {
        return b.get();
    }

    public double getC() {
        return c.get();
    }

    public double getFc() {
        return fc.get();
    }

    public double getError() {
        return error.get();
    }
}
