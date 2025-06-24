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
        itemsDisponibles.add(new Item("Botellita de leche", "PocionVida", 20, "Restaura 20 de vida y te calma la sed.", 15));
        itemsDisponibles.add(new Item("Lata de atún", "PocionVida", 50, "Restaura 50 de vida.", 40));
        
        // Armas (Nombre, Tipo, ValorEfecto = bonificación de ataque, Descripción, Precio)
        itemsDisponibles.add(new Item("Garras Afiladas", "Arma", 10, "Aumenta un poco tu ataque.", 30));
        itemsDisponibles.add(new Item("Collar de Púas", "Arma", 25, "Aumenta significativamente tu ataque.", 125));
        
        // Armaduras (Nombre, Tipo, ValorEfecto = bonificación de defensa, Descripción, Precio)
        itemsDisponibles.add(new Item("Ropa para el frío", "Armadura", 10, "Proporciona un poco de defensa.", 50));
        itemsDisponibles.add(new Item("Armadura de Latas de atún", "Armadura", 20, "Una resistente armadura improvisada.", 150));
        
        // Ítems especiales (Nombre, Tipo, ValorEfecto, Descripción, Precio)
        itemsDisponibles.add(new Item("Foto de tu dueño", "Generico", 0, "Podría traerte buena fortuna.", 100));
    }

    public List<Item> getItemsDisponibles() {
        return new ArrayList<>(itemsDisponibles);
    }
}