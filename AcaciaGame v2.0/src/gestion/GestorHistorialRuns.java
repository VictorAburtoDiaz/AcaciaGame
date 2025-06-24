package gestion;

import modelo.RunHistorial;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorHistorialRuns {
    private static final String ARCHIVO_HISTORIAL = "historial_runs.ser"; // Archivo para el historial

    private List<RunHistorial> historial;

    public GestorHistorialRuns() {
        this.historial = cargarHistorial(); // Intenta cargar el historial al inicializar
        if (this.historial == null) {
            this.historial = new ArrayList<>(); // Si no se pudo cargar, crea uno nuevo
        }
    }

    /**
     * Añade una nueva run al historial y guarda el historial actualizado.
     * @param run La instancia de RunHistorial a añadir.
     */
    public void añadirRun(RunHistorial run) {
        this.historial.add(run);
        guardarHistorial(); // Guarda cada vez que se añade una nueva run
        System.out.println("DEBUG: Run añadida al historial: " + run.getFechaHoraFormateada());
    }

    /**
     * Guarda la lista completa del historial en un archivo.
     */
    public boolean guardarHistorial() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_HISTORIAL))) {
            oos.writeObject(historial);
            System.out.println("DEBUG: Historial de runs guardado exitosamente en " + ARCHIVO_HISTORIAL);
            return true;
        } catch (IOException e) {
            System.err.println("ERROR: Error al guardar el historial de runs: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga la lista completa del historial desde un archivo.
     * @return La lista de RunHistorial cargada, o una lista vacía si no existe el archivo o hubo un error.
     */
    @SuppressWarnings("unchecked") // Suprimimos el warning de casting no seguro
    public List<RunHistorial> cargarHistorial() {
        File archivo = new File(ARCHIVO_HISTORIAL);
        if (!archivo.exists()) {
            System.out.println("DEBUG: No se encontró un archivo de historial de runs (" + ARCHIVO_HISTORIAL + ").");
            return new ArrayList<>(); // Retorna lista vacía si no existe
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_HISTORIAL))) {
            List<RunHistorial> historialCargado = (List<RunHistorial>) ois.readObject();
            System.out.println("DEBUG: Historial de runs cargado exitosamente desde " + ARCHIVO_HISTORIAL);
            return historialCargado;
        } catch (IOException e) {
            System.err.println("ERROR: Error de E/S al cargar el historial de runs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retorna lista vacía en caso de error
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Clase no encontrada al cargar el historial de runs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retorna lista vacía en caso de error
        }
    }

    /**
     * Obtiene una copia del historial actual.
     * @return Una lista inmutable del historial de runs.
     */
    public List<RunHistorial> getHistorial() {
        // Retorna una copia para evitar modificaciones externas directas a la lista original
        return new ArrayList<>(historial);
    }

    /**
     * Elimina una run específica del historial por su índice.
     * @param index El índice de la run a eliminar.
     * @return true si se eliminó con éxito, false si el índice está fuera de rango.
     */
    public boolean eliminarRun(int index) {
        if (index >= 0 && index < historial.size()) {
            RunHistorial runEliminada = historial.remove(index);
            guardarHistorial(); // Guarda el historial después de eliminar
            System.out.println("DEBUG: Run eliminada del historial: " + runEliminada.getFechaHoraFormateada());
            return true;
        }
        System.err.println("ERROR: Índice fuera de rango al intentar eliminar run: " + index);
        return false;
    }
    
    /**
     * Elimina todo el historial de runs.
     * Esto también elimina el archivo físico.
     */
    public boolean limpiarHistorialCompleto() {
        this.historial.clear(); // Limpia la lista en memoria
        File archivo = new File(ARCHIVO_HISTORIAL);
        if (archivo.exists()) {
            if (archivo.delete()) {
                System.out.println("DEBUG: Archivo de historial de runs eliminado completamente.");
                return true;
            } else {
                System.err.println("ERROR: No se pudo eliminar el archivo de historial de runs.");
                return false;
            }
        }
        System.out.println("DEBUG: No había archivo de historial para limpiar.");
        return true; // Si no existe, consideramos que ya está limpio
    }
}