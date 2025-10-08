package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.EstudianteService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import edu.escuelaing.sirha.service.SolicitudCambioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudiantesControlador {

    private final EstudianteService estudianteService;
    private final SemaforoAcademicoService semaforoAcademicoService;
    private final SolicitudCambioService solicitudCambioService;

    public EstudiantesControlador(EstudianteService estudianteService, SemaforoAcademicoService semaforoAcademicoService, SolicitudCambioService solicitudCambioService) {
        this.estudianteService = estudianteService;
        this.semaforoAcademicoService = semaforoAcademicoService;
        this.solicitudCambioService = solicitudCambioService;
    }
    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        Estudiante creado = estudianteService.crear(estudiante);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getId())).body(creado);
    }
    @GetMapping("/{codigo}")
    public ResponseEntity<Estudiante> buscarPorCodigo(@PathVariable String codigo) {
        return estudianteService.buscarPorCodigo(codigo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<Estudiante>> listarTodos() {
        return ResponseEntity.ok(estudianteService.listarTodos());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        estudianteService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        Estudiante actualizado = estudianteService.actualizar(id, estudiante);
        return ResponseEntity.ok(actualizado);
    }
    @PostMapping("/{id}/solicitudes")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambio(
            @PathVariable String id,
            @RequestParam String materiaOrigenId,
            @RequestParam String grupoOrigenId,
            @RequestParam String materiaDestinoId,
            @RequestParam String grupoDestinoId) {
        SolicitudCambio solicitud = estudianteService.crearSolicitudCambio(id, materiaOrigenId, grupoOrigenId, materiaDestinoId, grupoDestinoId);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + id + "/solicitudes/" + solicitud.getId())).body(solicitud);
    }

    /**
     * Crear solicitud de cambio de grupo
     */
    @PostMapping("/{id}/solicitudes/cambio-grupo")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambioGrupo(
            @PathVariable String id,
            @RequestBody SolicitudCambio solicitud) {
        solicitud.setEstudianteId(id);
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_GRUPO);
        SolicitudCambio creada = solicitudCambioService.crearSolicitud(solicitud);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + id + "/solicitudes/" + creada.getId())).body(creada);
    }

    /**
     * Crear solicitud de cambio de materia
     */
    @PostMapping("/{id}/solicitudes/cambio-materia")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambioMateria(
            @PathVariable String id,
            @RequestBody SolicitudCambio solicitud) {
        solicitud.setEstudianteId(id);
        solicitud.setTipoSolicitud(TipoSolicitud.CAMBIO_MATERIA);
        SolicitudCambio creada = solicitudCambioService.crearSolicitud(solicitud);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + id + "/solicitudes/" + creada.getId())).body(creada);
    }
    @GetMapping("/{id}/solicitudes")
    public ResponseEntity<List<SolicitudCambio>> consultarSolicitudes(@PathVariable String id) {
        List<SolicitudCambio> solicitudes = solicitudCambioService.obtenerSolicitudesPorEstudiante(id);
        return ResponseEntity.ok(solicitudes);
    }

    /**
     * Obtener historial de solicitudes del estudiante
     */
    @GetMapping("/{id}/solicitudes/historial")
    public ResponseEntity<List<SolicitudCambio>> obtenerHistorialSolicitudes(@PathVariable String id) {
        List<SolicitudCambio> historial = solicitudCambioService.obtenerSolicitudesPorEstudiante(id).stream()
                .sorted((s1, s2) -> s2.getFechaCreacion().compareTo(s1.getFechaCreacion()))
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(historial);
    }
    @GetMapping("/{id}/semaforo")
    public ResponseEntity<Map<String, EstadoSemaforo>> verMiSemaforo(@PathVariable String id) {
        Map<String, EstadoSemaforo> semaforo = semaforoAcademicoService.visualizarSemaforoEstudiante(id);
        if (semaforo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(semaforo);
    }
    @GetMapping("/{id}/semaforo/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> verEstadoMateria(@PathVariable String id, @PathVariable String materiaId) {
        Optional<EstadoSemaforo> estado = semaforoAcademicoService.consultarSemaforoMateria(id, materiaId);
        return estado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Visualización completa del semáforo académico del estudiante
     */
    @GetMapping("/{id}/semaforo-completo")
    public ResponseEntity<SemaforoVisualizacion> obtenerSemaforoCompleto(@PathVariable String id) {
        SemaforoVisualizacion semaforo = semaforoAcademicoService.obtenerSemaforoCompleto(id);
        if (semaforo.getEstudianteId() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(semaforo);
    }

    /**
     * Visualización detallada del semáforo académico del estudiante
     */
    @GetMapping("/{id}/semaforo-detallado")
    public ResponseEntity<SemaforoVisualizacion> obtenerSemaforoDetallado(@PathVariable String id) {
        SemaforoVisualizacion semaforo = semaforoAcademicoService.obtenerSemaforoDetallado(id);
        if (semaforo.getEstudianteId() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(semaforo);
    }
}