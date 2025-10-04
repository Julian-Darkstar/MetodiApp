# MetodiApp: Aplicación de Métodos Numéricos

## Descripción General

MetodiApp es una aplicación de escritorio desarrollada en JavaFX diseñada para resolver problemas matemáticos a través de diversos métodos numéricos. El objetivo principal del proyecto es proporcionar una herramienta educativa y práctica que sea a la vez potente, fácil de usar y altamente escalable.

## ¿Cómo Funciona?

La aplicación presenta una interfaz de usuario intuitiva donde el usuario puede seleccionar un método numérico, ingresar los datos requeridos (como una función matemática, intervalos, tolerancia, etc.) y obtener una solución detallada, a menudo presentada en forma de tabla con los resultados de cada iteración.

### Características Actuales

- **Método de la Regla Falsa:** Implementación completa con visualización de resultados en tabla.
- **Interfaz Gráfica Moderna:** Tema oscuro personalizado para una experiencia de usuario agradable.
- **Evaluador de Funciones:** Utiliza la librería `exp4j` para analizar y evaluar de forma segura las funciones matemáticas ingresadas por el usuario.

## Arquitectura Escalable

MetodiApp fue creado desde el principio con la escalabilidad en mente. La estructura del proyecto está organizada para facilitar la adición de nuevos métodos numéricos y funcionalidades en el futuro. La arquitectura se basa en los siguientes principios:

1.  **Separación de Responsabilidades:** El código está organizado en paquetes claros:
    *   `controllers`: Contienen la lógica de la interfaz de usuario (qué hacer cuando se presiona un botón).
    *   `models`: Definen la estructura de los datos (por ejemplo, una fila en la tabla de resultados).
    *   `services`: Clases de utilidad que realizan tareas específicas (como la navegación entre pantallas).
    *   `resources`: Contiene todos los recursos no-Java, como las vistas (`.fxml`), hojas de estilo (`.css`) e imágenes.

2.  **Navegación Centralizada:** Una clase `Navigation` se encarga de todos los cambios de pantalla, asegurando que la hoja de estilos y la configuración de la escena se mantengan consistentes en toda la aplicación.

3.  **Vistas y Controladores Desacoplados:** Cada método numérico tiene su propia vista FXML y su propio controlador, lo que permite desarrollar y depurar métodos de forma aislada sin afectar al resto de la aplicación.

Esta organización hace que añadir un nuevo método (por ejemplo, Bisección o Newton-Raphson) sea tan simple como crear una nueva vista, un nuevo controlador y conectarlo a la pantalla principal, siguiendo el patrón ya establecido.
