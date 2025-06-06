package ec.edu.epn.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.*;
import java.util.function.Consumer;

/**
 * Clase que representa la vista principal del visualizador de algoritmos de ordenamiento.
 *
 * <p>Permite al usuario seleccionar un algoritmo (Bubble Sort, Selection Sort o Insertion Sort),
 * elegir el tipo de lista de entrada (ordenada, inversamente ordenada, aleatoria),
 * visualizar el proceso de ordenamiento mediante animaciones y acceder a gráficas de rendimiento.</p>
 *
 * @author 
 * @version 1.0
 */
public class MainView {

    private RadioButton bubbleSortRadio;
    private RadioButton selectionSortRadio;
    private RadioButton insertionSortRadio;
    private ChoiceBox<String> listTypeChoiceBox;
    private AnchorPane visualizationPane;
    private ToggleGroup algorithmToggleGroup;
    private int[] data;
    private static final int NUM_BARS = 50;

    /**
     * Muestra la ventana principal de la aplicación con la interfaz gráfica.
     *
     * @param primaryStage el escenario principal de JavaFX.
     */
    public void show(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Panel izquierdo
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(10));
        Label titleLabel = new Label("Algoritmos de ordenamiento");

        algorithmToggleGroup = new ToggleGroup();

        bubbleSortRadio = new RadioButton("Bubble Sort");
        bubbleSortRadio.setToggleGroup(algorithmToggleGroup);

        selectionSortRadio = new RadioButton("Selection Sort");
        selectionSortRadio.setToggleGroup(algorithmToggleGroup);

        insertionSortRadio = new RadioButton("Insertion Sort");
        insertionSortRadio.setToggleGroup(algorithmToggleGroup);

        Label listLabel = new Label("Tipo de lista:");
        listLabel.setPadding(new Insets(10, 0, 0, 0));

        listTypeChoiceBox = new ChoiceBox<>();
        listTypeChoiceBox.getItems().addAll("Ordenada", "Inversamente ordenada", "Aleatoria");
        listTypeChoiceBox.getSelectionModel().selectFirst();

        leftPane.getChildren().addAll(
                titleLabel, bubbleSortRadio, selectionSortRadio, insertionSortRadio, listLabel, listTypeChoiceBox
        );

        root.setLeft(leftPane);

        // Panel central
        visualizationPane = new AnchorPane();
        visualizationPane.setStyle("-fx-background-color: #f4f4f4;");
        root.setCenter(visualizationPane);

        // Panel inferior
        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(10));
        bottomPane.setAlignment(Pos.CENTER_RIGHT);

        Button sortButton = new Button("Iniciar ordenamiento");
        sortButton.setOnAction(e -> startSorting());

        Button graphButton = new Button("Ver gráficas de rendimiento");
        graphButton.setOnAction(e -> showGraphWindow());

        bottomPane.getChildren().addAll(sortButton, graphButton);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Visualizador de Algoritmos de Ordenamiento");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Dibuja un arreglo de enteros como barras verticales dentro del panel de visualización.
     *
     * @param array el arreglo de enteros a representar gráficamente.
     */
    private void drawData(int[] array) {
        Platform.runLater(() -> {
            visualizationPane.getChildren().clear();
            double width = visualizationPane.getWidth();
            double height = visualizationPane.getHeight();
            double barWidth = width / array.length;
            int maxVal = Arrays.stream(array).max().orElse(1);

            for (int i = 0; i < array.length; i++) {
                double barHeight = (array[i] * height) / maxVal;
                Rectangle rect = new Rectangle(i * barWidth, height - barHeight, barWidth - 2, barHeight);
                rect.setFill(Color.DODGERBLUE);
                visualizationPane.getChildren().add(rect);
            }
        });
    }

    /**
     * Dibuja el arreglo y realiza una pausa breve para crear una animación visible.
     *
     * @param array el arreglo que se está ordenando.
     */
    private void sleepAndDraw(int[] array) {
        drawData(array);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Ejecuta un algoritmo de ordenamiento en un hilo separado y lo anima.
     *
     * @param array el arreglo original.
     * @param sortingAlgorithm el algoritmo de ordenamiento como función lambda.
     */
    private void animateSort(int[] array, Consumer<int[]> sortingAlgorithm) {
        new Thread(() -> {
            int[] copy = Arrays.copyOf(array, array.length);
            sortingAlgorithm.accept(copy);
        }).start();
    }

    /**
     * Inicia el proceso de ordenamiento según el algoritmo seleccionado.
     */
    public void startSorting() {
        generateList();
        if (bubbleSortRadio.isSelected()) {
            animateSort(data, this::bubbleSort);
        } else if (selectionSortRadio.isSelected()) {
            animateSort(data, this::selectionSort);
        } else if (insertionSortRadio.isSelected()) {
            animateSort(data, this::insertionSort);
        }
    }

    /**
     * Genera una lista de enteros basada en la opción seleccionada (ordenada, inversa, aleatoria).
     */
    private void generateList() {
        data = new int[NUM_BARS];
        for (int i = 0; i < NUM_BARS; i++) data[i] = i + 1;

        String selected = listTypeChoiceBox.getValue();
        if ("Inversamente ordenada".equals(selected)) {
            for (int i = 0; i < NUM_BARS / 2; i++) {
                int temp = data[i];
                data[i] = data[NUM_BARS - i - 1];
                data[NUM_BARS - i - 1] = temp;
            }
        } else if ("Aleatoria".equals(selected)) {
            List<Integer> temp = Arrays.asList(Arrays.stream(data).boxed().toArray(Integer[]::new));
            Collections.shuffle(temp);
            for (int i = 0; i < data.length; i++) data[i] = temp.get(i);
        }

        drawData(data);
    }

    /**
     * Implementación del algoritmo Bubble Sort con animación.
     *
     * @param array el arreglo a ordenar.
     */
    private void bubbleSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                    sleepAndDraw(array);
                }
            }
        }
    }

    /**
     * Implementación del algoritmo Selection Sort con animación.
     *
     * @param array el arreglo a ordenar.
     */
    private void selectionSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[min]) min = j;
            }
            if (min != i) {
                int tmp = array[i];
                array[i] = array[min];
                array[min] = tmp;
                sleepAndDraw(array);
            }
        }
    }

    /**
     * Implementación del algoritmo Insertion Sort con animación.
     *
     * @param array el arreglo a ordenar.
     */
    private void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                sleepAndDraw(array);
            }
            array[j + 1] = key;
            sleepAndDraw(array);
        }
    }

    /**
     * Muestra una ventana con las gráficas de rendimiento (actualmente placeholder).
     */
    private void showGraphWindow() {
        Stage graphStage = new Stage();
        graphStage.setTitle("Gráficas de rendimiento");
        graphStage.setScene(new Scene(new Label("Gráficas aquí..."), 300, 200));
        graphStage.show();
    }
}
