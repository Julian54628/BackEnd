package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.AdministradorService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    public AdministradorService administradorService;

    @Autowired
    public SemaforoAcademicoService semaforoAcademicoService;
    @GetMapping
    public List<Administrador> getAll() {
        return administradorService.listarTodos();
    }
    @GetMapping("/{id}")
    public Optional<Administrador> getById(@PathVariable String id) {
        return administradorService.buscarPorId(id);
    }
    @PostMapping
    public Administrador create(@RequestBody Administrador administrador) {
        return administradorService.crear(administrador);
    }
    @PutMapping("/grupo/{grupoId}/cupo")
    public Grupo modificarCupoGrupo(@PathVariable String grupoId, @RequestParam int nuevoCupo) {
        return administradorService.modificarCupoGrupo(grupoId, nuevoCupo);
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
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/desde-decanatura/{decanaturaId}")
    public Administrador crearDesdeDecanatura(@PathVariable String decanaturaId) {
        return administradorService.crearDesdeDecanatura(decanaturaId);
    }

    /**
     * Visualización completa del semáforo académico de un estudiante
     */
    @GetMapping("/semaforo/estudiante/{estudianteId}/completo")
    public ResponseEntity<SemaforoVisualizacion> obtenerSemaforoCompleto(@PathVariable String estudianteId) {
        SemaforoVisualizacion semaforo = semaforoAcademicoService.obtenerSemaforoCompleto(estudianteId);
        if (semaforo.getEstudianteId() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(semaforo);
    }

    /**
     * Visualización detallada del semáforo académico de un estudiante
     */
    @GetMapping("/semaforo/estudiante/{estudianteId}/detallado")
    public ResponseEntity<SemaforoVisualizacion> obtenerSemaforoDetallado(@PathVariable String estudianteId) {
        SemaforoVisualizacion semaforo = semaforoAcademicoService.obtenerSemaforoDetallado(estudianteId);
        if (semaforo.getEstudianteId() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(semaforo);
    }
}