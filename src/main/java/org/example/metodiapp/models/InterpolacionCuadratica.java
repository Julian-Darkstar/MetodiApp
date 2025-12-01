package org.example.metodiapp.models;

public class InterpolacionCuadratica {

    public static double interpolar(double x0, double y0, double x1, double y1, double x2, double y2, double x) {
        double b0 = y0;
        double b1 = (y1 - y0) / (x1 - x0);
        double b2 = ((y2 - y1) / (x2 - x1) - b1) / (x2 - x0);

        return b0 + b1 * (x - x0) + b2 * (x - x0) * (x - x1);
    }
}
