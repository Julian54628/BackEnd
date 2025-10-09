package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.model.EstadoSolicitud;
import edu.escuelaing.sirha.model.TipoPrioridad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RepositorioSolicitudCambio extends MongoRepository<SolicitudCambio, String> {

    List<SolicitudCambio> findByEstudianteId(String estudianteId);

    List<SolicitudCambio> findByEstado(EstadoSolicitud estado);

    List<SolicitudCambio> findByEstudianteIdAndEstado(String estudianteId, EstadoSolicitud estado);

    List<SolicitudCambio> findByMateriaDestinoId(String materiaDestinoId);

    List<SolicitudCambio> findByGrupoDestinoId(String grupoDestinoId);

    List<SolicitudCambio> findByFechaCreacionBetween(Date fechaInicio, Date fechaFin);

    List<SolicitudCambio> findByEstadoOrderByPrioridadDesc(EstadoSolicitud estado);

    List<SolicitudCambio> findByOrderByPrioridadDesc();

    @Query("{ 'estudianteId': ?0, 'materiaDestinoId': ?1, 'estado': { $in: ['PENDIENTE', 'EN_REVISION'] } }")
    boolean existsSolicitudActivaParaMateria(String estudianteId, String materiaDestinoId);

    long countByEstado(EstadoSolicitud estado);

    @Query("{ 'fechaCreacion': { $gte: ?0 } }")
    List<SolicitudCambio> findSolicitudesRecientes(Date fechaLimite);

    List<SolicitudCambio> findByDecanaturaId(String decanaturaId);
    List<SolicitudCambio> findByTipoPrioridad(TipoPrioridad tipoPrioridad);
    List<SolicitudCambio> findByDecanaturaIdAndEstado(String decanaturaId, EstadoSolicitud estado);
    List<SolicitudCambio> findByEstudianteIdAndMateriaDestinoIdAndEstadoIn(String estudianteId, String materiaDestinoId, List<EstadoSolicitud> estados);
    List<SolicitudCambio> findByAdministradorId(String administradorId);
    List<SolicitudCambio> findByTipoPrioridadAndEstado(TipoPrioridad tipoPrioridad, EstadoSolicitud estado);
    List<SolicitudCambio> findByTipoSolicitud(String tipoSolicitud);
}