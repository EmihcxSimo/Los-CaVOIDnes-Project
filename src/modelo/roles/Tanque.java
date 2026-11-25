package modelo.roles;

import modelo.Personaje;

/** Rol Tanque: mucha vida y recibe menos daño. */
public abstract class Tanque extends Personaje {
    protected Tanque(String nombre, int vidaMaxima) {
        super(nombre, vidaMaxima);
    }

    @Override
    public String getTipo() { return "Tanque"; }

    @Override
    protected int calcularDanio(int base) {
        return (int) Math.round(base * 0.9);
    }

    /** Armadura: recibe 30% menos de daño. */
    @Override
    public int recibirDanio(int puntos) {
        if (puntos < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo (recibido: " + puntos + ")");
        }
        return super.recibirDanio((int) Math.round(puntos * 0.7));
    }
}
