// src/modelo/Raton.java
package modelo;

import java.io.Serializable; // Añadir si planeas guardar/cargar ratones (recomendado)

public class Raton implements Serializable { // Implementar Serializable si guardas/cargas
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataque;
    private int defensa; // <--- NUEVO: Propiedad para la defensa del ratón
    private String tipo; // <--- NUEVO: Propiedad para el tipo de ratón (ej. "Comun", "Jefe")
    private int recompensaOro; // Oro que da al ser derrotado
    private int recompensaXP; // Experiencia que otorga al ser derrotado

    // --- Constructor Actualizado: Ahora acepta 7 parámetros ---
    public Raton(String nombre, int vidaMaxima, int ataque, int defensa, String tipo, int recompensaOro, int recompensaXP) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.ataque = ataque;
        this.defensa = defensa; // <--- Inicializar la defensa
        this.tipo = tipo;       // <--- Inicializar el tipo
        this.recompensaOro = recompensaOro;
        this.recompensaXP = recompensaXP;
    }

    // --- Métodos de Interacción y Acciones ---

    public void recibirDano(int cantidadDano) {
        // <--- MODIFICADO: Aplicar defensa al daño recibido
        int danoReal = Math.max(0, cantidadDano - this.defensa);
        vidaActual -= danoReal;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
        System.out.println(nombre + " recibió " + danoReal + " de daño. Vida restante: " + vidaActual);
    }

    public int atacar() {
        return ataque;
    }

    // --- Eliminada la duplicidad: esta es la única versión de estaViva() ---
    public boolean estaViva() {
        return vidaActual > 0;
    }
    

    // --- Getters (incluyendo los nuevos y los ya existentes) ---
    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() { // <--- NUEVO: Getter para la defensa
        return defensa;
    }

    public String getTipo() { // <--- NUEVO: Getter para el tipo
        return tipo;
    }

    public int getRecompensaOro() {
        return recompensaOro;
    }

    public int getRecompensaXP() {
        return recompensaXP;
    }

    @Override
    public String toString() {
        return nombre + " (Vida: " + vidaActual + "/" + vidaMaxima + ", Ataque: " + ataque + ", Defensa: " + defensa + ")";
    }
}