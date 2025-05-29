package ec.edu.epn.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphController extends Application {

    private LineChart<Number, Number> lineChart;

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) {
        stage.setTitle("Gráficas de rendimiento");

        // Ejes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Tamaño de datos");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tiempo (ms)");

        // Crear LineChart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Rendimiento de Algoritmos");
        lineChart.setAnimated(true);
        lineChart.setLegendVisible(true);
        lineChart.setCreateSymbols(true);
        lineChart.setVerticalGridLinesVisible(true);
        lineChart.setHorizontalGridLinesVisible(true);

        plotMockData();

        BorderPane root = new BorderPane(lineChart);
        Scene scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    @SuppressWarnings("unchecked")
    private void plotMockData() {
        XYChart.Series<Number, Number> bubble = new XYChart.Series<>();
        bubble.setName("Bubble Sort");

        XYChart.Series<Number, Number> selection = new XYChart.Series<>();
        selection.setName("Selection Sort");

        XYChart.Series<Number, Number> insertion = new XYChart.Series<>();
        insertion.setName("Insertion Sort");

        for (int n = 10; n <= 100; n += 10) {
            bubble.getData().add(new XYChart.Data<>(n, n * n * 0.02));
            selection.getData().add(new XYChart.Data<>(n, n * n * 0.017));
            insertion.getData().add(new XYChart.Data<>(n, n * n * 0.015));
        }

        lineChart.getData().addAll(bubble, selection, insertion);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
