package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import edu.escuelaing.sirha.repository.RepositorioSemaforoAcademico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SemaforoAcademicoServiceImpl implements SemaforoAcademicoService {

    @Autowired
    private RepositorioSemaforoAcademico repositorioSemaforoAcademico;

    private EstadoSemaforo mapearEstado(EstadoMateria estadoMateria) {
        switch (estadoMateria) {
            case APROBADA:
                return EstadoSemaforo.VERDE;
            case INSCRITA:
            case PENDIENTE:
                return EstadoSemaforo.AZUL;
            case REPROBADA:
            case CANCELADA:
                return EstadoSemaforo.ROJO;
            default:
                return EstadoSemaforo.AZUL;
        }
    }

    @Override
    public Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId) {
        Optional<SemaforoAcademico> optSemaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (!optSemaforo.isPresent()) {
            return Collections.emptyMap();
        }
        Map<String, EstadoMateria> historial = optSemaforo.get().getHistorialMaterias();
        return historial.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,entry -> mapearEstado(entry.getValue())));
    }

    @Override
    public Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId) {
        Optional<SemaforoAcademico> optSemaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (optSemaforo.isPresent()) {
            EstadoMateria estadoMateria = optSemaforo.get().getHistorialMaterias().get(materiaId);
            if (estadoMateria != null) {
                return Optional.of(mapearEstado(estadoMateria));
            }
        }
        return Optional.empty();
    }

    public Optional<SemaforoAcademico> obtenerSemaforoCompleto(String estudianteId) {
        return repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
    }

    public List<SemaforoAcademico> obtenerSemaforosPorPlan(String planAcademicoId) {
        return repositorioSemaforoAcademico.findByPlanAcademicoId(planAcademicoId);
    }

    public List<SemaforoAcademico> obtenerEstudiantesEnRiesgo() {
        return repositorioSemaforoAcademico.findEstudiantesEnRiesgo(3.0f, 3);
    }

    public long contarEstudiantesConCambioDePlan() {
        return repositorioSemaforoAcademico.countByCambioDePlanTrue();
    }
}