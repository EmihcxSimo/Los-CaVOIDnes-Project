package modelo.heroes;

import modelo.Personaje;
import modelo.roles.Healer;

public class Mercy extends Healer {
    public Mercy(String nombre) {
        super(nombre, 175);
    }

    @Override
    public String getTipo() { return "Healer - Mercy"; }

    @Override
    protected int calcularDanio(int base) {
        return super.calcularDanio(base) + 2;
    }

    /** Bastón Caduceus: curación fuerte y constante. */
    @Override
    public int curarAliado(Personaje aliado) {
        return aliado.curar(60);
    }
}
