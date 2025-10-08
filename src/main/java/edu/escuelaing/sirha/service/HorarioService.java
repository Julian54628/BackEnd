package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Horario;
import java.util.List;
import java.util.Optional;

public interface HorarioService {
    Horario crear(Horario horario);
    Optional<Horario> buscarPorId(String id);
    List<Horario> listarTodos();
    Horario actualizar(String id, Horario horario);
    void eliminarPorId(String id);
    List<Horario> consultarHorariosPorGrupo(String grupoId);
}