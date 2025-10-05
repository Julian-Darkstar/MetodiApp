package org.example.metodiapp.models;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class JacobiIteracion {

    private final IntegerProperty iteracion;
    private final DoubleProperty[] x;      // valores actuales
    private final DoubleProperty[] xPrev;  // valores de la iteración anterior
    private final DoubleProperty[] e;

    public JacobiIteracion(int iter, double[] xActual, double[] e, double[] xPrev) {
        this.iteracion = new SimpleIntegerProperty(iter);
        this.e = new DoubleProperty[xActual.length];
        this.x = new DoubleProperty[xActual.length];
        this.xPrev = new DoubleProperty[xActual.length];

        for (int i = 0; i < xActual.length; i++) {
            this.x[i] = new SimpleDoubleProperty(xActual[i]);
            this.e[i] = new SimpleDoubleProperty(e[i]);
            this.xPrev[i] = new SimpleDoubleProperty(xPrev[i]);
        }
    }

    public IntegerProperty iteracionProperty() {
        return iteracion;
    }

    public ObservableValue<Number> getXProperty(int index) {
        return x[index];
    }

    public ObservableValue<Number> getXPrevProperty(int index) {
        return xPrev[index];
    }

    public ObservableValue<Number> getEProperty(int index) {
        return e[index];
    }
}
