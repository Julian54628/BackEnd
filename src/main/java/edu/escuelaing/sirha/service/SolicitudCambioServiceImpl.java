package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.RepositorioSolicitudCambio;
import edu.escuelaing.sirha.repository.RepositorioEstudiante;
import edu.escuelaing.sirha.repository.RepositorioDecanatura;
import edu.escuelaing.sirha.repository.RepositorioMateria;
import edu.escuelaing.sirha.repository.RepositorioGrupo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SolicitudCambioServiceImpl implements SolicitudCambioService {

    @Autowired
    private RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    private RepositorioEstudiante repositorioEstudiante;

    @Autowired
    private RepositorioDecanatura repositorioDecanatura;

    @Autowired
    private RepositorioMateria repositorioMateria;


    @Override
    public SolicitudCambio crear(SolicitudCambio solicitud) {
        return crearSolicitud(solicitud);
    }

    @Override
    public SolicitudCambio crearSolicitud(SolicitudCambio solicitud) {
        if (!validarSolicitud(solicitud)) {
            throw new IllegalArgumentException("La solicitud no es válida");
        }
        
        // Asignar decanatura basada en la facultad de la materia
        asignarDecanatura(solicitud);
        
        // Generar ID único si no existe
        if (solicitud.getId() == null || solicitud.getId().isEmpty()) {
            solicitud.setId(UUID.randomUUID().toString());
        }
        
        // Establecer fecha de creación
        solicitud.setFechaCreacion(new Date());
        
        // Establecer estado inicial
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
    public List<SolicitudCambio> obtenerSolicitudesPorTipo(TipoSolicitud tipo) {
        return repositorioSolicitudCambio.findByTipoSolicitud(tipo);
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
        Optional<SolicitudCambio> solicitudOpt = repositorioSolicitudCambio.findById(id);
        if (solicitudOpt.isEmpty()) {
            throw new IllegalArgumentException("Solicitud no encontrada");
        }
        
        SolicitudCambio solicitud = solicitudOpt.get();
        solicitud.setEstado(estado);
        solicitud.setFechaRespuesta(new Date());
        
        if (respuesta != null) {
            solicitud.setRespuesta(respuesta);
        }
        
        if (justificacion != null) {
            solicitud.setJustificacion(justificacion);
        }
        
        return repositorioSolicitudCambio.save(solicitud);
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
        return repositorioSolicitudCambio.findAll().stream()
                .sorted((s1, s2) -> s2.getFechaCreacion().compareTo(s1.getFechaCreacion()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> obtenerEstadisticasSolicitudes() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<SolicitudCambio> todasLasSolicitudes = repositorioSolicitudCambio.findAll();
        
        // Estadísticas básicas
        estadisticas.put("totalSolicitudes", todasLasSolicitudes.size());
        estadisticas.put("solicitudesPendientes", todasLasSolicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.PENDIENTE).count());
        estadisticas.put("solicitudesAprobadas", todasLasSolicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count());
        estadisticas.put("solicitudesRechazadas", todasLasSolicitudes.stream()
                .filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count());
        
        // Estadísticas por tipo de solicitud
        Map<TipoSolicitud, Long> porTipo = todasLasSolicitudes.stream()
                .filter(s -> s.getTipoSolicitud() != null)
                .collect(Collectors.groupingBy(SolicitudCambio::getTipoSolicitud, Collectors.counting()));
        estadisticas.put("porTipo", porTipo);
        
        // Estadísticas por prioridad
        Map<TipoPrioridad, Long> porPrioridad = todasLasSolicitudes.stream()
                .filter(s -> s.getTipoPrioridad() != null)
                .collect(Collectors.groupingBy(SolicitudCambio::getTipoPrioridad, Collectors.counting()));
        estadisticas.put("porPrioridad", porPrioridad);
        
        // Tasa de aprobación por decanatura
        Map<String, Map<String, Long>> aprobacionPorDecanatura = todasLasSolicitudes.stream()
                .filter(s -> s.getDecanaturaId() != null)
                .collect(Collectors.groupingBy(
                    SolicitudCambio::getDecanaturaId,
                    Collectors.groupingBy(
                        s -> s.getEstado().toString(),
                        Collectors.counting()
                    )
                ));
        estadisticas.put("aprobacionPorDecanatura", aprobacionPorDecanatura);
        
        // Solicitudes por mes
        Map<String, Long> solicitudesPorMes = todasLasSolicitudes.stream()
                .collect(Collectors.groupingBy(
                    s -> String.format("%d-%02d", 
                        s.getFechaCreacion().getYear() + 1900,
                        s.getFechaCreacion().getMonth() + 1),
                    Collectors.counting()
                ));
        estadisticas.put("solicitudesPorMes", solicitudesPorMes);
        
        return estadisticas;
    }

    @Override
    public boolean validarSolicitud(SolicitudCambio solicitud) {
        if (solicitud == null) return false;
        
        if (solicitud.getEstudianteId() == null || solicitud.getEstudianteId().trim().isEmpty()) return false;
        if (solicitud.getTipoSolicitud() == null) return false;
        
        if (!repositorioEstudiante.existsById(solicitud.getEstudianteId())) return false;
        
        if (solicitud.getTipoSolicitud() == TipoSolicitud.CAMBIO_GRUPO) {
            if (solicitud.getMateriaOrigenId() == null || solicitud.getGrupoOrigenId() == null ||
                solicitud.getGrupoDestinoId() == null) {
                return false;
            }
        } else if (solicitud.getTipoSolicitud() == TipoSolicitud.CAMBIO_MATERIA) {
            if (solicitud.getMateriaOrigenId() == null || solicitud.getMateriaDestinoId() == null) {
                return false;
            }
        }
        
        List<SolicitudCambio> solicitudesSimilares = repositorioSolicitudCambio
            .findByEstudianteIdAndEstado(solicitud.getEstudianteId(), EstadoSolicitud.PENDIENTE);
            
        if (solicitudesSimilares != null && !solicitudesSimilares.isEmpty()) {
            for (SolicitudCambio s : solicitudesSimilares) {
                if (s.getTipoSolicitud() == solicitud.getTipoSolicitud() &&
                    ((solicitud.getTipoSolicitud() == TipoSolicitud.CAMBIO_GRUPO && 
                      s.getMateriaOrigenId().equals(solicitud.getMateriaOrigenId()) &&
                      s.getGrupoOrigenId().equals(solicitud.getGrupoOrigenId()) &&
                      s.getGrupoDestinoId().equals(solicitud.getGrupoDestinoId())) ||
                     (solicitud.getTipoSolicitud() == TipoSolicitud.CAMBIO_MATERIA &&
                      s.getMateriaOrigenId().equals(solicitud.getMateriaOrigenId()) &&
                      s.getMateriaDestinoId().equals(solicitud.getMateriaDestinoId())))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    @Override
    public boolean puedeCrearSolicitud(String estudianteId, String materiaId) {
        if (estudianteId == null || estudianteId.trim().isEmpty() || 
            materiaId == null || materiaId.trim().isEmpty()) {
            return false;
        }
        
        if (!repositorioEstudiante.existsById(estudianteId)) {
            return false;
        }
        
        List<SolicitudCambio> solicitudesPendientes = repositorioSolicitudCambio
            .findByEstudianteIdAndEstado(estudianteId, EstadoSolicitud.PENDIENTE);
            
        if (solicitudesPendientes != null) {
            for (SolicitudCambio solicitud : solicitudesPendientes) {
                if ((solicitud.getMateriaOrigenId() != null && solicitud.getMateriaOrigenId().equals(materiaId)) ||
                    (solicitud.getMateriaDestinoId() != null && solicitud.getMateriaDestinoId().equals(materiaId))) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void asignarDecanatura(SolicitudCambio solicitud) {
        try {
            String materiaId = (solicitud.getMateriaOrigenId() != null) ?
                solicitud.getMateriaOrigenId() : solicitud.getMateriaDestinoId();
                
            if (materiaId != null) {
                Optional<Materia> materiaOpt = repositorioMateria.findById(materiaId);
                if (materiaOpt.isPresent()) {
                    if (materiaOpt.get().getFacultad() != null) {
                        solicitud.setDecanaturaId(materiaOpt.get().getFacultad());
                        return;
                    }
                }
            }
            
            List<Decanatura> decanaturas = repositorioDecanatura.findAll();
            
            if (decanaturas != null && !decanaturas.isEmpty()) {
                solicitud.setDecanaturaId(decanaturas.get(0).getId());
            } else {
                solicitud.setDecanaturaId("DEFAULT_DECANATURA");
            }
        } catch (Exception e) {
            solicitud.setDecanaturaId("DEFAULT_DECANATURA");
        }
    }
}
