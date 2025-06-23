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
        // Pociones (Nombre, Descripción, Precio, Tipo, ValorEfecto)
        itemsDisponibles.add(new Item("Poción de Vida Pequeña", "Restaura 20 de vida.", 15, "PocionVida", 20)); // <--- VALOR EFECTO
        itemsDisponibles.add(new Item("Poción de Vida Mediana", "Restaura 50 de vida.", 40, "PocionVida", 50)); // <--- VALOR EFECTO
        
        // Armas (Nombre, Descripción, Precio, Tipo, ValorEfecto = bonificación de ataque)
        itemsDisponibles.add(new Item("Garras Afiladas", "Aumenta un poco tu ataque.", 30, "Arma", 5)); // <--- VALOR EFECTO
        itemsDisponibles.add(new Item("Collar de Púas", "Aumenta significativamente tu ataque.", 75, "Arma", 15)); // <--- VALOR EFECTO
        
        // Armaduras (Nombre, Descripción, Precio, Tipo, ValorEfecto = bonificación de defensa)
        itemsDisponibles.add(new Item("Vendaje Básico", "Proporciona un poco de defensa.", 20, "Armadura", 3)); // <--- VALOR EFECTO
        itemsDisponibles.add(new Item("Armadura de Latas", "Una resistente armadura improvisada.", 60, "Armadura", 10)); // <--- VALOR EFECTO
        
        // Ítems especiales (genéricos, su efecto se implementará más tarde si es necesario)
        itemsDisponibles.add(new Item("Amuleto de Suerte", "Podría traerte buena fortuna.", 100, "Generico", 0)); // <--- VALOR EFECTO
    }

    public List<Item> getItemsDisponibles() {
        return new ArrayList<>(itemsDisponibles); // Devolvemos una copia para evitar modificaciones externas directas
    }

    // Puedes añadir métodos para encontrar ítems por nombre, etc. si lo necesitas más adelante.
}