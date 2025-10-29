package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @GetMapping
    public List<Grupo> getAll() {
        return grupoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> getById(@PathVariable String id) {
        return grupoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Grupo create(@RequestBody Grupo grupo) {
        return grupoService.crear(grupo);
    }

    @PutMapping("/{id}/cupo")
    public ResponseEntity<?> updateCupo(@PathVariable String id, @RequestParam int nuevoCupo) {
        try {
            Grupo grupo = grupoService.actualizarCupo(id, nuevoCupo);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno"));
        }
    }

    @GetMapping("/{id}/cupo-disponible")
    public ResponseEntity<Boolean> verificarCupoDisponible(@PathVariable String id) {
        boolean disponible = grupoService.verificarCupoDisponible(id);
        return ResponseEntity.ok(disponible);
    }

    @GetMapping("/alerts")
    public ResponseEntity<?> groupsWithHighLoad(@RequestParam(required = false, defaultValue = "90") double threshold) {
        List<Grupo> groups = grupoService.obtenerGruposConAlertaCapacidad(threshold);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}/cupo-info")
    public ResponseEntity<?> getCupoInfo(@PathVariable String id) {
        Optional<Grupo> gOpt = grupoService.buscarPorId(id);
        if (gOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Grupo g = gOpt.get();
        double carga = grupoService.consultarCargaAcademica(id);
        boolean cupoDisponible = grupoService.verificarCupoDisponible(id);
        int inscritos = grupoService.consultarEstudiantesInscritos(id).size();
        Map<String, Object> resp = new HashMap<>();
        resp.put("grupoId", id);
        resp.put("cargaPorcentaje", carga);
        resp.put("cupoDisponible", cupoDisponible);
        resp.put("inscritos", inscritos);
        resp.put("cupoMaximo", g.getCupoMaximo());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<?> consultarEstudiantesInscritos(@PathVariable String id) {
        List<Estudiante> lista = grupoService.consultarEstudiantesInscritos(id);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}/inscritos/total")
    public ResponseEntity<?> totalInscritos(@PathVariable String id) {
        int total = grupoService.consultarEstudiantesInscritos(id).size();
        return ResponseEntity.ok(Map.of("grupoId", id, "total", total));
    }

    @GetMapping("/por-materia/{materiaId}")
    public ResponseEntity<List<Grupo>> gruposPorMateria(@PathVariable String materiaId) {
        return ResponseEntity.ok(grupoService.buscarPorMateria(materiaId));
    }

    @GetMapping("/por-profesor/{profesorId}")
    public ResponseEntity<List<Grupo>> gruposPorProfesor(@PathVariable String profesorId) {
        return ResponseEntity.ok(grupoService.buscarPorProfesor(profesorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        try {
            grupoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}