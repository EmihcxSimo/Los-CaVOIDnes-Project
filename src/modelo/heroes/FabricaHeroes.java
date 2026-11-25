package modelo.heroes;

import modelo.Personaje;

/**
 * Patrón Factory Method: un único lugar sabe qué clase concreta crear a partir de una clave.
 * Justificación: el menú y el lector de archivos necesitan crear héroes por nombre de clase
 * sin conocer los constructores; agregar un héroe nuevo solo toca esta clase.
 */
public final class FabricaHeroes {
    public static final String[] CLAVES = {"Tracer", "Soldado76", "Reinhardt", "Winston", "Mercy", "Ana"};
    public static final String[] ETIQUETAS = {
        "Tracer (DPS)", "Soldado: 76 (DPS)", "Reinhardt (Tanque)",
        "Winston (Tanque)", "Mercy (Healer)", "Ana (Healer)"
    };

    private FabricaHeroes() { }

    public static Personaje crear(String clave, String nombre) {
        if (clave == null) {
            throw new IllegalArgumentException("Hay que indicar qué héroe crear");
        }
        switch (clave) {
            case "Tracer":    return new Tracer(nombre);
            case "Soldado76": return new Soldado76(nombre);
            case "Reinhardt": return new Reinhardt(nombre);
            case "Winston":   return new Winston(nombre);
            case "Mercy":     return new Mercy(nombre);
            case "Ana":       return new Ana(nombre);
            default:
                throw new IllegalArgumentException("Héroe desconocido: " + clave);
        }
    }
}
