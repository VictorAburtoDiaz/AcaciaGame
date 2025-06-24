package modelo;

import java.io.Serializable;
import java.time.LocalDateTime; // Para la fecha y hora
import java.time.format.DateTimeFormatter; // Para formatear la fecha

public class RunHistorial implements Serializable {
    private static final long serialVersionUID = 1L; // Es buena práctica para Serializable

    private LocalDateTime fechaHora;
    private String resultado; // Ej: "Victoria", "Derrota"
    private int nivelAlcanzado;
    private int oroGanado; // Oro total al final de la run
    private int enemigosDerrotados;
    private String nombreUsuario; // Para saber quién jugó esta run

    public RunHistorial(String nombreUsuario, String resultado, int nivelAlcanzado, int oroGanado, int enemigosDerrotados) {
        this.fechaHora = LocalDateTime.now(); // Captura la fecha y hora actual
        this.nombreUsuario = nombreUsuario;
        this.resultado = resultado;
        this.nivelAlcanzado = nivelAlcanzado;
        this.oroGanado = oroGanado;
        this.enemigosDerrotados = enemigosDerrotados;
    }

    // --- Getters ---
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getFechaHoraFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return fechaHora.format(formatter);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getResultado() {
        return resultado;
    }

    public int getNivelAlcanzado() {
        return nivelAlcanzado;
    }

    public int getOroGanado() {
        return oroGanado;
    }

    public int getEnemigosDerrotados() {
        return enemigosDerrotados;
    }

    @Override
    public String toString() {
        return "Fecha: " + getFechaHoraFormateada() +
               " | Jugador: " + nombreUsuario +
               " | Resultado: " + resultado +
               " | Nivel: " + nivelAlcanzado +
               " | Oro: " + oroGanado +
               " | Enemigos: " + enemigosDerrotados;
    }
}