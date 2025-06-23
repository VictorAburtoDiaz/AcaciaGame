// src/gestion/GestorPartidas.java
package gestion;

import modelo.Acacia;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File; // Para manejar la ruta del archivo

public class GestorPartidas {
    private static final String NOMBRE_ARCHIVO_PARTIDA = "partida_acacia.ser"; // Archivo para guardar

    /**
     * Guarda la instancia de Acacia en un archivo.
     * @param acacia La instancia de Acacia a guardar.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    public boolean guardarPartida(Acacia acacia) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO_PARTIDA))) {
            oos.writeObject(acacia);
            System.out.println("DEBUG: Partida guardada exitosamente en " + NOMBRE_ARCHIVO_PARTIDA);
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo guardar la partida: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga una instancia de Acacia desde un archivo.
     * @return La instancia de Acacia cargada, o null si no se pudo cargar.
     */
    public Acacia cargarPartida() {
        File archivo = new File(NOMBRE_ARCHIVO_PARTIDA);
        if (!archivo.exists()) {
            System.out.println("DEBUG: No se encontró un archivo de partida guardada (" + NOMBRE_ARCHIVO_PARTIDA + ").");
            return null; // No hay partida para cargar
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO_PARTIDA))) {
            Acacia acaciaCargada = (Acacia) ois.readObject();
            System.out.println("DEBUG: Partida cargada exitosamente desde " + NOMBRE_ARCHIVO_PARTIDA);
            return acaciaCargada;
        } catch (Exception e) {
            System.err.println("ERROR: No se pudo cargar la partida: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}