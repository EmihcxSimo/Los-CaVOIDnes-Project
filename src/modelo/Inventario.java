package modelo;

import excepciones.InventarioLlenoException;
import interfaces.Persistible;

/** Guarda hasta 3 herramientas. Pertenece a un Personaje (composición). */
public class Inventario implements Persistible {
    public static final int CAPACIDAD = 3;

    private final Herramienta[] herramientas;
    private int cuantas;

    public Inventario() {
        this.herramientas = new Herramienta[CAPACIDAD];
        this.cuantas = 0;
    }

    public void agregar(Herramienta h) throws InventarioLlenoException {
        if (h == null) {
            throw new IllegalArgumentException("La herramienta no puede ser nula");
        }
        if (cuantas >= CAPACIDAD) {
            throw new InventarioLlenoException(CAPACIDAD);
        }
        herramientas[cuantas++] = h;
    }

    public int getCuantas() { return cuantas; }

    public Herramienta getHerramienta(int i) {
        if (i < 0 || i >= cuantas) {
            throw new IllegalArgumentException(
                    "Índice de herramienta fuera de rango: " + i + " (hay " + cuantas + ")");
        }
        return herramientas[i];
    }

    /** Usa la herramienta sana con más daño. Devuelve 0 si no hay ninguna utilizable. */
    public int usarMejor() {
        Herramienta mejor = null;
        for (int i = 0; i < cuantas; i++) {
            Herramienta h = herramientas[i];
            if (!h.estaRota() && (mejor == null || h.getDanio() > mejor.getDanio())) {
                mejor = h;
            }
        }
        return mejor == null ? 0 : mejor.usar();
    }

    @Override
    public String serializar() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cuantas; i++) {
            if (i > 0) sb.append('|');
            sb.append(herramientas[i].serializar());
        }
        return sb.toString();
    }
}
