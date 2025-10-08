package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api_administradores")
public class AdministradorController {
    @Autowired
    public AdministradorService administradorService;
    @GetMapping
    public List<Administrador> getAll() {
        return administradorService.listarTodos();
    }
    @GetMapping("Obtiene un administrador por su ID")
    public Optional<Administrador> getById(@PathVariable String id) {
        return administradorService.buscarPorId(id);
    }
    @PostMapping("/crear_administrador")
    public Administrador create(@RequestBody Administrador administrador) {
        return administradorService.crear(administrador);
    }
    @PutMapping("/actualizar_cupo_grupo´grupoId´")
    public Grupo modificarCupoGrupo(@PathVariable String grupoId, @RequestParam int nuevoCupo) {
        return administradorService.modificarCupoGrupo(grupoId, nuevoCupo);
    }
    @PostMapping("/configurar_periodo_cambio")
    public PeriodoCambio configurarPeriodo(@RequestBody PeriodoCambio periodo) {
        return administradorService.configurarPeriodo(periodo);
    }
    @GetMapping("/reportes_de_todas_las_solicitudes_de_cambio ")
    public List<SolicitudCambio> generarReportes() {
        return administradorService.generarReportes();
    }
    @PutMapping("/actualizar-estado-materia/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<?> modificarEstadoMateriaSemaforo(@PathVariable String estudianteId, @PathVariable String materiaId,
                                                            @RequestParam EstadoMateria nuevoEstado) {
        Optional<SemaforoAcademico> resultado = administradorService.modificarEstadoMateriaSemaforo(estudianteId, materiaId, nuevoEstado);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/crear-desde-decanatura/{decanaturaId}")
    public Administrador crearDesdeDecanatura(@PathVariable String decanaturaId) {
        return administradorService.crearDesdeDecanatura(decanaturaId);
    }
    /**
     * 26. Monitoreo alerta carga de los grupos = 90%
     */
    @GetMapping("/grupos/alerta-carga")
    public List<Grupo> obtenerGruposConAlertaCarga() {
        return administradorService.obtenerGruposConAlertaCarga();
    }
    /**
     * 28. Consulta global de solicitudes por prioridad
     */
    @GetMapping("/solicitudes/global-prioridad")
    public List<SolicitudCambio> consultarSolicitudesGlobalesPorPrioridad() {
        return administradorService.consultarSolicitudesGlobalesPorPrioridad();
    }
    /**
     * 35. Generar reporte de grupos más solicitados
     */
    @GetMapping("/reportes/grupos-mas-solicitados")
    public Map<String, Object> generarReporteGruposMasSolicitados() {
        return administradorService.generarReporteGruposMasSolicitados();
    }
    /**
     * 38. Generar reporte estadísticas de reasignación global
     */
    @GetMapping("/reportes/estadisticas-reasignacion")
    public Map<String, Object> generarReporteEstadisticasReasignacion() {
        return administradorService.generarReporteEstadisticasReasignacion();
    }
}