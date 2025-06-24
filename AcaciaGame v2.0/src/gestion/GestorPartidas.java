// src/gestion/GestorPartidas.java
package gestion;

import modelo.Acacia;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File; // Para manejar la ruta del archivo
import java.io.IOException; // Importar específicamente IOException
import java.lang.ClassNotFoundException; // Importar específicamente ClassNotFoundException

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
        } catch (IOException e) { // Cambiado de 'Exception' a 'IOException'
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
        } catch (IOException e) { // Cambiado de 'Exception' a 'IOException'
            System.err.println("ERROR: Error de E/S al cargar la partida: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) { // Agregado para manejar si la clase no se encuentra
            System.err.println("ERROR: Clase no encontrada al cargar la partida. Esto puede ocurrir si la definición de Acacia o sus atributos ha cambiado desde que se guardó: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina el archivo de la partida guardada.
     * @return true si el archivo fue eliminado exitosamente o no existía, false si hubo un error al borrar.
     */
    public boolean eliminarPartidaGuardada() {
        File archivo = new File(NOMBRE_ARCHIVO_PARTIDA);
        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println("DEBUG: Partida guardada (" + NOMBRE_ARCHIVO_PARTIDA + ") eliminada exitosamente.");
                return true;
            } else {
                System.err.println("ERROR: No se pudo eliminar la partida guardada (" + NOMBRE_ARCHIVO_PARTIDA + ").");
                return false;
            }
        } else {
            System.out.println("DEBUG: No hay partida guardada para eliminar (" + NOMBRE_ARCHIVO_PARTIDA + ").");
            return true; // Se considera un éxito si no hay nada que borrar
        }
    }
}