// src/interfaz/MenuPrincipalFrame.java
package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.Usuario; // Necesitamos saber qué usuario inició sesión
import modelo.Acacia; //Este error lo tuve al compilar, ya que no encontraba a la acacia.

public class MenuPrincipalFrame extends JFrame {
    private Usuario usuarioLogueado; // Para guardar el usuario que inició sesión

    public MenuPrincipalFrame(Usuario usuario) {
        super("Menú Principal - Juego de Acacia");
        this.usuarioLogueado = usuario; // Guardamos el usuario para usar su rol y nombre

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