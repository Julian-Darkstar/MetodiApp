module org.example.metodiapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires java.scripting;
    requires exp4j;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires kotlin.stdlib;

    // Abrimos el paquete a todos para facilitar el desarrollo con SceneBuilder
    opens org.example.metodiapp;
    exports org.example.metodiapp;
}