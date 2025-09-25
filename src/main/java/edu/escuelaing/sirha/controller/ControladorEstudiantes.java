package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.service.ServicioEstudiantes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class ControladorEstudiantes {

    private final ServicioEstudiantes servicio;

    public ControladorEstudiantes(ServicioEstudiantes servicio) {
        this.servicio = servicio;
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        Estudiante creado = servicio.crear(estudiante);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getCodigo())).body(creado);
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
}
