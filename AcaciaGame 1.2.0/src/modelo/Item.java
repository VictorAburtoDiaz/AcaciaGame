// src/modelo/Item.java
package modelo;

import java.io.Serializable; // Añadir si planeas guardar/cargar Items (recomendado)
import java.util.Objects;

public class Item implements Serializable { // Implementar Serializable
    private String nombre;
    private String tipo; // "Arma", "Armadura", "PocionVida", "PocionMana", etc.
    private int valorEfecto; // Daño para armas, defensa para armaduras, curación para pociones
    private String descripcion; // Descripción del item
    private int precio; // Precio del item para la tienda

    // Constructor con 5 parámetros (el que el compilador parece esperar)
    public Item(String nombre, String tipo, int valorEfecto, String descripcion, int precio) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valorEfecto = valorEfecto;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Si también quieres un constructor de 3 parámetros para simplicidad, puedes añadirlo así:
    // public Item(String nombre, String tipo, int valorEfecto) {
    //     this(nombre, tipo, valorEfecto, "Sin descripción", 0); // Llama al constructor de 5 parámetros
    // }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getValorEfecto() {
        return valorEfecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        if (tipo.equals("Arma")) {
            return nombre + " (Atk +" + valorEfecto + ") - " + precio + " Oro";
        } else if (tipo.equals("Armadura")) {
            return nombre + " (Def +" + valorEfecto + ") - " + precio + " Oro";
        } else if (tipo.equals("PocionVida")) {
            return nombre + " (Cura " + valorEfecto + " HP) - " + precio + " Oro";
        }
        return nombre + " (" + tipo + ": " + valorEfecto + ") - " + precio + " Oro";
    }
    // Método para clonar un ítem
    public Item clonar() {
    // ¡ESTE ORDEN DEBE COINCIDIR EXACTAMENTE CON EL ORDEN DEL CONSTRUCTOR PRINCIPAL!
    // Constructor: (nombre, tipo, valorEfecto, descripcion, precio)
    return new Item(this.nombre, this.tipo, this.valorEfecto, this.descripcion, this.precio);
    }

    
    
      @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Si es la misma instancia, son iguales
        if (o == null || getClass() != o.getClass()) return false; // Si nulo o de diferente clase, no son iguales
        Item item = (Item) o; // Castear a Item
        // Define la igualdad basada en las propiedades que hacen que un ítem sea único
        // Generalmente, el nombre y el tipo son suficientes para identificar un ítem de forma única
        return valorEfecto == item.valorEfecto &&
               precio == item.precio &&
               Objects.equals(nombre, item.nombre) &&
               Objects.equals(tipo, item.tipo) &&
               Objects.equals(descripcion, item.descripcion);
        // Podrías simplificar a solo nombre y tipo si esos son únicos para tus ítems:
        // return Objects.equals(nombre, item.nombre) && Objects.equals(tipo, item.tipo);
    }

    @Override
    public int hashCode() {
        // Genera un hash basado en las mismas propiedades usadas en equals
        return Objects.hash(nombre, tipo, valorEfecto, descripcion, precio);
        // O si simplificaste equals a nombre y tipo:
        // return Objects.hash(nombre, tipo);
    }
}