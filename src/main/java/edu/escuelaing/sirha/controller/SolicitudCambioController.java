package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.SolicitudCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudCambioController {

    @Autowired
    public SolicitudCambioService solicitudService;

    public void setSolicitudService(SolicitudCambioService solicitudService) {
        this.solicitudService = solicitudService;
    }


    public SolicitudCambio crear(SolicitudCambio solicitud) {
        return solicitudService.crearSolicitud(solicitud);
    }

    public Optional<SolicitudCambio> buscarPorId(String id) {
        return solicitudService.obtenerSolicitudPorId(id);
    }

    public List<SolicitudCambio> buscarPorEstado(String estado) {
        EstadoSolicitud est = EstadoSolicitud.valueOf(estado);
        return solicitudService.obtenerSolicitudesPorEstado(est);
    }

    public SolicitudCambio actualizarEstado(String id, String estado) {
        EstadoSolicitud est = EstadoSolicitud.valueOf(estado);
        return solicitudService.actualizarEstadoSolicitud(id, est, null, null);
    }

    public List<SolicitudCambio> listarTodos() {
        return solicitudService.obtenerTodasLasSolicitudes();
    }

    public void eliminarPorId(String id) {
        solicitudService.eliminarSolicitud(id);
    }

    public List<SolicitudCambio> buscarPorEstudiante(String estudianteId) {
        return solicitudService.obtenerSolicitudesPorEstudiante(estudianteId);
    }
}