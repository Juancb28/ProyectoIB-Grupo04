package ec.edu.epn;

import javafx.application.Application;
import javafx.stage.Stage;
import ec.edu.epn.view.MainView;

public class Main extends Application {
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        new MainView().show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}