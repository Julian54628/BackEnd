package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    @GetMapping
    public List<Grupo> getAll() {
        return grupoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Grupo> getById(@PathVariable String id) {
        return grupoService.buscarPorId(id);
    }

    @PostMapping
    public Grupo create(@RequestBody Grupo grupo) {
        return grupoService.crear(grupo);
    }

    @PutMapping("/{id}/cupo")
    public Grupo updateCupo(@PathVariable String id, @RequestParam int nuevoCupo) {
        return grupoService.actualizarCupo(id, nuevoCupo);
    }
}