package ec.edu.epn.model;

/**
 * Implementación del algoritmo de ordenamiento Insertion Sort.
 * 
 * Este algoritmo construye la lista ordenada elemento a elemento,
 * insertando cada nuevo elemento en la posición correcta dentro de
 * la parte ordenada del arreglo.
 * 
 * Complejidad temporal: O(n^2) en el peor caso, O(n) en el mejor caso
 * cuando el arreglo ya está ordenado.
 * 
 * @author 
 * @version 1.0
 */
public class InsertionSort implements SortAlgorithm {

    /**
     * Ordena el arreglo especificado utilizando el algoritmo Insertion Sort.
     * 
     * @param array el arreglo de enteros a ordenar
     */
    @Override
    public void sort(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; ++i) {
            int key = array[i];
            int j = i - 1;
            // Mover elementos del arreglo que son mayores que key hacia adelante
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }

    /**
     * Devuelve el nombre del algoritmo.
     * 
     * @return el nombre "Insertion Sort"
     */
    @Override
    public String getName() {
        return "Insertion Sort";
    }
}
