package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioEstudiante;
import edu.escuelaing.sirha.repository.RepositorioSolicitudCambio;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService {

    private final RepositorioEstudiante repositorioEstudiante;
    private final RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    public EstudianteServiceImpl(RepositorioEstudiante repositorioEstudiante,
                                 RepositorioSolicitudCambio repositorioSolicitudCambio) {
        this.repositorioEstudiante = repositorioEstudiante;
        this.repositorioSolicitudCambio = repositorioSolicitudCambio;
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        if (repositorioEstudiante.existsByCodigo(estudiante.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un estudiante con el código: " + estudiante.getCodigo());
        }
        return repositorioEstudiante.save(estudiante);
    }

    @Override
    public Optional<Estudiante> buscarPorCodigo(String codigo) {
        return repositorioEstudiante.findByCodigo(codigo);
    }

    @Override
    public Optional<Estudiante> buscarPorId(String id) {
        return repositorioEstudiante.findById(id);
    }

    @Override
    public List<Estudiante> listarTodos() {
        return repositorioEstudiante.findAll();
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioEstudiante.deleteById(id);
    }

    @Override
    public Estudiante actualizar(String id, Estudiante estudiante) {
        if (!repositorioEstudiante.existsById(id)) {
            throw new IllegalArgumentException("Estudiante no encontrado: " + id);
        }
        estudiante.setId(id);
        return repositorioEstudiante.save(estudiante);
    }

    @Override
    public SolicitudCambio crearSolicitudCambio(String estudianteId, String materiaOrigenId,
                                                String grupoOrigenId, String materiaDestinoId, String grupoDestinoId) {
        if (!repositorioEstudiante.existsById(estudianteId)) {
            throw new IllegalArgumentException("Estudiante no encontrado: " + estudianteId);
        }

        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setEstudianteId(estudianteId);
        solicitud.setMateriaOrigenId(materiaOrigenId);
        solicitud.setGrupoOrigenId(grupoOrigenId);
        solicitud.setMateriaDestinoId(materiaDestinoId);
        solicitud.setGrupoDestinoId(grupoDestinoId);
        solicitud.setFechaCreacion(new Date());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        return repositorioSolicitudCambio.save(solicitud);
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudes(String estudianteId) {
        return repositorioSolicitudCambio.findByEstudianteId(estudianteId);
    }

    @Override
    public List<Estudiante> buscarPorCarrera(String carrera) {
        return repositorioEstudiante.findByCarrera(carrera);
    }

    @Override
    public List<Estudiante> buscarPorSemestre(int semestre) {
        return repositorioEstudiante.findBySemestre(semestre);
    }
}