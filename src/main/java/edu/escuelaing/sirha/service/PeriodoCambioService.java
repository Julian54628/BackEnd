package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.PeriodoCambio;
import java.util.List;
import java.util.Optional;

public interface PeriodoCambioService {
    PeriodoCambio crear(PeriodoCambio periodo);
    Optional<PeriodoCambio> buscarPorId(String id);
    List<PeriodoCambio> listarTodos();
    PeriodoCambio actualizar(String id, PeriodoCambio periodo);
    void eliminarPorId(String id);
    boolean estaPeriodoActivo();
    Optional<PeriodoCambio> obtenerPeriodoActivo();
    List<PeriodoCambio> obtenerPeriodosVigentes();
    Optional<PeriodoCambio> obtenerPeriodoActivoActual();
    List<PeriodoCambio> obtenerPeriodosFuturos();
    List<PeriodoCambio> obtenerPeriodosPorTipo(String tipo);
}