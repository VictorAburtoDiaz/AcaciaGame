// src/modelo/Raton.java
package modelo;

public class Raton {
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataque;
    private int recompensaOro; // Oro que da al ser derrotado

    public Raton(String nombre, int vidaMaxima, int ataque, int recompensaOro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.ataque = ataque;
        this.recompensaOro = recompensaOro;
    }

    // --- Métodos de Interacción y Acciones ---

    public void recibirDano(int cantidadDano) {
        vidaActual -= cantidadDano;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
        System.out.println(nombre + " recibió " + cantidadDano + " de daño. Vida restante: " + vidaActual);
    }

    public int atacar() {
        return ataque;
    }

    public boolean estaVivo() {
        return vidaActual > 0;
    }
       public boolean estaViva() {
        return vidaActual > 0;
    }

    // --- Getters ---
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

    public int getRecompensaOro() {
        return recompensaOro;
    }

    @Override
    public String toString() {
        return nombre + " (Vida: " + vidaActual + "/" + vidaMaxima + ", Ataque: " + ataque + ")";
    }
}