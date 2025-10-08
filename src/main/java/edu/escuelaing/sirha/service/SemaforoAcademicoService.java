package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.SemaforoVisualizacion;
import java.util.Map;
import java.util.Optional;

public interface SemaforoAcademicoService {
    Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId);
    Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId);
    int getSemestreActual(String estudianteId);
    Map<String, Object> getForaneoEstudiante(String estudianteId);

    SemaforoVisualizacion obtenerSemaforoCompleto(String estudianteId);
    SemaforoVisualizacion obtenerSemaforoDetallado(String estudianteId);
}