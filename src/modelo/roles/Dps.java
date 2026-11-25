package modelo.roles;

import modelo.Personaje;

/** Rol DPS: mucho daño, poca vida. */
public abstract class Dps extends Personaje {
    protected Dps(String nombre, int vidaMaxima) {
        super(nombre, vidaMaxima);
    }

    @Override
    public String getTipo() { return "DPS"; }

    @Override
    protected int calcularDanio(int base) {
        return (int) Math.round(base * 1.25);
    }
}
