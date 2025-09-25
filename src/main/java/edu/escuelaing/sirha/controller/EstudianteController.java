package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public List<Estudiante> getAll() {
        return estudianteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Estudiante> getById(@PathVariable String id) {
        return estudianteService.buscarPorId(id);
    }

    @PostMapping
    public Estudiante create(@RequestBody Estudiante estudiante) {
        return estudianteService.crear(estudiante);
    }

    @PutMapping("/{id}")
    public Estudiante update(@PathVariable String id, @RequestBody Estudiante estudiante) {
        return estudianteService.actualizar(id, estudiante);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        estudianteService.eliminarPorId(id);
    }
}