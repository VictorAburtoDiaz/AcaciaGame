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
        // Pociones
        itemsDisponibles.add(new Item("Poción de Vida Pequeña", "Restaura 20 de vida.", 15, "PocionVida"));
        itemsDisponibles.add(new Item("Poción de Vida Mediana", "Restaura 50 de vida.", 40, "PocionVida"));
        
        // Armas (ej. mejoran el ataque)
        itemsDisponibles.add(new Item("Garras Afiladas", "Aumenta un poco tu ataque.", 30, "Arma"));
        itemsDisponibles.add(new Item("Collar de Púas", "Aumenta significativamente tu ataque.", 75, "Arma"));
        
        // Armaduras (ej. mejoran la defensa)
        itemsDisponibles.add(new Item("Vendaje Básico", "Proporciona un poco de defensa.", 20, "Armadura"));
        itemsDisponibles.add(new Item("Armadura de Latas", "Una resistente armadura improvisada.", 60, "Armadura"));
        
        // Ítems especiales (quizás con efectos únicos, por ahora genéricos)
        itemsDisponibles.add(new Item("Amuleto de Suerte", "Podría traerte buena fortuna.", 100, "Generico"));
    }

    public List<Item> getItemsDisponibles() {
        return new ArrayList<>(itemsDisponibles); // Devolvemos una copia para evitar modificaciones externas directas
    }

    // Puedes añadir métodos para encontrar ítems por nombre, etc. si lo necesitas más adelante.
}