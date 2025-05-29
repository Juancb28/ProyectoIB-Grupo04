package ec.edu.epn.view;


import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GraphView {

    private LineChart<Number, Number> lineChart;

    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // Label en la parte superior
        Label titleLabel = new Label("Gráfica: Tiempo de ejecución vs Tamaño de datos");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10;");
        root.setTop(titleLabel);

        // Ejes para el gráfico
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Tamaño de datos");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tiempo (ms)");

        // LineChart
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Rendimiento de Algoritmos");
        lineChart.setAnimated(true);
        lineChart.setLegendVisible(true);
        lineChart.setCreateSymbols(true);
        lineChart.setVerticalGridLinesVisible(true);
        lineChart.setHorizontalGridLinesVisible(true);

        root.setCenter(lineChart);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Gráficas de rendimiento");
        stage.show();
    }

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }
}
