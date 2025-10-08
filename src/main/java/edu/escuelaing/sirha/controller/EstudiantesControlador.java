package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Materia;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.service.EstudianteService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudiantesControlador {

    private final EstudianteService estudianteService;
    private final SemaforoAcademicoService semaforoAcademicoService;

    public EstudiantesControlador(EstudianteService estudianteService, SemaforoAcademicoService semaforoAcademicoService) {
        this.estudianteService = estudianteService;
        this.semaforoAcademicoService = semaforoAcademicoService;
    }
    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        Estudiante creado = estudianteService.crear(estudiante);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getId())).body(creado);
    }
    @GetMapping("/Busca un estudiante por su código{codigo}")
    public ResponseEntity<Estudiante> buscarPorCodigo(@PathVariable String codigo) {
        return estudianteService.buscarPorCodigo(codigo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<Estudiante>> listarTodos() {
        return ResponseEntity.ok(estudianteService.listarTodos());
    }
    @DeleteMapping("/Elimina un estudiante del sistema por su codigo{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        estudianteService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/Actualiza la información de un estudiante{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        Estudiante actualizado = estudianteService.actualizar(id, estudiante);
        return ResponseEntity.ok(actualizado);
    }
    @PostMapping("Crea una nueva solicitud de cambio de materia/{id}/solicitudes")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambio(
            @PathVariable String id,
            @RequestParam String materiaOrigenId,
            @RequestParam String grupoOrigenId,
            @RequestParam String materiaDestinoId,
            @RequestParam String grupoDestinoId) {
        SolicitudCambio solicitud = estudianteService.crearSolicitudCambio(id, materiaOrigenId, grupoOrigenId, materiaDestinoId, grupoDestinoId);
        return ResponseEntity.created(URI.create("/api/estudiantes/" + id + "/solicitudes/" + solicitud.getId())).body(solicitud);
    }
    @GetMapping("/Consulta todas las solicitudes de cambio{id}/solicitudes")
    public ResponseEntity<List<SolicitudCambio>> consultarSolicitudes(@PathVariable String id) {
        return ResponseEntity.ok(estudianteService.consultarSolicitudes(id));
    }
    @GetMapping("/Visualiza el semáforo académico completo{id}/semaforo")
    public ResponseEntity<Map<String, EstadoSemaforo>> verMiSemaforo(@PathVariable String id) {
        Map<String, EstadoSemaforo> semaforo = semaforoAcademicoService.visualizarSemaforoEstudiante(id);
        if (semaforo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(semaforo);
    }
    @GetMapping("/Consulta estado del semáforo para una materia específica{id}/semaforo/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> verEstadoMateria(@PathVariable String id, @PathVariable String materiaId) {
        Optional<EstadoSemaforo> estado = semaforoAcademicoService.consultarSemaforoMateria(id, materiaId);
        return estado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    /**
     * 37. Consultar avance del plan de estudios
     */
    @GetMapping("/{estudianteId}/avance-plan-estudios")
    public ResponseEntity<Map<String, Object>> consultarAvancePlanEstudios(@PathVariable String estudianteId) {
        Map<String, Object> avance = estudianteService.consultarAvancePlanEstudios(estudianteId);
        return ResponseEntity.ok(avance);
    }
    /**
     * 39. Asignar grupo a estudiante
     */
    @PutMapping("/{estudianteId}/asignar-grupo/{grupoId}")
    public ResponseEntity<Void> asignarGrupoAEstudiante(@PathVariable String estudianteId, @PathVariable String grupoId) {
        estudianteService.asignarGrupoAEstudiante(estudianteId, grupoId);
        return ResponseEntity.ok().build();
    }

    /**
     * Consultar horario del semestre actual del estudiante
     */
    @GetMapping("/{estudianteId}/horario-semestre-actual")
    public ResponseEntity<List<Grupo>> consultarHorarioSemestreActual(@PathVariable String estudianteId) {
        List<Grupo> horario = estudianteService.consultarHorarioSemestreActual(estudianteId);
        if (horario.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(horario);
    }

    /**
     * Consultar materias de semestres anteriores del estudiante
     */
    @GetMapping("/{estudianteId}/materias-semestres-anteriores")
    public ResponseEntity<List<Materia>> consultarMateriasSemestresAnteriores(@PathVariable String estudianteId) {
        List<Materia> materias = estudianteService.consultarMateriasSemestresAnteriores(estudianteId);
        if (materias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(materias);
    }

    /**
     * Consultar horario detallado del semestre actual del estudiante
     */
    @GetMapping("/{estudianteId}/horario-detallado-semestre-actual")
    public ResponseEntity<Map<String, Object>> consultarHorarioDetalladoSemestreActual(@PathVariable String estudianteId) {
        Map<String, Object> horarioDetallado = estudianteService.consultarHorarioDetalladoSemestreActual(estudianteId);
        if (horarioDetallado.containsKey("error")) {
            return ResponseEntity.badRequest().body(horarioDetallado);
        }
        return ResponseEntity.ok(horarioDetallado);
    }

    /**
     * Consultar resumen académico completo del estudiante
     */
    @GetMapping("/{estudianteId}/resumen-academico-completo")
    public ResponseEntity<Map<String, Object>> consultarResumenAcademicoCompleto(@PathVariable String estudianteId) {
        Map<String, Object> resumen = estudianteService.consultarResumenAcademicoCompleto(estudianteId);
        if (resumen.containsKey("error")) {
            return ResponseEntity.badRequest().body(resumen);
        }
        return ResponseEntity.ok(resumen);
    }
}