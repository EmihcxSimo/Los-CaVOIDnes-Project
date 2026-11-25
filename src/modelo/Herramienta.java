package modelo;

import interfaces.Persistible;

/** Arma o habilidad que lleva un héroe. */
public class Herramienta implements Persistible {
    public static final int DURABILIDAD_MAXIMA = 100;
    public static final int DANIO_MAXIMO = 100;
    private static final int DESGASTE_POR_USO = 10;

    private final String nombre;
    private final int danio;
    private int durabilidad;

    public Herramienta(String nombre, int danio) {
        this.nombre = Validacion.nombre(nombre, "El nombre de la herramienta");
        if (danio < 0 || danio > DANIO_MAXIMO) {
            throw new IllegalArgumentException(
                    "El daño debe estar entre 0 y " + DANIO_MAXIMO + " (recibido: " + danio + ")");
        }
        this.danio = danio;
        this.durabilidad = DURABILIDAD_MAXIMA;
    }

    public String getNombre() { return nombre; }
    public int getDanio() { return danio; }
    public int getDurabilidad() { return durabilidad; }
    public boolean estaRota() { return durabilidad <= 0; }

    /** Usa la herramienta: gasta durabilidad y devuelve su daño. */
    public int usar() {
        if (estaRota()) {
            throw new IllegalStateException(nombre + " está rota; hay que repararla primero");
        }
        durabilidad = entre(durabilidad - DESGASTE_POR_USO, 0, DURABILIDAD_MAXIMA);
        return danio;
    }

    public void reparar() {
        durabilidad = DURABILIDAD_MAXIMA;
    }

    /** Usado al cargar una partida guardada. */
    public void fijarDurabilidad(int valor) {
        if (valor < 0 || valor > DURABILIDAD_MAXIMA) {
            throw new IllegalArgumentException(
                    "La durabilidad debe estar entre 0 y " + DURABILIDAD_MAXIMA + " (recibido: " + valor + ")");
        }
        durabilidad = valor;
    }

    @Override
    public String serializar() {
        return nombre + ":" + danio + ":" + durabilidad;
    }

    @Override
    public String toString() {
        return nombre + " (daño " + danio + ", durabilidad " + durabilidad + ")";
    }

    private static int entre(int valor, int min, int max) {
        return Math.max(min, Math.min(max, valor));
    }
}
