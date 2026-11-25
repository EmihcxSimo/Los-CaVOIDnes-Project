package modelo.heroes;

import modelo.roles.Dps;

public class Tracer extends Dps {
    public Tracer(String nombre) {
        super(nombre, 150);
    }

    @Override
    public String getTipo() { return "DPS - Tracer"; }

    /** Pistolas de pulso: bonus por velocidad. */
    @Override
    protected int calcularDanio(int base) {
        return super.calcularDanio(base) + 5;
    }
}
