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
    public MateriaService materiaService;

    @GetMapping
    public List<Materia> listarTodos() {
        return materiaService.listarTodos();
    }

    @GetMapping("/Busca una materia específica por su identificador{id}")
    public Optional<Materia> buscarPorId(@PathVariable String id) {
        return materiaService.buscarPorId(id);
    }
    @GetMapping("/Busca una materia por su codigo/{codigo}")
    public Optional<Materia> buscarPorCodigo(@PathVariable String codigo) {
        return materiaService.buscarPorCodigo(codigo);
    }
    @PostMapping
    public Materia crear(@RequestBody Materia materia) {
        return materiaService.crear(materia);
    }
    @PutMapping("/Actualiza la información de una materia{id}")
    public Materia actualizar(@PathVariable String id, @RequestBody Materia materia) {
        return materiaService.actualizar(id, materia);
    }
    @DeleteMapping("/Elimina una materia del sistema por su identificador{id}")
    public void eliminarPorId(@PathVariable String id) {
        materiaService.eliminarPorId(id);
    }
    @GetMapping("/Consulta los grupos disponibles para una materia{id}/grupos")
    public List<Grupo> consultarGruposDisponibles(@PathVariable String id) {
        return materiaService.consultarGruposDisponibles(id);
    }
    @GetMapping("/Verifica la disponibilidad de una materia{id}/disponibilidad")
    public boolean verificarDisponibilidad(@PathVariable String id) {
        return materiaService.verificarDisponibilidad(id);
    }
    /**
     * 30. Modificar cupos de una materia
     */
    @PutMapping("/{materiaId}/modificar-cupos")
    public void modificarCuposMateria(@PathVariable String materiaId, @RequestParam int nuevoCupo) {
        materiaService.modificarCuposMateria(materiaId, nuevoCupo);
    }
    /**
     * 31. Registro materias grupos y cupos
     */
    @PostMapping("/registrar-completo")
    public Materia registrarMateriaConGrupos(@RequestBody Materia materia, @RequestBody List<Grupo> grupos) {
        return materiaService.registrarMateriaConGrupos(materia, grupos);
    }
    /**
     * 33. Consultar total inscritos por grupo de una materia
     */
    @GetMapping("/{materiaId}/total-inscritos")
    public int consultarTotalInscritosPorMateria(@PathVariable String materiaId) {
        return materiaService.consultarTotalInscritosPorMateria(materiaId);
    }
}