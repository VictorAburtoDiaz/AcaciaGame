// src/gestion/GestorJuego.java
package gestion;

import modelo.Acacia;
import modelo.Raton;
import modelo.Batalla;
import modelo.Item; // Para el manejo de ítems si es necesario
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GestorJuego {
    private Acacia acacia;
    private int faseActual;
    private List<Raton> enemigosRestantesFase;
    private Raton enemigoActual;
    private Batalla batallaActual;
    private Random random;

    // Puedes pasar la Acacia existente (comprada en la tienda)
    public GestorJuego(Acacia acacia) {
        this.acacia = acacia;
        this.faseActual = 1; // Empezamos en la fase 1
        this.random = new Random();
        iniciarNuevaFase(); // Cargar la primera fase
    }

    private void generarEnemigosFase() {
        enemigosRestantesFase = new ArrayList<>();
        int numEnemigos = 0;

        // Definimos la dificultad por fases
        switch (faseActual) {
            case 1:
                numEnemigos = 3;
                for (int i = 0; i < numEnemigos; i++) {
                    enemigosRestantesFase.add(new Raton("Ratón Común " + (i + 1), 30, 8, 10)); // Vida, Ataque, Oro
                }
                break;
            case 2:
                numEnemigos = 4;
                for (int i = 0; i < numEnemigos; i++) {
                    enemigosRestantesFase.add(new Raton("Ratón Robusto " + (i + 1), 50, 12, 15));
                }
                break;
            case 3: // Boss Final
                numEnemigos = 1;
                enemigosRestantesFase.add(new Raton("Gran Jefe Ratón", 150, 25, 100)); // El jefe final
                break;
            default:
                // Si hay más fases, se podrían añadir aquí
                numEnemigos = 2; // Default para cualquier otra fase si se expande
                for (int i = 0; i < numEnemigos; i++) {
                     enemigosRestantesFase.add(new Raton("Ratón Básico", 20, 5, 5));
                }
                break;
        }
        System.out.println("DEBUG: Fase " + faseActual + ": Generados " + numEnemigos + " enemigos.");
    }

    public void iniciarNuevaFase() {
        if (faseActual > 3) { // Si ya pasamos la fase del boss, el juego se gana
            // Lógica para terminar el juego (ganado)
            System.out.println("¡FELICIDADES! ¡Has recuperado la tarjeta de crédito!");
            enemigoActual = null; // No hay más enemigos
            return;
        }
        generarEnemigosFase();
        avanzarSiguienteEnemigo(); // Cargar el primer enemigo de la fase
    }

    public boolean avanzarSiguienteEnemigo() {
        if (!enemigosRestantesFase.isEmpty()) {
            enemigoActual = enemigosRestantesFase.remove(0); // Tomamos el primer enemigo de la lista
            batallaActual = new Batalla(acacia, enemigoActual);
            System.out.println("DEBUG: Nuevo enemigo: " + enemigoActual.getNombre());
            return true;
        } else {
            faseActual++; // Avanzamos a la siguiente fase
            if (faseActual <= 3) { // Si hay más fases, la iniciamos
                 System.out.println("DEBUG: Todos los enemigos de la Fase " + (faseActual -1) + " derrotados. Iniciando Fase " + faseActual + "...");
                 iniciarNuevaFase(); // Recursivamente inicia la siguiente fase
                 return enemigoActual != null; // True si hay un nuevo enemigo en la nueva fase
            } else {
                // Todas las fases completadas, juego ganado
                System.out.println("DEBUG: ¡Todos los ratones derrotados! Juego completado.");
                enemigoActual = null;
                return false;
            }
        }
    }

    public boolean estaJuegoTerminado() {
        return !acacia.estaViva() || faseActual > 3; // Juego terminado si Acacia muere o todas las fases completadas
    }

    public boolean estaPartidaGanada() {
        return acacia.estaViva() && faseActual > 3;
    }
    
    public Acacia getAcacia() {
        return acacia;
    }

    public Raton getEnemigoActual() {
        return enemigoActual;
    }

    public Batalla getBatallaActual() {
        return batallaActual;
    }

    public int getFaseActual() {
        return faseActual;
    }
}