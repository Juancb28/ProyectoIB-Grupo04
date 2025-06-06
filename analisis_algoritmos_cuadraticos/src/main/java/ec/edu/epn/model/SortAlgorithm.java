package ec.edu.epn.model;

/**
 * Interfaz que define la estructura básica para un algoritmo de ordenamiento.
 * Proporciona el método para ordenar un arreglo de enteros y obtener el nombre
 * del algoritmo.
 * 
 * Esta interfaz permite la implementación polimórfica de diferentes algoritmos
 * de ordenamiento,
 * facilitando su uso e integración en la aplicación.
 * 
 * @author
 * @version 1.0
 */
public interface SortAlgorithm {

    /**
     * Ordena el arreglo de enteros especificado.
     * 
     * @param array el arreglo de enteros a ordenar
     */
    void sort(int[] array);

    /**
     * Devuelve el nombre del algoritmo de ordenamiento.
     * 
     * @return el nombre del algoritmo
     */
    String getName();
}
