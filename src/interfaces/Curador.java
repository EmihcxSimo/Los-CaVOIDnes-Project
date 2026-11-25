package interfaces;

import modelo.Personaje;

/** Capacidad de curar a un aliado. Cada healer decide cuánto cura. */
public interface Curador {
    /** Cura al aliado y devuelve los puntos de vida realmente recuperados. */
    int curarAliado(Personaje aliado);
}
