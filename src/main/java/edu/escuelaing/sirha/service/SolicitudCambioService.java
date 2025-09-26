package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import java.util.List;
import java.util.Optional;

public interface SolicitudCambioService {
    SolicitudCambio crear(SolicitudCambio solicitud);
    Optional<SolicitudCambio> buscarPorId(String id);
    List<SolicitudCambio> listarTodos();
    List<SolicitudCambio> buscarPorEstado(EstadoSolicitud estado);
    List<SolicitudCambio> buscarPorEstudiante(String estudianteId);
    SolicitudCambio actualizarEstado(String solicitudId, EstadoSolicitud estado);
    void eliminarPorId(String id);
}
