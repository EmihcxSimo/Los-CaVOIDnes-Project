package modelo;

/** Validaciones compartidas por las clases del modelo. */
final class Validacion {
    private Validacion() { }

    /** Valida y devuelve el texto sin espacios sobrantes. */
    static String nombre(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío");
        }
        String limpio = texto.trim();
        if (limpio.length() > 30) {
            throw new IllegalArgumentException(campo + " no puede tener más de 30 caracteres");
        }
        if (!limpio.matches("[\\p{L}\\p{N} ._-]+")) {
            throw new IllegalArgumentException(
                    campo + " solo admite letras, números, espacios, punto, guion y guion bajo");
        }
        return limpio;
    }
}
