package ec.edu.epn.controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Controlador para la visualización de gráficos comparativos de rendimiento.
 * Muestra comparaciones entre los tiempos de ejecución teóricos y reales
 * de diferentes algoritmos de ordenamiento.
 */
public class GraphController {

        // CONSTANTES DE COLORES
        private static final String PRIMARY_COLOR = "#3498db";
        private static final String SECONDARY_COLOR = "#2ecc71";
        private static final String BAR_COLOR = "#9b59b6";
        private static final String WHITE_TEXT = "#ffffff";

        /**
         * Muestra la comparación de rendimiento entre algoritmos de ordenamiento.
         * 
         * @param stage          Escenario donde se mostrará el gráfico
         * @param bubbleTime     Tiempo de ejecución de Bubble Sort (ms)
         * @param selectionTime  Tiempo de ejecución de Selection Sort (ms)
         * @param insertionTime  Tiempo de ejecución de Insertion Sort (ms)
         * @param primaryColor   Color primario para el gráfico
         * @param secondaryColor Color secundario para el gráfico
         * @param barColor       Color para las barras del gráfico
         * @param darkBg         Color de fondo oscuro
         * @param lightText      Color de texto claro
         */
        @SuppressWarnings("exports")
        public void showPerformanceComparison(Stage stage, long bubbleTime, long selectionTime, long insertionTime,
                        String primaryColor, String secondaryColor, String barColor,
                        String darkBg, String lightText) {
                // Asegurar tiempos mínimos de 1ms para evitar divisiones por cero
                bubbleTime = Math.max(bubbleTime, 1);
                selectionTime = Math.max(selectionTime, 1);
                insertionTime = Math.max(insertionTime, 1);

                Stage lineStage = new Stage();
                showLineChart(lineStage, darkBg, WHITE_TEXT, bubbleTime, selectionTime, insertionTime);
        }

        /**
         * Crea y muestra un gráfico de líneas comparando tiempos teóricos y reales.
         * 
         * @param stage         Escenario para el gráfico
         * @param darkBg        Color de fondo oscuro
         * @param textColor     Color del texto
         * @param bubbleTime    Tiempo real de Bubble Sort
         * @param selectionTime Tiempo real de Selection Sort
         * @param insertionTime Tiempo real de Insertion Sort
         */
        @SuppressWarnings("unchecked")
        private void showLineChart(Stage stage, String darkBg, String textColor,
                        long bubbleTime, long selectionTime, long insertionTime) {
                stage.setTitle("Tendencia de Rendimiento");

                // Configuración del eje X
                NumberAxis xAxis = createXAxis();

                // Configuración del eje Y
                NumberAxis yAxis = createYAxis(bubbleTime, selectionTime, insertionTime);

                // Crear el gráfico de líneas
                LineChart<Number, Number> lineChart = configureLineChart(xAxis, yAxis, darkBg);

                // Crear series de datos teóricas y reales
                XYChart.Series<Number, Number> bubbleTheory = createSeries("Bubble Sort (Teórico)");
                XYChart.Series<Number, Number> selectionTheory = createSeries("Selection Sort (Teórico)");
                XYChart.Series<Number, Number> insertionTheory = createSeries("Insertion Sort (Teórico)");

                // Generar datos teóricos (complejidad O(n^2))
                generateTheoreticalData(bubbleTheory, selectionTheory, insertionTheory);

                // Crear series de datos reales
                XYChart.Series<Number, Number> bubbleReal = createSeries("Bubble Sort (Real)");
                XYChart.Series<Number, Number> selectionReal = createSeries("Selection Sort (Real)");
                XYChart.Series<Number, Number> insertionReal = createSeries("Insertion Sort (Real)");

                // Añadir datos reales (para tamaño de datos 50)
                addRealData(bubbleTime, selectionTime, insertionTime, bubbleReal, selectionReal, insertionReal);

                // Añadir todas las series al gráfico
                lineChart.getData().addAll(
                                bubbleTheory, selectionTheory, insertionTheory,
                                bubbleReal, selectionReal, insertionReal);

                // Aplicar estilos a los elementos del gráfico
                Platform.runLater(() -> {
                        applyChartStyles(lineChart, xAxis, yAxis, bubbleTheory, selectionTheory, insertionTheory,
                                        bubbleReal, selectionReal, insertionReal);
                });

                // Configurar y mostrar la escena
                BorderPane root = new BorderPane(lineChart);
                root.setStyle("-fx-background-color: " + darkBg + "; -fx-padding: 20;");
                Scene scene = new Scene(root, 800, 600);
                stage.setScene(scene);
                stage.show();
        }

