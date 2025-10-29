package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.SolicitudCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudCambioController {

    @Autowired
    private SolicitudCambioService solicitudService;

    @GetMapping
    public List<SolicitudCambio> obtenerTodasLasSolicitudes() {
        return solicitudService.obtenerTodasLasSolicitudes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCambio> obtenerSolicitudPorId(@PathVariable String id) {
        return solicitudService.obtenerSolicitudPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SolicitudCambio> crear(@RequestBody SolicitudCambio solicitud) {
        try {
            SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<SolicitudCambio> actualizarEstado(@PathVariable String id, @RequestParam String estado) {
        try {
            EstadoSolicitud est = EstadoSolicitud.valueOf(estado.toUpperCase());
            SolicitudCambio actualizada = solicitudService.actualizarEstado(id, est);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable String id) {
        try {
            solicitudService.eliminarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable String id) {
        try {
            solicitudService.eliminarSolicitud(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/filtrar")
    public ResponseEntity<?> listSolicitudes(
            @RequestParam(required = false) String decanaturaId,
            @RequestParam(required = false) String prioridad,
            @RequestParam(required = false, defaultValue = "fecha") String ordenarPor,
            @RequestParam(required = false, defaultValue = "asc") String orden) {

        List<SolicitudCambio> res = solicitudService.obtenerTodasLasSolicitudes();

        if (decanaturaId != null && !decanaturaId.isBlank()) {
            res = res.stream()
                    .filter(s -> decanaturaId.equals(s.getDecanaturaId()))
                    .collect(Collectors.toList());
        }

        if (prioridad != null && !prioridad.isBlank()) {
            try {
                TipoPrioridad p = TipoPrioridad.valueOf(prioridad.toUpperCase());
                res = res.stream()
                        .filter(s -> p == s.getTipoPrioridad())
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body("Prioridad inválida");
            }
        }

        if ("fecha".equalsIgnoreCase(ordenarPor)) {
            Comparator<SolicitudCambio> cmp = Comparator.comparing(SolicitudCambio::getFechaCreacion, Comparator.nullsLast(Date::compareTo));
            if ("desc".equalsIgnoreCase(orden)) {
                cmp = cmp.reversed();
            }
            res.sort(cmp);
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}/decisiones")
    public ResponseEntity<?> decisionHistory(@PathVariable String id) {
        List<String> history = solicitudService.obtenerHistorialPorSolicitud(id);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSolicitud(@PathVariable String id, @RequestBody SolicitudCambio payload) {
        try {
            if (payload.getId() == null || payload.getId().isEmpty()) {
                payload.setId(id);
            } else if (!id.equals(payload.getId())) {
                return ResponseEntity.badRequest().body("El id del path debe coincidir con el id del body");
            }
            SolicitudCambio updated = solicitudService.actualizar(payload);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/responder")
    public ResponseEntity<?> responderSolicitud(@PathVariable String id, @RequestBody Map<String, Object> action) {
        String accion = action.getOrDefault("accion", "").toString().toUpperCase();
        String justificacion = action.getOrDefault("justificacion", null) != null
                ? action.get("justificacion").toString()
                : null;
        try {
            switch (accion) {
                case "APROBAR":
                    return ResponseEntity.ok(solicitudService.aprobarSolicitud(id, justificacion));
                case "RECHAZAR":
                    return ResponseEntity.ok(solicitudService.rechazarSolicitud(id, justificacion));
                case "SOLICITAR_INFO":
                    return ResponseEntity.ok(solicitudService.actualizarEstadoSolicitud(id, EstadoSolicitud.EN_REVISION, "Solicitar información", justificacion));
                default:
                    return ResponseEntity.badRequest().body("Acción inválida. Use APROBAR, RECHAZAR o SOLICITAR_INFO");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/por-prioridad")
    public ResponseEntity<?> solicitudesPorPrioridad(@RequestParam String prioridad) {
        try {
            TipoPrioridad p = TipoPrioridad.valueOf(prioridad.toUpperCase());
            List<SolicitudCambio> res = solicitudService.obtenerSolicitudesPorPrioridad(p);
            return ResponseEntity.ok(res);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Prioridad inválida");
        }
    }
}