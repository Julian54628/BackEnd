package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.SemaforoAcademico;
import java.util.List;
import java.util.Optional;

public interface RepositorioSemaforos {
    SemaforoAcademico guardar(SemaforoAcademico semaforo);
    Optional<SemaforoAcademico> buscarPorCodigo(String codigo);
    Optional<SemaforoAcademico> buscarPorId(String id);
    List<SemaforoAcademico> listarTodos();
    void eliminarPorId(String id);
}
