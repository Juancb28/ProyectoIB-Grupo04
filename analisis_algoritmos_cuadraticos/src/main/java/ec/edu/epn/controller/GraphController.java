package ec.edu.epn.controller;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;

public class GraphController {

        private static final String PRIMARY_COLOR = "#3498db";
        private static final String SECONDARY_COLOR = "#2ecc71";
        private static final String BAR_COLOR = "#9b59b6";
        private static final String WHITE_TEXT = "#ffffff";

        @SuppressWarnings("exports")
        public void showPerformanceComparison(Stage stage, long bubbleTime, long selectionTime, long insertionTime,
                        String primaryColor, String secondaryColor, String barColor,
                        String darkBg, String lightText) {
                bubbleTime = Math.max(bubbleTime, 1);
                selectionTime = Math.max(selectionTime, 1);
                insertionTime = Math.max(insertionTime, 1);

                Stage lineStage = new Stage();
                showLineChart(lineStage, darkBg, WHITE_TEXT, bubbleTime, selectionTime, insertionTime);
        }

        @SuppressWarnings("unchecked")
        private void showLineChart(Stage stage, String darkBg, String textColor,
                        long bubbleTime, long selectionTime, long insertionTime) {
                stage.setTitle("Tendencia de Rendimiento");

                // Configurar eje X con más espacio
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
                xAxis.setTickLength(10); // Longitud de las marcas del eje

                // Configurar eje Y con más espacio
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
                yAxis.setTickLength(10); // Longitud de las marcas del eje

                // Crear el gráfico con más margen
                LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setTitle("Tendencia de Rendimiento (Teórico vs Real)");
                lineChart.setAnimated(false);
                lineChart.setLegendVisible(true);
                lineChart.setCreateSymbols(true);
                lineChart.setHorizontalGridLinesVisible(true);
                lineChart.setVerticalGridLinesVisible(true);
                lineChart.setHorizontalZeroLineVisible(false);

                // Añadir más margen alrededor del gráfico
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

                // Crear series (código existente...)
                XYChart.Series<Number, Number> bubbleTheory = createSeries("Bubble Sort (Teórico)");
                XYChart.Series<Number, Number> selectionTheory = createSeries("Selection Sort (Teórico)");
                XYChart.Series<Number, Number> insertionTheory = createSeries("Insertion Sort (Teórico)");

                for (int n = 10; n <= 100; n += 5) {
                        bubbleTheory.getData().add(new XYChart.Data<>(n, n * n * 0.02));
                        selectionTheory.getData().add(new XYChart.Data<>(n, n * n * 0.017));
                        insertionTheory.getData().add(new XYChart.Data<>(n, n * n * 0.015));
                }

                XYChart.Series<Number, Number> bubbleReal = createSeries("Bubble Sort (Real)");
                XYChart.Series<Number, Number> selectionReal = createSeries("Selection Sort (Real)");
                XYChart.Series<Number, Number> insertionReal = createSeries("Insertion Sort (Real)");

                bubbleTime = Math.max(bubbleTime, 1);
                selectionTime = Math.max(selectionTime, 1);
                insertionTime = Math.max(insertionTime, 1);

                bubbleReal.getData().add(new XYChart.Data<>(50, bubbleTime));
                selectionReal.getData().add(new XYChart.Data<>(50, selectionTime));
                insertionReal.getData().add(new XYChart.Data<>(50, insertionTime));

                lineChart.getData().addAll(
                                bubbleTheory, selectionTheory, insertionTheory,
                                bubbleReal, selectionReal, insertionReal);

                // Aumentar el tamaño de los símbolos y separación
                Platform.runLater(() -> {
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

                        // Estilo para los puntos reales (más grandes)
                        setNodeStyle(bubbleReal, PRIMARY_COLOR);
                        setNodeStyle(selectionReal, SECONDARY_COLOR);
                        setNodeStyle(insertionReal, BAR_COLOR);

                        // Aplicar estilos a los ejes y título
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
                                // Separar items de la leyenda
                                legend.setStyle(legend.getStyle() + "-fx-hgap: 20px; -fx-vgap: 10px;");
                        }
                });

                // Configurar layout con más espacio
                BorderPane root = new BorderPane(lineChart);
                root.setStyle("-fx-background-color: " + darkBg + "; -fx-padding: 20;");
                Scene scene = new Scene(root, 800, 600);
                stage.setScene(scene);
                stage.show();
        }

        // Modificar el estilo de los nodos para hacerlos más grandes
        private void setNodeStyle(XYChart.Series<Number, Number> series, String color) {
                for (XYChart.Data<Number, Number> data : series.getData()) {
                        if (data.getNode() != null) {
                                data.getNode().setStyle(
                                                "-fx-background-color: " + color + ", white; " +
                                                                "-fx-background-insets: 0, 2; " +
                                                                "-fx-background-radius: 5px; " + // Hacer los puntos más
                                                                                                 // grandes
                                                                "-fx-padding: 5px; " + // Añadir más espacio alrededor
                                                                "-fx-shape: \"M0,4 L4,0 L8,4 L4,8 Z\";");
                        }
                }
        }

        private XYChart.Series<Number, Number> createSeries(String name) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(name);
                return series;
        }
}