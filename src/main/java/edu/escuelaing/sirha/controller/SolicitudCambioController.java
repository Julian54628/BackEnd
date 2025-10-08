package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.service.SolicitudCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudCambioController {

    @Autowired
    public SolicitudCambioService solicitudService;

    @GetMapping
    public List<SolicitudCambio> listarTodos() {
        return solicitudService.listarTodos();
    }

    @GetMapping("/Busca una solicitud de cambio específica{id}")
    public Optional<SolicitudCambio> buscarPorId(@PathVariable String id) {
        return solicitudService.buscarPorId(id);
    }

    @PostMapping
    public SolicitudCambio crear(@RequestBody SolicitudCambio solicitud) {
        return solicitudService.crear(solicitud);
    }

    @PutMapping("/Actualiza el estado de una solicitud{id}/estado")
    public SolicitudCambio actualizarEstado(@PathVariable String id, @RequestParam String estado) {
        return solicitudService.actualizarEstado(id, EstadoSolicitud.valueOf(estado));
    }

    @DeleteMapping("/Elimina una solicitud de cambio del sistema{id}")
    public void eliminarPorId(@PathVariable String id) {
        solicitudService.eliminarPorId(id);
    }

    @GetMapping("/Busca solicitudes de cambio por estado/{estado}")
    public List<SolicitudCambio> buscarPorEstado(@PathVariable String estado) {
        return solicitudService.buscarPorEstado(EstadoSolicitud.valueOf(estado));
    }

    @GetMapping("/Busca todas las solicitudes de cambio asociadas a un estudiante/{estudianteId}")
    public List<SolicitudCambio> buscarPorEstudiante(@PathVariable String estudianteId) {
        return solicitudService.buscarPorEstudiante(estudianteId);
    }
}