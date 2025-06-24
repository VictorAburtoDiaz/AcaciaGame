// src/gestion/GestorJuego.java
package gestion;

import modelo.Acacia;
import modelo.Raton;
import modelo.Batalla;
import modelo.Item;
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
    private int totalEnemigosDerrotados;

    public GestorJuego(Acacia acacia) {
        this.acacia = acacia;
        this.random = new Random();
        this.totalEnemigosDerrotados = 0; 
        // NOTA: El oro inicial de Acacia (100) DEBE ser dado ANTES de pasarla al GestorJuego,
        // por ejemplo, en la clase que crea el JuegoFrame o al cargar el usuario.
        reiniciarProgresoBatalla(); // Llama al nuevo método para iniciar la primera run
    }

    // NUEVO MÉTODO: Reinicia el progreso de las batallas para una nueva run
    public void reiniciarProgresoBatalla() {
        this.faseActual = 1;
        this.totalEnemigosDerrotados = 0;
        // Siempre empezamos en la fase 1 para una nueva run
        // Asegurarse de que los enemigos se regeneren para la nueva run
        // y que el primer enemigo de la fase 1 se cargue.
        iniciarNuevaFase(); // Esto regenera enemigosRestantesFase y avanza al primer enemigo.
    }

    private void generarEnemigosFase() {
        enemigosRestantesFase = new ArrayList<>();
        int numEnemigos = 0;

        switch (faseActual) {
            case 1:
                numEnemigos = 3;
                for (int i = 0; i < numEnemigos; i++) {
                    enemigosRestantesFase.add(new Raton("Ratón Común " + (i + 1), 40, 10, 1, "Comun", 10, 20));
                }
                break;
            case 2:
                numEnemigos = 4;
                for (int i = 0; i < numEnemigos; i++) {
                    enemigosRestantesFase.add(new Raton("Ratón Robusto " + (i + 1), 100, 22, 2, "Comun", 15, 30));
                }
                break;
            case 3: // Boss Final
                numEnemigos = 1;
                enemigosRestantesFase.add(new Raton("Gran Jefe Ratón", 300, 35, 5, "Jefe", 50, 100));
                break;
            default: // Esto no debería alcanzarse si el juego termina en fase 3
                numEnemigos = 2;
                for (int i = 0; i < numEnemigos; i++) {
                    enemigosRestantesFase.add(new Raton("Ratón Básico", 20, 5, 0, "Comun", 5, 10));
                }
                break;
        }
        System.out.println("DEBUG: Fase " + faseActual + ": Generados " + numEnemigos + " enemigos.");
    }

    public void iniciarNuevaFase() {
        if (faseActual > 3) {
            // Esto significa que el jugador ha ganado la partida completa.
            // No generamos más enemigos. El JuegoFrame lo detectará como victoria.
            enemigoActual = null;
            batallaActual = null; // No hay batalla activa si ya se ganó
            System.out.println("DEBUG: ¡Juego completado! No se puede iniciar una nueva fase.");
            return;
        }
        generarEnemigosFase();
        // Automáticamente avanza al primer enemigo de la fase si hay alguno
        if (!enemigosRestantesFase.isEmpty()) {
            avanzarSiguienteEnemigoInterno(); // Carga el primer enemigo de la nueva fase
        } else {
            // Esto puede pasar si generarEnemigosFase no añade enemigos (lo cual no debería ocurrir con tu switch)
            enemigoActual = null;
            batallaActual = null;
        }
    }

    // ESTE ES EL MÉTODO 'avanzarSiguienteEnemigo' MODIFICADO Y COMPLETO
   
    public boolean avanzarSiguienteEnemigo() {
        
        // Lógica para dar recompensas del enemigo ANTERIORMENTE derrotado
        if (enemigoActual != null && !enemigoActual.estaViva()) { // Asegurarse de que el enemigo fue derrotado
            totalEnemigosDerrotados++;
            System.out.println("DEBUG: Total enemigos derrotados: " + totalEnemigosDerrotados);
            int oroGanado = enemigoActual.getRecompensaOro();
            int xpGanada = enemigoActual.getRecompensaXP();

            acacia.anadirOro(oroGanado);
            if (batallaActual != null) {
                batallaActual.agregarMensajeLog("Has ganado " + oroGanado + " de Oro al derrotar a " + enemigoActual.getNombre() + ".");
            }

            boolean subioDeNivel = acacia.ganarExperiencia(xpGanada);
            if (batallaActual != null) {
                batallaActual.agregarMensajeLog("Has ganado " + xpGanada + " de Experiencia.");
                if (subioDeNivel) {
                    batallaActual.agregarMensajeLog("¡" + acacia.getNombre() + " ha subido al Nivel " + acacia.getNivel() + "!");
                }
            }

            if (random.nextInt(100) < 20) {
                // Constructor de Item: nombre, tipo, valorEfecto, descripcion, precio
                Item pocion = new Item("Queso", "PocionVida", 20, "Cura un poco de vida.", 5);
                acacia.anadirItem(pocion);
                if (batallaActual != null) {
                    batallaActual.agregarMensajeLog("¡Has encontrado una " + pocion.getNombre() + "!");
                }
            }
        }

        // Lógica para cargar el PRÓXIMO enemigo O avanzar de fase
        if (!enemigosRestantesFase.isEmpty()) {
            return avanzarSiguienteEnemigoInterno(); // Carga el siguiente enemigo de la fase actual
        } else {
            // La fase actual ha terminado
            faseActual++;
            if (faseActual <= 3) { // Hay más fases por jugar
                System.out.println("DEBUG: Todos los enemigos de la Fase " + (faseActual - 1) + " derrotados. Iniciando Fase " + faseActual + "...");
                iniciarNuevaFase(); // Inicia la siguiente fase (genera sus enemigos y carga el primero)
                // Devuelve true si se pudo cargar un nuevo enemigo para la siguiente fase
                return enemigoActual != null;
            } else { // Todas las fases completadas (Juego ganado)
                System.out.println("DEBUG: ¡Todos los ratones derrotados! Juego completado.");
                enemigoActual = null;
                batallaActual = null;
                return false; // El juego ha terminado por victoria
            }
        }
    }

    // Nuevo método auxiliar para cargar el siguiente enemigo de la lista actual de la fase
    private boolean avanzarSiguienteEnemigoInterno() {
        if (!enemigosRestantesFase.isEmpty()) {
            enemigoActual = enemigosRestantesFase.remove(0);
            // Si el constructor de Raton YA ESTABLECE vidaActual = vidaMaxima,
            // entonces no necesitas clonar aquí, porque cada Raton generado en generarEnemigosFase
            // ya es una nueva instancia "fresca" con vida llena.
            // Si no lo hace, deberías añadir Raton.clonar() y usarlo aquí.
            
            batallaActual = new Batalla(acacia, enemigoActual);
            batallaActual.agregarMensajeLog("¡Un " + enemigoActual.getNombre() + " salvaje aparece!");
            System.out.println("DEBUG: Nuevo enemigo: " + enemigoActual.getNombre());
            return true;
        }
        return false; // No hay más enemigos en esta fase
    }

    public boolean estaJuegoTerminado() {
        // El juego termina si Acacia está derrotada O si todas las fases han sido completadas
        return !acacia.estaViva() || faseActual > 3;
    }

    public boolean estaPartidaGanada() {
        // La partida se gana si Acacia está viva Y todas las fases han sido completadas
        return acacia.estaViva() && faseActual > 3;
    }

    // Getters existentes
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
        //¡NUEVO GETTER PARA EL TOTAL DE ENEMIGOS DERROTADOS!
    public int getTotalEnemigosDerrotados() {
        return totalEnemigosDerrotados;
    }
}