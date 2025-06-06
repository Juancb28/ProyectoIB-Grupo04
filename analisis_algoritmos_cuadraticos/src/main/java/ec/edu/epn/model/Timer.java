package ec.edu.epn.model;

/**
 * La clase {@code Timer} proporciona una forma sencilla de medir el tiempo
 * transcurrido entre dos eventos utilizando nanosegundos como base.
 * <p>
 * Se puede usar para calcular el rendimiento de algoritmos de ordenamiento
 * o cualquier otro proceso donde se requiera medir el tiempo de ejecución.
 * </p>
 * 
 * Ejemplo de uso:
 * 
 * <pre>{@code
 * Timer timer = new Timer();
 * timer.start();
 * // ejecutar algún proceso
 * timer.stop();
 * System.out.println("Tiempo transcurrido: " + timer.getElapsedMilliseconds() + " ms");
 * }</pre>
 * 
 * @author
 * @version 1.0
 */
public class Timer {

    /** Marca de tiempo de inicio en nanosegundos */
    private long start;

    /** Marca de tiempo de fin en nanosegundos */
    private long end;

    /**
     * Inicia la medición del tiempo.
     */
    public void start() {
        start = System.nanoTime();
    }

    /**
     * Detiene la medición del tiempo.
     */
    public void stop() {
        end = System.nanoTime();
    }

    /**
     * Devuelve el tiempo transcurrido entre el inicio y el fin en milisegundos.
     *
     * @return tiempo transcurrido en milisegundos
     */
    public double getElapsedMilliseconds() {
        return (end - start) / 1_000_000.0;
    }
}