        /**
         * Crea y configura el eje X del gráfico.
         * 
         * @return NumberAxis configurado
         */
        private NumberAxis createXAxis() {
                NumberAxis xAxis = new NumberAxis();
                xAxis.setLabel("Tamaño de datos");
                xAxis.setStyle(
                                "-fx-tick-label-fill: " + WHITE_TEXT + "; " +
                                                "-fx-axis-label-fill: " + WHITE_TEXT + ";");
                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(0);
                xAxis.setUpperBound(100);
                xAxis.setTickUnit(10);
                xAxis.setTickMarkVisible(true);
                xAxis.setMinorTickVisible(false);
                xAxis.setTickLength(10);
                return xAxis;
        }

        /**
         * Crea y configura el eje Y del gráfico.
         * 
         * @param bubbleTime    Tiempo de Bubble Sort
         * @param selectionTime Tiempo de Selection Sort
         * @param insertionTime Tiempo de Insertion Sort
         * @return NumberAxis configurado
         */
        private NumberAxis createYAxis(long bubbleTime, long selectionTime, long insertionTime) {
                NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("Tiempo (ms)");
                yAxis.setStyle(
                                "-fx-tick-label-fill: " + WHITE_TEXT + "; " +
                                                "-fx-axis-label-fill: " + WHITE_TEXT + ";");
                yAxis.setAutoRanging(false);
                yAxis.setLowerBound(0);

                long maxTime = Math.max(Math.max(bubbleTime, selectionTime), insertionTime);
                double theoreticalMax = 100 * 100 * 0.02;
                yAxis.setUpperBound(Math.max(maxTime, theoreticalMax) * 1.2);
                yAxis.setTickUnit(yAxis.getUpperBound() / 10);
                yAxis.setTickMarkVisible(true);
                yAxis.setMinorTickVisible(false);
                yAxis.setTickLength(10);
                return yAxis;
        }

        /**
         * Configura el gráfico de líneas principal.
         * 
         * @param xAxis  Eje X
         * @param yAxis  Eje Y
         * @param darkBg Color de fondo
         * @return LineChart configurado
         */
        private LineChart<Number, Number> configureLineChart(NumberAxis xAxis, NumberAxis yAxis, String darkBg) {
                LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setTitle("Tendencia de Rendimiento (Teórico vs Real)");
                lineChart.setAnimated(false);
                lineChart.setLegendVisible(true);
                lineChart.setCreateSymbols(true);
                lineChart.setHorizontalGridLinesVisible(true);
                lineChart.setVerticalGridLinesVisible(true);
                lineChart.setHorizontalZeroLineVisible(false);
                lineChart.setPadding(new Insets(20, 30, 30, 30));

                String lineChartStyle = String.format(
                                "-fx-background-color: %s; " +
                                                "-fx-text-fill: %s; " +
                                                "-fx-title-fill: %s; " +
                                                "-fx-legend-text-fill: %s; " +
                                                "-fx-tick-label-fill: %s; " +
                                                "-fx-axis-label-fill: %s; " +
                                                "-fx-chart-vertical-grid-lines-visible: true; " +
                                                "-fx-chart-horizontal-grid-lines-visible: true;",
                                darkBg, WHITE_TEXT, WHITE_TEXT, WHITE_TEXT, WHITE_TEXT, WHITE_TEXT);

                lineChart.setStyle(lineChartStyle);
                return lineChart;
        }

        /**
         * Genera datos teóricos para las series.
         * 
         * @param bubbleTheory    Serie para Bubble Sort teórico
         * @param selectionTheory Serie para Selection Sort teórico
         * @param insertionTheory Serie para Insertion Sort teórico
         */
        private void generateTheoreticalData(XYChart.Series<Number, Number> bubbleTheory,
                        XYChart.Series<Number, Number> selectionTheory,
                        XYChart.Series<Number, Number> insertionTheory) {
                for (int n = 10; n <= 100; n += 5) {
                        bubbleTheory.getData().add(new XYChart.Data<>(n, n * n * 0.02));
                        selectionTheory.getData().add(new XYChart.Data<>(n, n * n * 0.017));
                        insertionTheory.getData().add(new XYChart.Data<>(n, n * n * 0.015));
                }
        }

        /**
         * Añade datos reales a las series.
         * 
         * @param bubbleTime    Tiempo real de Bubble Sort
         * @param selectionTime Tiempo real de Selection Sort
         * @param insertionTime Tiempo real de Insertion Sort
         * @param bubbleReal    Serie para Bubble Sort real
         * @param selectionReal Serie para Selection Sort real
         * @param insertionReal Serie para Insertion Sort real
         */
        private void addRealData(long bubbleTime, long selectionTime, long insertionTime,
                        XYChart.Series<Number, Number> bubbleReal,
                        XYChart.Series<Number, Number> selectionReal,
                        XYChart.Series<Number, Number> insertionReal) {
                bubbleTime = Math.max(bubbleTime, 1);
                selectionTime = Math.max(selectionTime, 1);
                insertionTime = Math.max(insertionTime, 1);

                bubbleReal.getData().add(new XYChart.Data<>(50, bubbleTime));
                selectionReal.getData().add(new XYChart.Data<>(50, selectionTime));
                insertionReal.getData().add(new XYChart.Data<>(50, insertionTime));
        }

