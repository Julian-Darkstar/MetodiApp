package org.example.metodiapp.models;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class MRegresion {
    private double[] coefficients;

    public void calculateRegression(double[][] x, double[] y) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        int numObservations = x.length;
        int numVariables = x[0].length -1;
        double[][] xWithoutIntercept = new double[numObservations][numVariables];
        for(int i=0; i<numObservations; i++){
            for(int j=0; j<numVariables; j++){
                xWithoutIntercept[i][j] = x[i][j+1];
            }
        }

        regression.newSampleData(y, xWithoutIntercept);
        coefficients = regression.estimateRegressionParameters();
    }

    public double[] getCoefficients() {
        return coefficients;
    }
}