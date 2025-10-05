package org.example.metodiapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.example.metodiapp.models.IteracionNewtonMulti;
import org.example.metodiapp.services.Navigation;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class NewtonMultiController {

    // --- Campos de la Interfaz (Inputs) ---
    @FXML private TextField funcion1TextField;
    @FXML private TextField funcion2TextField;
    @FXML private TextField xInicialTextField;
    @FXML private TextField yInicialTextField;
    @FXML private TextField toleranciaTextField;
    @FXML private Button calcularButton;

    // --- Campos de la Interfaz (Resultados) ---
    @FXML private Label raizLabel;
    @FXML private TableView<IteracionNewtonMulti> tablaResultados;

    // --- Columnas de la Tabla ---
    @FXML private TableColumn<IteracionNewtonMulti, Number> colIteracion;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colX;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colY;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colF1;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colF2;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colDf1dx;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colDf1dy;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colDf2dx;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colDf2dy;
    @FXML private TableColumn<IteracionNewtonMulti, Number> colErrorX; // Nueva columna
    @FXML private TableColumn<IteracionNewtonMulti, Number> colErrorY; // Nueva columna

    private ObservableList<IteracionNewtonMulti> iteraciones;

    @FXML
    public void initialize() {
        // Conexión manual y explícita de las columnas
        colIteracion.setCellValueFactory(cellData -> cellData.getValue().iteracionProperty());
        colX.setCellValueFactory(cellData -> cellData.getValue().xProperty());
        colY.setCellValueFactory(cellData -> cellData.getValue().yProperty());
        colF1.setCellValueFactory(cellData -> cellData.getValue().f1Property());
        colF2.setCellValueFactory(cellData -> cellData.getValue().f2Property());
        colDf1dx.setCellValueFactory(cellData -> cellData.getValue().df1dxProperty());
        colDf1dy.setCellValueFactory(cellData -> cellData.getValue().df1dyProperty());
        colDf2dx.setCellValueFactory(cellData -> cellData.getValue().df2dxProperty());
        colDf2dy.setCellValueFactory(cellData -> cellData.getValue().df2dyProperty());
        colErrorX.setCellValueFactory(cellData -> cellData.getValue().errorXProperty()); // Conexión nueva
        colErrorY.setCellValueFactory(cellData -> cellData.getValue().errorYProperty()); // Conexión nueva

        iteraciones = FXCollections.observableArrayList();
        tablaResultados.setItems(iteraciones);
    }

    @FXML
    protected void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Sintaxis de Funciones");
        alert.setHeaderText("Cómo escribir funciones matemáticas");

        String ayudaTexto = "Use 'x' e 'y' como las variables.\n\n" +
                "Operadores Aritméticos:\n" +
                "  +  (suma)\n" +
                "  -  (resta)\n" +
                "  *  (multiplicación)\n" +
                "  /  (división)\n" +
                "  ^  (potencia, ej: x^2)\n\n" +
                "Funciones Comunes:\n" +
                "  sin(x), cos(y), tan(x*y)\n" +
                "  sqrt(x^2 + y^2)  (raíz cuadrada)\n" +
                "  exp(x)  (e elevado a la x)\n\n" +
                "Logaritmos:\n" +
                "  ln(x)    (logaritmo natural)\n" +
                "  log10(y) (logaritmo base 10)\n" +
                "  log(base, expr) (logaritmo en cualquier base)\n\n" +
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
            String f1Str = funcion1TextField.getText();
            String f2Str = funcion2TextField.getText();
            double xVal = Double.parseDouble(xInicialTextField.getText());
            double yVal = Double.parseDouble(yInicialTextField.getText());
            double tolerancia = Double.parseDouble(toleranciaTextField.getText());

            Argument x = new Argument("x");
            Argument y = new Argument("y");

            Expression f1 = new Expression(f1Str, x, y);
            Expression f2 = new Expression(f2Str, x, y);
            Expression df1dx = new Expression("der(" + f1Str + ", x)", x, y);
            Expression df1dy = new Expression("der(" + f1Str + ", y)", x, y);
            Expression df2dx = new Expression("der(" + f2Str + ", x)", x, y);
            Expression df2dy = new Expression("der(" + f2Str + ", y)", x, y);

            int iter = 0;
            int maxIter = 100;
            double errorX = 100.0;
            double errorY = 100.0;

            while (Math.max(errorX, errorY) > tolerancia && iter < maxIter) {
                iter++;
                x.setArgumentValue(xVal);
                y.setArgumentValue(yVal);

                double f1Val = f1.calculate();
                double f2Val = f2.calculate();
                double df1dxVal = df1dx.calculate();
                double df1dyVal = df1dy.calculate();
                double df2dxVal = df2dx.calculate();
                double df2dyVal = df2dy.calculate();

                double detJ = df1dxVal * df2dyVal - df1dyVal * df2dxVal;
                if (Math.abs(detJ) < 1e-12) {
                    raizLabel.setText("Error: El determinante del Jacobiano es cero.");
                    return;
                }

                double dx = (-f1Val * df2dyVal - (-f2Val * df1dyVal)) / detJ;
                double dy = (df1dxVal * -f2Val - (-f1Val * df2dxVal)) / detJ;

                double xNuevo = xVal + dx;
                double yNuevo = yVal + dy;

                // Calcular errores relativos porcentuales para cada variable
                errorX = (Math.abs(xNuevo) > 1e-12) ? Math.abs(dx / xNuevo) * 100 : 0.0;
                errorY = (Math.abs(yNuevo) > 1e-12) ? Math.abs(dy / yNuevo) * 100 : 0.0;

                iteraciones.add(new IteracionNewtonMulti(iter, xVal, yVal, df1dxVal, df1dyVal, df2dxVal, df2dyVal, f1Val, f2Val, errorX, errorY));

                xVal = xNuevo;
                yVal = yNuevo;
            }

            if (Math.max(errorX, errorY) <= tolerancia) {
                raizLabel.setText(String.format("Solución encontrada: x=%.6f, y=%.6f", xVal, yVal));
            } else {
                raizLabel.setText("El método no convergió después de " + maxIter + " iteraciones.");
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
        funcion1TextField.clear();
        funcion2TextField.clear();
        xInicialTextField.clear();
        yInicialTextField.clear();
        toleranciaTextField.clear();
        iteraciones.clear();
        raizLabel.setText("Raíz: ");
    }

    @FXML
    protected void volver() {
        Navigation.navigateTo("hello-view.fxml", calcularButton);
    }
}
