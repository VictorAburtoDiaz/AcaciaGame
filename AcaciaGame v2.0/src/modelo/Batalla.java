// src/modelo/Batalla.java
package modelo;

import java.io.Serializable;

public class Batalla implements Serializable {
    private Acacia acacia;
    private Raton raton;
    private StringBuilder logBatalla; // Usamos StringBuilder para construir el log de manera eficiente

    public Batalla(Acacia acacia, Raton raton) {
        this.acacia = acacia;
        this.raton = raton;
        this.logBatalla = new StringBuilder(); // Inicializamos el StringBuilder
        agregarMensajeLog("¡Una nueva batalla comienza entre " + acacia.getNombre() + " y " + raton.getNombre() + "!");
    }

    public Acacia getAcacia() {
        return acacia;
    }

    public Raton getRaton() {
        return raton;
    }

    // Nuevo método para añadir mensajes al log
    public void agregarMensajeLog(String mensaje) {
        this.logBatalla.append(mensaje).append("\n");
    }

    public String getLogBatalla() {
        return logBatalla.toString();
    }

    // --- NUEVO MÉTODO: Limpia el log de la batalla ---
    public void limpiarLog() {
        this.logBatalla = new StringBuilder(); // Crea un nuevo StringBuilder, borrando el anterior.
        // Opcional: Podrías añadir un mensaje inicial aquí si quieres que el log siempre empiece con algo.
        // Por ejemplo: agregarMensajeLog("Preparando nueva fase...");
    }

    /**
     * Turno de Acacia: ataca al ratón.
     * @return true si el ratón fue derrotado, false en caso contrario.
     */
    public boolean turnoAcaciaAtaca() {
        if (!acacia.estaViva() || !raton.estaViva()) {
            return false; // La batalla ya terminó
        }
        int danoAcacia = acacia.atacar();
        raton.recibirDano(danoAcacia);
        agregarMensajeLog(acacia.getNombre() + " ataca a " + raton.getNombre() + " causando " + danoAcacia + " de daño.");
        return !raton.estaViva();
    }

    /**
     * Turno de Acacia: usa un ítem.
     * @param item El ítem que Acacia intenta usar.
     * @return true si el ítem fue usado exitosamente, false en caso contrario.
     */
    public boolean turnoAcaciaUsaItem(Item item) {
        if (!acacia.estaViva() || !raton.estaViva()) {
            return false; // La batalla ya terminó
        }

        boolean usado = acacia.usarItem(item);
        if (usado) {
            // Si el método 'usarItem' de Acacia ya añade un mensaje al log, no dupliques.
            // Si no, puedes añadir uno aquí, por ejemplo:
            // agregarMensajeLog(acacia.getNombre() + " usó " + item.getNombre() + " y " + item.getDescripcion() + ".");
        } else {
            agregarMensajeLog("No se pudo usar " + item.getNombre() + ".");
        }
        return usado;
    }

    /**
     * Turno del ratón: ataca a Acacia.
     * @return true si Acacia fue derrotada, false en caso contrario.
     */
    public boolean turnoRatonAtaca() {
        if (!acacia.estaViva() || !raton.estaViva()) {
            return false; // La batalla ya terminó
        }
        int danoRaton = raton.atacar();
        acacia.recibirDano(danoRaton);
        agregarMensajeLog(raton.getNombre() + " ataca a " + acacia.getNombre() + " causando " + danoRaton + " de daño.");
        return !acacia.estaViva();
    }
}