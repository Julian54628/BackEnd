package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Horario;
import edu.escuelaing.sirha.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los horarios de los grupos.
 * 34. Administrar horarios por grupos: crearlos, actualizarlos, borrar y consultar
 */
@RestController
@RequestMapping("/api/horarios")
public class HorarioController {
    @Autowired
    public HorarioService horarioService;
    @PostMapping
    public Horario crear(@RequestBody Horario horario) {
        return horarioService.crear(horario);
    }
    @GetMapping("/{id}")
    public Optional<Horario> buscarPorId(@PathVariable String id) {
        return horarioService.buscarPorId(id);
    }
    @GetMapping
    public List<Horario> listarTodos() {
        return horarioService.listarTodos();
    }
    @PutMapping("/{id}")
    public Horario actualizar(@PathVariable String id, @RequestBody Horario horario) {
        return horarioService.actualizar(id, horario);
    }
    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable String id) {
        horarioService.eliminarPorId(id);
    }
    @GetMapping("/grupo/{grupoId}")
    public List<Horario> consultarHorariosPorGrupo(@PathVariable String grupoId) {
        return horarioService.consultarHorariosPorGrupo(grupoId);
    }
}