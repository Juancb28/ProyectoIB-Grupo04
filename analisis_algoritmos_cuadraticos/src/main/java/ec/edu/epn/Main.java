package ec.edu.epn;

import javafx.application.Application;
import javafx.stage.Stage;
import ec.edu.epn.controller.MainController;

public class Main extends Application {
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        MainController controller = new MainController();
        controller.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}