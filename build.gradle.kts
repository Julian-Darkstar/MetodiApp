plugins {
    java
    application
    kotlin("jvm") version "1.9.22"
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.1.4-rc"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.example.metodiapp")
    mainClass.set("org.example.metodiapp.HelloApplication")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing", "javafx.media")
}

dependencies {
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") { exclude(group = "org.openjfx") }
    implementation("net.synedra:validatorfx:0.6.1") { exclude(group = "org.openjfx") }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("eu.hansolo:tilesfx:21.0.9") { exclude(group = "org.openjfx") }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.22")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    jpackage {
        installerName = "MetodiApp"
        installerType = "msi"
        appVersion = "1.0.0"
        vendor = "MetodiApp Team"
        icon = project.file("src/main/resources/org/example/metodiapp/images/logoMetodiApp.ico").absolutePath

        // Opciones específicas de Windows
    }
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "zip-9", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "MetodiApp"
        jvmArgs = listOf("-Dlog4j.configurationFile=./log4j2.xml")
    }
}
