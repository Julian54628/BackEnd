package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.EstudianteService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudiantesControlador {

    private final EstudianteService estudianteService;
    private final SemaforoAcademicoService semaforoAcademicoService;

    @Autowired
    public EstudiantesControlador(EstudianteService estudianteService, SemaforoAcademicoService semaforoAcademicoService) {
        this.estudianteService = estudianteService;
        this.semaforoAcademicoService = semaforoAcademicoService;
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        try {
            Estudiante creado = estudianteService.crear(estudiante);
            return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getId())).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Estudiante> buscarPorCodigo(@PathVariable String codigo) {
        return estudianteService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> buscarPorId(@PathVariable String id) {
        return estudianteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Estudiante> listarTodos() {
        return estudianteService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        try {
            estudianteService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        try {
            Estudiante actualizado = estudianteService.actualizar(id, estudiante);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/semaforo")
    public ResponseEntity<Map<String, EstadoSemaforo>> verMiSemaforo(@PathVariable String id) {
        Map<String, EstadoSemaforo> semaforo = semaforoAcademicoService.visualizarSemaforoEstudiante(id);
        return semaforo == null || semaforo.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(semaforo);
    }

    @GetMapping("/{id}/semestre-actual")
    public ResponseEntity<Integer> getSemestreActual(@PathVariable String id) {
        try {
            int semestre = semaforoAcademicoService.getSemestreActual(id);
            return semestre > 0 ? ResponseEntity.ok(semestre) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<?> assignMateria(@PathVariable String estudianteId, @PathVariable String materiaId) {
        Object res = estudianteService.assignMateriaToStudent(estudianteId, materiaId);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<?> removeMateria(@PathVariable String estudianteId, @PathVariable String materiaId) {
        estudianteService.removeMateriaFromStudent(estudianteId, materiaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{estudianteId}/foro/actual")
    public ResponseEntity<?> foroSemestreActual(@PathVariable String estudianteId) {
        Object res = estudianteService.getCurrentSemesterForum(estudianteId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{estudianteId}/foro/anteriores")
    public ResponseEntity<?> foroSemestresAnteriores(@PathVariable String estudianteId) {
        Object res = estudianteService.getPastSemestersForum(estudianteId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{estudianteId}/info")
    public ResponseEntity<?> infoEstudiante(@PathVariable String estudianteId) {
        Object res = estudianteService.getStudentFullInfo(estudianteId);
        return ResponseEntity.ok(res);
    }
}