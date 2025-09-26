package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Materia;
import edu.escuelaing.sirha.model.Grupo;
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
    public List<Materia> listarTodos() {
        return materiaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Materia> buscarPorId(@PathVariable String id) {
        return materiaService.buscarPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
    public Optional<Materia> buscarPorCodigo(@PathVariable String codigo) {
        return materiaService.buscarPorCodigo(codigo);
    }

    @PostMapping
    public Materia crear(@RequestBody Materia materia) {
        return materiaService.crear(materia);
    }

    @PutMapping("/{id}")
    public Materia actualizar(@PathVariable String id, @RequestBody Materia materia) {
        return materiaService.actualizar(id, materia);
    }

    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable String id) {
        materiaService.eliminarPorId(id);
    }

    @GetMapping("/{id}/grupos")
    public List<Grupo> consultarGruposDisponibles(@PathVariable String id) {
        return materiaService.consultarGruposDisponibles(id);
    }

    @GetMapping("/{id}/disponibilidad")
    public boolean verificarDisponibilidad(@PathVariable String id) {
        return materiaService.verificarDisponibilidad(id);
    }
}