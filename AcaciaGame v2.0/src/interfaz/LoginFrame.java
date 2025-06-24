// src/interfaz/LoginFrame.java
package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gestion.GestorUsuarios; // Importamos nuestro gestor de usuarios
import modelo.Usuario;       // Importamos la clase Usuario
import interfaz.MenuPrincipalFrame;
import interfaz.HistoriaFrame;

public class LoginFrame extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonLogin;
    private JLabel mensajeError; // Para mostrar mensajes de error

    private GestorUsuarios gestorUsuarios; // Instancia del gestor de usuarios

    public LoginFrame() {
        super("Login - Juego de Acacia"); // Título de la ventana

        gestorUsuarios = new GestorUsuarios(); // Inicializamos el gestor de usuarios

        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setResizable(false); // No permite redimensionar la ventana

        // Panel principal para organizar los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // 4 filas, 1 columna, con espacios
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen interno

        // Componentes
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        campoUsuario = new JTextField(15);
        JLabel etiquetaContrasena = new JLabel("Contraseña:");
        campoContrasena = new JPasswordField(15);
        botonLogin = new JButton("Iniciar Sesión");
        mensajeError = new JLabel(""); // Etiqueta vacía para mensajes de error
        mensajeError.setForeground(Color.RED); // Texto rojo para errores

        // Añadir componentes al panel
        panel.add(etiquetaUsuario);
        panel.add(campoUsuario);
        panel.add(etiquetaContrasena);
        panel.add(campoContrasena);
        panel.add(botonLogin);
        panel.add(mensajeError);

        // Añadir el panel a la ventana
        add(panel);

        // --- Manejo del evento del botón ---
        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuarioIngresado = campoUsuario.getText();
                String contrasenaIngresada = new String(campoContrasena.getPassword()); // Importante: obtener como String

                Usuario usuarioValidado = gestorUsuarios.validarCredenciales(usuarioIngresado, contrasenaIngresada);

                if (usuarioValidado != null) {
                    // Credenciales correctas
                        mensajeError.setText("");

                            System.out.println("DEBUG: Usuario validado: " + usuarioValidado.getNombreUsuario() + " (Rol: " + usuarioValidado.getRol() + ")");

                            // *** ESTA ES LA LÍNEA CLAVE QUE DEBES AGREGAR/MODIFICAR ***
                            
                        HistoriaFrame historiaFrame = new HistoriaFrame(usuarioValidado);
                        historiaFrame.setVisible(true);
                        
                        
                 

                         dispose(); // Cierra la ventana de Login
                        System.out.println("DEBUG: LoginFrame cerrado.");

                        } else {
                            // Credenciales incorrectas
                             mensajeError.setText("Usuario o contraseña incorrectos.");
                         System.out.println("DEBUG: Intento de login fallido para usuario: " + usuarioIngresado);
                        }
            }
        });
    }

    public static void main(String[] args) {
        // Aseguramos que la GUI se ejecute en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}