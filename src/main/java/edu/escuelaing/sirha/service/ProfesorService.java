package edu.escuelaing.sirha.service;

import java.util.List;
import java.util.Optional;

public interface ProfesorService {
    Profesor crear(Profesor profesor);
    Optional<Profesor> buscarPorId(String id);
    Optional<Profesor> buscarPorCodigo(String codigo);
    List<Profesor> listarTodos();
    Profesor actualizar(String id, Profesor profesor);
    void eliminarPorId(String id);
    List<Grupo> consultarGruposAsignados(String profesorId);
    Grupo asignarProfesorAGrupo(String profesorId, String grupoId);
    Grupo retirarProfesorDeGrupo(String grupoId);
}

