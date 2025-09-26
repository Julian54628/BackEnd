package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.model.SolicitudCambio;
import java.util.List;
import java.util.Optional;

public interface EstudianteService {
    Estudiante crear(Estudiante estudiante);
    Optional<Estudiante> buscarPorCodigo(String codigo);
    Optional<Estudiante> buscarPorId(String id);
    List<Estudiante> listarTodos();
    void eliminarPorId(String id);
    Estudiante actualizar(String id, Estudiante estudiante);
    SolicitudCambio crearSolicitudCambio(String estudianteId, String materiaOrigenId,
    String grupoOrigenId, String materiaDestinoId, String grupoDestinoId);
    List<SolicitudCambio> consultarSolicitudes(String estudianteId);
}