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
    private SolicitudCambioService solicitudService;

    @GetMapping
    public List<SolicitudCambio> listarTodos() {
        return solicitudService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<SolicitudCambio> buscarPorId(@PathVariable String id) {
        return solicitudService.buscarPorId(id);
    }

    @PostMapping
    public SolicitudCambio crear(@RequestBody SolicitudCambio solicitud) {
        return solicitudService.crear(solicitud);
    }

    @PutMapping("/{id}/estado")
    public SolicitudCambio actualizarEstado(@PathVariable String id, @RequestParam String estado) {
        return solicitudService.actualizarEstado(id, EstadoSolicitud.valueOf(estado));
    }

    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable String id) {
        solicitudService.eliminarPorId(id);
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudCambio> buscarPorEstado(@PathVariable String estado) {
        return solicitudService.buscarPorEstado(EstadoSolicitud.valueOf(estado));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<SolicitudCambio> buscarPorEstudiante(@PathVariable String estudianteId) {
        return solicitudService.buscarPorEstudiante(estudianteId);
    }
}