package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.PeriodoCambio;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PeriodoCambioServiceImpl implements PeriodoCambioService {

    private final Map<String, PeriodoCambio> periodos = new HashMap<>();

    @Override
    public PeriodoCambio crear(PeriodoCambio periodo) {
        periodos.put(periodo.getId(), periodo);
        return periodo;
    }

    @Override
    public Optional<PeriodoCambio> buscarPorId(String id) {
        return Optional.ofNullable(periodos.get(id));
    }

    @Override
    public List<PeriodoCambio> listarTodos() {
        return new ArrayList<>(periodos.values());
    }

    @Override
    public PeriodoCambio actualizar(String id, PeriodoCambio periodo) {
        periodos.put(id, periodo);
        return periodo;
    }

    @Override
    public void eliminarPorId(String id) {
        periodos.remove(id);
    }

    @Override
    public boolean estaPeriodoActivo() {
        return periodos.values().stream().anyMatch(PeriodoCambio::isActivo);
    }

    @Override
    public Optional<PeriodoCambio> obtenerPeriodoActivo() {
        return periodos.values().stream().filter(PeriodoCambio::isActivo).findFirst();
    }
}
