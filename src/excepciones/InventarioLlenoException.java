package excepciones;

/** Excepción propia del dominio: se intenta equipar más herramientas de las que caben. */
public class InventarioLlenoException extends Exception {
    private final int capacidad;

    public InventarioLlenoException(int capacidad) {
        super("El inventario está lleno (máximo " + capacidad + " herramientas)");
        this.capacidad = capacidad;
    }

    public int getCapacidad() {
        return capacidad;
    }
}
