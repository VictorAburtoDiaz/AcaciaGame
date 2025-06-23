// src/modelo/Acacia.java
package modelo;

import java.util.ArrayList;
import java.util.List;

public class Acacia {
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataque;
    private int defensa;
    private List<Item> inventario; // Lista de ítems que Acacia posee
    private int oro; // Moneda para comprar ítems

    public Acacia(String nombre, int vidaMaxima, int ataque, int defensa, int oroInicial) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima; // Empieza con la vida al máximo
        this.ataque = ataque;
        this.defensa = defensa;
        this.inventario = new ArrayList<>();
        this.oro = oroInicial;
    }

    // --- Métodos de Interacción y Acciones ---

    public void recibirDano(int cantidadDano) {
        int danoReducido = Math.max(0, cantidadDano - defensa); // Reducir daño por defensa
        vidaActual -= danoReducido;
        if (vidaActual < 0) {
            vidaActual = 0; // La vida no puede ser negativa
        }
        System.out.println(nombre + " recibió " + danoReducido + " de daño. Vida restante: " + vidaActual);
    }

    public void curar(int cantidadCuracion) {
        vidaActual += cantidadCuracion;
        if (vidaActual > vidaMaxima) {
            vidaActual = vidaMaxima; // La vida no puede exceder el máximo
        }
        System.out.println(nombre + " se curó " + cantidadCuracion + " puntos. Vida actual: " + vidaActual);
    }

    public int atacar() {
        // En un juego real, aquí podrías añadir variaciones (críticos, fallos)
        return ataque;
    }

    public boolean estaViva() {
        return vidaActual > 0;
    }

    public void agregarItem(Item item) {
        inventario.add(item);
        System.out.println(nombre + " obtuvo: " + item.getNombre());
    }

    public boolean usarItem(Item item) {
        if (inventario.contains(item)) {
            // Lógica para usar el ítem (ej. poción, equipar arma)
            // Por ahora, solo simula el uso
            System.out.println(nombre + " usó: " + item.getNombre());
            inventario.remove(item); // Eliminar el ítem del inventario
            return true;
        }
        return false;
    }

    public boolean comprarItem(Item item) {
        if (oro >= item.getPrecio()) {
            oro -= item.getPrecio();
            agregarItem(item);
            System.out.println(nombre + " compró " + item.getNombre() + ". Oro restante: " + oro);
            return true;
        } else {
            System.out.println("No tienes suficiente oro para comprar " + item.getNombre());
            return false;
        }
    }

    // --- Getters y Setters ---

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public List<Item> getInventario() {
        return inventario;
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }
}