package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Profesor;
import java.util.List;
import java.util.Optional;

public interface RepositorioProfesor {
    Profesor guardar(Profesor profesor);
    Optional<Profesor> buscarPorCodigo(String codigo);
    Optional<Profesor> buscarPorId(String id);
    List<Profesor> listarTodos();
    void eliminarPorId(String id);
}
