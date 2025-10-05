package org.example.metodiapp.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Clase que representa una fila de datos (una iteración) para la tabla del método de Newton-Raphson Multivariable (2x2).
 */
public class IteracionNewtonMulti {
    private final IntegerProperty iteracion;
    private final DoubleProperty x;
    private final DoubleProperty y;
    private final DoubleProperty df1dx;
    private final DoubleProperty df1dy;
    private final DoubleProperty df2dx;
    private final DoubleProperty df2dy;
    private final DoubleProperty f1;
    private final DoubleProperty f2;
    private final DoubleProperty errorX; // Reemplaza a error
    private final DoubleProperty errorY; // Reemplaza a error

    public IteracionNewtonMulti(int iteracion, double x, double y, double df1dx, double df1dy, double df2dx, double df2dy, double f1, double f2, double errorX, double errorY) {
        this.iteracion = new SimpleIntegerProperty(iteracion);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.df1dx = new SimpleDoubleProperty(df1dx);
        this.df1dy = new SimpleDoubleProperty(df1dy);
        this.df2dx = new SimpleDoubleProperty(df2dx);
        this.df2dy = new SimpleDoubleProperty(df2dy);
        this.f1 = new SimpleDoubleProperty(f1);
        this.f2 = new SimpleDoubleProperty(f2);
        this.errorX = new SimpleDoubleProperty(errorX);
        this.errorY = new SimpleDoubleProperty(errorY);
    }

    // --- Getters para las Propiedades de JavaFX (necesarios para la TableView) ---
    public IntegerProperty iteracionProperty() { return iteracion; }
    public DoubleProperty xProperty() { return x; }
    public DoubleProperty yProperty() { return y; }
    public DoubleProperty df1dxProperty() { return df1dx; }
    public DoubleProperty df1dyProperty() { return df1dy; }
    public DoubleProperty df2dxProperty() { return df2dx; }
    public DoubleProperty df2dyProperty() { return df2dy; }
    public DoubleProperty f1Property() { return f1; }
    public DoubleProperty f2Property() { return f2; }
    public DoubleProperty errorXProperty() { return errorX; } // Nuevo
    public DoubleProperty errorYProperty() { return errorY; } // Nuevo
}
