// src/modelo/Acacia.java
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
                                //se añade funcion para poder "guardar partidas/runs"
public class Acacia implements Serializable {
    private String nombre;
    private int vidaActual;
    private int vidaMaxima;
    private int ataqueBase; // <--- Nuevo: El ataque base de Acacia
    private int ataqueEquipado; // <--- Nuevo: Bonificación de ataque del arma
    private int defensaBase; // <--- Nuevo: La defensa base de Acacia
    private int defensaEquipada; // <--- Nuevo: Bonificación de defensa de la armadura
    private int ataque;
    private int defensa;
    private List<Item> inventario; // Lista de ítems que Acacia posee
    private int oro; // Moneda para comprar ítems

    // Items equipados
    private Item armaEquipada; // <--- Nuevo
    private Item armaduraEquipada; // <--- Nuevo
    
    public Acacia(String nombre, int vidaMaxima, int ataque, int defensa, int oroInicial) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima; // Empieza con la vida al máximo
        this.ataque = ataque;
        this.defensa = defensa;
        this.ataqueBase = ataque; // <--- Asignar a ataqueBase
        this.ataqueEquipado = 0; // Inicialmente sin bonificación
        this.defensaBase = defensa; // <--- Asignar a defensaBase
        this.defensaEquipada = 0; // Inicialmente sin bonificación
        this.inventario = new ArrayList<>();
        this.oro = oroInicial;
        this.armaEquipada = null; // Inicialmente no hay arma equipada
        this.armaduraEquipada = null; // Inicialmente no hay armadura equipada
    }

 // --- Métodos de Interacción y Acciones ---

    public void recibirDano(int cantidadDano) {
        // La defensa total es base + equipada
        int defensaTotal = defensaBase + defensaEquipada;
        int danoReducido = Math.max(0, cantidadDano - defensaTotal);
        vidaActual -= danoReducido;
        if (vidaActual < 0) {
            vidaActual = 0;
        }
        System.out.println(nombre + " recibió " + danoReducido + " de daño. Vida restante: " + vidaActual);
    }

    public void curar(int cantidadCuracion) {
        vidaActual += cantidadCuracion;
        if (vidaActual > vidaMaxima) {
            vidaActual = vidaMaxima;
        }
        System.out.println(nombre + " se curó " + cantidadCuracion + " puntos. Vida actual: " + vidaActual);
    }

        public int atacar() {
        return ataqueBase + ataqueEquipado; // Ataque total es base + equipada
    }

    public boolean estaViva() {
        return vidaActual > 0;
    }

    public void agregarItem(Item item) {
        inventario.add(item);
        System.out.println(nombre + " obtuvo: " + item.getNombre());
    }
    
    // --- NUEVOS MÉTODOS PARA EQUIPAR/DESEQUIPAR ---

    public boolean equiparItem(Item item) {
        if (inventario.contains(item)) {
            if (item.getTipo().equals("Arma")) {
                if (this.armaEquipada != null) {
                    desequiparItem(this.armaEquipada); // Desequipar el arma actual primero
                }
                this.armaEquipada = item;
                this.ataqueEquipado = item.getValorEfecto();
                inventario.remove(item); // El ítem equipado ya no está en el inventario "libre"
                System.out.println(nombre + " equipó " + item.getNombre() + ". Ataque total: " + getAtaque());
                return true;
            } else if (item.getTipo().equals("Armadura")) {
                if (this.armaduraEquipada != null) {
                    desequiparItem(this.armaduraEquipada); // Desequipar la armadura actual primero
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
                this.ataqueEquipado = 0; // Eliminar bonificación
                this.armaEquipada = null;
                inventario.add(item); // Devolver al inventario libre
                System.out.println(nombre + " desequipó " + item.getNombre() + ".");
            } else if (item.equals(this.armaduraEquipada)) {
                this.defensaEquipada = 0; // Eliminar bonificación
                this.armaduraEquipada = null;
                inventario.add(item); // Devolver al inventario libre
                System.out.println(nombre + " desequipó " + item.getNombre() + ".");
            }
        }
    }

    // El método usarItem se mantiene para Pociones, etc.
    public boolean usarItem(Item item) {
        // Este método ahora se centrará en ítems de "un solo uso" como las pociones.
        // Los ítems de equipamiento se manejan con equiparItem.
        if (inventario.contains(item)) {
            if (item.getTipo().equals("PocionVida")) {
                curar(item.getValorEfecto()); // Usa el valorEfecto de la poción
                inventario.remove(item); // Consumir la poción
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
            agregarItem(item); // Los ítems comprados van al inventario inicialmente
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
        this.vidaActual = vidaActual;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    // getAtaque ahora retorna el ataque TOTAL
    public int getAtaque() {
        return ataqueBase + ataqueEquipado;
    }

    // setAtaque debería modificar el ataqueBase si quieres permitir aumentar el ataque base
    public void setAtaqueBase(int ataqueBase) { // <--- Nuevo setter para ataqueBase
        this.ataqueBase = ataqueBase;
    }
    
    // getDefensa ahora retorna la defensa TOTAL
    public int getDefensa() {
        return defensaBase + defensaEquipada;
    }

    // setDefensa debería modificar la defensaBase
    public void setDefensaBase(int defensaBase) { // <--- Nuevo setter para defensaBase
        this.defensaBase = defensaBase;
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
    
    public Item getArmaEquipada() { // <--- Nuevo getter
        return armaEquipada;
    }

    public Item getArmaduraEquipada() { // <--- Nuevo getter
        return armaduraEquipada;
    }
}