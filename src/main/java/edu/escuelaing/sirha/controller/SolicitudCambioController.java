package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.service.SolicitudCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudCambioController {

    @Autowired
    private SolicitudCambioService solicitudService;

    @GetMapping
    public List<SolicitudCambio> getAll() {
        return solicitudService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<SolicitudCambio> getById(@PathVariable String id) {
        return solicitudService.buscarPorId(id);
    }

    @PostMapping
    public SolicitudCambio create(@RequestBody SolicitudCambio solicitud) {
        return solicitudService.crear(solicitud);
    }

    @PutMapping("/{id}/estado")
    public SolicitudCambio updateEstado(@PathVariable String id, @RequestParam String estado) {
        return solicitudService.actualizarEstado(id, EstadoSolicitud.valueOf(estado));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        solicitudService.eliminarPorId(id);
    }
}