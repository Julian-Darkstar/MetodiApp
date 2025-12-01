package org.example.metodiapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.metodiapp.Navigation;
import org.example.metodiapp.models.NewtonInterpolation;

import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class NewtonInterpolationController {

    @FXML private Button volverButton;
    // --- Enlaces con el FXML ---
    @FXML private TextField inputX;
    @FXML private TextField inputY;
    @FXML private TextField inputEvalX;
    @FXML private TextArea areaResultados;
    @FXML private TableView<NewtonInterpolation> tablaDatos;
    @FXML private TableColumn<NewtonInterpolation, Number> colX;
    @FXML private TableColumn<NewtonInterpolation, Number> colY;

    // Modelo de datos
    private final ObservableList<NewtonInterpolation> listaPuntos = FXCollections.observableArrayList();
    private final DecimalFormat df = new DecimalFormat("#.######");


    @FXML
    public void initialize() {
        tablaDatos.setItems(listaPuntos);

        // Configurar PropertyValueFactory para el Modelo Punto
        colX.setCellValueFactory(cellData -> cellData.getValue().xProperty());
        colY.setCellValueFactory(cellData -> cellData.getValue().yProperty());

        areaResultados.setText("Presione 'Agregar Punto' para empezar.");
    }

    // --- Lógica de la Interfaz (Event Handlers) ---

    @FXML
    private void agregarPunto() {
        try {
            double x = Double.parseDouble(inputX.getText());
            double y = Double.parseDouble(inputY.getText());

            if (listaPuntos.stream().anyMatch(p -> p.getX() == x)) {
                mostrarAlerta("Error", "El valor X = " + x + " ya existe. Los nodos X deben ser distintos.");
                return;
            }

            listaPuntos.add(new NewtonInterpolation(x, y));
            inputX.clear();
            inputY.clear();
            inputX.requestFocus();
            areaResultados.setText("Punto agregado. Agregue más o presione 'Calcular Interpolación'.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Por favor ingrese números válidos en X e Y.");
        }
    }

    @FXML
    private void limpiarPuntos() {
        listaPuntos.clear();
        reiniciarPantalla();
    }

    @FXML
    private void reiniciarPantalla() {
        listaPuntos.clear();
        inputX.clear();
        inputY.clear();
        inputEvalX.clear();
        areaResultados.clear();
        areaResultados.setText("Sistema reiniciado. Comience agregando los puntos.");
    }

    // --- Lógica Matemática (Algoritmo de Newton) ---

    @FXML
    private void calcularNewton() {
        int n = listaPuntos.size();
        if (n < 2) {
            areaResultados.setText("Se necesitan al menos 2 puntos para interpolar.");
            return;
        }

        double[] x = listaPuntos.stream().mapToDouble(NewtonInterpolation::getX).toArray();
        double[][] diff = new double[n][n];

        for (int i = 0; i < n; i++) {
            diff[i][0] = listaPuntos.get(i).getY(); // Columna 0: f[xi]
        }

        // 1. Cálculo de Diferencias Divididas
        IntStream.range(1, n).forEach(j ->
                IntStream.range(0, n - j).forEach(i -> {
                    double numerador = diff[i + 1][j - 1] - diff[i][j - 1];
                    double denominador = x[i + j] - x[i];
                    diff[i][j] = numerador / denominador;
                })
        );

        // Coeficientes son la diagonal superior
        double[] b = IntStream.range(0, n).mapToDouble(i -> diff[0][i]).toArray();

        // 2. Construir la salida
        StringBuilder sb = new StringBuilder();

        imprimirTabla(sb, n, x, diff);
        imprimirPolinomio(sb, n, x, b);
        evaluarPolinomio(sb, n, x, b);

        areaResultados.setText(sb.toString());
    }

    private void imprimirTabla(StringBuilder sb, int n, double[] x, double[][] diff) {
        sb.append("--- Tabla de Diferencias Divididas ---\n");
        sb.append(String.format("%-10s", "X"));
        for(int i=0; i<n; i++) sb.append(String.format("%-15s", "Orden " + i));
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-10s", df.format(x[i])));
            for (int j = 0; j < n - i; j++) {
                sb.append(String.format("%-15s", df.format(diff[i][j])));
            }
            sb.append("\n");
        }
        //
    }

    private void imprimirPolinomio(StringBuilder sb, int n, double[] x, double[] b) {
        sb.append("\n--- Polinomio de Newton P(x) ---\n");
        sb.append("P(x) = ").append(df.format(b[0]));
        for (int i = 1; i < n; i++) {
            double coef = b[i];
            if (coef >= 0) sb.append(" + ");
            else sb.append(" - ");
            sb.append(df.format(Math.abs(coef)));

            for (int k = 0; k < i; k++) {
                sb.append("(x - ").append(df.format(x[k])).append(")");
            }
        }
        sb.append("\n");
    }

    private void evaluarPolinomio(StringBuilder sb, int n, double[] x, double[] b) {
        if (!inputEvalX.getText().isEmpty()) {
            try {
                double valorX = Double.parseDouble(inputEvalX.getText());
                double resultado = 0;

                // Evaluación P(x) = b0 + b1(x-x0) + b2(x-x0)(x-x1) + ...
                double termino = 1;
                resultado = b[0];

                for (int i = 1; i < n; i++) {
                    termino = termino * (valorX - x[i-1]);
                    resultado += b[i] * termino;
                }

                sb.append("\n--- Evaluación Final ---\n");
                sb.append("f(").append(df.format(valorX)).append(") ≈ ").append(df.format(resultado));

            } catch (NumberFormatException ex) {
                sb.append("\nAdvertencia: Valor a estimar no válido.");
            }
        }
    }

    // --- Métodos de Utilidad ---

    @FXML
    private void mostrarAyuda() {
        mostrarAlerta("Ayuda - Interpolación de Newton", "Este método construye un polinomio de grado (n-1) que pasa exactamente por los 'n' puntos de datos. Utiliza la tabla de diferencias divididas para encontrar los coeficientes del polinomio.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    protected void volver() {
        System.out.println("Volviendo al menú principal...");
        Navigation.navigateTo("hello-view.fxml", volverButton);
    }
}