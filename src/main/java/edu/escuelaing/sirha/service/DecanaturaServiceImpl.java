package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import edu.escuelaing.sirha.repository.RepositorioDecanatura;

@Service
public class DecanaturaServiceImpl implements DecanaturaService {

    private final Map<String, Decanatura> decanaturas = new HashMap<>();
    private final List<SolicitudCambio> solicitudes = new ArrayList<>();
    private final RepositorioDecanatura repositorioDecanatura;

    @Autowired
    public DecanaturaServiceImpl(RepositorioDecanatura repositorioDecanatura) {
        this.repositorioDecanatura = repositorioDecanatura;
    }

    @Override
    public Decanatura crear(Decanatura decanatura) {
        Decanatura saved = repositorioDecanatura.save(decanatura);
        decanaturas.put(saved.getId(), saved);
        return saved;
    }

    @Override
    public Optional<Decanatura> buscarPorId(String id) {
        return repositorioDecanatura.findById(id);
    }

    @Override
    public List<Decanatura> listarTodos() {
        return repositorioDecanatura.findAll();
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
        Optional<Decanatura> opt = repositorioDecanatura.findById(decanaturaId);
        if (opt.isEmpty()) return null;
        Decanatura d = opt.get();
        d.setEsAdministrador(true);
        Decanatura updated = repositorioDecanatura.save(d);
        return updated;
    }

    @Override
    public Decanatura revocarPermisosAdministrador(String decanaturaId) {
        Optional<Decanatura> opt = repositorioDecanatura.findById(decanaturaId);
        if (opt.isEmpty()) return null;
        Decanatura d = opt.get();
        d.setEsAdministrador(false);
        Decanatura updated = repositorioDecanatura.save(d);
        return updated;
    }
}
