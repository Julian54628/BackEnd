package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
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
    private DecanaturaService decanaturaService;

    @Autowired
    private SemaforoAcademicoService semaforoService;

    @GetMapping
    public List<Decanatura> getAll() {
        return decanaturaService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Decanatura> getById(@PathVariable String id) {
        return decanaturaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Decanatura create(@RequestBody Decanatura decanatura) {
        return decanaturaService.crear(decanatura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Decanatura> actualizar(@PathVariable String id, @RequestBody Decanatura decanatura) {
        try {
            Decanatura actualizada = decanaturaService.actualizar(id, decanatura);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        try {
            decanaturaService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/solicitudes/pendientes")
    public List<SolicitudCambio> consultarSolicitudesPendientes() {
        return decanaturaService.consultarSolicitudesPendientes();
    }

    @PutMapping("/solicitudes/{solicitudId}/revisar")
    public ResponseEntity<SolicitudCambio> revisarSolicitud(
            @PathVariable String solicitudId,
            @RequestParam EstadoSolicitud estado,
            @RequestParam String respuesta) {
        try {
            SolicitudCambio solicitud = decanaturaService.revisarSolicitud(solicitudId, estado, respuesta);
            return ResponseEntity.ok(solicitud);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/solicitudes/{solicitudId}/aprobar-especial")
    public ResponseEntity<Void> aprobarSolicitudEspecial(@PathVariable String solicitudId) {
        try {
            decanaturaService.aprobarSolicitudEspecial(solicitudId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/otorgar-admin")
    public ResponseEntity<Decanatura> otorgarPermisosAdministrador(@PathVariable String id) {
        try {
            Decanatura decanatura = decanaturaService.otorgarPermisosAdministrador(id);
            return ResponseEntity.ok(decanatura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/revocar-admin")
    public ResponseEntity<Decanatura> revocarPermisosAdministrador(@PathVariable String id) {
        try {
            Decanatura decanatura = decanaturaService.revocarPermisosAdministrador(id);
            return ResponseEntity.ok(decanatura);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/solicitudes/{solicitudId}/responder")
    public ResponseEntity<SolicitudCambio> responderSolicitud(
            @PathVariable String solicitudId,
            @RequestParam EstadoSolicitud estado,
            @RequestParam(required = false) String respuesta,
            @RequestParam(required = false) String justificacion) {
        try {
            String respuestaFinal = respuesta != null ? respuesta : "";
            SolicitudCambio solicitud = decanaturaService.revisarSolicitud(solicitudId, estado, respuestaFinal);
            return ResponseEntity.ok(solicitud);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
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
}