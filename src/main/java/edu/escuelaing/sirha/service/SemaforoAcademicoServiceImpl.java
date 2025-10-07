package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SemaforoAcademicoServiceImpl implements SemaforoAcademicoService {

    private final Map<String, SemaforoAcademico> semaforos = new HashMap<>();

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
        Optional<SemaforoAcademico> optSemaforo = Optional.ofNullable(semaforos.get(estudianteId));
        if (!optSemaforo.isPresent()) {
            return Collections.emptyMap();
        }
        Map<String, EstadoMateria> historial = optSemaforo.get().getHistorialMaterias();
        return historial.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> mapearEstado(entry.getValue())));
    }
    @Override
    public Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId) {
        Optional<SemaforoAcademico> optSemaforo = Optional.ofNullable(semaforos.get(estudianteId));
        if (optSemaforo.isPresent()) {
            EstadoMateria estadoMateria = optSemaforo.get().getHistorialMaterias().get(materiaId);
            if (estadoMateria != null) {
                return Optional.of(mapearEstado(estadoMateria));
            }
        }
        return Optional.empty();
    }
}
