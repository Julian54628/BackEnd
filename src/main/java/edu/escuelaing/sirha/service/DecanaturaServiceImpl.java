package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DecanaturaServiceImpl implements DecanaturaService {

    private final Map<String, Decanatura> decanaturas = new HashMap<>();
    private final List<SolicitudCambio> solicitudes = new ArrayList<>();

    @Override
    public Decanatura crear(Decanatura decanatura) {
        decanaturas.put(decanatura.getId(), decanatura);
        return decanatura;
    }

    @Override
    public Optional<Decanatura> buscarPorId(String id) {
        return Optional.ofNullable(decanaturas.get(id));
    }

    @Override
    public List<Decanatura> listarTodos() {
        return new ArrayList<>(decanaturas.values());
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesPendientes() {
        List<SolicitudCambio> pendientes = new ArrayList<>();
        for (SolicitudCambio s : solicitudes) {
            if (s.getEstado() == EstadoSolicitud.PENDIENTE) {
                pendientes.add(s);
            }
        }
        return pendientes;
    }

    @Override
    public SolicitudCambio revisarSolicitud(String solicitudId, EstadoSolicitud estado, String respuesta) {
        for (SolicitudCambio s : solicitudes) {
            if (s.getId().equals(solicitudId)) {
                s.setEstado(estado);
                s.setRespuesta(respuesta);
                return s;
            }
        }
        return null;
    }

    @Override
    public void aprobarSolicitudEspecial(String solicitudId) {
        for (SolicitudCambio s : solicitudes) {
            if (s.getId().equals(solicitudId)) {
                s.setEstado(EstadoSolicitud.APROBADA);
            }
        }
    }

    @Override
    public Decanatura otorgarPermisosAdministrador(String decanaturaId) {
        Decanatura decanatura = decanaturas.get(decanaturaId);
        if (decanatura != null) {
            decanatura.setEsAdministrador(true);
        }
        return decanatura;
    }

    @Override
    public Decanatura revocarPermisosAdministrador(String decanaturaId) {
        Decanatura decanatura = decanaturas.get(decanaturaId);
        if (decanatura != null) {
            decanatura.setEsAdministrador(false);
        }
        return decanatura;
    }
}
