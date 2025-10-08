package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.repository.RepositorioSolicitudCambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudCambioServiceImpl implements SolicitudCambioService {

    @Autowired
    private RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Override
    public SolicitudCambio crear(SolicitudCambio solicitud) {
        return repositorioSolicitudCambio.save(solicitud);
    }

    @Override
    public Optional<SolicitudCambio> buscarPorId(String id) {
        return repositorioSolicitudCambio.findById(id);
    }

    @Override
    public List<SolicitudCambio> listarTodos() {
        return repositorioSolicitudCambio.findAll();
    }

    @Override
    public List<SolicitudCambio> buscarPorEstado(EstadoSolicitud estado) {
        return repositorioSolicitudCambio.findByEstado(estado);
    }

    @Override
    public List<SolicitudCambio> buscarPorEstudiante(String estudianteId) {
        return repositorioSolicitudCambio.findByEstudianteId(estudianteId);
    }

    @Override
    public SolicitudCambio actualizarEstado(String solicitudId, EstadoSolicitud estado) {
        Optional<SolicitudCambio> solicitudOpt = repositorioSolicitudCambio.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            SolicitudCambio solicitud = solicitudOpt.get();
            solicitud.setEstado(estado);
            return repositorioSolicitudCambio.save(solicitud);
        }
        return null;
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioSolicitudCambio.deleteById(id);
    }
}