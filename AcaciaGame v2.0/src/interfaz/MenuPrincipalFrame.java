// src/interfaz/MenuPrincipalFrame.java
package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gestion.GestorPartidas;
import modelo.Usuario;
import modelo.Acacia;
import gestion.GestorHistorialRuns; // ¡IMPORTANTE: Añade esta importación!


public class MenuPrincipalFrame extends JFrame {
    private Usuario usuarioLogueado;
    private GestorPartidas gestorPartidas;
    // No necesitamos instanciar GestorHistorialRuns aquí, HistorialRunsFrame lo hará.

    public MenuPrincipalFrame(Usuario usuario) {
        super("Menú Principal - Juego de Acacia");
        this.usuarioLogueado = usuario;
        this.gestorPartidas = new GestorPartidas();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350); // Aumentar un poco el tamaño para más botones
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10)); // 0 filas (se ajusta), 1 columna
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Etiqueta de bienvenida
        JLabel bienvenidaLabel = new JLabel("Bienvenido, " + usuarioLogueado.getNombreUsuario() + "!");
        bienvenidaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bienvenidaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(bienvenidaLabel);

        // Botón "Jugar" (disponible para ambos)
        JButton botonJugar = new JButton("Iniciar Nueva Partida");
        panel.add(botonJugar);
        
        // Botón "Cargar Partida"
        JButton botonCargarPartida = new JButton("Cargar Partida Guardada");
        panel.add(botonCargarPartida);

        // Botón "Ver Historial" (disponible para todos)
        JButton botonVerHistorial = new JButton("Ver Historial de Aventuras");
        panel.add(botonVerHistorial);

        // Botón "Administrar Historial" (solo para Administrador)
            if (usuarioLogueado.getRol().equals("Administrador")) {
            JButton botonAdministrarHistorial = new JButton("Administrar Historial");
            panel.add(botonAdministrarHistorial);

        // Listener para el botón Administrar Historial
            botonAdministrarHistorial.addActionListener(new ActionListener() {
         @Override
        public void actionPerformed(ActionEvent e) {
            // Abre el HistorialRunsFrame para administradores (con opciones de eliminación)
            HistorialRunsFrame historialFrame = new HistorialRunsFrame(true); // <--- CAMBIO AQUÍ: ¡Se pasa 'true'!
            historialFrame.setVisible(true);
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
                Acacia acaciaNuevaPartida = new Acacia("Acacia", 100, 15, 5, 100);
                JuegoFrame juegoFrame = new JuegoFrame(acaciaNuevaPartida, usuarioLogueado);
                juegoFrame.setVisible(true);
                dispose();
            }
        });
        
        // Listener para cargar las partidas.
        botonCargarPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Acacia acaciaCargada = gestorPartidas.cargarPartida();
                if (acaciaCargada != null) {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "Partida cargada exitosamente para " + acaciaCargada.getNombre() + ".",
                            "Cargar Partida",
                            JOptionPane.INFORMATION_MESSAGE);
                    JuegoFrame juegoFrame = new JuegoFrame(acaciaCargada, usuarioLogueado);
                    juegoFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(MenuPrincipalFrame.this,
                            "No se encontró una partida guardada.",
                            "Cargar Partida",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

            // Listener para el botón Ver Historial (AHORA ABRE EL FRAME DE HISTORIAL)
            botonVerHistorial.addActionListener(new ActionListener() {
    @Override
            public void actionPerformed(ActionEvent e) {
             // Abre el HistorialRunsFrame para todos los usuarios (solo ver)
            HistorialRunsFrame historialFrame = new HistorialRunsFrame(false); // <--- CAMBIO AQUÍ: ¡Se pasa 'false'!
            historialFrame.setVisible(true);
            }
         });

        // Listener para el botón Cerrar Sesión
        botonCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
    }
}