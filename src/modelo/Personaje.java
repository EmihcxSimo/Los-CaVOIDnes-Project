package modelo;

import excepciones.InventarioLlenoException;
import interfaces.Persistible;

/** Clase abstracta base de todos los héroes de Overwatch. */
public abstract class Personaje implements Persistible {
    private static final int ATAQUE_BASICO = 5;

    private final String nombre;
    private final int vidaMaxima;
    private int vida;
    private final Inventario inventario;
    private Personaje companero;

    protected Personaje(String nombre, int vidaMaxima) {
        this.nombre = Validacion.nombre(nombre, "El nombre del héroe");
        if (vidaMaxima <= 0) {
            throw new IllegalArgumentException(
                    "La vida máxima debe ser mayor que 0 (recibido: " + vidaMaxima + ")");
        }
        this.vidaMaxima = vidaMaxima;
        this.vida = vidaMaxima;
        this.inventario = new Inventario();
        this.companero = null;
    }

    public abstract String getTipo();

    protected abstract int calcularDanio(int base);

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getCuantasHerramientas() { return inventario.getCuantas(); }
    public Herramienta getHerramienta(int i) { return inventario.getHerramienta(i); }
    public Personaje getCompanero() { return companero; }
    public boolean estaVivo() { return vida > 0; }

    public void equipar(Herramienta h) throws InventarioLlenoException {
        inventario.agregar(h);
    }

    public void setCompanero(Personaje companero) {
        if (companero == this) {
            throw new IllegalArgumentException(nombre + " no puede ser su propio compañero");
        }
        this.companero = companero;
    }

    /** Resta vida y devuelve los puntos realmente perdidos. */
    public int recibirDanio(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo (recibido: " + puntos + ")");
        }
        int antes = vida;
        vida = Math.max(0, vida - puntos);
        return antes - vida;
    }

    /** Suma vida y devuelve los puntos realmente recuperados. */
    public int curar(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("La curación no puede ser negativa (recibido: " + puntos + ")");
        }
        if (!estaVivo()) {
            throw new IllegalStateException(nombre + " fue derrotado y no puede curarse");
        }
        int antes = vida;
        vida = Math.min(vidaMaxima, vida + puntos);
        return vida - antes;
    }

    /** Ataca a otro personaje y devuelve la vida que le quitó. */
    public int atacar(Personaje objetivo) {
        if (objetivo == null) {
            throw new IllegalArgumentException("Hay que elegir un objetivo");
        }
        if (objetivo == this) {
            throw new IllegalArgumentException(nombre + " no puede atacarse a sí mismo");
        }
        if (!estaVivo()) {
            throw new IllegalStateException(nombre + " fue derrotado y no puede atacar");
        }
        if (!objetivo.estaVivo()) {
            throw new IllegalStateException(objetivo.getNombre() + " ya fue derrotado");
        }
        int base = Math.max(ATAQUE_BASICO, inventario.usarMejor());
        return objetivo.recibirDanio(calcularDanio(base));
    }

    /** Cura un poco al compañero. Devuelve lo curado (0 si no pudo ayudar). */
    public int ayudarCompanero() {
        if (companero == null || !estaVivo() || !companero.estaVivo()) {
            return 0;
        }
        return companero.curar(vidaMaxima / 10);
    }

    /** Solo para cargar partidas guardadas. */
    public void fijarVida(int valor) {
        if (valor < 0 || valor > vidaMaxima) {
            throw new IllegalArgumentException(
                    "La vida debe estar entre 0 y " + vidaMaxima + " (recibido: " + valor + ")");
        }
        vida = valor;
    }

    @Override
    public String serializar() {
        String nombreCompanero = (companero == null) ? "-" : companero.getNombre();
        return getClass().getSimpleName() + ";" + nombre + ";" + vida + ";"
                + nombreCompanero + ";" + inventario.serializar();
    }

    @Override
    public String toString() {
        String comp = (companero == null) ? "sin compañero" : "compañero: " + companero.getNombre();
        return String.format("[%s] %s - Vida: %d/%d - Herramientas: %d - %s",
                getTipo(), nombre, vida, vidaMaxima, getCuantasHerramientas(), comp);
    }
}
