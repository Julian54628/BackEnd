package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            grupoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cupo")
    public ResponseEntity<Grupo> updateCupo(@PathVariable String id, @RequestParam int nuevoCupo) {
        try {
            Grupo grupo = grupoService.actualizarCupo(id, nuevoCupo);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/cupo-disponible")
    public ResponseEntity<Boolean> verificarCupoDisponible(@PathVariable String id) {
        boolean disponible = grupoService.verificarCupoDisponible(id);
        return ResponseEntity.ok(disponible);
    }

    @GetMapping("/{id}/carga-academica")
    public ResponseEntity<Float> consultarCargaAcademica(@PathVariable String id) {
        float carga = grupoService.consultarCargaAcademica(id);
        return ResponseEntity.ok(carga);
    }

    @GetMapping("/{id}/estudiantes")
    public List<Estudiante> consultarEstudiantesInscritos(@PathVariable String id) {
        return grupoService.consultarEstudiantesInscritos(id);
    }

    @GetMapping("/{id}/lista-espera")
    public List<Estudiante> consultarListaEspera(@PathVariable String id) {
        Optional<Grupo> grupoOpt = grupoService.buscarPorId(id);
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            List<Estudiante> listaEspera = new ArrayList<>();
            int cupoMaximo = grupo.getCupoMaximo();
            int inscritosActuales = grupo.getEstudiantesInscritosIds().size();
            if (inscritosActuales > cupoMaximo) {
                for (int i = cupoMaximo; i < inscritosActuales && i < grupo.getEstudiantesInscritosIds().size(); i++) {
                    Estudiante estudianteEnEspera = new Estudiante();
                    estudianteEnEspera.setId(grupo.getEstudiantesInscritosIds().get(i));
                    estudianteEnEspera.setNombre("Estudiante en espera " + (i + 1));
                    estudianteEnEspera.setCodigo("COD" + (i + 1));
                    listaEspera.add(estudianteEnEspera);
                }
            }
            return listaEspera;
        }
        return new ArrayList<>();
    }

    @PostMapping("/{id}/lista-espera/agregar/{estudianteId}")
    public ResponseEntity<String> agregarAListaEspera(@PathVariable String id, @PathVariable String estudianteId) {
        try {
            Optional<Grupo> grupoOpt = grupoService.buscarPorId(id);
            if (grupoOpt.isPresent()) {
                Grupo grupo = grupoOpt.get();
                if (grupo.getEstudiantesInscritosIds().size() >= grupo.getCupoMaximo()) {
                    if (!grupo.getEstudiantesInscritosIds().contains(estudianteId)) {
                        grupo.getEstudiantesInscritosIds().add(estudianteId);
                        grupoService.crear(grupo);
                        return ResponseEntity.ok("Estudiante agregado a lista de espera del grupo " + id);
                    } else {
                        return ResponseEntity.badRequest().body("El estudiante ya está en el grupo o lista de espera");
                    }
                } else {
                    return ResponseEntity.badRequest().body("El grupo aún tiene cupos disponibles");
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar a lista de espera: " + e.getMessage());
        }
    }

    @GetMapping("/alertas-capacidad")
    public List<Grupo> consultarGruposConAlertaCapacidad(@RequestParam(defaultValue = "90") double porcentajeAlerta) {
        return grupoService.obtenerGruposConAlertaCapacidad(porcentajeAlerta);
    }

    @GetMapping("/materia/{materiaId}")
    public List<Grupo> buscarPorMateria(@PathVariable String materiaId) {
        return grupoService.buscarPorMateria(materiaId);
    }

    @GetMapping("/profesor/{profesorId}")
    public List<Grupo> buscarPorProfesor(@PathVariable String profesorId) {
        return grupoService.buscarPorProfesor(profesorId);
    }
}