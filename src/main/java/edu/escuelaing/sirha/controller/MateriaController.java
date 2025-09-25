package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Materia;
import edu.escuelaing.sirha.service.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @GetMapping
    public List<Materia> getAll() {
        return materiaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Materia> getById(@PathVariable String id) {
        return materiaService.buscarPorId(id);
    }

    @PostMapping
    public Materia create(@RequestBody Materia materia) {
        return materiaService.crear(materia);
    }

    @PutMapping("/{id}")
    public Materia update(@PathVariable String id, @RequestBody Materia materia) {
        return materiaService.actualizar(id, materia);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        materiaService.eliminarPorId(id);
    }
}