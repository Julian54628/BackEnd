package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Materia;
import java.util.List;
import java.util.Optional;

public interface RepositorioMateria {
    Materia guardar(Materia materia);
    Optional<Materia> buscarPorCodigo(String codigo);
    Optional<Materia> buscarPorId(String id);
    List<Materia> listarTodos();
    void eliminarPorId(String id);
}
