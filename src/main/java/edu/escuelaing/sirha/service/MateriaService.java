package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Materia;
import edu.escuelaing.sirha.model.Grupo;
import java.util.List;
import java.util.Optional;

public interface MateriaService {
    Materia crear(Materia materia);
    Optional<Materia> buscarPorId(String id);
    Optional<Materia> buscarPorCodigo(String codigo);
    List<Materia> listarTodos();
    void eliminarPorId(String id);
    Materia actualizar(String id, Materia materia);
    List<Grupo> consultarGruposDisponibles(String materiaId);
    boolean verificarDisponibilidad(String materiaId);
    void modificarCuposMateria(String materiaId, int nuevoCupo);
    Materia registrarMateriaConGrupos(Materia materia, List<Grupo> grupos);
    int consultarTotalInscritosPorMateria(String materiaId);
}

