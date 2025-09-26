package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class ControladorEstudiantes {

    private final EstudianteService servicio;

    public ControladorEstudiantes(EstudianteService servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        Estudiante creado = servicio.crear(estudiante);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getId())).body(creado);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Estudiante> buscarPorCodigo(@PathVariable String codigo) {
        return servicio.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> listarTodos() {
        return ResponseEntity.ok(servicio.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        servicio.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        Estudiante actualizado = servicio.actualizar(id, estudiante);
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/solicitudes")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambio(
            @PathVariable String id,
            @RequestParam String materiaOrigenId,
            @RequestParam String grupoOrigenId,
            @RequestParam String materiaDestinoId,
            @RequestParam String grupoDestinoId) {
        SolicitudCambio solicitud = servicio.crearSolicitudCambio(id, materiaOrigenId, grupoOrigenId, materiaDestinoId, grupoDestinoId);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + id + "/solicitudes/" + solicitud.getId())).body(solicitud);
    }

    @GetMapping("/{id}/solicitudes")
    public ResponseEntity<List<SolicitudCambio>> consultarSolicitudes(@PathVariable String id) {
        return ResponseEntity.ok(servicio.consultarSolicitudes(id));
    }
}