package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.AdministradorService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import edu.escuelaing.sirha.service.SolicitudCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "*")
public class AdministradorController {

    @Autowired
    private SolicitudCambioService solicitudCambioService;

    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private SemaforoAcademicoService semaforoAcademicoService;

    @GetMapping
    public List<Administrador> getAll() {
        return administradorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> getById(@PathVariable String id) {
        return administradorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Administrador> create(@RequestBody Administrador administrador) {
        try {
            Administrador creado = administradorService.crear(administrador);
            return ResponseEntity.ok(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/grupo/{grupoId}/cupo")
    public ResponseEntity<Grupo> modificarCupoGrupo(@PathVariable String grupoId, @RequestParam int nuevoCupo) {
        try {
            Grupo grupo = administradorService.modificarCupoGrupo(grupoId, nuevoCupo);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/periodo")
    public PeriodoCambio configurarPeriodo(@RequestBody PeriodoCambio periodo) {
        return administradorService.configurarPeriodo(periodo);
    }

    @GetMapping("/reportes")
    public List<SolicitudCambio> generarReportes() {
        return administradorService.generarReportes();
    }

    @PutMapping("/semaforo/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<?> modificarEstadoMateriaSemaforo(@PathVariable String estudianteId, @PathVariable String materiaId,
                                                            @RequestParam EstadoMateria nuevoEstado) {
        Optional<SemaforoAcademico> resultado = administradorService.modificarEstadoMateriaSemaforo(estudianteId, materiaId, nuevoEstado);
        return resultado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/casos-excepcionales")
    public ResponseEntity<?> listCasosExcepcionales() {
        try {
            List<?> casos = administradorService.listCasosExcepcionales();
            return ResponseEntity.ok(casos);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/casos-excepcionales/{id}/aprobar")
    public ResponseEntity<?> aprobarCaso(@PathVariable String id, @RequestBody(required = false) Map<String, Object> payload) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Id de caso inválido");
        }
        try {
            Long lid = Long.parseLong(id);
            Object res = administradorService.aprobarCasoEspecial(lid, payload);
            return ResponseEntity.ok(res);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body("Id de caso debe ser numérico");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/casos-excepcionales/{id}/rechazar")
    public ResponseEntity<?> rechazarCaso(@PathVariable String id, @RequestBody(required = false) Map<String, Object> payload) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Id de caso inválido");
        }
        try {
            Long lid = Long.parseLong(id);
            Object res = administradorService.rechazarCasoEspecial(lid, payload);
            return ResponseEntity.ok(res);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body("Id de caso debe ser numérico");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/casos-excepcionales/{id}/solicitar-info")
    public ResponseEntity<?> solicitarInfo(@PathVariable String id, @RequestBody Map<String, Object> info) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Id de caso inválido");
        }
        try {
            Long lid = Long.parseLong(id);
            Object res = administradorService.solicitarInfoCasoEspecial(lid, info);
            return ResponseEntity.ok(res);
        } catch (NumberFormatException nfe) {
            return ResponseEntity.badRequest().body("Id de caso debe ser numérico");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}