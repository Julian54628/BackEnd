package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Profesor;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    public ProfesorService profesorService;

    @GetMapping
    public List<Profesor> listarTodos() {
        return profesorService.listarTodos();
    }

    @GetMapping("/Busca un profesor específico por su{id}")
    public Optional<Profesor> buscarPorId(@PathVariable String id) {
        return profesorService.buscarPorId(id);
    }

    @GetMapping("/Busca un profesor por su codigo/{codigo}")
    public Optional<Profesor> buscarPorCodigo(@PathVariable String codigo) {
        return profesorService.buscarPorCodigo(codigo);
    }

    @PostMapping
    public Profesor crear(@RequestBody Profesor profesor) {
        return profesorService.crear(profesor);
    }

    @DeleteMapping("/Elimina un profesor del sistema por su{id}")
    public void eliminarPorId(@PathVariable String id) {
        profesorService.eliminarPorId(id);
    }

    @GetMapping("/Consulta los grupos académicos asignados a un profesor{id}/grupos")
    public List<Grupo> consultarGruposAsignados(@PathVariable String id) {
        return profesorService.consultarGruposAsignados(id);
    }
    /**
     * 32. Asignación de un profesor a un grupo
     */
    @PutMapping("/{profesorId}/asignar-grupo/{grupoId}")
    public Grupo asignarProfesorAGrupo(@PathVariable String profesorId, @PathVariable String grupoId) {
        return profesorService.asignarProfesorAGrupo(profesorId, grupoId);
    }
    /**
     * 32. Retiro de un profesor de un grupo
     */
    @PutMapping("/retirar-grupo/{grupoId}")
    public Grupo retirarProfesorDeGrupo(@PathVariable String grupoId) {
        return profesorService.retirarProfesorDeGrupo(grupoId);
    }
}