package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Profesor;
import edu.escuelaing.sirha.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @GetMapping
    public List<Profesor> getAll() {
        return profesorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Profesor> getById(@PathVariable String id) {
        return profesorService.buscarPorId(id);
    }

    @PostMapping
    public Profesor create(@RequestBody Profesor profesor) {
        return profesorService.crear(profesor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        profesorService.eliminarPorId(id);
    }
}