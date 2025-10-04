import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GaussJordanGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField txtFilas;
    private JTextField txtColumnas;
    private JButton btnCrearMatriz;
    private JTable tablaMatriz;
    private DefaultTableModel modeloTabla;
    private JButton btnResolver;
    private JButton btnReiniciar;
    private JButton btnTerminar;
    private JTextArea areaResultado;
    private JScrollPane scrollTabla;
    private JScrollPane scrollResultado;

    private double[][] matriz;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GaussJordanGUI();
            }
        });
    }

    public GaussJordanGUI() {
        crearInterfaz();
    }

    private void crearInterfaz() {
        frame = new JFrame("Resolución de Matrices - Método Gauss-Jordan");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Panel superior para ingresar dimensiones
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Dimensiones de la Matriz"));

        panelSuperior.add(new JLabel("Filas (m):"));
        txtFilas = new JTextField(5);
        panelSuperior.add(txtFilas);

        panelSuperior.add(new JLabel("Columnas (n):"));
        txtColumnas = new JTextField(5);
        panelSuperior.add(txtColumnas);

        btnCrearMatriz = new JButton("Crear Matriz");
        panelSuperior.add(btnCrearMatriz);

        // Panel central para la tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Ingreso de Datos"));

        modeloTabla = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        tablaMatriz = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tablaMatriz);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior para botones y resultados
        JPanel panelInferior = new JPanel(new BorderLayout());

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnResolver = new JButton("Resolver por Gauss-Jordan");
        btnReiniciar = new JButton("Reiniciar");
        btnTerminar = new JButton("Terminar Método");

        panelBotones.add(btnResolver);
        panelBotones.add(btnReiniciar);
        panelBotones.add(btnTerminar);

        panelInferior.add(panelBotones, BorderLayout.NORTH);

        areaResultado = new JTextArea(10, 50);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollResultado = new JScrollPane(areaResultado);
        scrollResultado.setBorder(BorderFactory.createTitledBorder("Proceso y Resultados"));
        panelInferior.add(scrollResultado, BorderLayout.CENTER);

        // Agregar paneles al frame principal
        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(panelCentral, BorderLayout.CENTER);
        frame.add(panelInferior, BorderLayout.SOUTH);

        // Configurar eventos
        configurarEventos();

        frame.pack();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void configurarEventos() {
        btnCrearMatriz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearMatriz();
            }
        });

        btnResolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolverMatriz();
            }
        });

        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarPrograma();
            }
        });

        btnTerminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void crearMatriz() {
        try {
            int m = Integer.parseInt(txtFilas.getText());
            int n = Integer.parseInt(txtColumnas.getText());

            if (m <= 0 || n <= 0) {
                JOptionPane.showMessageDialog(frame, "Las dimensiones deben ser mayores que cero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (n == 1) {
                JOptionPane.showMessageDialog(frame, "Debe haber al menos 2 columnas (variables + término independiente)",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Configurar la tabla
            modeloTabla.setRowCount(m);
            modeloTabla.setColumnCount(n);

            // Configurar nombres de columnas con x, y, z...
            String[] nombresColumnas = new String[n];
            char variable = 'x';
            for (int i = 0; i < n - 1; i++) {
                if (i < 3) {
                    // Para las primeras 3 variables usar x, y, z
                    nombresColumnas[i] = String.valueOf((char)('x' + i));
                } else {
                    // Para más variables usar x4, x5, etc.
                    nombresColumnas[i] = "x" + (i + 1);
                }
            }
            nombresColumnas[n - 1] = "Término";
            modeloTabla.setColumnIdentifiers(nombresColumnas);

            // Dejar todas las casillas vacías
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    modeloTabla.setValueAt("", i, j);
                }
            }

            JOptionPane.showMessageDialog(frame,
                    "Matriz creada. Ingrese los coeficientes en la tabla.\n" +
                            "Última columna: términos independientes",
                    "Matriz Creada", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Ingrese valores numéricos válidos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resolverMatriz() {
        try {
            int m = modeloTabla.getRowCount();
            int n = modeloTabla.getColumnCount();

            if (m == 0 || n == 0) {
                JOptionPane.showMessageDialog(frame, "Primero debe crear una matriz",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener datos de la tabla
            matriz = new double[m][n];
            boolean datosValidos = true;
            StringBuilder errores = new StringBuilder();

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    String valor = modeloTabla.getValueAt(i, j).toString().trim();
                    if (valor.isEmpty()) {
                        errores.append("Fila ").append(i + 1).append(", Columna ").append(j + 1).append(": vacía\n");
                        datosValidos = false;
                    } else {
                        try {
                            matriz[i][j] = Double.parseDouble(valor);
                        } catch (NumberFormatException ex) {
                            errores.append("Fila ").append(i + 1).append(", Columna ").append(j + 1).append(": '").append(valor).append("' no es número válido\n");
                            datosValidos = false;
                        }
                    }
                }
            }

            if (!datosValidos) {
                JOptionPane.showMessageDialog(frame,
                        "Errores en los datos:\n" + errores.toString() +
                                "\nPor favor, complete todas las casillas con números válidos.",
                        "Error en datos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostrar matriz original
            areaResultado.setText("=== MATRIZ ORIGINAL ===\n");
            areaResultado.append(mostrarMatrizString(matriz));

            // Resolver por Gauss-Jordan
            resolverGaussJordan();

            // Mostrar matriz reducida
            areaResultado.append("\n=== MATRIZ REDUCIDA ===\n");
            areaResultado.append(mostrarMatrizString(matriz));

            // Mostrar solución
            areaResultado.append("\n=== SOLUCIÓN DEL SISTEMA ===\n");
            areaResultado.append(mostrarSolucionString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                    "Error al resolver el sistema: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resolverGaussJordan() {
        int filas = matriz.length;
        int columnas = matriz[0].length;

        areaResultado.append("\n=== PROCESO DE ELIMINACIÓN ===\n");

        for (int pivote = 0; pivote < filas && pivote < columnas - 1; pivote++) {
            // Hacer el elemento pivote igual a 1
            double divisor = matriz[pivote][pivote];

            // Si el pivote es cero, buscar intercambio de filas
            if (Math.abs(divisor) < 1e-10) {
                boolean intercambiado = intercambiarFila(pivote);
                if (!intercambiado) {
                    areaResultado.append("⚠️ No se puede encontrar pivote no nulo en columna " + (pivote + 1) + "\n");
                    continue;
                }
                divisor = matriz[pivote][pivote];
            }

            // Dividir toda la fila por el pivote
            for (int j = 0; j < columnas; j++) {
                matriz[pivote][j] /= divisor;
            }

            // Hacer ceros en la columna del pivote
            for (int i = 0; i < filas; i++) {
                if (i != pivote) {
                    double factor = matriz[i][pivote];
                    for (int j = 0; j < columnas; j++) {
                        matriz[i][j] -= factor * matriz[pivote][j];
                    }
                }
            }

            // Mostrar paso intermedio
            areaResultado.append("\nPaso " + (pivote + 1) + ":\n");
            areaResultado.append(mostrarMatrizString(matriz));
        }
    }

    private boolean intercambiarFila(int filaPivote) {
        for (int i = filaPivote + 1; i < matriz.length; i++) {
            if (Math.abs(matriz[i][filaPivote]) > 1e-10) {
                // Intercambiar filas
                double[] temp = matriz[filaPivote];
                matriz[filaPivote] = matriz[i];
                matriz[i] = temp;
                areaResultado.append("↔ Intercambiadas filas " + (filaPivote + 1) + " y " + (i + 1) + "\n");
                return true;
            }
        }
        return false;
    }

    private String mostrarMatrizString(double[][] matriz) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            sb.append("| ");
            for (int j = 0; j < matriz[0].length; j++) {
                if (j == matriz[0].length - 1) {
                    sb.append("| ");
                }
                sb.append(String.format("%10.4f ", matriz[i][j]));
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    private String mostrarSolucionString() {
        StringBuilder sb = new StringBuilder();
        int filas = matriz.length;
        int columnas = matriz[0].length;

        // Nombres de variables
        String[] variables = new String[columnas - 1];
        for (int i = 0; i < variables.length; i++) {
            if (i < 3) {
                variables[i] = String.valueOf((char)('x' + i));
            } else {
                variables[i] = "x" + (i + 1);
            }
        }

        for (int i = 0; i < filas && i < columnas - 1; i++) {
            // Verificar si la fila es válida
            boolean filaValida = false;
            for (int j = 0; j < columnas - 1; j++) {
                if (Math.abs(matriz[i][j]) > 1e-10) {
                    filaValida = true;
                    break;
                }
            }

            if (filaValida) {
                sb.append(String.format("%s = %.4f\n", variables[i], matriz[i][columnas - 1]));
            } else {
                if (Math.abs(matriz[i][columnas - 1]) > 1e-10) {
                    sb.append(String.format("Ecuación %d: Inconsistente (0 = %.4f)\n",
                            i + 1, matriz[i][columnas - 1]));
                } else {
                    sb.append(String.format("Ecuación %d: Variable libre\n", i + 1));
                }
            }
        }

        // Si hay más variables que ecuaciones
        if (columnas - 1 > filas) {
            sb.append("\nSistema subdeterminado - infinitas soluciones\n");
            for (int i = filas; i < columnas - 1; i++) {
                sb.append(String.format("%s = variable libre\n", variables[i]));
            }
        }

        return sb.toString();
    }

    private void reiniciarPrograma() {
        // Limpiar todos los campos
        txtFilas.setText("");
        txtColumnas.setText("");
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);
        areaResultado.setText("");
        matriz = null;

        JOptionPane.showMessageDialog(frame,
                "Programa reiniciado. Puede comenzar con una nueva matriz.",
                "Reiniciado", JOptionPane.INFORMATION_MESSAGE);
    }
}