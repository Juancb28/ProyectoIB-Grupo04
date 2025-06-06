package ec.edu.epn.model;

/**
 * Implementación del algoritmo de ordenamiento Bubble Sort.
 * 
 * Este algoritmo compara pares adyacentes de elementos y los intercambia
 * si están en el orden incorrecto, repitiendo este proceso hasta que
 * el arreglo esté completamente ordenado.
 * 
 * Complejidad temporal: O(n^2) en el peor y promedio caso.
 * 
 * @author 
 * @version 1.0
 */
public class BubbleSort implements SortAlgorithm {

    /**
     * Ordena el arreglo especificado utilizando el algoritmo Bubble Sort.
     * 
     * @param array el arreglo de enteros a ordenar
     */
    @Override
    public void sort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Devuelve el nombre del algoritmo.
     * 
     * @return el nombre "Bubble Sort"
     */
    @Override
    public String getName() {
        return "Bubble Sort";
    }
}
