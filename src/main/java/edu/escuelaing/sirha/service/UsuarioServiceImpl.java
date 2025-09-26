package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Usuario;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public Optional<Usuario> autenticar(String username, String password) {
        return usuarios.values().stream().filter(u -> username.equals(u.getUsername()) && password.equals(u.getPasswordHash())).findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarios.values().stream().filter(u -> username.equals(u.getUsername())).findFirst();
    }

    @Override
    public boolean tienePermiso(String usuarioId, String accion) {
        Usuario usuario = usuarios.get(usuarioId);
        if (usuario == null) return false;
        return usuario.getRol() != null;
    }

    @Override
    public void cambiarPassword(String usuarioId, String nuevoPassword) {
        Usuario usuario = usuarios.get(usuarioId);
        if (usuario != null) {
            usuario.setPasswordHash(nuevoPassword);
        }
    }
}
