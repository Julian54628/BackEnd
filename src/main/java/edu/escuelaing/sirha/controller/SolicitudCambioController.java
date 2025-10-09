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

    @PostMapping
    public ResponseEntity<SolicitudCambio> crear(@RequestBody SolicitudCambio solicitud) {
        try {
            SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<SolicitudCambio> buscarPorId(@PathVariable String id) {
        Optional<SolicitudCambio> solicitud = solicitudService.obtenerSolicitudPorId(id);
        return solicitud.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar-todas")
    public List<SolicitudCambio> listarTodos() {
        return solicitudService.obtenerTodasLasSolicitudes();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable String id) {
        try {
            solicitudService.eliminarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudCambio> buscarPorEstado(@PathVariable String estado) {
        EstadoSolicitud est = EstadoSolicitud.valueOf(estado.toUpperCase());
        return solicitudService.obtenerSolicitudesPorEstado(est);
    }

    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<SolicitudCambio> actualizarEstado(@PathVariable String id, @RequestParam String estado) {
        try {
            EstadoSolicitud est = EstadoSolicitud.valueOf(estado.toUpperCase());
            SolicitudCambio actualizada = solicitudService.actualizarEstado(id, est);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<SolicitudCambio> buscarPorEstudiante(@PathVariable String estudianteId) {
        return solicitudService.obtenerSolicitudesPorEstudiante(estudianteId);
    }

    @GetMapping("/{id}/historial-completo")
    public List<String> historial(@PathVariable String id) {
        return solicitudService.obtenerHistorialPorSolicitud(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCambio> obtenerSolicitudPorId(@PathVariable String id) {
        Optional<SolicitudCambio> solicitud = solicitudService.obtenerSolicitudPorId(id);
        return solicitud.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<String>> obtenerHistorialPorSolicitud(@PathVariable String id) {
        List<String> historial = solicitudService.obtenerHistorialPorSolicitud(id);
        return ResponseEntity.ok(historial);
    }

    @GetMapping
    public List<SolicitudCambio> obtenerTodasLasSolicitudes() {
        return solicitudService.obtenerTodasLasSolicitudes();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCambio> actualizarSolicitud(@PathVariable String id, @RequestBody SolicitudCambio solicitud) {
        try {
            solicitud.setId(id);
            SolicitudCambio actualizada = solicitudService.actualizar(solicitud);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable String id) {
        try {
            solicitudService.eliminarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/aprobar-especial")
    public ResponseEntity<SolicitudCambio> aprobarSolicitudEspecial(@PathVariable String id, @RequestParam String justificacion) {
        try {
            SolicitudCambio aprobada = solicitudService.aprobarSolicitud(id, justificacion);
            return ResponseEntity.ok(aprobada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/casos-especiales")
    public List<SolicitudCambio> consultarCasosEspeciales() {
        return solicitudService.obtenerSolicitudesPorPrioridad(TipoPrioridad.ESPECIAL);
    }
    @GetMapping("/estadisticas")
    public Map<String, Object> generarEstadisticasReasignacion() {
        return solicitudService.obtenerEstadisticasSolicitudes();
    }
}