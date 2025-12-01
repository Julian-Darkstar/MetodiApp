package org.example.metodiapp.models;

import javafx.beans.property.SimpleDoubleProperty;

/**
 * Representa un punto (x, y) para la interpolación.
 */
public class NewtonInterpolation {
    private final SimpleDoubleProperty x;
    private final SimpleDoubleProperty y;

    public NewtonInterpolation(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }

    // Getters para FXML TableView
    public SimpleDoubleProperty xProperty() {
        return x;
    }

    public SimpleDoubleProperty yProperty() {
        return y;
    }

    public double getX() {
        return x.get();
    }

    public double getY() {
        return y.get();
    }
}