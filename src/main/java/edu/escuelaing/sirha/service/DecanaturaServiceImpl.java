package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.repository.RepositorioDecanatura;
import edu.escuelaing.sirha.repository.RepositorioSolicitudCambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DecanaturaServiceImpl implements DecanaturaService {

    @Autowired
    private RepositorioDecanatura repositorioDecanatura;

    @Autowired
    private RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Override
    public Decanatura crear(Decanatura decanatura) {
        return repositorioDecanatura.save(decanatura);
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
    public Decanatura actualizar(String id, Decanatura decanatura) {
        Optional<Decanatura> decanaturaExistente = repositorioDecanatura.findById(id);
        if (decanaturaExistente.isPresent()) {
            decanatura.setId(id);
            return repositorioDecanatura.save(decanatura);
        }
        return null;
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioDecanatura.deleteById(id);
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesPendientes() {
        return repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE);
    }

    @Override
    public SolicitudCambio revisarSolicitud(String solicitudId, EstadoSolicitud estado, String respuesta) {
        Optional<SolicitudCambio> solicitudOpt = repositorioSolicitudCambio.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            SolicitudCambio solicitud = solicitudOpt.get();
            solicitud.setEstado(estado);
            solicitud.setRespuesta(respuesta);
            solicitud.setFechaRespuesta(new Date());
            return repositorioSolicitudCambio.save(solicitud);
        }
        return null;
    }

    @Override
    public void aprobarSolicitudEspecial(String solicitudId) {
        Optional<SolicitudCambio> solicitudOpt = repositorioSolicitudCambio.findById(solicitudId);
        if (solicitudOpt.isPresent()) {
            SolicitudCambio solicitud = solicitudOpt.get();
            solicitud.setEstado(EstadoSolicitud.APROBADA);
            solicitud.setFechaRespuesta(new Date());
            repositorioSolicitudCambio.save(solicitud);
        }
    }

    @Override
    public Decanatura otorgarPermisosAdministrador(String decanaturaId) {
        Optional<Decanatura> decanaturaOpt = repositorioDecanatura.findById(decanaturaId);
        if (decanaturaOpt.isPresent()) {
            Decanatura decanatura = decanaturaOpt.get();
            decanatura.setEsAdministrador(true);
            return repositorioDecanatura.save(decanatura);
        }
        return null;
    }

    @Override
    public Decanatura revocarPermisosAdministrador(String decanaturaId) {
        Optional<Decanatura> decanaturaOpt = repositorioDecanatura.findById(decanaturaId);
        if (decanaturaOpt.isPresent()) {
            Decanatura decanatura = decanaturaOpt.get();
            decanatura.setEsAdministrador(false);
            return repositorioDecanatura.save(decanatura);
        }
        return null;
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesPorDecanaturaYPrioridad(String decanaturaId) {
        List<SolicitudCambio> solicitudesDecanatura = repositorioSolicitudCambio.findAll().stream()
                .filter(s -> s.getDecanaturaId() != null && s.getDecanaturaId().equals(decanaturaId)).toList();
        return solicitudesDecanatura.stream()
                .sorted((s1, s2) -> Integer.compare(s2.getPrioridad(), s1.getPrioridad())).toList();
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesPorDecanaturaYFechaLlegada(String decanaturaId) {
        List<SolicitudCambio> solicitudesDecanatura = repositorioSolicitudCambio.findAll().stream()
                .filter(s -> s.getDecanaturaId() != null && s.getDecanaturaId().equals(decanaturaId)).toList();
        return solicitudesDecanatura.stream().sorted((s1, s2) -> s1.getFechaCreacion().compareTo(s2.getFechaCreacion()))
                .toList();
    }

    @Override
    public Map<String, Object> consultarTasaAprobacionRechazo(String decanaturaId) {
        List<SolicitudCambio> solicitudesDecanatura = repositorioSolicitudCambio.findAll().stream()
                .filter(s -> s.getDecanaturaId() != null && s.getDecanaturaId().equals(decanaturaId)).toList();
        long total = solicitudesDecanatura.size();
        long aprobadas = solicitudesDecanatura.stream().filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count();
        long rechazadas = solicitudesDecanatura.stream().filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count();
        Map<String, Object> tasas = new HashMap<>();
        tasas.put("totalSolicitudes", total);
        tasas.put("aprobadas", aprobadas);
        tasas.put("rechazadas", rechazadas);
        tasas.put("tasaAprobacion", total > 0 ? (double) aprobadas / total * 100 : 0);
        tasas.put("tasaRechazo", total > 0 ? (double) rechazadas / total * 100 : 0);
        return tasas;
    }
}