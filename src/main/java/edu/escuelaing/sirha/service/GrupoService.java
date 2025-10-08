package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Estudiante;
import java.util.List;
import java.util.Optional;

public interface GrupoService {
    Grupo crear(Grupo grupo);
    Optional<Grupo> buscarPorId(String id);
    List<Grupo> listarTodos();
    void eliminarPorId(String id);
    Grupo actualizarCupo(String grupoId, int nuevoCupo);
    boolean verificarCupoDisponible(String grupoId);
    float consultarCargaAcademica(String grupoId);
    List<Estudiante> consultarEstudiantesInscritos(String grupoId);
    List<Grupo> obtenerGruposConAlertaCapacidad(double porcentajeAlerta);
}

