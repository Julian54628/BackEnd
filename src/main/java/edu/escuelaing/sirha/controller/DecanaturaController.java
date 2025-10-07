package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.service.DecanaturaService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/decanaturas")
public class DecanaturaController {

    @Autowired
    public DecanaturaService decanaturaService;

    @Autowired
    public SemaforoAcademicoService semaforoService;

    @GetMapping
    public List<Decanatura> getAll() {
        return decanaturaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Decanatura> getById(@PathVariable String id) {
        return decanaturaService.buscarPorId(id);
    }

    @PostMapping
    public Decanatura create(@RequestBody Decanatura decanatura) {
        return decanaturaService.crear(decanatura);
    }

    @GetMapping("/solicitudes/pendientes")
    public List<SolicitudCambio> consultarSolicitudesPendientes() {
        return decanaturaService.consultarSolicitudesPendientes();
    }

    @PutMapping("/solicitudes/{solicitudId}/revisar")
    public SolicitudCambio revisarSolicitud(
            @PathVariable String solicitudId,
            @RequestParam EstadoSolicitud estado,
            @RequestParam String respuesta) {
        return decanaturaService.revisarSolicitud(solicitudId, estado, respuesta);
    }

    @PutMapping("/solicitudes/{solicitudId}/aprobar-especial")
    public void aprobarSolicitudEspecial(@PathVariable String solicitudId) {
        decanaturaService.aprobarSolicitudEspecial(solicitudId);
    }
    @GetMapping("/semaforo/estudiante/{estudianteId}")
    public ResponseEntity<Map<String, EstadoSemaforo>> visualizarSemaforoEstudiante(
            @PathVariable String estudianteId) {
        Map<String, EstadoSemaforo> semaforo = semaforoService.visualizarSemaforoEstudiante(estudianteId);
        return ResponseEntity.ok(semaforo);
    }

    @GetMapping("/semaforo/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> consultarSemaforoMateria(
            @PathVariable String estudianteId,
            @PathVariable String materiaId) {

        Optional<EstadoSemaforo> estado = semaforoService.consultarSemaforoMateria(estudianteId, materiaId);
        return estado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/otorgar-admin")
    public Decanatura otorgarPermisosAdministrador(@PathVariable String id) {
        return decanaturaService.otorgarPermisosAdministrador(id);
    }

    @PutMapping("/{id}/revocar-admin")
    public Decanatura revocarPermisosAdministrador(@PathVariable String id) {
        return decanaturaService.revocarPermisosAdministrador(id);
    }
}