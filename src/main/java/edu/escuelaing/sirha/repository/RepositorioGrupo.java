package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Grupo;
import java.util.List;
import java.util.Optional;

public interface RepositorioGrupo {
    Grupo guardar(Grupo grupo);
    Optional<Grupo> buscarPorCodigo(String codigo);
    Optional<Grupo> buscarPorId(String id);
    List<Grupo> listarTodos();
    void eliminarPorId(String id);
}
