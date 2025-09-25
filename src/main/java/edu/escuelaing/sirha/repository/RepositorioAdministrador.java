package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Administrador;
import java.util.List;
import java.util.Optional;

public interface RepositorioAdministrador {
    Administrador guardar(Administrador administrador);
    Optional<Administrador> buscarPorCodigo(String codigo);
    Optional<Administrador> buscarPorId(String id);
    List<Administrador> listarTodos();
    void eliminarPorId(String id);
}
