package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioPeriodoCambio;
import edu.escuelaing.sirha.model.PeriodoCambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PeriodoCambioServiceImpl implements PeriodoCambioService {

    private final RepositorioPeriodoCambio repositorioPeriodoCambio;

    @Autowired
    public PeriodoCambioServiceImpl(RepositorioPeriodoCambio repositorioPeriodoCambio) {
        this.repositorioPeriodoCambio = repositorioPeriodoCambio;
    }

    @Override
    public PeriodoCambio crear(PeriodoCambio periodo) {
        validarPeriodo(periodo);
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
        if (!repositorioPeriodoCambio.existsById(id)) {
            throw new IllegalArgumentException("Periodo no encontrado: " + id);
        }
        validarPeriodo(periodo);
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

    @Override
    public List<PeriodoCambio> obtenerPeriodosVigentes() {
        return repositorioPeriodoCambio.findPeriodosVigentesEnFecha(new Date());
    }

    @Override
    public Optional<PeriodoCambio> obtenerPeriodoActivoActual() {
        return repositorioPeriodoCambio.findPeriodoActivoEnFecha(new Date());
    }

    @Override
    public List<PeriodoCambio> obtenerPeriodosFuturos() {
        return repositorioPeriodoCambio.findPeriodosFuturos(new Date());
    }

    @Override
    public List<PeriodoCambio> obtenerPeriodosPorTipo(String tipo) {
        return repositorioPeriodoCambio.findByTipo(tipo);
    }

    private void validarPeriodo(PeriodoCambio periodo) {
        if (periodo.getFechaInicio() == null || periodo.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }

        if (periodo.getFechaInicio().after(periodo.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
}