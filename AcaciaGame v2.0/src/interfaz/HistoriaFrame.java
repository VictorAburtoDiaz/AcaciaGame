package interfaz;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HistoriaFrame extends JFrame {

    private Usuario usuarioLogueado;

    public HistoriaFrame(Usuario usuario) {
        super("La Historia de Acacia");
        this.usuarioLogueado = usuario;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        FondoPanel panelFondo = new FondoPanel("/recursos/fondo_historia.png"); 
        panelFondo.setLayout(new BorderLayout(10, 10));
        panelFondo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título de la historia
        JLabel tituloHistoria = new JLabel("¡La Aventura de Acacia Comienza!", SwingConstants.CENTER);
        tituloHistoria.setFont(new Font("Serif", Font.BOLD, 26)); 
        tituloHistoria.setForeground(Color.WHITE); 
        panelFondo.add(tituloHistoria, BorderLayout.NORTH);

        // *** CAMBIOS AQUÍ: PANEL SEMITRANSPARENTE PARA EL TEXTO ***
        JPanel panelContenedorTexto = new JPanel(new BorderLayout()); // Un nuevo panel para contener el JScrollPane
        panelContenedorTexto.setBackground(new Color(0, 0, 0, 150)); // Fondo negro con 150 de opacidad (semitransparente)
        panelContenedorTexto.setOpaque(true); // Muy importante: hacerlo opaco para que el color de fondo se pinte

        JTextArea areaHistoria = new JTextArea();
        areaHistoria.setEditable(false);
        areaHistoria.setLineWrap(true);
        areaHistoria.setWrapStyleWord(true);
        areaHistoria.setText(
            "En el apacible hogar de su dueño, Acacia, una valiente y astuta gata, " +
            "disfrutaba de una vida llena de mimos y, lo más importante, de una dieta " +
            "exquisita gracias a la tarjeta de crédito de su humano.\n\n" +
            "Pero un día, ¡la tragedia golpeó! Una infame banda de ratones, conocida " +
            "por sus fechorías, se infiltró en la casa y, con una audacia nunca antes " +
            "vista, ¡robó la preciada tarjeta de crédito! Sin ella, la comida gourmet " +
            "de Acacia estaba en peligro.\n\n" +
            "Con el estómago rugiendo y su orgullo felino herido, Acacia ha jurado " +
            "recuperar lo que es suyo. Su misión: ¡infiltrarse en la guarida de los " +
            "ratones, derrotar a sus líderes y traer de vuelta la tarjeta de crédito " +
            "para asegurar su suministro ilimitado de deliciosa comida!"
        );
        areaHistoria.setFont(new Font("Arial", Font.PLAIN, 15));
        areaHistoria.setForeground(Color.WHITE); // Mantenemos el texto blanco
        areaHistoria.setOpaque(false); // El JTextArea sigue siendo transparente para que el fondo del JScrollPane/panelContenedorTexto se vea

        JScrollPane scrollPane = new JScrollPane(areaHistoria);
        scrollPane.setOpaque(false); // El JScrollPane debe ser transparente
        scrollPane.getViewport().setOpaque(false); // El viewport del JScrollPane también transparente
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Eliminar el borde del scrollpane para que se vea más limpio

        panelContenedorTexto.add(scrollPane, BorderLayout.CENTER); // Añadimos el scrollPane al nuevo panel contenedor
        panelFondo.add(panelContenedorTexto, BorderLayout.CENTER); // Añadimos el panel contenedor al panel de fondo
        // *** FIN DE LOS CAMBIOS PARA EL PANEL SEMITRANSPARENTE ***


        // Panel para el botón "Continuar"
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setOpaque(false); // Hacerlo transparente
        JButton botonContinuar = new JButton("¡Comenzar la Aventura!");
        botonContinuar.setFont(new Font("Arial", Font.BOLD, 18));
        botonContinuar.setBackground(new Color(60, 179, 113));
        botonContinuar.setForeground(Color.WHITE);
        botonContinuar.setFocusPainted(false);
        botonContinuar.setBorder(BorderFactory.createRaisedBevelBorder());

        botonContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuPrincipalFrame menuPrincipal = new MenuPrincipalFrame(usuarioLogueado);
                menuPrincipal.setVisible(true);
                dispose();
            }
        });
        panelBoton.add(botonContinuar);
        panelFondo.add(panelBoton, BorderLayout.SOUTH);

        add(panelFondo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Usuario testUser = new Usuario("Test", "password", "Jugador");
            new HistoriaFrame(testUser).setVisible(true);
        });
    }
}