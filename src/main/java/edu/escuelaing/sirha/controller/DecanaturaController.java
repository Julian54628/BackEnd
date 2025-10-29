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
}