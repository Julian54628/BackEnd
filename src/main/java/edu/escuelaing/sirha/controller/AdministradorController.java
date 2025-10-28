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
    public Administrador create(@RequestBody Administrador administrador) {
        return administradorService.crear(administrador);
    }

    @PutMapping("/grupo/{grupoId}/cupo")
    public ResponseEntity<Grupo> modificarCupoGrupo(@PathVariable String grupoId, @RequestParam int nuevoCupo) {
        try {
            Grupo grupo = administradorService.modificarCupoGrupo(grupoId, nuevoCupo);
            return ResponseEntity.ok(grupo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
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

    @PostMapping("/desde-decanatura/{decanaturaId}")
    public ResponseEntity<Administrador> crearDesdeDecanatura(@PathVariable String decanaturaId) {
        try {
            Administrador admin = administradorService.crearDesdeDecanatura(decanaturaId);
            return ResponseEntity.ok(admin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministrador(@PathVariable String id) {
        boolean eliminado = administradorService.eliminarAdministrador(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/semaforo/estudiante/{estudianteId}/completo")
    public ResponseEntity<SemaforoVisualizacion> obtenerSemaforoCompleto(@PathVariable String estudianteId) {
        SemaforoVisualizacion semaforo = semaforoAcademicoService.obtenerSemaforoCompleto(estudianteId);
        return semaforo.getEstudianteId() != null ? ResponseEntity.ok(semaforo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/semaforo/estudiante/{estudianteId}/detallado")
    public ResponseEntity<SemaforoVisualizacion> obtenerSemaforoDetallado(@PathVariable String estudianteId) {
        SemaforoVisualizacion semaforo = semaforoAcademicoService.obtenerSemaforoDetallado(estudianteId);
        return semaforo.getEstudianteId() != null ? ResponseEntity.ok(semaforo) : ResponseEntity.notFound().build();
    }

    @GetMapping("/reportes/grupos-solicitados")
    public List<Map<String, Object>> generarReporteGruposMasSolicitados() {
        List<SolicitudCambio> solicitudes = solicitudCambioService.obtenerTodasLasSolicitudes();
        Map<String, Long> conteoPorGrupo = solicitudes.stream()
                .filter(s -> s.getGrupoDestinoId() != null)
                .collect(Collectors.groupingBy(SolicitudCambio::getGrupoDestinoId, Collectors.counting()));

        return conteoPorGrupo.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> reporte = new HashMap<>();
                    reporte.put("grupoId", entry.getKey());
                    reporte.put("totalSolicitudes", entry.getValue());
                    return reporte;
                }).collect(Collectors.toList());
    }

    @GetMapping("/reportes/estadisticas-reasignacion")
    public Map<String, Object> generarReporteEstadisticasReasignacion() {
        return solicitudCambioService.obtenerEstadisticasSolicitudes();
    }
}