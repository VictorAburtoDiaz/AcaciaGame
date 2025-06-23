// src/modelo/Item.java
package modelo;


import java.io.Serializable; //recordar que esto es para poder guardar partidas, serializando lo que son los items tambien.

public class Item implements Serializable  {
    private String nombre;
    private String descripcion;
    private int precio; // Precio para comprar en la tienda
    private String tipo; // "Pocion", "Arma", "Armadura", "Generico"

    public Item(String nombre, String descripcion, int precio, String tipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.tipo = tipo;
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

    @Override
    public String toString() {
        return nombre + " (" + precio + " oro)";
    }
}