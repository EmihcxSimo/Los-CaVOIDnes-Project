package persistencia;

import excepciones.InventarioLlenoException;
import interfaces.Persistible;
import modelo.Herramienta;
import modelo.Personaje;
import modelo.heroes.FabricaHeroes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda y lee el equipo en un archivo de texto.
 * Formato de cada línea: Clase;nombre;vida;compañero;herr1|herr2  (herr = nombre:daño:durabilidad)
 */
public final class RepositorioEquipo {
    private RepositorioEquipo() { }

    public static void guardar(Path ruta, List<? extends Persistible> elementos) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Persistible p : elementos) {
            lineas.add(p.serializar());
        }
        Files.write(ruta, lineas, StandardCharsets.UTF_8);
    }

    public static List<Personaje> cargar(Path ruta) throws IOException {
        if (!Files.exists(ruta)) {
            throw new IOException("No existe el archivo " + ruta + "; guarda el equipo primero (opción 7)");
        }
        List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
        List<Personaje> equipo = new ArrayList<>();
        Map<String, Personaje> porNombre = new HashMap<>();
        List<String> companeros = new ArrayList<>();

        int numero = 0;
        for (String linea : lineas) {
            numero++;
            if (linea.isBlank()) continue;
            try {
                String[] partes = linea.split(";", -1);
                if (partes.length != 5) {
                    throw new IllegalArgumentException("se esperaban 5 campos y hay " + partes.length);
                }
                Personaje p = FabricaHeroes.crear(partes[0], partes[1]);
                p.fijarVida(Integer.parseInt(partes[2]));
                if (!partes[4].isEmpty()) {
                    for (String texto : partes[4].split("\\|")) {
                        String[] h = texto.split(":");
                        if (h.length != 3) {
                            throw new IllegalArgumentException("herramienta mal formada: " + texto);
                        }
                        Herramienta herramienta = new Herramienta(h[0], Integer.parseInt(h[1]));
                        herramienta.fijarDurabilidad(Integer.parseInt(h[2]));
                        p.equipar(herramienta);
                    }
                }
                equipo.add(p);
                porNombre.put(p.getNombre(), p);
                companeros.add(partes[3]);
            } catch (IllegalArgumentException | InventarioLlenoException e) {
                throw new IOException("Archivo dañado en la línea " + numero + ": " + e.getMessage(), e);
            }
        }

        for (int i = 0; i < equipo.size(); i++) {
            Personaje compa = porNombre.get(companeros.get(i));
            if (compa != null && compa != equipo.get(i)) {
                equipo.get(i).setCompanero(compa);
            }
        }
        return equipo;
    }
}
