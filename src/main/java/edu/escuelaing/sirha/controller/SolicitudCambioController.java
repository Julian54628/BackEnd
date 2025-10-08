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

    /**
     * Crear nueva solicitud de cambio (Estudiante)
     */
    @PostMapping
    public ResponseEntity<SolicitudCambio> crearSolicitud(@RequestBody SolicitudCambio solicitud) {
        SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(creada);
    }

    /**
     * Crear solicitud de cambio de grupo
     */
    @PostMapping("/cambio-grupo")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambioGrupo(@RequestBody SolicitudCambio solicitud) {
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(creada);
    }

    /**
     * Crear solicitud de cambio de materia
     */
    @PostMapping("/cambio-materia")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambioMateria(@RequestBody SolicitudCambio solicitud) {
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_MATERIA);
        SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.ok(creada);
    }

    /**
     * Obtener todas las solicitudes
     */
    @GetMapping
    public ResponseEntity<List<SolicitudCambio>> obtenerTodasLasSolicitudes() {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerTodasLasSolicitudes();
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener solicitud por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCambio> obtenerSolicitudPorId(@PathVariable String id) {
        Optional<SolicitudCambio> solicitud = solicitudService.obtenerSolicitudPorId(id);
        return solicitud.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener solicitudes por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<SolicitudCambio>> obtenerSolicitudesPorEstado(@PathVariable EstadoSolicitud estado) {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerSolicitudesPorEstado(estado);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener solicitudes por estudiante
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<SolicitudCambio>> obtenerSolicitudesPorEstudiante(@PathVariable String estudianteId) {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerSolicitudesPorEstudiante(estudianteId);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener solicitudes por decanatura
     */
    @GetMapping("/decanatura/{decanaturaId}")
    public ResponseEntity<List<SolicitudCambio>> obtenerSolicitudesPorDecanatura(@PathVariable String decanaturaId) {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerSolicitudesPorDecanatura(decanaturaId);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener solicitudes por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<SolicitudCambio>> obtenerSolicitudesPorTipo(@PathVariable TipoSolicitud tipo) {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerSolicitudesPorTipo(tipo);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener solicitudes por prioridad
     */
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<SolicitudCambio>> obtenerSolicitudesPorPrioridad(@PathVariable TipoPrioridad prioridad) {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerSolicitudesPorPrioridad(prioridad);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Actualizar estado de solicitud (Decano/Administrador)
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudCambio> actualizarEstadoSolicitud(
            @PathVariable String id,
            @RequestParam EstadoSolicitud estado,
            @RequestParam(required = false) String respuesta,
            @RequestParam(required = false) String justificacion) {
        SolicitudCambio actualizada = solicitudService.actualizarEstadoSolicitud(id, estado, respuesta, justificacion);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Aprobar solicitud (Decano/Administrador)
     */
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudCambio> aprobarSolicitud(
            @PathVariable String id,
            @RequestParam(required = false) String justificacion) {
        SolicitudCambio aprobada = solicitudService.aprobarSolicitud(id, justificacion);
        return ResponseEntity.ok(aprobada);
    }

    /**
     * Rechazar solicitud (Decano/Administrador)
     */
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudCambio> rechazarSolicitud(
            @PathVariable String id,
            @RequestParam String justificacion) {
        SolicitudCambio rechazada = solicitudService.rechazarSolicitud(id, justificacion);
        return ResponseEntity.ok(rechazada);
    }

    /**
     * Obtener historial de solicitudes
     */
    @GetMapping("/historial")
    public ResponseEntity<List<SolicitudCambio>> obtenerHistorialSolicitudes() {
        List<SolicitudCambio> historial = solicitudService.obtenerHistorialSolicitudes();
        return ResponseEntity.ok(historial);
    }

    /**
     * Obtener estadísticas de solicitudes
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasSolicitudes() {
        Map<String, Object> estadisticas = solicitudService.obtenerEstadisticasSolicitudes();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtener solicitudes pendientes por decanatura
     */
    @GetMapping("/decanatura/{decanaturaId}/pendientes")
    public ResponseEntity<List<SolicitudCambio>> obtenerSolicitudesPendientesPorDecanatura(@PathVariable String decanaturaId) {
        List<SolicitudCambio> solicitudes = solicitudService.obtenerSolicitudesPendientesPorDecanatura(decanaturaId);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Eliminar solicitud (Solo Administrador)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable String id) {
        solicitudService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }
}