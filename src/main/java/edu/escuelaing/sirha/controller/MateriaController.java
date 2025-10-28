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

    @GetMapping("/{materiaId}/grupos-disponibles")
    public List<Grupo> consultarGruposDisponibles(@PathVariable String materiaId) {
        return materiaService.consultarGruposDisponibles(materiaId);
    }

    @GetMapping("/{materiaId}/disponibilidad")
    public ResponseEntity<Boolean> verificarDisponibilidad(@PathVariable String materiaId) {
        boolean disponible = materiaService.verificarDisponibilidad(materiaId);
        return ResponseEntity.ok(disponible);
    }

    @PutMapping("/{materiaId}/cupo")
    public ResponseEntity<Void> modificarCuposMateria(@PathVariable String materiaId, @RequestParam int nuevoCupo) {
        try {
            materiaService.modificarCuposMateria(materiaId, nuevoCupo);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/con-grupos")
    public ResponseEntity<Materia> registrarMateriaConGrupos(@RequestBody Materia materia, @RequestBody List<Grupo> grupos) {
        try {
            Materia materiaCreada = materiaService.registrarMateriaConGrupos(materia, grupos);
            return ResponseEntity.ok(materiaCreada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{materiaId}/total-inscritos")
    public ResponseEntity<Integer> consultarTotalInscritosPorMateria(@PathVariable String materiaId) {
        int total = materiaService.consultarTotalInscritosPorMateria(materiaId);
        return ResponseEntity.ok(total);
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

    @DeleteMapping("/grupos/{grupoId}/retirar/{estudianteId}")
    public ResponseEntity<Grupo> retirarEstudianteDeGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        try {
            Grupo grupo = materiaService.retirarEstudianteDeGrupo(grupoId, estudianteId);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{materiaId}/asignar-estudiante/{estudianteId}")
    public ResponseEntity<Boolean> asignarMateriaAEstudiante(@PathVariable String materiaId, @PathVariable String estudianteId) {
        boolean asignado = materiaService.asignarMateriaAEstudiante(materiaId, estudianteId);
        return ResponseEntity.ok(asignado);
    }

    @DeleteMapping("/{materiaId}/retirar-estudiante/{estudianteId}")
    public ResponseEntity<Boolean> retirarMateriaDeEstudiante(@PathVariable String materiaId, @PathVariable String estudianteId) {
        boolean retirado = materiaService.retirarMateriaDeEstudiante(materiaId, estudianteId);
        return ResponseEntity.ok(retirado);
    }

    @GetMapping("/facultad/{facultad}")
    public List<Materia> buscarPorFacultad(@PathVariable String facultad) {
        return materiaService.buscarPorFacultad(facultad);
    }

    @GetMapping("/creditos/{creditos}")
    public List<Materia> buscarPorCreditos(@PathVariable int creditos) {
        return materiaService.buscarPorCreditos(creditos);
    }

    @GetMapping("/reportes/mas-solicitadas")
    public List<Map<String, Object>> generarReporteMateriasMasSolicitadas() {
        return new ArrayList<>();
    }

    @GetMapping("/{materiaId}/avance-plan")
    public Map<String, Object> consultarAvancePlanEstudios(@PathVariable String materiaId) {
        Map<String, Object> avance = new HashMap<>();
        avance.put("materiaId", materiaId);
        avance.put("totalInscritos", materiaService.consultarTotalInscritosPorMateria(materiaId));
        return avance;
    }
}