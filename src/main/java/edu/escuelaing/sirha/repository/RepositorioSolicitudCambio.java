package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.TipoPrioridad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@Repository
public interface RepositorioSolicitudCambio extends MongoRepository<SolicitudCambio, String> {

    Optional<SolicitudCambio> findByIdSolicitud(int idSolicitud);

    List<SolicitudCambio> findByEstudianteId(String estudianteId);

    List<SolicitudCambio> findByEstado(EstadoSolicitud estado);

    List<SolicitudCambio> findByEstudianteIdAndEstado(String estudianteId, EstadoSolicitud estado);

    List<SolicitudCambio> findByMateriaDestinoId(String materiaDestinoId);

    List<SolicitudCambio> findByGrupoDestinoId(String grupoDestinoId);

    List<SolicitudCambio> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    List<SolicitudCambio> findByEstadoOrderByPrioridadDesc(EstadoSolicitud estado);

    List<SolicitudCambio> findByOrderByPrioridadDesc();

    List<SolicitudCambio> findAllByOrderByFechaCreacionDesc();

    // Comprueba si existe una solicitud con estados 'activos' (pendiente o en revisión)
    default boolean existsSolicitudActivaParaMateria(String estudianteId, String materiaDestinoId) {
        List<EstadoSolicitud> activos = Arrays.asList(EstadoSolicitud.PENDIENTE, EstadoSolicitud.EN_REVISION);
        return !findByEstudianteIdAndMateriaDestinoIdAndEstadoIn(estudianteId, materiaDestinoId, activos).isEmpty();
    }

    // Devuelve solicitudes posteriores a una fecha, ordenadas por fecha de creación (más recientes primero)
    List<SolicitudCambio> findByFechaCreacionAfterOrderByFechaCreacionDesc(Date fechaLimite);

    List<SolicitudCambio> findByDecanaturaId(String decanaturaId);

    List<SolicitudCambio> findByTipoPrioridad(TipoPrioridad tipoPrioridad);

    List<SolicitudCambio> findByDecanaturaIdAndEstado(String decanaturaId, EstadoSolicitud estado);

    List<SolicitudCambio> findByEstudianteIdAndMateriaDestinoIdAndEstadoIn(String estudianteId, String materiaDestinoId, List<EstadoSolicitud> estados);

    List<SolicitudCambio> findByAdministradorId(String administradorId);

    List<SolicitudCambio> findByTipoPrioridadAndEstado(TipoPrioridad tipoPrioridad, EstadoSolicitud estado);

    List<SolicitudCambio> findByMateriaOrigenId(String materiaOrigenId);

    List<SolicitudCambio> findByGrupoOrigenId(String grupoOrigenId);

    boolean existsByIdSolicitud(int idSolicitud);

    long countByEstado(EstadoSolicitud estado);

    long countByEstudianteId(String estudianteId);

    default SolicitudCambio guardarSolicitud(SolicitudCambio solicitud) {
        return save(solicitud);
    }

    default boolean eliminarSolicitudSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<SolicitudCambio> obtenerSolicitudPorId(String id) {
        return findById(id);
    }

    default boolean existeSolicitudPorId(String id) {
        return existsById(id);
    }
}