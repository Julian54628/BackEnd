package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Estudiante;
import java.util.List;
import java.util.Optional;

public interface RepositorioEstudiantes {
    Estudiante guardar(Estudiante estudiante);
    Optional<Estudiante> buscarPorCodigo(String codigo);
    Optional<Estudiante> buscarPorId(String id);
    List<Estudiante> listarTodos();
    void eliminarPorId(String id);
}
