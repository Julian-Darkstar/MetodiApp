module org.example.metodiapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires java.scripting;
    requires exp4j;
    requires MathParser.org.mXparser; // Permiso para la nueva librería
    requires commons.math3;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires kotlin.stdlib;



    // Abre los paquetes principales para permitir el acceso por reflexión
    opens org.example.metodiapp;
    opens org.example.metodiapp.controllers;
    opens org.example.metodiapp.models;

    // Abre el paquete de imágenes para los módulos que lo necesitan
    opens org.example.metodiapp.images to javafx.graphics, javafx.fxml;

    exports org.example.metodiapp;
    exports org.example.metodiapp.controllers;
}