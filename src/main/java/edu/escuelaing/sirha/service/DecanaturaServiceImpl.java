package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioDecanatura;
import edu.escuelaing.sirha.repository.RepositorioSolicitudCambio;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DecanaturaServiceImpl implements DecanaturaService {

    private final RepositorioDecanatura repositorioDecanatura;
    private final RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    public DecanaturaServiceImpl(RepositorioDecanatura repositorioDecanatura,
                                 RepositorioSolicitudCambio repositorioSolicitudCambio) {
        this.repositorioDecanatura = repositorioDecanatura;
        this.repositorioSolicitudCambio = repositorioSolicitudCambio;
    }

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
        if (!repositorioDecanatura.existsById(id)) {
            throw new IllegalArgumentException("Decanatura no encontrada: " + id);
        }
        decanatura.setId(id);
        return repositorioDecanatura.save(decanatura);
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
        return repositorioSolicitudCambio.findById(solicitudId)
                .map(solicitud -> {
                    solicitud.setEstado(estado);
                    solicitud.setRespuesta(respuesta);
                    solicitud.setFechaRespuesta(new Date());
                    return repositorioSolicitudCambio.save(solicitud);
                })
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));
    }

    @Override
    public void aprobarSolicitudEspecial(String solicitudId) {
        repositorioSolicitudCambio.findById(solicitudId)
                .ifPresent(solicitud -> {
                    solicitud.setEstado(EstadoSolicitud.APROBADA);
                    solicitud.setFechaRespuesta(new Date());
                    repositorioSolicitudCambio.save(solicitud);
                });
    }

    @Override
    public Decanatura otorgarPermisosAdministrador(String decanaturaId) {
        return repositorioDecanatura.findById(decanaturaId)
                .map(decanatura -> {
                    decanatura.setEsAdministrador(true);
                    return repositorioDecanatura.save(decanatura);
                })
                .orElseThrow(() -> new IllegalArgumentException("Decanatura no encontrada: " + decanaturaId));
    }

    @Override
    public Decanatura revocarPermisosAdministrador(String decanaturaId) {
        return repositorioDecanatura.findById(decanaturaId)
                .map(decanatura -> {
                    decanatura.setEsAdministrador(false);
                    return repositorioDecanatura.save(decanatura);
                })
                .orElseThrow(() -> new IllegalArgumentException("Decanatura no encontrada: " + decanaturaId));
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesPorDecanaturaYPrioridad(String decanaturaId) {
        return repositorioSolicitudCambio.findByDecanaturaId(decanaturaId).stream()
                .sorted(Comparator.comparing(SolicitudCambio::getPrioridad).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesPorDecanaturaYFechaLlegada(String decanaturaId) {
        return repositorioSolicitudCambio.findByDecanaturaId(decanaturaId).stream()
                .sorted(Comparator.comparing(SolicitudCambio::getFechaCreacion))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> consultarTasaAprobacionRechazo(String decanaturaId) {
        List<SolicitudCambio> solicitudesDecanatura = repositorioSolicitudCambio.findByDecanaturaId(decanaturaId);

        long total = solicitudesDecanatura.size();
        long aprobadas = solicitudesDecanatura.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count();
        long rechazadas = solicitudesDecanatura.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count();

        Map<String, Object> tasas = new HashMap<>();
        tasas.put("totalSolicitudes", total);
        tasas.put("aprobadas", aprobadas);
        tasas.put("rechazadas", rechazadas);
        tasas.put("tasaAprobacion", total > 0 ? (double) aprobadas / total * 100 : 0);
        tasas.put("tasaRechazo", total > 0 ? (double) rechazadas / total * 100 : 0);

        return tasas;
    }
}