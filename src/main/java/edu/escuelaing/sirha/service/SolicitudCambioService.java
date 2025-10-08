package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface SolicitudCambioService {
    SolicitudCambio crear(SolicitudCambio solicitud);
    SolicitudCambio crearSolicitud(SolicitudCambio solicitud);
    Optional<SolicitudCambio> buscarPorId(String id);
    Optional<SolicitudCambio> obtenerSolicitudPorId(String id);
    List<SolicitudCambio> listarTodos();
    List<SolicitudCambio> obtenerTodasLasSolicitudes();
    SolicitudCambio actualizar(SolicitudCambio solicitud);
    void eliminarPorId(String id);
    void eliminarSolicitud(String id);
    List<SolicitudCambio> buscarPorEstado(EstadoSolicitud estado);
    List<SolicitudCambio> obtenerSolicitudesPorEstado(EstadoSolicitud estado);
    List<SolicitudCambio> buscarPorEstudiante(String estudianteId);
    List<SolicitudCambio> obtenerSolicitudesPorEstudiante(String estudianteId);
    List<SolicitudCambio> obtenerSolicitudesPorDecanatura(String decanaturaId);
    List<SolicitudCambio> obtenerSolicitudesPorTipo(TipoSolicitud tipo);
    List<SolicitudCambio> obtenerSolicitudesPorPrioridad(TipoPrioridad prioridad);
    List<SolicitudCambio> obtenerSolicitudesPendientesPorDecanatura(String decanaturaId);
    SolicitudCambio actualizarEstado(String solicitudId, EstadoSolicitud estado);
    SolicitudCambio actualizarEstadoSolicitud(String id, EstadoSolicitud estado, String respuesta, String justificacion);
    SolicitudCambio aprobarSolicitud(String id, String justificacion);
    SolicitudCambio rechazarSolicitud(String id, String justificacion);
    List<SolicitudCambio> obtenerHistorialSolicitudes();
    Map<String, Object> obtenerEstadisticasSolicitudes();
    boolean validarSolicitud(SolicitudCambio solicitud);
    boolean puedeCrearSolicitud(String estudianteId, String materiaId);
    List<String> obtenerHistorialPorSolicitud(String id);
}
