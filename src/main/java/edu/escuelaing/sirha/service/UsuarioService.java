package edu.escuelaing.sirha.service;

import java.util.Optional;

public interface UsuarioService {
    Optional<Usuario> autenticar(String username, String password);
    Optional<Usuario> buscarPorUsername(String username);
    boolean tienePermiso(String usuarioId, String accion);
    void cambiarPassword(String usuarioId, String nuevoPassword);
}

