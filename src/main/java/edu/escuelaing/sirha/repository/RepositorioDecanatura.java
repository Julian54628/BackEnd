package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Decanatura;
import java.util.List;
import java.util.Optional;

public interface RepositorioDecanatura {
    Decanatura guardar(Decanatura decanatura);
    Optional<Decanatura> buscarPorCodigo(String codigo);
    Optional<Decanatura> buscarPorId(String id);
    List<Decanatura> listarTodos();
    void eliminarPorId(String id);
}
