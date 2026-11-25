package modelo.heroes;

import modelo.roles.Tanque;

public class Reinhardt extends Tanque {
    public Reinhardt(String nombre) {
        super(nombre, 350);
    }

    @Override
    public String getTipo() { return "Tanque - Reinhardt"; }

    /** El martillo golpea fuerte. */
    @Override
    protected int calcularDanio(int base) {
        return super.calcularDanio(base) + 15;
    }
}
