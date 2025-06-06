package ec.edu.epn;

import javafx.application.Application;
import javafx.stage.Stage;
import ec.edu.epn.controller.MainController;

/**
 * Clase principal del proyecto JavaFX.
 * 
 * <p>
 * Esta clase extiende {@link javafx.application.Application} e inicia
 * la aplicación llamando al controlador principal {@link MainController}.
 * </p>
 * 
 * <p>
 * Forma parte de la arquitectura MVC, actuando como punto de entrada.
 * </p>
 * 
 * @author [Tu Nombre]
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Método de inicio de la aplicación JavaFX.
     * 
     * <p>
     * Se crea una instancia del controlador principal y se le pasa
     * el escenario primario (Stage) para iniciar la interfaz.
     * </p>
     *
     * @param primaryStage el escenario principal de JavaFX
     */
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        MainController controller = new MainController();
        controller.start(primaryStage);
    }

    /**
     * Método principal que lanza la aplicación JavaFX.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
