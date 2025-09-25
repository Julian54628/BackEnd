package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.PeriodoCambio;
import java.util.List;
import java.util.Optional;

public interface RepositorioPeriodoCambio {
    PeriodoCambio guardar(PeriodoCambio periodo);
    Optional<PeriodoCambio> buscarPorCodigo(String codigo);
    Optional<PeriodoCambio> buscarPorId(String id);
    List<PeriodoCambio> listarTodos();
    void eliminarPorId(String id);
}
