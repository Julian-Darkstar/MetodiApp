package org.example.metodiapp.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

/**
 * Clase de utilidad para manejar la navegación entre vistas (archivos FXML).
 * Se asegura de que la escena principal nunca sea reemplazada, conservando así los estilos.
 */
public class Navigation {

    /**
     * Cambia el contenido de la escena actual para mostrar una nueva vista.
     *
     * @param fxmlFile El nombre del archivo FXML a cargar (ej: "regla-falsa-view.fxml").
     * @param contextNode Un nodo cualquiera de la escena actual (ej: un botón) para poder obtener la escena.
     */
    public static void navigateTo(String fxmlFile, Node contextNode) {
        if (contextNode == null || contextNode.getScene() == null) {
            System.err.println("Error de navegación: El nodo de contexto o su escena son nulos.");
            return;
        }

        try {
            // Construye la ruta completa al recurso FXML
            String fxmlPath = "/org/example/metodiapp/" + fxmlFile;
            Parent newRoot = FXMLLoader.load(Objects.requireNonNull(Navigation.class.getResource(fxmlPath)));

            // La clave: Reemplaza el contenido (root) de la escena actual en lugar de crear una nueva.
            contextNode.getScene().setRoot(newRoot);

        } catch (IOException | NullPointerException e) {
            System.err.println("Error al cargar la vista: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
