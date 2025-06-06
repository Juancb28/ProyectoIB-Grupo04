package ec.edu.epn.model;

/**
 * Implementación del algoritmo de ordenamiento Selection Sort.
 * 
 * Este algoritmo ordena un arreglo seleccionando repetidamente
 * el elemento mínimo del arreglo no ordenado y colocándolo
 * al inicio de la parte ordenada.
 * 
 * Complejidad temporal: O(n^2)
 * 
 * @author
 * @version 1.0
 */
public class SelectionSort implements SortAlgorithm {

    /**
     * Ordena el arreglo especificado utilizando el algoritmo Selection Sort.
     * 
     * @param array el arreglo de enteros a ordenar
     */
    @Override
    public void sort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
    }

    /**
     * Devuelve el nombre del algoritmo.
     * 
     * @return el nombre "Selection Sort"
     */
    @Override
    public String getName() {
        return "Selection Sort";
    }
}
