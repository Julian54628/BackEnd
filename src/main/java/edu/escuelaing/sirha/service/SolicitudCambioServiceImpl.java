package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudCambioServiceImpl implements SolicitudCambioService {

    private final RepositorioSolicitudCambio repositorioSolicitudCambio;
    private final RepositorioEstudiante repositorioEstudiante;
    private final RepositorioDecanatura repositorioDecanatura;
    private final RepositorioMateria repositorioMateria;
    private final RepositorioPeriodoCambio repositorioPeriodoCambio;

    @Autowired
    public SolicitudCambioServiceImpl(RepositorioSolicitudCambio repositorioSolicitudCambio,
                                      RepositorioEstudiante repositorioEstudiante,
                                      RepositorioDecanatura repositorioDecanatura,
                                      RepositorioMateria repositorioMateria,
                                      RepositorioPeriodoCambio repositorioPeriodoCambio) {
        this.repositorioSolicitudCambio = repositorioSolicitudCambio;
        this.repositorioEstudiante = repositorioEstudiante;
        this.repositorioDecanatura = repositorioDecanatura;
        this.repositorioMateria = repositorioMateria;
        this.repositorioPeriodoCambio = repositorioPeriodoCambio;
    }

    @Override
    public SolicitudCambio crear(SolicitudCambio solicitud) {
        return crearSolicitud(solicitud);
    }

    @Override
    public SolicitudCambio crearSolicitud(SolicitudCambio solicitud) {
        if (!validarSolicitud(solicitud)) {
            throw new IllegalArgumentException("La solicitud no es válida");
        }

        if (!puedeCrearSolicitud(solicitud.getEstudianteId(), solicitud.getMateriaDestinoId())) {
            throw new IllegalArgumentException("El estudiante ya tiene una solicitud activa para esta materia");
        }

        if (!estaEnPeriodoActivo()) {
            throw new IllegalArgumentException("No hay un período activo para crear solicitudes");
        }

        asignarDecanatura(solicitud);

        if (solicitud.getId() == null || solicitud.getId().isEmpty()) {
            solicitud.setId(UUID.randomUUID().toString());
        }

        solicitud.setFechaCreacion(new Date());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        return repositorioSolicitudCambio.save(solicitud);
    }

    @Override
    public Optional<SolicitudCambio> buscarPorId(String id) {
        return obtenerSolicitudPorId(id);
    }

    @Override
    public Optional<SolicitudCambio> obtenerSolicitudPorId(String id) {
        return repositorioSolicitudCambio.findById(id);
    }

    @Override
    public List<SolicitudCambio> listarTodos() {
        return obtenerTodasLasSolicitudes();
    }

    @Override
    public List<SolicitudCambio> obtenerTodasLasSolicitudes() {
        return repositorioSolicitudCambio.findAll();
    }

    @Override
    public SolicitudCambio actualizar(SolicitudCambio solicitud) {
        if (!repositorioSolicitudCambio.existsById(solicitud.getId())) {
            throw new IllegalArgumentException("Solicitud no encontrada: " + solicitud.getId());
        }
        return repositorioSolicitudCambio.save(solicitud);
    }

    @Override
    public void eliminarPorId(String id) {
        eliminarSolicitud(id);
    }

    @Override
    public void eliminarSolicitud(String id) {
        repositorioSolicitudCambio.deleteById(id);
    }

    @Override
    public List<SolicitudCambio> buscarPorEstado(EstadoSolicitud estado) {
        return obtenerSolicitudesPorEstado(estado);
    }

    @Override
    public List<SolicitudCambio> obtenerSolicitudesPorEstado(EstadoSolicitud estado) {
        return repositorioSolicitudCambio.findByEstado(estado);
    }

    @Override
    public List<SolicitudCambio> buscarPorEstudiante(String estudianteId) {
        return obtenerSolicitudesPorEstudiante(estudianteId);
    }

    @Override
    public List<SolicitudCambio> obtenerSolicitudesPorEstudiante(String estudianteId) {
        return repositorioSolicitudCambio.findByEstudianteId(estudianteId);
    }

    @Override
    public List<SolicitudCambio> obtenerSolicitudesPorDecanatura(String decanaturaId) {
        return repositorioSolicitudCambio.findByDecanaturaId(decanaturaId);
    }

    @Override
    public List<SolicitudCambio> obtenerSolicitudesPorPrioridad(TipoPrioridad prioridad) {
        return repositorioSolicitudCambio.findByTipoPrioridad(prioridad);
    }

    @Override
    public List<SolicitudCambio> obtenerSolicitudesPendientesPorDecanatura(String decanaturaId) {
        return repositorioSolicitudCambio.findByDecanaturaIdAndEstado(decanaturaId, EstadoSolicitud.PENDIENTE);
    }

    @Override
    public SolicitudCambio actualizarEstado(String solicitudId, EstadoSolicitud estado) {
        return actualizarEstadoSolicitud(solicitudId, estado, null, null);
    }

    @Override
    public SolicitudCambio actualizarEstadoSolicitud(String id, EstadoSolicitud estado, String respuesta, String justificacion) {
        return repositorioSolicitudCambio.findById(id)
                .map(solicitud -> {
                    solicitud.setEstado(estado);
                    solicitud.setFechaRespuesta(new Date());

                    if (respuesta != null) {
                        solicitud.setRespuesta(respuesta);
                    }

                    if (justificacion != null) {
                        solicitud.setJustificacion(justificacion);
                    }

                    return repositorioSolicitudCambio.save(solicitud);
                })
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + id));
    }

    @Override
    public SolicitudCambio aprobarSolicitud(String id, String justificacion) {
        return actualizarEstadoSolicitud(id, EstadoSolicitud.APROBADA, "Solicitud aprobada", justificacion);
    }

    @Override
    public SolicitudCambio rechazarSolicitud(String id, String justificacion) {
        return actualizarEstadoSolicitud(id, EstadoSolicitud.RECHAZADA, "Solicitud rechazada", justificacion);
    }

    @Override
    public List<SolicitudCambio> obtenerHistorialSolicitudes() {
        return repositorioSolicitudCambio.findAllByOrderByFechaCreacionDesc();
    }

    @Override
    public Map<String, Object> obtenerEstadisticasSolicitudes() {
        List<SolicitudCambio> todasLasSolicitudes = repositorioSolicitudCambio.findAll();

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalSolicitudes", todasLasSolicitudes.size());
        estadisticas.put("solicitudesPendientes",
                todasLasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.PENDIENTE).count());
        estadisticas.put("solicitudesAprobadas",
                todasLasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count());
        estadisticas.put("solicitudesRechazadas",
                todasLasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count());

        Map<TipoPrioridad, Long> porPrioridad = todasLasSolicitudes.stream()
                .collect(Collectors.groupingBy(SolicitudCambio::getTipoPrioridad, Collectors.counting()));
        estadisticas.put("porPrioridad", porPrioridad);

        return estadisticas;
    }

    @Override
    public boolean validarSolicitud(SolicitudCambio solicitud) {
        if (solicitud == null) {
            return false;
        }

        if (solicitud.getEstudianteId() == null || !repositorioEstudiante.existsById(solicitud.getEstudianteId())) {
            return false;
        }

        if (solicitud.getMateriaDestinoId() == null || !repositorioMateria.existsById(solicitud.getMateriaDestinoId())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean puedeCrearSolicitud(String estudianteId, String materiaId) {
        List<SolicitudCambio> solicitudesActivas = repositorioSolicitudCambio
                .findByEstudianteIdAndMateriaDestinoIdAndEstadoIn(
                        estudianteId,
                        materiaId,
                        Arrays.asList(EstadoSolicitud.PENDIENTE, EstadoSolicitud.EN_REVISION)
                );

        return solicitudesActivas.isEmpty();
    }

    @Override
    public List<String> obtenerHistorialPorSolicitud(String id) {
        return repositorioSolicitudCambio.findById(id)
                .map(SolicitudCambio::getHistorialEstados)
                .orElseGet(ArrayList::new);
    }

    private void asignarDecanatura(SolicitudCambio solicitud) {
        String materiaId = solicitud.getMateriaDestinoId();
        if (materiaId != null) {
            repositorioMateria.findById(materiaId)
                    .ifPresent(materia -> {
                        String facultad = materia.getFacultad();
                        List<Decanatura> decanaturas = repositorioDecanatura.findByFacultad(facultad);
                        if (!decanaturas.isEmpty()) {
                            solicitud.setDecanaturaId(decanaturas.get(0).getId());
                        }
                    });
        }
    }

    private boolean estaEnPeriodoActivo() {
        return repositorioPeriodoCambio.findByActivoTrue().stream()
                .anyMatch(periodo -> {
                    Date ahora = new Date();
                    return !ahora.before(periodo.getFechaInicio()) && !ahora.after(periodo.getFechaFin());
                });
    }
}