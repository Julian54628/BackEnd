package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.service.DecanaturaService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @PutMapping("/{id}")
    public Decanatura actualizar(@PathVariable String id, @RequestBody Decanatura decanatura) {
        return decanaturaService.actualizar(id, decanatura);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        decanaturaService.eliminarPorId(id);
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
    
    @PutMapping("/{id}/otorgar-admin")
    public Decanatura otorgarPermisosAdministrador(@PathVariable String id) {
        return decanaturaService.otorgarPermisosAdministrador(id);
    }

    @PutMapping("/{id}/revocar-admin")
    public Decanatura revocarPermisosAdministrador(@PathVariable String id) {
        return decanaturaService.revocarPermisosAdministrador(id);
    }
    @PutMapping("/solicitudes/{solicitudId}/responder")
    public SolicitudCambio responderSolicitud(
            @PathVariable String solicitudId,
            @RequestParam EstadoSolicitud estado,
            @RequestParam(required = false) String respuesta,
            @RequestParam(required = false) String justificacion) {
        return decanaturaService.revisarSolicitud(solicitudId, estado, respuesta != null ? respuesta : "");
    }

    @GetMapping("/{decanaturaId}/solicitudes/prioridad")
    public List<SolicitudCambio> consultarSolicitudesPorPrioridad(@PathVariable String decanaturaId) {
        return decanaturaService.consultarSolicitudesPorDecanaturaYPrioridad(decanaturaId);
    }

    @GetMapping("/{decanaturaId}/solicitudes/fecha")
    public List<SolicitudCambio> consultarSolicitudesPorFechaLlegada(@PathVariable String decanaturaId) {
        return decanaturaService.consultarSolicitudesPorDecanaturaYFechaLlegada(decanaturaId);
    }

    @GetMapping("/{decanaturaId}/tasas-aprobacion")
    public Map<String, Object> consultarTasasAprobacionRechazo(@PathVariable String decanaturaId) {
        return decanaturaService.consultarTasaAprobacionRechazo(decanaturaId);
    }

    @GetMapping("/solicitudes/global-prioridad")
    public List<SolicitudCambio> consultarSolicitudesGlobalPorPrioridad(@RequestParam TipoPrioridad prioridad) {
        return new ArrayList<>(); // Implementación requiere inyección de SolicitudCambioService
    }
}