package org.example.metodiapp.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.metodiapp.Navigation;
import org.example.metodiapp.models.JacobiIteracion;


public class JacobiController {

    public Button volverButton;
    @FXML private TextField numVariablesField, errorPermitidoField;
    @FXML private VBox ecuacionesBox, despejadasBox;
    @FXML private TableView<JacobiIteracion> tablaJacobi;
    @FXML private Label resultadoLabel;

    private int n;
    private double errorPermitido;
    private TextField[][] coefFields;
    private TextField[] resFields;

    @FXML
    public void generarEcuaciones() {
        ecuacionesBox.getChildren().clear();
        despejadasBox.getChildren().clear();
        tablaJacobi.getColumns().clear();

        try {
            n = Integer.parseInt(numVariablesField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un número válido de variables.");
            return;
        }

        coefFields = new TextField[n][n];
        resFields = new TextField[n];

        // Generar campos para coeficientes y resultados
        for (int i = 0; i < n; i++) {
            HBox fila = new HBox(5);
            for (int j = 0; j < n; j++) {
                coefFields[i][j] = new TextField();
                coefFields[i][j].setPromptText("X" + (j + 1));
                coefFields[i][j].setPrefWidth(60);
                fila.getChildren().add(coefFields[i][j]);
            }
            Label lblX = new Label(" = ");
            resFields[i] = new TextField();
            resFields[i].setPromptText("c" + (i + 1));
            resFields[i].setPrefWidth(60);
            fila.getChildren().addAll(lblX, resFields[i]);
            ecuacionesBox.getChildren().add(fila);
        }

        crearColumnasDinamicas();
    }

    private void crearColumnasDinamicas() {
        tablaJacobi.getColumns().clear();

        // Columna de iteración
        TableColumn<JacobiIteracion, Number> colIter = new TableColumn<>("Iter");
        colIter.setCellValueFactory(d -> d.getValue().iteracionProperty());
        tablaJacobi.getColumns().add(colIter);

        // Columnas X1..Xn (iteración anterior)
        for (int i = 0; i < n; i++) {
            TableColumn<JacobiIteracion, Number> colXPrev = new TableColumn<>("X" + (i + 1));
            final int idx = i;
            colXPrev.setCellValueFactory(d -> d.getValue().getXPrevProperty(idx));
            tablaJacobi.getColumns().add(colXPrev);
        }

        // Columnas XN1..XNn (iteración actual)
        for (int i = 0; i < n; i++) {
            TableColumn<JacobiIteracion, Number> colXNuevo = new TableColumn<>("XN" + (i + 1));
            final int idx = i;
            colXNuevo.setCellValueFactory(d -> d.getValue().getXProperty(idx));
            tablaJacobi.getColumns().add(colXNuevo);
        }

        // Columnas de error
        for (int i = 0; i < n; i++) {
            TableColumn<JacobiIteracion, Number> colE = new TableColumn<>("E" + (i + 1));
            final int idx = i;
            colE.setCellValueFactory(d -> {

                return new SimpleDoubleProperty(d.getValue().getEProperty(idx).getValue().doubleValue() * 100);
            });
            tablaJacobi.getColumns().add(colE);
        }
    }

    @FXML
    public void calcularJacobi() {
        try {
            errorPermitido = Double.parseDouble(errorPermitidoField.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese un error válido.");
            return;
        }

        double[][] A = new double[n][n];
        double[] b = new double[n];

        // Leer valores
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = Double.parseDouble(coefFields[i][j].getText());
            }
            b[i] = Double.parseDouble(resFields[i].getText());
        }

        despejarEcuaciones(A, b);
        ejecutarJacobi(A, b);
    }

    private void despejarEcuaciones(double[][] A, double[] b) {
        despejadasBox.getChildren().clear();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder("X" + (i + 1) + " = (" + b[i]);
            for (int j = 0; j < n; j++) {
                if (j != i && A[i][j] != 0)
                    sb.append(" - (" + A[i][j] + " * X" + (j + 1) + ")");
            }
            sb.append(") / " + A[i][i]);
            despejadasBox.getChildren().add(new Label(sb.toString()));
        }
    }

    private void ejecutarJacobi(double[][] A, double[] b) {
        ObservableList<JacobiIteracion> data = FXCollections.observableArrayList();
        double[] x = new double[n];        // valores actuales
        double[] xNuevo = new double[n];   // valores calculados
        double[] error = new double[n];    // errores

        boolean continuar = true;
        int iter = 0;
        double[] xPrev = new double[n];    // iteración anterior real

        while (iter < 100) {
            iter++;
            xPrev = x.clone(); // guardamos iteración anterior
            continuar = false;

            for (int i = 0; i < n; i++) {
                double suma = b[i];
                for (int j = 0; j < n; j++) {
                    if (j != i) suma -= A[i][j] * x[j];
                }
                xNuevo[i] = suma / A[i][i];
                error[i] = Math.abs((xNuevo[i] - x[i]) / xNuevo[i]);

                if (error[i] > errorPermitido) continuar = true;
            }

            data.add(new JacobiIteracion(iter, xNuevo.clone(), error.clone(), xPrev));
            System.arraycopy(xNuevo, 0, x, 0, n);

            if (!continuar) break;
        }

        // Última iteración final con errores reales
        double[] xFinal = x.clone();
        double[] errorFinal = new double[n];
        for (int i = 0; i < n; i++) {
            errorFinal[i] = Math.abs((xFinal[i] - xPrev[i]) / xFinal[i]);
        }

        data.add(new JacobiIteracion(iter + 1, xFinal, errorFinal, xPrev));

        tablaJacobi.setItems(data);

        // Mostrar resultado final
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("X").append(i + 1).append(" = ").append(String.format("%.6f ", x[i]));
        }
        resultadoLabel.setText(sb.toString());
    }




    private void mostrarAlerta(String titulo, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    public void mostrarAyuda() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Método de Jacobi");
        a.setContentText("Resuelve sistemas Ax = b iterativamente.\n\n" +
                "1. Ingrese número de variables.\n" +
                "2. Llene los coeficientes del sistema.\n" +
                "3. Presione 'Generar' y luego 'Calcular'.");
        a.showAndWait();
    }

    @FXML
    public void reiniciarPantalla() {
        // Limpiar campos de texto de número de variables y error permitido
        numVariablesField.clear();
        errorPermitidoField.clear();

        // Limpiar campos de coeficientes y resultados
        if (coefFields != null) {
            for (int i = 0; i < coefFields.length; i++) {
                for (int j = 0; j < coefFields[i].length; j++) {
                    if (coefFields[i][j] != null) coefFields[i][j].clear();
                }
                if (resFields[i] != null) resFields[i].clear();
            }
        }

        // Limpiar VBoxes de ecuaciones y despejadas
        if (ecuacionesBox != null) ecuacionesBox.getChildren().clear();
        if (despejadasBox != null) despejadasBox.getChildren().clear();

        // Limpiar tabla
        if (tablaJacobi != null) {
            tablaJacobi.getColumns().clear();
            tablaJacobi.getItems().clear();
        }

        // Limpiar resultado
        if (resultadoLabel != null) resultadoLabel.setText("Aún no calculado");

        // Reiniciar variables internas
        n = 0;
        errorPermitido = 0.0;
        coefFields = null;
        resFields = null;
    }


    @FXML
    protected void volver() {
        System.out.println("Volviendo al menú principal...");
        Navigation.navigateTo("hello-view.fxml", volverButton);
    }
}
