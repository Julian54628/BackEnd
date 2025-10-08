package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    @Autowired
    private RepositorioEstudiante repositorioEstudiante;

    @Autowired
    private RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    private RepositorioGrupo repositorioGrupo;

    @Autowired
    private RepositorioMateria repositorioMateria;

    @Autowired
    private RepositorioPlanAcademico repositorioPlanAcademico;

    @Autowired
    private RepositorioSemaforoAcademico repositorioSemaforoAcademico;

    @Override
    public Estudiante crear(Estudiante estudiante) {
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
        estudiante.setId(id);
        return repositorioEstudiante.save(estudiante);
    }

    @Override
    public SolicitudCambio crearSolicitudCambio(String estudianteId, String materiaOrigenId,
                                                String grupoOrigenId, String materiaDestinoId, String grupoDestinoId) {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId(UUID.randomUUID().toString());
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
    public List<Grupo> consultarHorarioSemestreActual(String estudianteId) {
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return List.of();
        }
        Estudiante estudiante = estudianteOpt.get();
        List<Grupo> horarioActual = new ArrayList<>();
        for (String grupoId : estudiante.getHorariosIds()) {
            Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
            grupoOpt.ifPresent(horarioActual::add);
        }
        return horarioActual;
    }

    @Override
    public List<Materia> consultarMateriasSemestreAnterior(String estudianteId) {
        return repositorioMateria.findAll();
    }

    @Override
    public Map<String, Object> consultarAvancePlanEstudios(String estudianteId) {
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        Map<String, Object> avance = new HashMap<>();
        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            if (estudiante.getPlanAcademicoId() != null) {
                Optional<PlanAcademico> planOpt = repositorioPlanAcademico.findById(estudiante.getPlanAcademicoId());
                if (planOpt.isPresent()) {
                    PlanAcademico plan = planOpt.get();
                    Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
                    int materiasAprobadas = 0;
                    if (semaforoOpt.isPresent()) {
                        SemaforoAcademico semaforo = semaforoOpt.get();
                        materiasAprobadas = (int) semaforo.getHistorialMaterias().values().stream()
                                .filter(estado -> estado == EstadoMateria.APROBADA).count();
                    }
                    int totalMateriasPlan = plan.getMateriasObligatoriasIds().size() + plan.getMateriasElectivasIds().size();
                    double porcentajeAvance = totalMateriasPlan > 0 ? (double) materiasAprobadas / totalMateriasPlan * 100 : 0;
                    avance.put("estudianteId", estudianteId);
                    avance.put("materiasAprobadas", materiasAprobadas);
                    avance.put("totalMateriasPlan", totalMateriasPlan);
                    avance.put("porcentajeAvance", porcentajeAvance);
                    avance.put("semestreActual", estudiante.getSemestre());
                    avance.put("carrera", estudiante.getCarrera());
                    avance.put("planAcademico", plan.getNombre());
                    avance.put("creditosTotalesPlan", plan.getCreditosTotales());
                }
            }
        }
        return avance;
    }

    @Override
    public void asignarGrupoAEstudiante(String estudianteId, String grupoId) {
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
        if (estudianteOpt.isPresent() && grupoOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            Grupo grupo = grupoOpt.get();
            if (grupo.getEstudiantesInscritosIds().size() < grupo.getCupoMaximo()) {
                if (!grupo.getEstudiantesInscritosIds().contains(estudianteId)) {
                    grupo.getEstudiantesInscritosIds().add(estudianteId);
                    repositorioGrupo.save(grupo);
                }
                if (!estudiante.getHorariosIds().contains(grupoId)) {
                    estudiante.getHorariosIds().add(grupoId);
                    repositorioEstudiante.save(estudiante);
                }
            }
        }
    }
    public List<Estudiante> obtenerEstudiantesPorCarrera(String carrera) {
        return repositorioEstudiante.findByCarrera(carrera);
    }
    public List<Estudiante> obtenerEstudiantesPorSemestre(int semestre) {
        return repositorioEstudiante.findBySemestre(semestre);
    }
    public long contarEstudiantesActivos() {
        return repositorioEstudiante.findByActivoTrue().size();
    }
}