package app;

import excepciones.InventarioLlenoException;
import modelo.Herramienta;
import modelo.Personaje;
import modelo.heroes.FabricaHeroes;
import persistencia.RepositorioEquipo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Menú de consola para gestionar un equipo de Overwatch. */
public class Main {
    private static final Path ARCHIVO = Paths.get("equipo.txt");
    private static final Scanner TECLADO = new Scanner(System.in);
    private static final List<Personaje> equipo = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== OVERWATCH: GESTOR DE EQUIPO ===");
        int opcion = -1;
        try {
            do {
                mostrarMenu();
                opcion = leerEntero("Elige una opción: ", 0, 8);
                try {
                    ejecutar(opcion);
                } catch (IllegalArgumentException | IllegalStateException | InventarioLlenoException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error de archivo: " + e.getMessage());
                }
            } while (opcion != 0);
        } catch (NoSuchElementException e) {
            System.out.println("\nSe terminó la entrada. Hasta luego.");
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("1. Crear héroe");
        System.out.println("2. Ver equipo");
        System.out.println("3. Equipar herramienta");
        System.out.println("4. Asignar compañero");
        System.out.println("5. Atacar");
        System.out.println("6. Ronda de apoyo (todos ayudan a su compañero)");
        System.out.println("7. Guardar equipo en archivo");
        System.out.println("8. Cargar equipo desde archivo");
        System.out.println("0. Salir");
    }

    private static void ejecutar(int opcion) throws InventarioLlenoException, IOException {
        switch (opcion) {
            case 1: crearHeroe(); break;
            case 2: verEquipo(); break;
            case 3: equiparHerramienta(); break;
            case 4: asignarCompanero(); break;
            case 5: atacar(); break;
            case 6: rondaDeApoyo(); break;
            case 7:
                RepositorioEquipo.guardar(ARCHIVO, equipo);
                System.out.println("Equipo guardado en " + ARCHIVO + " (" + equipo.size() + " héroes).");
                break;
            case 8:
                List<Personaje> cargados = RepositorioEquipo.cargar(ARCHIVO);
                equipo.clear();
                equipo.addAll(cargados);
                System.out.println("Equipo cargado: " + equipo.size() + " héroes.");
                break;
            default:
                System.out.println("Hasta luego.");
        }
    }

    private static void crearHeroe() {
        System.out.println("Héroes disponibles:");
        for (int i = 0; i < FabricaHeroes.ETIQUETAS.length; i++) {
            System.out.println("  " + (i + 1) + ". " + FabricaHeroes.ETIQUETAS[i]);
        }
        int eleccion = leerEntero("Elige el héroe: ", 1, FabricaHeroes.CLAVES.length);
        String nombre = leerTexto("Nombre del personaje: ");
        for (Personaje p : equipo) {
            if (p.getNombre().equalsIgnoreCase(nombre.trim())) {
                throw new IllegalArgumentException("Ya existe un héroe llamado " + p.getNombre());
            }
        }
        Personaje nuevo = FabricaHeroes.crear(FabricaHeroes.CLAVES[eleccion - 1], nombre);
        equipo.add(nuevo);
        System.out.println("Creado: " + nuevo);
    }

    private static void verEquipo() {
        if (equipo.isEmpty()) {
            System.out.println("El equipo está vacío.");
            return;
        }
        // Polimorfismo: un solo for, cada objeto responde con su propio toString/getTipo.
        for (Personaje p : equipo) {
            System.out.println("  " + p);
        }
    }

    private static void equiparHerramienta() throws InventarioLlenoException {
        Personaje p = elegirPersonaje("¿A quién equipar?");
        String nombre = leerTexto("Nombre de la herramienta: ");
        int danio = leerEntero("Daño (0-" + Herramienta.DANIO_MAXIMO + "): ", 0, Herramienta.DANIO_MAXIMO);
        p.equipar(new Herramienta(nombre, danio));
        System.out.println(p.getNombre() + " ahora lleva " + p.getCuantasHerramientas() + " herramienta(s).");
    }

    private static void asignarCompanero() {
        Personaje a = elegirPersonaje("¿Quién necesita compañero?");
        Personaje b = elegirPersonaje("¿Quién será su compañero?");
        a.setCompanero(b);
        System.out.println(b.getNombre() + " es ahora compañero de " + a.getNombre() + ".");
    }

    private static void atacar() {
        Personaje atacante = elegirPersonaje("¿Quién ataca?");
        Personaje objetivo = elegirPersonaje("¿A quién ataca?");
        int quitado = atacante.atacar(objetivo);
        System.out.println(atacante.getNombre() + " le quita " + quitado + " de vida a " + objetivo.getNombre()
                + " (le quedan " + objetivo.getVida() + ").");
        if (!objetivo.estaVivo()) {
            System.out.println(objetivo.getNombre() + " fue derrotado.");
        }
    }

    private static void rondaDeApoyo() {
        if (equipo.isEmpty()) {
            throw new IllegalStateException("Aún no hay héroes; crea uno con la opción 1");
        }
        // Polimorfismo: cada héroe ayuda a su manera (los healers usan su habilidad).
        for (Personaje p : equipo) {
            int curado = p.ayudarCompanero();
            System.out.println("  " + p.getNombre() + " curó " + curado + " de vida.");
        }
    }

    private static Personaje elegirPersonaje(String pregunta) {
        if (equipo.isEmpty()) {
            throw new IllegalStateException("Aún no hay héroes; crea uno con la opción 1");
        }
        System.out.println(pregunta);
        for (int i = 0; i < equipo.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + equipo.get(i).getNombre());
        }
        return equipo.get(leerEntero("Número: ", 1, equipo.size()) - 1);
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return TECLADO.nextLine().trim();
    }

    /** Pide un entero hasta que el usuario escriba uno válido dentro del rango. */
    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = TECLADO.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor < min || valor > max) {
                    System.out.println("Escribe un número entre " + min + " y " + max + ".");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("\"" + linea + "\" no es un número válido.");
            }
        }
    }
}