        /**
         * Aplica estilos a los elementos del gráfico.
         * 
         * @param lineChart       Gráfico de líneas
         * @param xAxis           Eje X
         * @param yAxis           Eje Y
         * @param bubbleTheory    Serie teórica Bubble
         * @param selectionTheory Serie teórica Selection
         * @param insertionTheory Serie teórica Insertion
         * @param bubbleReal      Serie real Bubble
         * @param selectionReal   Serie real Selection
         * @param insertionReal   Serie real Insertion
         */
        private void applyChartStyles(LineChart<Number, Number> lineChart, NumberAxis xAxis, NumberAxis yAxis,
                        XYChart.Series<Number, Number> bubbleTheory,
                        XYChart.Series<Number, Number> selectionTheory,
                        XYChart.Series<Number, Number> insertionTheory,
                        XYChart.Series<Number, Number> bubbleReal,
                        XYChart.Series<Number, Number> selectionReal,
                        XYChart.Series<Number, Number> insertionReal) {
                // Estilo para las líneas teóricas
                if (bubbleTheory.getNode() != null) {
                        bubbleTheory.getNode()
                                        .setStyle("-fx-stroke: " + PRIMARY_COLOR + "; -fx-stroke-width: 2px;");
                }
                if (selectionTheory.getNode() != null) {
                        selectionTheory.getNode().setStyle(
                                        "-fx-stroke: " + SECONDARY_COLOR + "; -fx-stroke-width: 2px;");
                }
                if (insertionTheory.getNode() != null) {
                        insertionTheory.getNode()
                                        .setStyle("-fx-stroke: " + BAR_COLOR + "; -fx-stroke-width: 2px;");
                }

                // Estilo para los puntos reales
                setNodeStyle(bubbleReal, PRIMARY_COLOR);
                setNodeStyle(selectionReal, SECONDARY_COLOR);
                setNodeStyle(insertionReal, BAR_COLOR);

                // Estilo para los ejes y título
                styleChartElements(lineChart, xAxis, yAxis);
        }

        /**
         * Estiliza los elementos del gráfico (ejes, título, leyenda).
         * 
         * @param lineChart Gráfico de líneas
         * @param xAxis     Eje X
         * @param yAxis     Eje Y
         */
        private void styleChartElements(LineChart<Number, Number> lineChart, NumberAxis xAxis, NumberAxis yAxis) {
                Node xAxisLabel = xAxis.lookup(".axis-label");
                if (xAxisLabel != null) {
                        xAxisLabel.setStyle("-fx-text-fill: " + WHITE_TEXT + ";");
                }

                Node yAxisLabel = yAxis.lookup(".axis-label");
                if (yAxisLabel != null) {
                        yAxisLabel.setStyle("-fx-text-fill: " + WHITE_TEXT + ";");
                }

                Node chartTitle = lineChart.lookup(".chart-title");
                if (chartTitle != null) {
                        chartTitle.setStyle("-fx-text-fill: " + WHITE_TEXT + "; -fx-font-size: 16px;");
                }

                Node legend = lineChart.lookup(".chart-legend");
                if (legend != null) {
                        legend.setStyle("-fx-text-fill: " + WHITE_TEXT + "; -fx-font-size: 14px;");
                        legend.setStyle(legend.getStyle() + "-fx-hgap: 20px; -fx-vgap: 10px;");
                }
        }

        /**
         * Establece el estilo para los nodos de datos en el gráfico.
         * 
         * @param series Serie de datos
         * @param color  Color para los nodos
         */
        private void setNodeStyle(XYChart.Series<Number, Number> series, String color) {
                for (XYChart.Data<Number, Number> data : series.getData()) {
                        if (data.getNode() != null) {
                                data.getNode().setStyle(
                                                "-fx-background-color: " + color + ", white; " +
                                                                "-fx-background-insets: 0, 2; " +
                                                                "-fx-background-radius: 5px; " +
                                                                "-fx-padding: 5px; " +
                                                                "-fx-shape: \"M0,4 L4,0 L8,4 L4,8 Z\";");
                        }
                }
        }

        /**
         * Crea una nueva serie de datos para el gráfico.
         * 
         * @param name Nombre de la serie
         * @return Nueva serie configurada
         */
        private XYChart.Series<Number, Number> createSeries(String name) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(name);
                return series;
        }
}