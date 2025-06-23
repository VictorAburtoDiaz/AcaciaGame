
package modelo;

// src/modelo/Usuario.java


public class Usuario {
    private final String nombreUsuario;
    private final String contrasena;
    private final String rol; // Podría ser "Administrador" o "Jugador"

    public Usuario(String nombreUsuario, String contrasena, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    // Método para verificar la contraseña
    public boolean verificarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }
}
