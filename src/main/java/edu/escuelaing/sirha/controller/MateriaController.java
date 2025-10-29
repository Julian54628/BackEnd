package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity<Materia> crear(@RequestBody Materia materia) {
        try {
            Materia creada = materiaService.crear(materia);
            return ResponseEntity.ok(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> buscarPorId(@PathVariable String id) {
        return materiaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Materia> buscarPorCodigo(@PathVariable String codigo) {
        return materiaService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Materia> listarTodos() {
        return materiaService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> actualizar(@PathVariable String id, @RequestBody Materia materia) {
        try {
            Materia actualizada = materiaService.actualizar(id, materia);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable String id) {
        try {
            materiaService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/grupos/{grupoId}/inscribir/{estudianteId}")
    public ResponseEntity<Grupo> inscribirEstudianteEnGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        try {
            Grupo grupo = materiaService.inscribirEstudianteEnGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{materiaId}/capacidad")
    public ResponseEntity<?> getCapacidad(@PathVariable String materiaId) {
        try {
            Map<String, Object> res = new HashMap<>();
            int inscritos = materiaService.consultarTotalInscritosPorMateria(materiaId);
            boolean tieneDisponibilidad = materiaService.verificarDisponibilidad(materiaId);
            int gruposDisponibles = materiaService.consultarGruposDisponibles(materiaId).size();

            res.put("materiaId", materiaId);
            res.put("inscritos", inscritos);
            res.put("tieneDisponibilidad", tieneDisponibilidad);
            res.put("gruposDisponibles", gruposDisponibles);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno"));
        }
    }

    @PutMapping("/{materiaId}/cupo")
    public ResponseEntity<?> updateCuposMateria(@PathVariable String materiaId, @RequestBody Map<String, Object> payload) {
        if (payload == null || !payload.containsKey("nuevoCupo")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falta campo 'nuevoCupo' en el body"));
        }
        try {
            Object v = payload.get("nuevoCupo");
            int nuevoCupo;
            if (v instanceof Number) {
                nuevoCupo = ((Number) v).intValue();
            } else {
                nuevoCupo = Integer.parseInt(String.valueOf(v));
            }
            materiaService.modificarCuposMateria(materiaId, nuevoCupo);
            return ResponseEntity.ok(Map.of("materiaId", materiaId, "nuevoCupo", nuevoCupo));
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body(Map.of("error", "nuevoCupo debe ser numérico"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno"));
        }
    }
}