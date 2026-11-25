package modelo.heroes;

import modelo.roles.Dps;

public class Soldado76 extends Dps {
    public Soldado76(String nombre) {
        super(nombre, 200);
    }

    @Override
    public String getTipo() { return "DPS - Soldado: 76"; }

    /** Visor táctico: bonus de precisión. */
    @Override
    protected int calcularDanio(int base) {
        return super.calcularDanio(base) + 8;
    }
}
