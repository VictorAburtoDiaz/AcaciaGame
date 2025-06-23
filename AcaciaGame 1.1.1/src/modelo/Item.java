// src/modelo/Item.java
package modelo;

import java.io.Serializable;


// se actualiza el codigo de items para poder ser utilizables !!

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final String descripcion;
    private final int precio;
    private final String tipo; // "PocionVida", "Arma", "Armadura", "Generico"
    private final int valorEfecto; // Nuevo: Para curación (pociones) o mejora de stat (arma/armadura)

    public Item(String nombre, String descripcion, int precio, String tipo, int valorEfecto) { // <--- MODIFICADO
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
        this.valorEfecto = valorEfecto; // <--- NUEVA ASIGNACIÓN
    }

    // --- Getters ---
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public String getTipo() {
        return tipo;
    }
    
    public int getValorEfecto() { // <--- NUEVO GETTER
        return valorEfecto;
    }

    @Override
    public String toString() {
        // Mejorar la representación del ítem
        String efectoStr = "";
        if (tipo.equals("PocionVida")) {
            efectoStr = " (Cura " + valorEfecto + " HP)";
        } else if (tipo.equals("Arma")) {
            efectoStr = " (+ " + valorEfecto + " Ataque)";
        } else if (tipo.equals("Armadura")) {
            efectoStr = " (+ " + valorEfecto + " Defensa)";
        }
        return nombre + efectoStr + " [" + precio + " oro]"; // Añadir el precio para la tienda
    }
}