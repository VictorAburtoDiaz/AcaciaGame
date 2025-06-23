// src/modelo/Acacia.java
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//Actualizada para que pueda ganar experiencia y estadisticas version 1.1.2

public class Acacia implements Serializable {
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataqueBase;
    private int ataqueEquipado;
    private int defensaBase;
    private int defensaEquipada;
    private List<Item> inventario;
    private int oro;

    // --- PROPIEDADES PARA EL SISTEMA DE NIVELACIÓN ---
    private int nivel;
    private int experienciaActual;
    private int experienciaParaSiguienteNivel;
    // --------------------------------------------------

    // Items equipados
    private Item armaEquipada;
    private Item armaduraEquipada;

    public Acacia(String nombre, int vidaMaxima, int ataqueBaseInicial, int defensaBaseInicial, int oroInicial) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima; // Empieza con la vida al máximo
        this.ataqueBase = ataqueBaseInicial;
        this.ataqueEquipado = 0;
        this.defensaBase = defensaBaseInicial;
        this.defensaEquipada = 0;
        this.inventario = new ArrayList<>();
        this.oro = oroInicial;
        this.armaEquipada = null;
        this.armaduraEquipada = null;

        // --- INICIALIZACIÓN DE NIVELACIÓN ---
        this.nivel = 1;
        this.experienciaActual = 0;
        this.experienciaParaSiguienteNivel = calcularXPParaSiguienteNivel(1);
        // ------------------------------------
    }

    // --- Métodos de Interacción y Acciones ---

    public void recibirDano(int cantidadDano) {
        int defensaTotal = defensaBase + defensaEquipada;
        int danoReducido = Math.max(0, cantidadDano - defensaTotal);
        setVidaActual(vidaActual - danoReducido);
        System.out.println(nombre + " recibió " + danoReducido + " de daño. Vida restante: " + vidaActual);
    }

    public void curar(int cantidadCuracion) {
        setVidaActual(this.vidaActual + cantidadCuracion);
        System.out.println(nombre + " se curó " + cantidadCuracion + " puntos. Vida actual: " + vidaActual);
    }

    // --- NUEVO MÉTODO: Restaura la vida de Acacia a su máximo ---
    public void restaurarVidaCompleta() {
        this.vidaActual = this.vidaMaxima;
        System.out.println(nombre + " ha restaurado su vida a " + vidaActual + "/" + vidaMaxima + ".");
    }

    public int atacar() {
        return ataqueBase + ataqueEquipado;
    }

    public boolean estaViva() {
        return vidaActual > 0;
    }

    public void anadirItem(Item item) {
        inventario.add(item);
        System.out.println(nombre + " obtuvo: " + item.getNombre());
    }

    public void anadirOro(int cantidad) {
        this.oro += cantidad;
    }

    // --- MÉTODOS PARA EL SISTEMA DE NIVELACIÓN ---

    public int getNivel() {
        return nivel;
    }

    public int getExperienciaActual() {
        return experienciaActual;
    }

    public int getExperienciaParaSiguienteNivel() {
        return experienciaParaSiguienteNivel;
    }

    private int calcularXPParaSiguienteNivel(int nivelActual) {
        return 100 + (nivelActual - 1) * 50;
    }

    public boolean ganarExperiencia(int xpGanada) {
        this.experienciaActual += xpGanada;
        boolean subioDeNivel = false;
        while (this.experienciaActual >= this.experienciaParaSiguienteNivel) {
            subioDeNivel = true;
            this.experienciaActual -= this.experienciaParaSiguienteNivel;
            this.nivel++;
            subirEstadisticasPorNivel();
            this.experienciaParaSiguienteNivel = calcularXPParaSiguienteNivel(this.nivel);
            System.out.println(nombre + " ha subido al Nivel " + nivel + "!");
        }
        return subioDeNivel;
    }

    private void subirEstadisticasPorNivel() {
        this.vidaMaxima += 10;
        this.vidaActual = this.vidaMaxima; // Curar a Acacia completamente al subir de nivel (opcional)
        this.ataqueBase += 2;
        this.defensaBase += 1;
    }

    // --- MÉTODOS PARA EQUIPAR/DESEQUIPAR ---

    public boolean equiparItem(Item item) {
        if (inventario.contains(item)) {
            if (item.getTipo().equals("Arma")) {
                if (this.armaEquipada != null) {
                    desequiparItem(this.armaEquipada);
                }
                this.armaEquipada = item;
                this.ataqueEquipado = item.getValorEfecto();
                inventario.remove(item);
                System.out.println(nombre + " equipó " + item.getNombre() + ". Ataque total: " + getAtaque());
                return true;
            } else if (item.getTipo().equals("Armadura")) {
                if (this.armaduraEquipada != null) {
                    desequiparItem(this.armaduraEquipada);
                }
                this.armaduraEquipada = item;
                this.defensaEquipada = item.getValorEfecto();
                inventario.remove(item);
                System.out.println(nombre + " equipó " + item.getNombre() + ". Defensa total: " + getDefensa());
                return true;
            }
        }
        System.out.println("No se pudo equipar " + item.getNombre() + ".");
        return false;
    }

    public void desequiparItem(Item item) {
        if (item != null) {
            if (item.equals(this.armaEquipada)) {
                this.ataqueEquipado = 0;
                this.armaEquipada = null;
                inventario.add(item);
                System.out.println(nombre + " desequipó " + item.getNombre() + ".");
            } else if (item.equals(this.armaduraEquipada)) {
                this.defensaEquipada = 0;
                this.armaduraEquipada = null;
                inventario.add(item);
                System.out.println(nombre + " desequipó " + item.getNombre() + ".");
            }
        }
    }

    public boolean usarItem(Item item) {
        if (inventario.contains(item)) {
            if (item.getTipo().equals("PocionVida")) {
                curar(item.getValorEfecto());
                inventario.remove(item);
                System.out.println(nombre + " usó: " + item.getNombre());
                return true;
            } else {
                System.out.println(item.getNombre() + " no es un ítem consumible para usar de esta forma.");
                return false;
            }
        }
        return false;
    }

    public boolean comprarItem(Item item) {
        if (oro >= item.getPrecio()) {
            oro -= item.getPrecio();
            anadirItem(item);
            System.out.println(nombre + " compró " + item.getNombre() + ". Oro restante: " + oro);
            return true;
        } else {
            System.out.println("No tienes suficiente oro para comprar " + item.getNombre());
            return false;
        }
    }

    // --- Getters y Setters Actualizados ---

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public void setVidaActual(int vidaActual) {
        this.vidaActual = Math.max(0, Math.min(vidaActual, vidaMaxima));
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaque() {
        return ataqueBase + ataqueEquipado;
    }

    public int getDefensa() {
        return defensaBase + defensaEquipada;
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

    public Item getArmaEquipada() {
        return armaEquipada;
    }

    public Item getArmaduraEquipada() {
        return armaduraEquipada;
    }
}