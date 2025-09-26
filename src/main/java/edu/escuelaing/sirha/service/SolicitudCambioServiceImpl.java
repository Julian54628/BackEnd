package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SolicitudCambioServiceImpl implements SolicitudCambioService {

    private final Map<String, SolicitudCambio> solicitudes = new HashMap<>();

    @Override
    public SolicitudCambio crear(SolicitudCambio solicitud) {
        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    @Override
    public Optional<SolicitudCambio> buscarPorId(String id) {
        return Optional.ofNullable(solicitudes.get(id));
    }

    @Override
    public List<SolicitudCambio> listarTodos() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public List<SolicitudCambio> buscarPorEstado(EstadoSolicitud estado) {
        List<SolicitudCambio> resultado = new ArrayList<>();
        for (SolicitudCambio s : solicitudes.values()) {
            if (estado.equals(s.getEstado())) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    @Override
    public List<SolicitudCambio> buscarPorEstudiante(String estudianteId) {
        List<SolicitudCambio> resultado = new ArrayList<>();
        for (SolicitudCambio s : solicitudes.values()) {
            if (estudianteId.equals(s.getEstudianteId())) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    @Override
    public SolicitudCambio actualizarEstado(String solicitudId, EstadoSolicitud estado) {
        SolicitudCambio solicitud = solicitudes.get(solicitudId);
        if (solicitud != null) {
            solicitud.setEstado(estado);
        }
        return solicitud;
    }

    @Override
    public void eliminarPorId(String id) {
        solicitudes.remove(id);
    }
}
