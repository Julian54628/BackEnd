package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.SolicitudCambio;
import java.util.List;
import java.util.Optional;

public interface RepositorioSolicitudCambio {
    SolicitudCambio guardar(SolicitudCambio solicitud);
    Optional<SolicitudCambio> buscarPorCodigo(String codigo);
    Optional<SolicitudCambio> buscarPorId(String id);
    List<SolicitudCambio> listarTodos();
    void eliminarPorId(String id);
}

