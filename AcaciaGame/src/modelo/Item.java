// src/modelo/Item.java
package modelo;

public class Item {
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