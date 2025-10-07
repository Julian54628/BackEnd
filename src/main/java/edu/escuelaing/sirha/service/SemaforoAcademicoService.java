package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import java.util.Map;
import java.util.Optional;

public interface SemaforoAcademicoService {
    Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId);
    Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId);
}