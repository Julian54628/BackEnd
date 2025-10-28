package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping
    public List<Profesor> listarTodos() {
        return profesorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> buscarPorId(@PathVariable String id) {
        return profesorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Profesor> buscarPorCodigo(@PathVariable String codigo) {
        return profesorService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Profesor crear(@RequestBody Profesor profesor) {
        return profesorService.crear(profesor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizar(@PathVariable String id, @RequestBody Profesor profesor) {
        try {
            Profesor actualizado = profesorService.actualizar(id, profesor);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable String id) {
        try {
            profesorService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/grupos")
    public List<Grupo> consultarGruposAsignados(@PathVariable String id) {
        return profesorService.consultarGruposAsignados(id);
    }

    @PostMapping("/{profesorId}/grupos/{grupoId}")
    public ResponseEntity<Grupo> asignarProfesorAGrupo(@PathVariable String profesorId, @PathVariable String grupoId) {
        try {
            Grupo grupo = profesorService.asignarProfesorAGrupo(profesorId, grupoId);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/grupos/{grupoId}/profesor")
    public ResponseEntity<Grupo> retirarProfesorDeGrupo(@PathVariable String grupoId) {
        try {
            Grupo grupo = profesorService.retirarProfesorDeGrupo(grupoId);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/departamento/{departamento}")
    public List<Profesor> buscarPorDepartamento(@PathVariable String departamento) {
        return profesorService.buscarPorDepartamento(departamento);
    }

    @GetMapping("/facultad/{facultad}")
    public List<Profesor> buscarPorFacultad(@PathVariable String facultad) {
        return profesorService.buscarPorFacultad(facultad);
    }

    @GetMapping("/{id}/horarios")
    public List<Horario> consultarHorariosProfesor(@PathVariable String id) {
        return profesorService.consultarHorariosProfesor(id);
    }

    @GetMapping("/{id}/carga-academica")
    public float consultarCargaAcademicaProfesor(@PathVariable String id) {
        return profesorService.consultarCargaAcademicaProfesor(id);
    }

    @GetMapping("/{id}/estudiantes")
    public List<Estudiante> consultarEstudiantesAsignados(@PathVariable String id) {
        return profesorService.consultarEstudiantesAsignados(id);
    }
}