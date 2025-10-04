package org.example.metodiapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/metodiapp/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        // Carga la hoja de estilos local y segura
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/metodiapp/styles.css")).toExternalForm());
        stage.setTitle("MetodiApp");
        stage.setScene(scene);
        stage.show();
    }
}
