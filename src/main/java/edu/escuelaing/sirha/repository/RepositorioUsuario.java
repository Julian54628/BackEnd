package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface RepositorioUsuario {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorCodigo(String codigo);
    Optional<Usuario> buscarPorId(String id);
    List<Usuario> listarTodos();
    void eliminarPorId(String id);
}
