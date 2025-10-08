package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SemaforoVisualizacion;
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
}