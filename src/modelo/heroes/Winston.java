package modelo.heroes;

import modelo.roles.Tanque;

public class Winston extends Tanque {
    public Winston(String nombre) {
        super(nombre, 450);
    }

    @Override
    public String getTipo() { return "Tanque - Winston"; }

    /** Cañón Tesla: bonus pequeño. */
    @Override
    protected int calcularDanio(int base) {
        return super.calcularDanio(base) + 6;
    }
}
