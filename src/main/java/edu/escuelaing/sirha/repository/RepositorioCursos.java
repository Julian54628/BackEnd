package edu.escuelaing.sirha.repository;
import java.util.List;
import java.util.Optional;

public interface RepositorioCursos<Curso> {
    Curso guardar(Curso curso);
    Optional<Curso> buscarPorCodigo(String codigo);
    Optional<Curso> buscarPorId(String id);
    List<Curso> listarTodos();
    void eliminarPorId(String id);
}
