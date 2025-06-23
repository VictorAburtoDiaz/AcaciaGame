// src/interfaz/MenuPrincipalFrame.java
package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gestion.GestorPartidas; // Se agrega el gestor de partidas guardadas.
import modelo.Usuario; // Necesitamos saber qué usuario inició sesión
import modelo.Acacia; //Este error lo tuve al compilar, ya que no encontraba a la acacia.


public class MenuPrincipalFrame extends JFrame {
    private Usuario usuarioLogueado; // Para guardar el usuario que inició sesión
    private GestorPartidas gestorPartidas; // Se agrega el gestor de partidas en el menu principal.
    
    public MenuPrincipalFrame(Usuario usuario) {
        super("Menú Principal - Juego de Acacia");
        this.usuarioLogueado = usuario; // Guardamos el usuario para usar su rol y nombre
        this.gestorPartidas = new GestorPartidas(); //Aca se inicia el gestor de partidas guardadas.

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10)); // 0 filas (se ajusta), 1 columna
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etiqueta de bienvenida
        JLabel bienvenidaLabel = new JLabel("Bienvenido, " + usuarioLogueado.getNombreUsuario() + "!");
        bienvenidaLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrar texto
        bienvenidaLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente más grande y negrita
        panel.add(bienvenidaLabel);

        // Botón "Jugar" (disponible para ambos)
        JButton botonJugar = new JButton("Iniciar Nueva Partida (Jugar)");
        panel.add(botonJugar);
        
        // Botón "Cargar Partida" (Nuevo)
        JButton botonCargarPartida = new JButton("Cargar Partida Guardada");
        panel.add(botonCargarPartida);

        // Botón "Guardar Partida" (Nuevo - solo para jugadores, o para admin si queremos)
        // Podríamos hacerlo condicional como el de Administrar, pero por simplicidad lo dejo para todos
        JButton botonGuardarPartida = new JButton("Guardar Partida Actual");
        panel.add(botonGuardarPartida);

        // Botón "Ver Historial" (disponible para ambos)
        JButton botonVerHistorial = new JButton("Ver Historial de Partidas");
        panel.add(botonVerHistorial);

        // Botón "Administrar" (solo para Administrador)
        if (usuarioLogueado.getRol().equals("Administrador")) {
            JButton botonAdministrar = new JButton("Administrar Historial");
            panel.add(botonAdministrar);

            // Listener para el botón Administrar
            botonAdministrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "Funcionalidad de Administración aquí.\n(Próximamente: Borrar Historial)",
                            "Administración",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Aquí se abriría el AdminFrame o la parte de administración del HistorialFrame
                }
            });
        }

        // Botón "Cerrar Sesión"
        JButton botonCerrarSesion = new JButton("Cerrar Sesión");
        panel.add(botonCerrarSesion);

        add(panel);

        // --- Listeners para los botones ---

        // Listener para el botón Jugar
        botonJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Crear una nueva instancia de Acacia para esta partida/run
                // Estos valores serán los iniciales para cada nueva partida.
                Acacia acaciaNuevaPartida = new Acacia("Acacia", 100, 15, 5, 100); // Nombre, VidaMax, Ataque, Defensa, OroInicial
                   // Aquí el cambio: pasar también usuarioLogueado a la TiendaFrame
                TiendaFrame tienda = new TiendaFrame(acaciaNuevaPartida, usuarioLogueado); // <--- MODIFICADO
                tienda.setVisible(true);
               
                // No cerramos el MenuPrincipalFrame inmediatamente,
                // ya que la tienda podría cerrarse y volver aquí,
                // o podríamos querer que el menú esté detrás.
                // Si prefieres cerrarlo al abrir la tienda:
                //Acabo de activar el dispose para que no se vea nada atras... a ver como queda.
                 dispose();
            }
        });
        
        //Listener para cargar las partidas.
         botonCargarPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Acacia acaciaCargada = gestorPartidas.cargarPartida();
                if (acaciaCargada != null) {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "Partida cargada exitosamente para " + acaciaCargada.getNombre() + ".",
                            "Cargar Partida",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Abrir la Tienda (o directamente el JuegoFrame si no quieres pasar por la tienda de nuevo)
                    // Pasamos la Acacia cargada y el usuario actual
                    TiendaFrame tienda = new TiendaFrame(acaciaCargada, usuarioLogueado);
                    tienda.setVisible(true);
                    dispose(); // Cerrar el menú principal
                } else {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "No se encontró una partida guardada.",
                            "Cargar Partida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        botonGuardarPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Para guardar, necesitamos la instancia de Acacia que está jugando.
                // Esto es un desafío: ¿qué Acacia guardar si no hay partida activa?
                // Por ahora, solo permitiremos guardar una partida *que ya se haya iniciado*.
                // Esto significa que el botón "Guardar" debería estar en el JuegoFrame,
                // o que necesitamos un "estado" de Acacia actual en MenuPrincipalFrame.

                // Para esta implementación inicial, vamos a simular que si el usuario
                // hace clic en "Guardar", se guarda la *última* Acacia jugada
                // o una Acacia por defecto. Esto es un placeholder.
                // La lógica real sería:
                // 1. Obtener la Acacia actual del JuegoFrame (cuando el jugador esté en partida)
                // 2. O solo permitir guardar cuando se sale de una partida exitosamente.

                // *** Placeholder SIMPLE: Podríamos guardar la Acacia por defecto para probar. ***
                
                Acacia acaciaParaGuardar = new Acacia("Acacia", 100, 15, 5, 100); // Crea una Acacia temporal para guardar
                if (gestorPartidas.guardarPartida(acaciaParaGuardar)) {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "Partida guardada exitosamente (Acacia por defecto).",
                            "Guardar Partida",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "Error al guardar la partida.",
                            "Guardar Partida",
                            JOptionPane.ERROR_MESSAGE);
                }
                // *** NOTA: La lógica de guardar en tiempo real es más compleja y se haría en JuegoFrame. ***
                // Esto es solo para que el botón funcione y podamos probar la serialización.
            }
        });

        
        
        // Listener para el botón Ver Historial
        botonVerHistorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                        "Mostrando el historial de partidas.\n(Próximamente: HistorialFrame)",
                        "Historial",
                        JOptionPane.INFORMATION_MESSAGE);
                // Aquí se abriría el HistorialFrame
            }
        });

        // Listener para el botón Cerrar Sesión
        botonCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre de nuevo la ventana de Login
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose(); // Cierra la ventana actual del menú principal
            }
        });
    }
}