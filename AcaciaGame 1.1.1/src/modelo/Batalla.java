// src/modelo/Batalla.java
package modelo;

public class Batalla {
    private Acacia acacia;
    private Raton raton;
    private StringBuilder logBatalla; // Para registrar lo que sucede en la batalla

    public Batalla(Acacia acacia, Raton raton) {
        this.acacia = acacia;
        this.raton = raton;
        this.logBatalla = new StringBuilder();
        log("¡Una batalla ha comenzado entre " + acacia.getNombre() + " y " + raton.getNombre() + "!");
    }

    private void log(String mensaje) {
        logBatalla.append(mensaje).append("\n");
        System.out.println(mensaje); // También imprimir en consola para depuración
    }

    public String getLogBatalla() {
        return logBatalla.toString();
    }

    /**
     * Simula un turno de ataque de Acacia.
     * @return true si el ratón fue derrotado, false en caso contrario.
     */
    public boolean turnoAcaciaAtaca() {
        if (!acacia.estaViva() || !raton.estaVivo()) {
            return false; // La batalla ya terminó
        }

        int danoAcacia = acacia.atacar();
        raton.recibirDano(danoAcacia);
        log(acacia.getNombre() + " ataca a " + raton.getNombre() + " y le inflige " + danoAcacia + " de daño.");

        if (!raton.estaViva()) {
            log(raton.getNombre() + " ha sido derrotado!");
            acacia.setOro(acacia.getOro() + raton.getRecompensaOro()); // Acacia gana oro
            log(acacia.getNombre() + " ganó " + raton.getRecompensaOro() + " oro.");
            return true; // Ratón derrotado
        }
        return false; // Batalla continúa
    }

    /**
     * Simula un turno de ataque del ratón.
     * @return true si Acacia fue derrotada, false en caso contrario.
     */
    public boolean turnoRatonAtaca() {
        if (!acacia.estaViva() || !raton.estaViva()) {
            return false; // La batalla ya terminó
        }

        int danoRaton = raton.atacar();
        acacia.recibirDano(danoRaton);
        log(raton.getNombre() + " ataca a " + acacia.getNombre() + " y le inflige " + danoRaton + " de daño.");

        if (!acacia.estaViva()) {
            log(acacia.getNombre() + " ha sido derrotada!");
            return true; // Acacia derrotada
        }
        return false; // Batalla continúa
    }

    /**
     * Simula el uso de un ítem por Acacia.
     * @param item El ítem que Acacia intenta usar.
     * @return true si el ítem fue usado, false en caso contrario.
     */
    
    public boolean turnoAcaciaUsaItem(Item item) {
        if (acacia.getInventario().contains(item)) { // Primero, verifica si lo tiene en el inventario
             if (item.getTipo().equals("PocionVida")) {
                 int curacion = item.getValorEfecto(); // <--- ¡OBTENER CURACIÓN DEL ÍTEM!
                 acacia.curar(curacion); // Usa el método curar de Acacia
                 acacia.getInventario().remove(item); // Eliminar el ítem del inventario después de usarlo
                 log(acacia.getNombre() + " usó " + item.getNombre() + " y se curó " + curacion + " puntos de vida.");
                 return true;
             } else if (item.getTipo().equals("Arma") || item.getTipo().equals("Armadura")) {
                 // Si es un arma o armadura, intentar equiparla
                 if (acacia.equiparItem(item)) { // Usar el nuevo método de Acacia
                     log(acacia.getNombre() + " equipó " + item.getNombre() + ".");
                     return true;
                 } else {
                     log("No se pudo equipar " + item.getNombre() + " en combate.");
                     return false;
                 }
             } else {
                 log("Acacia intentó usar " + item.getNombre() + ", pero no se sabe cómo usarlo en combate (aún).");
                 return false;
             }
        } else {
            log(acacia.getNombre() + " no tiene " + item.getNombre() + " en su inventario.");
            return false;
        }
    }
}