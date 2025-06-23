// src/gestion/GestorItems.java
package gestion;

import modelo.Item;
import java.util.ArrayList;
import java.util.List;

public class GestorItems {
    private List<Item> itemsDisponibles;

    public GestorItems() {
        itemsDisponibles = new ArrayList<>();
        cargarItemsIniciales();
    }

    private void cargarItemsIniciales() {
        // Constructor de Item: public Item(String nombre, String tipo, int valorEfecto, String descripcion, int precio)
        //                                      (1)       (2)         (3)          (4)         (5)

        // Pociones (Nombre, Tipo, ValorEfecto, Descripción, Precio)
        itemsDisponibles.add(new Item("Poción de Vida Pequeña", "PocionVida", 20, "Restaura 20 de vida.", 15));
        itemsDisponibles.add(new Item("Poción de Vida Mediana", "PocionVida", 50, "Restaura 50 de vida.", 40));
        
        // Armas (Nombre, Tipo, ValorEfecto = bonificación de ataque, Descripción, Precio)
        itemsDisponibles.add(new Item("Garras Afiladas", "Arma", 5, "Aumenta un poco tu ataque.", 30));
        itemsDisponibles.add(new Item("Collar de Púas", "Arma", 15, "Aumenta significativamente tu ataque.", 75));
        
        // Armaduras (Nombre, Tipo, ValorEfecto = bonificación de defensa, Descripción, Precio)
        itemsDisponibles.add(new Item("Vendaje Básico", "Armadura", 3, "Proporciona un poco de defensa.", 20));
        itemsDisponibles.add(new Item("Armadura de Latas", "Armadura", 10, "Una resistente armadura improvisada.", 60));
        
        // Ítems especiales (Nombre, Tipo, ValorEfecto, Descripción, Precio)
        itemsDisponibles.add(new Item("Amuleto de Suerte", "Generico", 0, "Podría traerte buena fortuna.", 100));
    }

    public List<Item> getItemsDisponibles() {
        return new ArrayList<>(itemsDisponibles);
    }
}