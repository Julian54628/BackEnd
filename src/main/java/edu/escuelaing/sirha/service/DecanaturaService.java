package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import java.util.List;
import java.util.Optional;

public interface DecanaturaService {
    Decanatura crear(Decanatura decanatura);
    Optional<Decanatura> buscarPorId(String id);
    List<Decanatura> listarTodos();
    List<SolicitudCambio> consultarSolicitudesPendientes();
    SolicitudCambio revisarSolicitud(String solicitudId, EstadoSolicitud estado, String respuesta);
    void aprobarSolicitudEspecial(String solicitudId);
    Decanatura otorgarPermisosAdministrador(String decanaturaId);
    Decanatura revocarPermisosAdministrador(String decanaturaId);
}


