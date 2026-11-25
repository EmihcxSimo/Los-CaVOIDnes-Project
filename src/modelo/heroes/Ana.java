package modelo.heroes;

import modelo.Personaje;
import modelo.roles.Healer;

public class Ana extends Healer {
    public Ana(String nombre) {
        super(nombre, 200);
    }

    @Override
    public String getTipo() { return "Healer - Ana"; }

    /** Rifle biótico: daño decente para un healer. */
    @Override
    protected int calcularDanio(int base) {
        return super.calcularDanio(base) + 10;
    }

    /** Dardo biótico: curación media. */
    @Override
    public int curarAliado(Personaje aliado) {
        return aliado.curar(45);
    }
}
