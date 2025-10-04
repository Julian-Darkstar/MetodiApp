package org.example.metodiapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/metodiapp/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load()); // El tamaño se ajustará con la maximización

        // Carga la hoja de estilos local y segura
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/metodiapp/styles.css")).toExternalForm());

        // --- CÓDIGO DEL ICONO (VERSIÓN FINAL Y ROBUSTA) ---
        // Se usa getResourceAsStream, que ahora funciona gracias a la correcta configuración de module-info.
        try (InputStream iconStream = getClass().getResourceAsStream("/org/example/metodiapp/images/logoMetodiApp.png")) {
            if (iconStream != null) {
                stage.getIcons().add(new Image(iconStream));
            } else {
                System.err.println("ADVERTENCIA: No se pudo cargar el icono de la aplicación. Recurso no encontrado.");
            }
        } catch (Exception e) {
            System.err.println("ADVERTENCIA: Ocurrió un error al procesar el icono de la aplicación.");
            e.printStackTrace();
        }

        stage.setTitle("MetodiApp");
        stage.setScene(scene);

        // Maximiza la ventana al iniciar
        stage.setMaximized(true);

        stage.show();
    }
}
