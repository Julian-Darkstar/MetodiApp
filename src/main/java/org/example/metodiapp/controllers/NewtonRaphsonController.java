package org.example.metodiapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.example.metodiapp.models.IteracionNewton;
import org.example.metodiapp.services.Navigation;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class NewtonRaphsonController {

    // --- Campos de la Interfaz (Inputs) ---
    @FXML private TextField funcionTextField;
    @FXML private TextField valorInicialTextField;
    @FXML private TextField errorTextField;
    @FXML private Button calcularButton;

    // --- Campos de la Interfaz (Resultados) ---
    @FXML private Label raizLabel;
    @FXML private TableView<IteracionNewton> tablaResultados;

    // --- Columnas de la Tabla ---
    @FXML private TableColumn<IteracionNewton, Number> colIteracion;
    @FXML private TableColumn<IteracionNewton, Number> colXi;
    @FXML private TableColumn<IteracionNewton, Number> colFxi;
    @FXML private TableColumn<IteracionNewton, Number> colDfxi;
    @FXML private TableColumn<IteracionNewton, Number> colXi1;
    @FXML private TableColumn<IteracionNewton, Number> colError;

    private ObservableList<IteracionNewton> iteraciones;

    @FXML
    public void initialize() {
        colIteracion.setCellValueFactory(cellData -> cellData.getValue().iteracionProperty());
        colXi.setCellValueFactory(cellData -> cellData.getValue().xiProperty());
        colFxi.setCellValueFactory(cellData -> cellData.getValue().fxiProperty());
        colDfxi.setCellValueFactory(cellData -> cellData.getValue().dfxiProperty());
        colXi1.setCellValueFactory(cellData -> cellData.getValue().xi1Property());
        colError.setCellValueFactory(cellData -> cellData.getValue().errorProperty());

        iteraciones = FXCollections.observableArrayList();
        tablaResultados.setItems(iteraciones);
    }

    @FXML
    protected void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Sintaxis de Funciones");
        alert.setHeaderText("Cómo escribir funciones matemáticas");

        String ayudaTexto = "Use 'x' como la variable.\n\n" +
                "Operadores Aritméticos:\n" +
                "  +  (suma)\n" +
                "  -  (resta)\n" +
                "  *  (multiplicación)\n" +
                "  /  (división)\n" +
                "  ^  (potencia, ej: x^2)\n\n" +
                "Funciones Comunes:\n" +
                "  sin(x), cos(x), tan(x)\n" +
                "  asin(x), acos(x), atan(x)\n" +
                "  sqrt(x)  (raíz cuadrada)\n" +
                "  exp(x)  (e elevado a la x)\n\n" +
                "Logaritmos:\n" +
                "  ln(x)    (logaritmo natural)\n" +
                "  log10(x) (logaritmo base 10)\n" +
                "  log(base, x) (logaritmo en cualquier base)\n\n" +
                "Constantes:\n" +
                "  pi\n" +
                "  e";

        TextArea textArea = new TextArea(ayudaTexto);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    @FXML
    protected void calcular() {
        iteraciones.clear();
        raizLabel.setText("Calculando...");

        try {
            String funcionStr = funcionTextField.getText();
            double raiz = Double.parseDouble(valorInicialTextField.getText());
            double errorPermitido = Double.parseDouble(errorTextField.getText());

            Argument x = new Argument("x");
            Expression funcion = new Expression(funcionStr, x);
            Expression derivada = new Expression("der(" + funcionStr + ", x)", x);

            double errorCalculado = 100.0;
            int iteracion = 0;
            int maxIteraciones = 50;

            do {
                iteracion++;
                x.setArgumentValue(raiz);

                double valorFuncion = funcion.calculate();
                double valorDerivada = derivada.calculate();

                if (Math.abs(valorDerivada) < 1E-12) {
                    raizLabel.setText("Error: La derivada es cero. El método no puede continuar.");
                    return;
                }

                double nuevaRaiz = raiz - (valorFuncion / valorDerivada);
                errorCalculado = (Math.abs(nuevaRaiz) > 1E-12) ? Math.abs((nuevaRaiz - raiz) / nuevaRaiz) * 100 : 0.0;

                iteraciones.add(new IteracionNewton(iteracion, raiz, valorFuncion, valorDerivada, nuevaRaiz, errorCalculado));

                raiz = nuevaRaiz;

            } while (errorCalculado > errorPermitido && iteracion < maxIteraciones);

            if (errorCalculado <= errorPermitido) {
                raizLabel.setText(String.format("Solución encontrada: %.8f", raiz));
            } else {
                raizLabel.setText("El método no convergió después de " + maxIteraciones + " iteraciones.");
            }

        } catch (NumberFormatException e) {
            raizLabel.setText("Error: Asegúrate de que los valores de entrada sean números válidos.");
        } catch (Exception e) {
            raizLabel.setText("Error en la función o cálculo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void reiniciar() {
        funcionTextField.clear();
        valorInicialTextField.clear();
        errorTextField.clear();
        iteraciones.clear();
        raizLabel.setText("Raíz: ");
    }

    @FXML
    protected void volver() {
        Navigation.navigateTo("hello-view.fxml", calcularButton);
    }
}
