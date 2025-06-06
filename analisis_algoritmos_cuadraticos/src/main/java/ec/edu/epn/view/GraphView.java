package ec.edu.epn.view;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Clase encargada de mostrar una ventana con una gráfica de líneas
 * que representa el rendimiento de los algoritmos de ordenamiento.
 * <p>
 * La gráfica muestra el tiempo de ejecución (en milisegundos) frente al tamaño
 * de los datos.
 * Esta vista puede ser reutilizada para representar múltiples series de datos
 * de rendimiento.
 * </p>
 * 
 * @author
 * @version 1.0
 */
public class GraphView {

    /** Gráfico de líneas que se mostrará en la interfaz */
    private LineChart<Number, Number> lineChart;

    /**
     * Muestra la ventana con la gráfica de rendimiento.
     *
     * @param stage el escenario donde se cargará la vista de la gráfica.
     */
    public void show(Stage stage) {
        BorderPane root = new BorderPane();

        // Etiqueta de título en la parte superior
        Label titleLabel = new Label("Gráfica: Tiempo de ejecución vs Tamaño de datos");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10;");
        root.setTop(titleLabel);

        // Configuración de ejes
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Tamaño de datos");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tiempo (ms)");

        // Configuración del gráfico de líneas
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

    /**
     * Devuelve el objeto LineChart para que pueda ser manipulado desde otras
     * clases,
     * por ejemplo, para agregar series de datos.
     *
     * @return el gráfico de líneas.
     */
    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }
}
