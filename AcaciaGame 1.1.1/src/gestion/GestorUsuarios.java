// src/gestion/GestorUsuarios.java
package gestion;

import modelo.Usuario;
import java.util.HashMap;
import java.util.Map;

public class GestorUsuarios {
    private Map<String, Usuario> usuarios; // Usaremos un HashMap para almacenar usuarios por su nombre

    public GestorUsuarios() {
        usuarios = new HashMap<>();
        // Cargar usuarios predefinidos (por ahora, en memoria)
        cargarUsuariosIniciales();
    }

    private void cargarUsuariosIniciales() {
        // Usuario Administrador
        usuarios.put("admin", new Usuario("admin", "admin123", "Administrador"));
        // Usuario Jugador
        usuarios.put("jugador", new Usuario("jugador123", "pass", "Jugador"));
        usuarios.put("acacia", new Usuario("acacia", "gato", "Jugador")); // Otro jugador de ejemplo
    }

    public Usuario validarCredenciales(String nombreUsuario, String contrasena) {
        Usuario usuario = usuarios.get(nombreUsuario); // Intenta obtener el usuario por nombre
        if (usuario != null && usuario.verificarContrasena(contrasena)) {
            return usuario; // Credenciales válidas, devuelve el objeto Usuario
        }
        return null; // Credenciales inválidas
    }
}