package ec.edu.epn.controller;

import ec.edu.epn.model.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
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
    private Timer timer = new Timer();
    private Button startSortBtn;
    private Label timeLabel; // Nuevo label para mostrar el tiempo

    private final String PRIMARY_COLOR = "#3498db";
    private final String SECONDARY_COLOR = "#2ecc71";
    private final String ACCENT_COLOR = "#e74c3c";
    private final String HIGHLIGHT_COLOR = "#f1c40f";
    private final String DARK_BG = "#2c3e50";
    private final String LIGHT_TEXT = "#ecf0f1";
    private final String BAR_COLOR = "#9b59b6";

    private final SortAlgorithm bubbleSort = new BubbleSort();
    private final SortAlgorithm selectionSort = new SelectionSort();
    private final SortAlgorithm insertionSort = new InsertionSort();

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        String modernStyle = String.format(
                "-fx-base: %s; -fx-background: %s; -fx-control-inner-background: #34495e; " +
                        "-fx-text-fill: %s; -fx-accent: %s; -fx-font-family: 'Segoe UI', Helvetica, Arial, sans-serif;",
                DARK_BG, DARK_BG, LIGHT_TEXT, PRIMARY_COLOR);

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
        startSortBtn = createStyledButton("Iniciar Ordenamiento", SECONDARY_COLOR);
        startSortBtn.setOnAction(e -> {
            if (!running) {
                startSorting();
            }
        });

        Button stopSortBtn = createStyledButton("Detener", ACCENT_COLOR);
        stopSortBtn.setOnAction(e -> {
            running = false;
            startSortBtn.setDisable(false);
        });

        Button resetBtn = createStyledButton("Reiniciar", PRIMARY_COLOR);
        resetBtn.setOnAction(e -> {
            running = false;
            generateList();
            startSortBtn.setDisable(false);
            updateTimeDisplay(0.0); // Resetear el tiempo
        });

        Button compareBtn = createStyledButton("Comparar Tiempos", BAR_COLOR);
        compareBtn.setOnAction(e -> showComparisonChart());

        // Crear el label para mostrar el tiempo de ejecución
        timeLabel = new Label("Tiempo: 0.00 ms");
        timeLabel.setStyle(
                "-fx-text-fill: " + HIGHLIGHT_COLOR + ";" +
                        "-fx-font-size: 16;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10;" +
                        "-fx-background-color: rgba(52, 73, 94, 0.8);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: " + HIGHLIGHT_COLOR + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;");

        // Crear un contenedor para los botones (lado izquierdo)
        HBox buttonsContainer = new HBox(15);
        buttonsContainer.getChildren().addAll(startSortBtn, stopSortBtn, resetBtn, compareBtn);

        // Crear el panel inferior principal
        HBox bottomPane = new HBox();
        bottomPane.setPadding(new Insets(15));

        // Agregar los botones al lado izquierdo
        bottomPane.getChildren().add(buttonsContainer);

        // Crear un espaciador para empujar el tiempo hacia la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomPane.getChildren().add(spacer);

        // Agregar el label del tiempo al lado derecho
        bottomPane.getChildren().add(timeLabel);

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
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + color + ", 20%);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 8, 0, 0, 2);"));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);"));

        return button;
    }

    // Nuevo método para actualizar el display del tiempo
    private void updateTimeDisplay(double milliseconds) {
        Platform.runLater(() -> {
            timeLabel.setText(String.format("Tiempo: %.2f ms", milliseconds));
        });
    }

    private void generateList() {
        final int NUM_ELEMENTS = 50; // Definimos 50 elementos
        final int MAX_VALUE = 100; // Valor máximo de 250
        data = new int[NUM_ELEMENTS];
        Random rnd = new Random();

        String selectedType = listTypeChoiceBox.getSelectionModel().getSelectedItem();
        switch (selectedType) {
            case "Ordenada":
                for (int i = 0; i < NUM_ELEMENTS; i++)
                    data[i] = i + 1;
                break;
            case "Inversamente ordenada":
                for (int i = 0; i < NUM_ELEMENTS; i++)
                    data[i] = NUM_ELEMENTS - i;
                break;
            case "Aleatoria":
                for (int i = 0; i < NUM_ELEMENTS; i++)
                    data[i] = rnd.nextInt(MAX_VALUE) + 1;
                break;
            case "Casi ordenada":
                for (int i = 0; i < NUM_ELEMENTS; i++)
                    data[i] = i + 1;
                for (int i = 0; i < NUM_ELEMENTS / 10; i++) {
                    int idx1 = rnd.nextInt(NUM_ELEMENTS);
                    int idx2 = rnd.nextInt(NUM_ELEMENTS);
                    int temp = data[idx1];
                    data[idx1] = data[idx2];
                    data[idx2] = temp;
                }
                break;
            case "Con duplicados":
                for (int i = 0; i < NUM_ELEMENTS; i++)
                    data[i] = rnd.nextInt(MAX_VALUE / 5) + 1;
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
        if (running)
            return;

        if (!bubbleSortRadio.isSelected() && !selectionSortRadio.isSelected() && !insertionSortRadio.isSelected()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText("Ningún algoritmo seleccionado");
                alert.setContentText("Por favor seleccione un algoritmo de ordenamiento");
                alert.showAndWait();
            });
            return;
        }

        running = true;
        startSortBtn.setDisable(true);
        updateTimeDisplay(0.0); // Resetear el tiempo al iniciar

        new Thread(() -> {
            try {
                timer.start();

                if (bubbleSortRadio.isSelected()) {
                    bubbleSortWithAnimation();
                } else if (selectionSortRadio.isSelected()) {
                    selectionSortWithAnimation();
                } else if (insertionSortRadio.isSelected()) {
                    insertionSortWithAnimation();
                }

                timer.stop();
                Platform.runLater(() -> {
                    double elapsedTime = timer.getElapsedMilliseconds();
                    updateTimeDisplay(elapsedTime);
                    running = false;
                    startSortBtn.setDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    running = false;
                    startSortBtn.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error durante el ordenamiento");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void bubbleSortWithAnimation() throws InterruptedException {
        int n = data.length;
        boolean swapped;

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
            if (!swapped)
                break;
        }
    }

    private void selectionSortWithAnimation() throws InterruptedException {
        int n = data.length;

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
    }

    private void insertionSortWithAnimation() throws InterruptedException {
        int n = data.length;

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
            if (j + 1 >= 0)
                unhighlightBar(j + 1);
            Thread.sleep((long) (201 - speedSlider.getValue()));
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

    private void showComparisonChart() {
        int[] testData = new int[50];
        Random rnd = new Random();
        for (int i = 0; i < 50; i++)
            testData[i] = rnd.nextInt(50) + 1;

        Timer bubbleTimer = new Timer();
        bubbleTimer.start();
        bubbleSort.sort(Arrays.copyOf(testData, testData.length));
        bubbleTimer.stop();

        Timer selectionTimer = new Timer();
        selectionTimer.start();
        selectionSort.sort(Arrays.copyOf(testData, testData.length));
        selectionTimer.stop();

        Timer insertionTimer = new Timer();
        insertionTimer.start();
        insertionSort.sort(Arrays.copyOf(testData, testData.length));
        insertionTimer.stop();

        Stage stage = new Stage();
        GraphController graphController = new GraphController();
        graphController.showPerformanceComparison(
                stage,
                (long) bubbleTimer.getElapsedMilliseconds(),
                (long) selectionTimer.getElapsedMilliseconds(),
                (long) insertionTimer.getElapsedMilliseconds(),
                PRIMARY_COLOR,
                SECONDARY_COLOR,
                BAR_COLOR,
                DARK_BG,
                LIGHT_TEXT);
    }
}