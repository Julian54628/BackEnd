package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Horario;
import edu.escuelaing.sirha.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @PostMapping
    public Horario crear(@RequestBody Horario horario) {
        return horarioService.crear(horario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horario> buscarPorId(@PathVariable String id) {
        return horarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Horario> listarTodos() {
        return horarioService.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizar(@PathVariable String id, @RequestBody Horario horario) {
        try {
            Horario actualizado = horarioService.actualizar(id, horario);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable String id) {
        try {
            horarioService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/grupo/{grupoId}")
    public List<Horario> consultarHorariosPorGrupo(@PathVariable String grupoId) {
        return horarioService.consultarHorariosPorGrupo(grupoId);
    }

    @GetMapping("/dia/{diaSemana}")
    public List<Horario> consultarHorariosPorDia(@PathVariable String diaSemana) {
        return horarioService.consultarHorariosPorDia(diaSemana);
    }

    @GetMapping("/salon/{salon}")
    public List<Horario> consultarHorariosPorSalon(@PathVariable String salon) {
        return horarioService.consultarHorariosPorSalon(salon);
    }
}