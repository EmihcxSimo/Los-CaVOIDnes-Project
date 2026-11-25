package modelo.roles;

import interfaces.Curador;
import modelo.Personaje;

/** Rol Healer: poco daño, pero cura a su compañero con su propia habilidad. */
public abstract class Healer extends Personaje implements Curador {
    protected Healer(String nombre, int vidaMaxima) {
        super(nombre, vidaMaxima);
    }

    @Override
    public String getTipo() { return "Healer"; }

    @Override
    protected int calcularDanio(int base) {
        return (int) Math.round(base * 0.6);
    }

    /** Los healers usan su habilidad de curación en lugar de la curación básica. */
    @Override
    public int ayudarCompanero() {
        Personaje c = getCompanero();
        if (c == null || !estaVivo() || !c.estaVivo()) {
            return 0;
        }
        return curarAliado(c);
    }
}
