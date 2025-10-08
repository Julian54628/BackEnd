package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    public GrupoService grupoService;

    @GetMapping
    public List<Grupo> getAll() {
        return grupoService.listarTodos();
    }

    @GetMapping("/Obtiene un grupo específico por su identificador{id}")
    public Optional<Grupo> getById(@PathVariable String id) {
        return grupoService.buscarPorId(id);
    }

    @PostMapping
    public Grupo create(@RequestBody Grupo grupo) {
        return grupoService.crear(grupo);
    }

    @DeleteMapping("/Elimina un grupo del sistema por su identificador{id}")
    public void delete(@PathVariable String id) {
        grupoService.eliminarPorId(id);
    }

    @PutMapping("/Actualiza el cupo disponible de un grupo{id}/cupo")
    public Grupo updateCupo(@PathVariable String id, @RequestParam int nuevoCupo) {
        return grupoService.actualizarCupo(id, nuevoCupo);
    }

    @GetMapping("/Verifica si un grupo tiene cupos disponibles{id}/cupo-disponible")
    public boolean verificarCupoDisponible(@PathVariable String id) {
        return grupoService.verificarCupoDisponible(id);
    }

    @GetMapping("/Consulta la carga académica de un grupo{id}/carga-academica")
    public float consultarCargaAcademica(@PathVariable String id) {
        return grupoService.consultarCargaAcademica(id);
    }

    @GetMapping("/Consulta la lista de estudiantes inscritos en un grupo{id}/estudiantes")
    public List<Estudiante> consultarEstudiantesInscritos(@PathVariable String id) {
        return grupoService.consultarEstudiantesInscritos(id);
    }


}