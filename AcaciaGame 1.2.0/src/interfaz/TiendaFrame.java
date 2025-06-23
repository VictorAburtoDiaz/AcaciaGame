// src/interfaz/TiendaFrame.java
package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import modelo.Acacia;
import modelo.Item;
import gestion.GestorItems;
import modelo.Usuario;

public class TiendaFrame extends JFrame {
    private Usuario usuarioLogueado;
    private Acacia acacia; // La instancia de Acacia que está comprando
    private GestorItems gestorItems;
    private JList<Item> listaItems; // Para mostrar los ítems disponibles
    private DefaultListModel<Item> modeloListaItems; // Modelo para JList
    private JLabel labelOro; // Para mostrar el oro actual de Acacia
    private JButton botonComprar;
    private JButton botonContinuar;

    // Constructor que recibe la instancia de Acacia
    public TiendaFrame(Acacia acacia, Usuario usuario) {
        super("Tienda de Artículos - Juego de Acacia");
        this.acacia = acacia;
        this.usuarioLogueado = usuario;
        this.gestorItems = new GestorItems(); // Inicializamos el gestor de ítems

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra esta ventana
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Panel Superior: Oro de Acacia ---
        JPanel panelOro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelOro = new JLabel("Oro de Acacia: " + acacia.getOro() + " monedas");
        labelOro.setFont(new Font("Arial", Font.BOLD, 14));
        panelOro.add(labelOro);
        panelPrincipal.add(panelOro, BorderLayout.NORTH);

        // --- Panel Central: Lista de Ítems ---
        modeloListaItems = new DefaultListModel<>();
        for (Item item : gestorItems.getItemsDisponibles()) {
            modeloListaItems.addElement(item);
        }
        listaItems = new JList<>(modeloListaItems);
        listaItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaItems.setLayoutOrientation(JList.VERTICAL);
        JScrollPane scrollPane = new JScrollPane(listaItems);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // --- Panel Inferior: Botones de Acción ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botonComprar = new JButton("Comprar Ítem");
        botonContinuar = new JButton("Continuar al Juego");

        panelBotones.add(botonComprar);
        panelBotones.add(botonContinuar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // --- Listeners ---
        botonComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item itemSeleccionado = listaItems.getSelectedValue();
                if (itemSeleccionado != null) {
                    // ¡MODIFICACIÓN CLAVE AQUÍ!
                    // Creamos una copia del ítem seleccionado para que Acacia tenga su propia instancia.
                    // Esto es crucial para que cada ítem en el inventario de Acacia sea independiente
                    // y no se modifique si la "plantilla" en GestorItems cambia, o si múltiples ítems
                    // del mismo tipo se compran.
                    Item itemParaAcacia = itemSeleccionado.clonar();

                    if (acacia.comprarItem(itemParaAcacia)) {
                        labelOro.setText("Oro de Acacia: " + acacia.getOro() + " monedas");
                        JOptionPane.showMessageDialog(TiendaFrame.this,
                                itemSeleccionado.getNombre() + " comprado con éxito!",
                                "Compra Exitosa",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(TiendaFrame.this,
                                "No tienes suficiente oro para comprar " + itemSeleccionado.getNombre() + ".",
                                "Oro Insuficiente",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(TiendaFrame.this,
                            "Por favor, selecciona un ítem para comprar.",
                            "Ningún Ítem Seleccionado",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        botonContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                JOptionPane.showMessageDialog(TiendaFrame.this,
                        "¡Listo para la aventura! Volviendo a la partida...",
                        "Volver al Juego",
                        JOptionPane.INFORMATION_MESSAGE);
                
               
                
                // --- MODIFICACIÓN: NO CREAR UN NUEVO JuegoFrame ---
                // Simplemente cierra esta ventana de la tienda.
                // La JuegoFrame original (que deshabilitamos) tiene un WindowListener
                // esperando este evento de cierre para reactivarse y actualizarse.
               dispose(); 
            }
        });
    }
}