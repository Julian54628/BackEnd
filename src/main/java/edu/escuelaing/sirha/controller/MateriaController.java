package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Grupo;
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
    public MateriaService materiaService;

    public Materia crear(Materia materia) { return materiaService.crear(materia); }
    public Optional<Materia> buscarPorId(String id) { return materiaService.buscarPorId(id); }
    public Optional<Materia> buscarPorCodigo(String codigo) { return materiaService.buscarPorCodigo(codigo); }
    public List<Materia> listarTodos() { return materiaService.listarTodos(); }
    public Materia actualizar(String id, Materia materia) { return materiaService.actualizar(id, materia); }
    public void eliminarPorId(String id) { materiaService.eliminarPorId(id); }
    public List<Grupo> consultarGruposDisponibles(String materiaId) { return materiaService.consultarGruposDisponibles(materiaId); }
    public boolean verificarDisponibilidad(String materiaId) { return materiaService.verificarDisponibilidad(materiaId); }

    @PostMapping("/grupos/{grupoId}/inscribir/{estudianteId}")
    public Grupo inscribirEstudianteEnGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        return materiaService.inscribirEstudianteEnGrupo(grupoId, estudianteId);
    }
    public Grupo retirarEstudianteDeGrupo(@PathVariable String grupoId, @PathVariable String estudianteId) {
        return materiaService.retirarEstudianteDeGrupo(grupoId, estudianteId);
    }

    @PostMapping("/{materiaId}/asignar-estudiante/{estudianteId}")
    public boolean asignarMateriaAEstudiante(@PathVariable String materiaId, @PathVariable String estudianteId) {
        return materiaService.asignarMateriaAEstudiante(materiaId, estudianteId);
    }

    @DeleteMapping("/{materiaId}/retirar-estudiante/{estudianteId}")
    public boolean retirarMateriaDeEstudiante(@PathVariable String materiaId, @PathVariable String estudianteId) {
        return materiaService.retirarMateriaDeEstudiante(materiaId, estudianteId);
    }
}