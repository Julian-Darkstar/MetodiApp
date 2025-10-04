package org.example.metodiapp.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class IteracionReglaFalsa {
    private final IntegerProperty iteracion;
    private final DoubleProperty a;
    private final DoubleProperty b;
    private final DoubleProperty fa; // Nueva propiedad
    private final DoubleProperty fb; // Nueva propiedad
    private final DoubleProperty xr; // Renombrada de 'c'
    private final DoubleProperty fxr; // Renombrada de 'fc'
    private final DoubleProperty error;

    public IteracionReglaFalsa(int iteracion, double a, double b, double fa, double fb, double xr, double fxr, double error) {
        this.iteracion = new SimpleIntegerProperty(iteracion);
        this.a = new SimpleDoubleProperty(a);
        this.b = new SimpleDoubleProperty(b);
        this.fa = new SimpleDoubleProperty(fa); // Inicializa la nueva propiedad
        this.fb = new SimpleDoubleProperty(fb); // Inicializa la nueva propiedad
        this.xr = new SimpleDoubleProperty(xr);
        this.fxr = new SimpleDoubleProperty(fxr);
        this.error = new SimpleDoubleProperty(error);
    }

    // --- Getters para los valores primitivos ---
    public int getIteracion() { return iteracion.get(); }
    public double getA() { return a.get(); }
    public double getB() { return b.get(); }
    public double getFa() { return fa.get(); } // Getter nuevo
    public double getFb() { return fb.get(); } // Getter nuevo
    public double getXr() { return xr.get(); }
    public double getFxr() { return fxr.get(); }
    public double getError() { return error.get(); }

    // --- Getters para las Propiedades de JavaFX (Esto es lo que necesita la TableView) ---
    public IntegerProperty iteracionProperty() { return iteracion; }
    public DoubleProperty aProperty() { return a; }
    public DoubleProperty bProperty() { return b; }
    public DoubleProperty faProperty() { return fa; } // Property getter nuevo
    public DoubleProperty fbProperty() { return fb; } // Property getter nuevo
    public DoubleProperty xrProperty() { return xr; }
    public DoubleProperty fxrProperty() { return fxr; }
    public DoubleProperty errorProperty() { return error; }
}
