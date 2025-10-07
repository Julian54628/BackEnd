package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;

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
    Optional<PeriodoCambio> getSemestreActual();
    Optional<PeriodoCambio> getSemestreAnterior();
}