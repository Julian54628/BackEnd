package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.repository.RepositorioPeriodoCambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodoCambioServiceImpl implements PeriodoCambioService {

    @Autowired
    private RepositorioPeriodoCambio repositorioPeriodoCambio;

    @Override
    public PeriodoCambio crear(PeriodoCambio periodo) {
        return repositorioPeriodoCambio.save(periodo);
    }

    @Override
    public Optional<PeriodoCambio> buscarPorId(String id) {
        return repositorioPeriodoCambio.findById(id);
    }

    @Override
    public List<PeriodoCambio> listarTodos() {
        return repositorioPeriodoCambio.findAll();
    }

    @Override
    public PeriodoCambio actualizar(String id, PeriodoCambio periodo) {
        periodo.setId(id);
        return repositorioPeriodoCambio.save(periodo);
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioPeriodoCambio.deleteById(id);
    }

    @Override
    public boolean estaPeriodoActivo() {
        return repositorioPeriodoCambio.existsByActivoTrue();
    }

    @Override
    public Optional<PeriodoCambio> obtenerPeriodoActivo() {
        return repositorioPeriodoCambio.findByActivoTrue().stream().findFirst();
    }

    public List<PeriodoCambio> obtenerPeriodosVigentes() {
        return repositorioPeriodoCambio.findPeriodosVigentesEnFecha(new Date());
    }

    public Optional<PeriodoCambio> obtenerPeriodoActivoActual() {
        return repositorioPeriodoCambio.findPeriodoActivoEnFecha(new Date());
    }

    public List<PeriodoCambio> obtenerPeriodosFuturos() {
        return repositorioPeriodoCambio.findPeriodosFuturos(new Date());
    }

    public List<PeriodoCambio> obtenerPeriodosPorTipo(String tipo) {
        return repositorioPeriodoCambio.findByTipo(tipo);
    }
}