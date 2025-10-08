package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Administrador;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import edu.escuelaing.sirha.model.SolicitudCambio;

import java.util.List;
import java.util.Optional;

public interface AdministradorService {
    Optional<SemaforoAcademico> modificarEstadoMateriaSemaforo(String estudianteId, String materiaId, EstadoMateria nuevoEstado);
    Administrador crear(Administrador administrador);
    Optional<Administrador> buscarPorId(String id);
    List<Administrador> listarTodos();
    Grupo modificarCupoGrupo(String grupoId, int nuevoCupo);
    PeriodoCambio configurarPeriodo(PeriodoCambio periodo);
    List<SolicitudCambio> generarReportes();
    Administrador crearDesdeDecanatura(String decanaturaId);
}
