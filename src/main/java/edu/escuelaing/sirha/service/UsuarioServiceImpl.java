package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioUsuario;
import edu.escuelaing.sirha.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public UsuarioServiceImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Optional<Usuario> autenticar(String username, String password) {
        return repositorioUsuario.findByUsername(username)
                .filter(usuario -> usuario.isActivo())
                .filter(usuario -> password != null && password.equals(usuario.getPasswordHash()));
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return repositorioUsuario.findByUsername(username);
    }

    @Override
    public boolean tienePermiso(String usuarioId, String accion) {
        return repositorioUsuario.findById(usuarioId)
                .map(usuario -> usuario.getRol() != null && usuario.isActivo())
                .orElse(false);
    }

    @Override
    public void cambiarPassword(String usuarioId, String nuevoPassword) {
        repositorioUsuario.findById(usuarioId)
                .ifPresent(usuario -> {
                    usuario.setPasswordHash(nuevoPassword);
                    repositorioUsuario.save(usuario);
                });
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        if (repositorioUsuario.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + usuario.getUsername());
        }
        return repositorioUsuario.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return repositorioUsuario.findById(id);
    }

    @Override
    public void desactivarUsuario(String usuarioId) {
        repositorioUsuario.findById(usuarioId)
                .ifPresent(usuario -> {
                    usuario.setActivo(false);
                    repositorioUsuario.save(usuario);
                });
    }

    @Override
    public void activarUsuario(String usuarioId) {
        repositorioUsuario.findById(usuarioId)
                .ifPresent(usuario -> {
                    usuario.setActivo(true);
                    repositorioUsuario.save(usuario);
                });
    }
}