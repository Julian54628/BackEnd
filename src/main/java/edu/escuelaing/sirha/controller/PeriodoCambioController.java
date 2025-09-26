package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.service.PeriodoCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoCambioController {

    @Autowired
    public PeriodoCambioService periodoService;

    @GetMapping
    public List<PeriodoCambio> getAll() {
        return periodoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<PeriodoCambio> getById(@PathVariable String id) {
        return periodoService.buscarPorId(id);
    }

    @GetMapping("/activo")
    public Optional<PeriodoCambio> getActivo() {
        return periodoService.obtenerPeriodoActivo();
    }

    @PostMapping
    public PeriodoCambio create(@RequestBody PeriodoCambio periodo) {
        return periodoService.crear(periodo);
    }

    @PutMapping("/{id}")
    public PeriodoCambio update(@PathVariable String id, @RequestBody PeriodoCambio periodo) {
        return periodoService.actualizar(id, periodo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        periodoService.eliminarPorId(id);
    }
}