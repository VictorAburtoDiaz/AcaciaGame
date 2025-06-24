package interfaz;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO; // Para cargar la imagen
import java.io.File; // Para manejar rutas de archivo

public class FondoPanel extends JPanel {
    private Image backgroundImage;

    // Constructor que carga la imagen
    public FondoPanel(String imagePath) {
        try {
            // Carga la imagen. Usa ClassLoader para que funcione si lo empaquetas en un JAR.
            // Asegúrate de que la ruta sea relativa a la raíz del classpath (normalmente src/)
            backgroundImage = ImageIO.read(getClass().getResource(imagePath));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + imagePath);
            e.printStackTrace();
            // Opcional: mostrar un mensaje al usuario o usar un fondo de color si la imagen falla
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Asegura que el fondo del componente se dibuje primero
        if (backgroundImage != null) {
            // Dibuja la imagen para que llene todo el panel
            // Escala la imagen para que se ajuste al tamaño actual del panel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}