package edu.escuelaing.sirha.service;

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
    Grupo inscribirEstudianteEnGrupo(String grupoId, String estudianteId);
    Grupo retirarEstudianteDeGrupo(String grupoId, String estudianteId);
    boolean asignarMateriaAEstudiante(String materiaId, String estudianteId);
    boolean retirarMateriaDeEstudiante(String materiaId, String estudianteId);
}

