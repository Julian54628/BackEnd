package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Usuario;
import edu.escuelaing.sirha.repository.RepositorioUsuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario repositorioUsuario;

    public UsuarioServiceImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Optional<Usuario> autenticar(String username, String password) {
        return repositorioUsuario.findByUsername(username)
                .filter(u -> password != null && password.equals(u.getPasswordHash()));
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return repositorioUsuario.findByUsername(username);
    }

    @Override
    public boolean tienePermiso(String usuarioId, String accion) {
        return repositorioUsuario.findById(usuarioId)
                .map(u -> u.getRol() != null && u.isActivo())
                .orElse(false);
    }

    @Override
    public void cambiarPassword(String usuarioId, String nuevoPassword) {
        repositorioUsuario.findById(usuarioId).ifPresent(u -> {
            u.setPasswordHash(nuevoPassword);
            repositorioUsuario.save(u);
        });
    }
}
