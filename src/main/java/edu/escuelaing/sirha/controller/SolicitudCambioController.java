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
    private SolicitudCambioService solicitudService;

    @GetMapping
    public List<SolicitudCambio> obtenerTodasLasSolicitudes() {
        return solicitudService.obtenerTodasLasSolicitudes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCambio> obtenerSolicitudPorId(@PathVariable String id) {
        return solicitudService.obtenerSolicitudPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolicitudCambio> crear(@RequestBody SolicitudCambio solicitud) {
        try {
            SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable String id) {
        try {
            solicitudService.eliminarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudCambio> buscarPorEstado(@PathVariable String estado) {
        try {
            EstadoSolicitud est = EstadoSolicitud.valueOf(estado.toUpperCase());
            return solicitudService.obtenerSolicitudesPorEstado(est);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<SolicitudCambio> buscarPorEstudiante(@PathVariable String estudianteId) {
        return solicitudService.obtenerSolicitudesPorEstudiante(estudianteId);
    }

    @PostMapping("/crear")
    public ResponseEntity<SolicitudCambio> crearSolicitud(@RequestBody SolicitudCambio solicitud) {
        try {
            SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<SolicitudCambio> buscarSolicitudPorId(@PathVariable String id) {
        return solicitudService.obtenerSolicitudPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar-todas")
    public List<SolicitudCambio> listarTodasLasSolicitudes() {
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

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<String>> obtenerHistorialPorSolicitud(@PathVariable String id) {
        List<String> historial = solicitudService.obtenerHistorialPorSolicitud(id);
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/decanatura/{decanaturaId}")
    public List<SolicitudCambio> obtenerSolicitudesPorDecanatura(@PathVariable String decanaturaId) {
        return solicitudService.obtenerSolicitudesPorDecanatura(decanaturaId);
    }

    @GetMapping("/casos-especiales")
    public List<SolicitudCambio> consultarCasosEspeciales() {
        return solicitudService.obtenerSolicitudesPorPrioridad(TipoPrioridad.ESPECIAL);
    }

    @GetMapping("/estadisticas")
    public Map<String, Object> generarEstadisticasReasignacion() {
        return solicitudService.obtenerEstadisticasSolicitudes();
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

    @GetMapping("/prioridad/{prioridad}")
    public List<SolicitudCambio> obtenerPorPrioridad(@PathVariable TipoPrioridad prioridad) {
        return solicitudService.obtenerSolicitudesPorPrioridad(prioridad);
    }

    @GetMapping("/periodo/{periodoId}")
    public List<SolicitudCambio> obtenerPorPeriodo(@PathVariable String periodoId) {
        return solicitudService.obtenerSolicitudesPorPeriodo(periodoId);
    }

    @GetMapping("/contar/estado/{estado}")
    public long contarPorEstado(@PathVariable String estado) {
        try {
            EstadoSolicitud est = EstadoSolicitud.valueOf(estado.toUpperCase());
            return solicitudService.contarSolicitudesPorEstado(est);
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }
}