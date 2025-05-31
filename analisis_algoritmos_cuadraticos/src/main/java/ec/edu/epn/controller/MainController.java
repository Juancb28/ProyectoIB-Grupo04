package ec.edu.epn.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainController extends Application {

    private static final int NUM_BARS = 50;
    private int[] data;
    private RadioButton bubbleSortRadio;
    private RadioButton selectionSortRadio;
    private RadioButton insertionSortRadio;
    private ChoiceBox<String> listTypeChoiceBox;
    private AnchorPane visualizationPane;
    private Slider speedSlider;
    private List<Rectangle> bars = new ArrayList<>();
    private List<Text> valueLabels = new ArrayList<>();
    private boolean running = false;

    // Nueva paleta de colores vibrantes
    private final String PRIMARY_COLOR = "#3498db";  // Azul brillante
    private final String SECONDARY_COLOR = "#2ecc71"; // Verde esmeralda
    private final String ACCENT_COLOR = "#e74c3c";   // Rojo coral
    private final String HIGHLIGHT_COLOR = "#f1c40f"; // Amarillo vibrante
    private final String DARK_BG = "#2c3e50";       // Azul oscuro
    private final String LIGHT_TEXT = "#ecf0f1";     // Blanco suave
    private final String BAR_COLOR = "#9b59b6";      // Morado vibrante

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        String modernStyle =
                "-fx-base: " + DARK_BG + ";\n" +
                "-fx-background: " + DARK_BG + ";\n" +
                "-fx-control-inner-background: #34495e;\n" +
                "-fx-text-fill: " + LIGHT_TEXT + ";\n" +
                "-fx-accent: " + PRIMARY_COLOR + ";\n" +
                "-fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;";

        VBox leftPane = createControlPanel();
        leftPane.setStyle(modernStyle + "-fx-padding: 15; -fx-background-color: " + DARK_BG + ";");

        visualizationPane = new AnchorPane();
        visualizationPane.setStyle("-fx-background-color: #34495e;");
        visualizationPane.setPrefSize(900, 550);

        HBox bottomPane = createBottomPanel();
        bottomPane.setStyle("-fx-background-color: " + DARK_BG + ";");

        BorderPane root = new BorderPane();
        root.setLeft(leftPane);
        root.setCenter(visualizationPane);
        root.setBottom(bottomPane);
        root.setStyle("-fx-background-color: " + DARK_BG + ";");

        Scene scene = new Scene(root, 1200, 750);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Algoritmos de Ordenamiento - Animación Visual");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();

        generateList();
    }

    private VBox createControlPanel() {
        ToggleGroup algorithmToggleGroup = new ToggleGroup();

        bubbleSortRadio = createStyledRadioButton("Bubble Sort", algorithmToggleGroup, true);
        selectionSortRadio = createStyledRadioButton("Selection Sort", algorithmToggleGroup, false);
        insertionSortRadio = createStyledRadioButton("Insertion Sort", algorithmToggleGroup, false);

        listTypeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(
                "Ordenada", "Inversamente ordenada", "Aleatoria", "Casi ordenada", "Con duplicados"));
        listTypeChoiceBox.getSelectionModel().selectFirst();
        listTypeChoiceBox.setStyle("-fx-font-size: 14; -fx-text-fill: " + LIGHT_TEXT + ";");

        speedSlider = new Slider(1, 200, 50);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(50);
        speedSlider.setMinorTickCount(5);
        speedSlider.setStyle("-fx-control-inner-background: " + PRIMARY_COLOR + ";");

        Label speedLabel = new Label("Velocidad de animación:");
        speedLabel.setStyle("-fx-text-fill: " + LIGHT_TEXT + "; -fx-font-size: 14;");

        VBox leftPane = new VBox(15);
        leftPane.setPadding(new Insets(15));

        Label algLabel = new Label("ALGORITMOS DE ORDENAMIENTO");
        algLabel.setStyle("-fx-text-fill: " + HIGHLIGHT_COLOR + "; -fx-font-weight: bold; -fx-font-size: 20;");

        Label listTypeLabel = new Label("Tipo de lista:");
        listTypeLabel.setStyle("-fx-text-fill: " + LIGHT_TEXT + "; -fx-font-size: 14;");

        leftPane.getChildren().addAll(
                algLabel,
                new Separator(),
                bubbleSortRadio,
                selectionSortRadio,
                insertionSortRadio,
                new Separator(),
                listTypeLabel,
                listTypeChoiceBox,
                new Separator(),
                speedLabel,
                speedSlider);

        return leftPane;
    }

    private RadioButton createStyledRadioButton(String text, ToggleGroup group, boolean selected) {
        RadioButton radio = new RadioButton(text);
        radio.setToggleGroup(group);
        radio.setSelected(selected);
        radio.setStyle("-fx-text-fill: " + LIGHT_TEXT + "; -fx-font-size: 14;");
        return radio;
    }

    private HBox createBottomPanel() {
        Button startSortBtn = createStyledButton("Iniciar Ordenamiento", SECONDARY_COLOR);
        startSortBtn.setOnAction(e -> {
            if (!running) {
                running = true;
                startSorting();
            }
        });

        Button stopSortBtn = createStyledButton("Detener", ACCENT_COLOR);
        stopSortBtn.setOnAction(e -> running = false);

        Button resetBtn = createStyledButton("Reiniciar", PRIMARY_COLOR);
        resetBtn.setOnAction(e -> {
            running = false;
            generateList();
        });

        Button compareBtn = createStyledButton("Comparar Tiempos", BAR_COLOR);
        compareBtn.setOnAction(e -> showComparisonChart());

        HBox bottomPane = new HBox(15);
        bottomPane.setPadding(new Insets(15));
        bottomPane.getChildren().addAll(startSortBtn, stopSortBtn, resetBtn, compareBtn);

        return bottomPane;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14;" +
                "-fx-padding: 10 20;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + color + ", 20%);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14;" +
                "-fx-padding: 10 20;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14;" +
                "-fx-padding: 10 20;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        ));

        return button;
    }

    private void generateList() {
        data = new int[NUM_BARS];
        Random rnd = new Random();

        String selectedType = listTypeChoiceBox.getSelectionModel().getSelectedItem();
        switch (selectedType) {
            case "Ordenada":
                for (int i = 0; i < NUM_BARS; i++) data[i] = i + 1;
                break;
            case "Inversamente ordenada":
                for (int i = 0; i < NUM_BARS; i++) data[i] = NUM_BARS - i;
                break;
            case "Aleatoria":
                for (int i = 0; i < NUM_BARS; i++) data[i] = rnd.nextInt(NUM_BARS) + 1;
                break;
            case "Casi ordenada":
                for (int i = 0; i < NUM_BARS; i++) data[i] = i + 1;
                for (int i = 0; i < NUM_BARS / 10; i++) {
                    int idx1 = rnd.nextInt(NUM_BARS);
                    int idx2 = rnd.nextInt(NUM_BARS);
                    int temp = data[idx1];
                    data[idx1] = data[idx2];
                    data[idx2] = temp;
                }
                break;
            case "Con duplicados":
                for (int i = 0; i < NUM_BARS; i++) data[i] = rnd.nextInt(NUM_BARS / 5) + 1;
                break;
        }

        Platform.runLater(() -> {
            visualizationPane.getChildren().clear();
            bars.clear();
            valueLabels.clear();

            double width = visualizationPane.getWidth();
            double height = visualizationPane.getHeight();
            double marginLeft = 30;
            double marginRight = 30;
            double usableWidth = width - marginLeft - marginRight;
            double barWidth = usableWidth / data.length;

            int maxVal = Arrays.stream(data).max().orElse(1);

            for (int i = 0; i < data.length; i++) {
                double barHeight = (data[i] * (height - 50)) / maxVal;
                double x = marginLeft + i * barWidth;
                double y = height - barHeight - 20;

                Rectangle rect = new Rectangle(barWidth - 3, barHeight);
                rect.setX(x);
                rect.setY(y);
                rect.setFill(Color.web(BAR_COLOR));
                rect.setArcWidth(5);
                rect.setArcHeight(5);

                Text valueLabel = new Text(String.valueOf(data[i]));
                valueLabel.setFont(Font.font(10));
                valueLabel.setFill(Color.web(LIGHT_TEXT));
                valueLabel.setX(x + (barWidth / 2) - 6);
                valueLabel.setY(y - 5);

                bars.add(rect);
                valueLabels.add(valueLabel);

                visualizationPane.getChildren().addAll(rect, valueLabel);
            }
        });
    }

    private void startSorting() {
        new Thread(() -> {
            String algorithm = getSelectedAlgorithm();
            switch (algorithm) {
                case "Bubble":
                    bubbleSortWithAnimation();
                    break;
                case "Selection":
                    selectionSortWithAnimation();
                    break;
                case "Insertion":
                    insertionSortWithAnimation();
                    break;
            }
            running = false;
        }).start();
    }

    private String getSelectedAlgorithm() {
        if (bubbleSortRadio.isSelected()) return "Bubble";
        else if (selectionSortRadio.isSelected()) return "Selection";
        else return "Insertion";
    }

    private void bubbleSortWithAnimation() {
        int n = data.length;
        boolean swapped;

        try {
            for (int i = 0; i < n - 1 && running; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1 && running; j++) {
                    highlightBars(j, j + 1, HIGHLIGHT_COLOR);
                    Thread.sleep((long) (201 - speedSlider.getValue()));

                    if (data[j] > data[j + 1]) {
                        swapData(j, j + 1);
                        swapped = true;
                        Thread.sleep((long) (201 - speedSlider.getValue()));
                    }
                    unhighlightBars(j, j + 1);
                }
                if (!swapped) break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void selectionSortWithAnimation() {
        int n = data.length;

        try {
            for (int i = 0; i < n - 1 && running; i++) {
                int minIdx = i;
                highlightBar(minIdx, HIGHLIGHT_COLOR);

                for (int j = i + 1; j < n && running; j++) {
                    highlightBar(j, ACCENT_COLOR);
                    Thread.sleep((long) (201 - speedSlider.getValue()));

                    if (data[j] < data[minIdx]) {
                        unhighlightBar(minIdx);
                        minIdx = j;
                        highlightBar(minIdx, HIGHLIGHT_COLOR);
                    } else {
                        unhighlightBar(j);
                    }
                }

                if (minIdx != i) {
                    swapData(i, minIdx);
                    Thread.sleep((long) (201 - speedSlider.getValue()));
                }
                unhighlightBar(minIdx);
                unhighlightBar(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void insertionSortWithAnimation() {
        int n = data.length;

        try {
            for (int i = 1; i < n && running; i++) {
                int key = data[i];
                int j = i - 1;
                highlightBar(i, HIGHLIGHT_COLOR);

                while (j >= 0 && data[j] > key && running) {
                    highlightBar(j, ACCENT_COLOR);
                    Thread.sleep((long) (201 - speedSlider.getValue()));

                    data[j + 1] = data[j];
                    updateBarHeight(j + 1);
                    unhighlightBar(j + 1);
                    j--;
                }
                data[j + 1] = key;
                updateBarHeight(j + 1);
                unhighlightBar(i);
                if (j + 1 >= 0) unhighlightBar(j + 1);
                Thread.sleep((long) (201 - speedSlider.getValue()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void highlightBars(int idx1, int idx2, String color) {
        Platform.runLater(() -> {
            bars.get(idx1).setFill(Color.web(color));
            bars.get(idx2).setFill(Color.web(color));
        });
    }

    private void unhighlightBars(int idx1, int idx2) {
        Platform.runLater(() -> {
            bars.get(idx1).setFill(Color.web(BAR_COLOR));
            bars.get(idx2).setFill(Color.web(BAR_COLOR));
        });
    }

    private void highlightBar(int idx, String color) {
        Platform.runLater(() -> bars.get(idx).setFill(Color.web(color)));
    }

    private void unhighlightBar(int idx) {
        Platform.runLater(() -> bars.get(idx).setFill(Color.web(BAR_COLOR)));
    }

    private void swapData(int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;

        Platform.runLater(() -> {
            double height1 = bars.get(i).getHeight();
            double height2 = bars.get(j).getHeight();
            double y1 = bars.get(i).getY();
            double y2 = bars.get(j).getY();

            bars.get(i).setHeight(height2);
            bars.get(j).setHeight(height1);
            bars.get(i).setY(y2);
            bars.get(j).setY(y1);

            valueLabels.get(i).setText(String.valueOf(data[i]));
            valueLabels.get(j).setText(String.valueOf(data[j]));
            valueLabels.get(i).setY(y2 - 5);
            valueLabels.get(j).setY(y1 - 5);
        });
    }

    private void updateBarHeight(int idx) {
        Platform.runLater(() -> {
            int maxVal = Arrays.stream(data).max().orElse(1);
            double height = visualizationPane.getHeight();
            double barHeight = (data[idx] * (height - 50)) / maxVal;
            double y = height - barHeight - 20;

            bars.get(idx).setHeight(barHeight);
            bars.get(idx).setY(y);
            valueLabels.get(idx).setText(String.valueOf(data[idx]));
            valueLabels.get(idx).setY(y - 5);
        });
    }

    @SuppressWarnings("unchecked")
    private void showComparisonChart() {
        Stage stage = new Stage();
        stage.setTitle("Comparación de Tiempos - Arreglo de 50 Elementos");

        int[] testData = new int[50];
        Random rnd = new Random();
        for (int i = 0; i < 50; i++) testData[i] = rnd.nextInt(50) + 1;

        long bubbleTime = measureSortTime(Arrays.copyOf(testData, testData.length), "Bubble");
        long selectionTime = measureSortTime(Arrays.copyOf(testData, testData.length), "Selection");
        long insertionTime = measureSortTime(Arrays.copyOf(testData, testData.length), "Insertion");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Algoritmo");
        xAxis.setStyle("-fx-tick-label-fill: " + LIGHT_TEXT + ";");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tiempo (milisegundos)");
        yAxis.setStyle("-fx-tick-label-fill: " + LIGHT_TEXT + ";");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Comparación de Tiempos de Ejecución");
        barChart.setStyle("-fx-background-color: " + DARK_BG + "; -fx-text-fill: " + LIGHT_TEXT + ";");
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Data<String, Number> bubbleData = new XYChart.Data<>("Bubble Sort", bubbleTime);
        XYChart.Data<String, Number> selectionData = new XYChart.Data<>("Selection Sort", selectionTime);
        XYChart.Data<String, Number> insertionData = new XYChart.Data<>("Insertion Sort", insertionTime);

        series.getData().addAll(bubbleData, selectionData, insertionData);
        barChart.getData().add(series);

        // Personalizar colores de las barras
        bubbleData.getNode().setStyle("-fx-bar-fill: " + PRIMARY_COLOR + ";");
        selectionData.getNode().setStyle("-fx-bar-fill: " + SECONDARY_COLOR + ";");
        insertionData.getNode().setStyle("-fx-bar-fill: " + BAR_COLOR + ";");

        VBox layout = new VBox(barChart);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: " + DARK_BG + ";");

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private long measureSortTime(int[] array, String algorithm) {
        long start = System.nanoTime();

        switch (algorithm) {
            case "Bubble":
                bubbleSortNoAnimation(array);
                break;
            case "Selection":
                selectionSortNoAnimation(array);
                break;
            case "Insertion":
                insertionSortNoAnimation(array);
                break;
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    private void bubbleSortNoAnimation(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    private void selectionSortNoAnimation(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = arr[minIdx];
                arr[minIdx] = arr[i];
                arr[i] = temp;
            }
        }
    }

    private void insertionSortNoAnimation(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}