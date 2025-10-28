package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.service.PeriodoCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoCambioController {

    @Autowired
    private PeriodoCambioService periodoService;

    @GetMapping
    public List<PeriodoCambio> getAll() {
        return periodoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoCambio> getById(@PathVariable String id) {
        return periodoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activo")
    public ResponseEntity<PeriodoCambio> getActivo() {
        return periodoService.obtenerPeriodoActivo()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PeriodoCambio create(@RequestBody PeriodoCambio periodo) {
        return periodoService.crear(periodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodoCambio> update(@PathVariable String id, @RequestBody PeriodoCambio periodo) {
        try {
            PeriodoCambio actualizado = periodoService.actualizar(id, periodo);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            periodoService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/vigente")
    public boolean estaPeriodoActivo() {
        return periodoService.estaPeriodoActivo();
    }

    @GetMapping("/vigentes")
    public List<PeriodoCambio> obtenerPeriodosVigentes() {
        return periodoService.obtenerPeriodosVigentes();
    }

    @GetMapping("/activo-actual")
    public ResponseEntity<PeriodoCambio> obtenerPeriodoActivoActual() {
        return periodoService.obtenerPeriodoActivoActual()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/futuros")
    public List<PeriodoCambio> obtenerPeriodosFuturos() {
        return periodoService.obtenerPeriodosFuturos();
    }

    @GetMapping("/tipo/{tipo}")
    public List<PeriodoCambio> obtenerPeriodosPorTipo(@PathVariable String tipo) {
        return periodoService.obtenerPeriodosPorTipo(tipo);
    }
}