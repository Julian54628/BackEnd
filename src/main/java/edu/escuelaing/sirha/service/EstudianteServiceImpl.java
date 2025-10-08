package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private RepositorioHorario repositorioHorario;

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
        // 1. Buscar el estudiante
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return List.of();
        }
        
        // 2. Obtener los grupos del semestre actual
        Estudiante estudiante = estudianteOpt.get();
        List<Grupo> horarioActual = new ArrayList<>();
        
        // 3. Buscar cada grupo en MongoDB
        for (String grupoId : estudiante.getHorariosIds()) {
            Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
            if (grupoOpt.isPresent()) {
                horarioActual.add(grupoOpt.get());
            }
        }
        
        return horarioActual;
    }

    @Override
    public List<Materia> consultarMateriasSemestresAnteriores(String estudianteId) {
        // 1. Buscar el estudiante
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return List.of();
        }
        
        // 2. Buscar su semáforo académico
        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforoOpt.isEmpty()) {
            return List.of();
        }
        
        // 3. Obtener materias del historial que estén aprobadas o reprobadas
        SemaforoAcademico semaforo = semaforoOpt.get();
        List<String> materiasIds = semaforo.getHistorialMaterias().entrySet().stream()
            .filter(entry -> entry.getValue() == EstadoMateria.APROBADA || 
                            entry.getValue() == EstadoMateria.REPROBADA)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        // 4. Buscar las materias en MongoDB
        if (materiasIds.isEmpty()) {
            return List.of();
        }
        
        return repositorioMateria.findAllById(materiasIds);
    }

    @Override
    public Map<String, Object> consultarHorarioDetalladoSemestreActual(String estudianteId) {
        Map<String, Object> horarioDetallado = new HashMap<>();
        
        // 1. Buscar el estudiante
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            horarioDetallado.put("error", "Estudiante no encontrado");
            return horarioDetallado;
        }
        
        Estudiante estudiante = estudianteOpt.get();
        horarioDetallado.put("estudianteId", estudianteId);
        horarioDetallado.put("nombre", estudiante.getNombre());
        horarioDetallado.put("codigo", estudiante.getCodigo());
        horarioDetallado.put("semestre", estudiante.getSemestre());
        horarioDetallado.put("carrera", estudiante.getCarrera());
        
        // 2. Obtener grupos del semestre actual
        List<Map<String, Object>> gruposDetallados = new ArrayList<>();
        for (String grupoId : estudiante.getHorariosIds()) {
            Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
            if (grupoOpt.isPresent()) {
                Grupo grupo = grupoOpt.get();
                Map<String, Object> grupoDetallado = new HashMap<>();
                grupoDetallado.put("grupoId", grupo.getId());
                grupoDetallado.put("idGrupo", grupo.getIdGrupo());
                grupoDetallado.put("cupoMaximo", grupo.getCupoMaximo());
                grupoDetallado.put("estudiantesInscritos", grupo.getCantidadInscritos());
                
                // 3. Obtener información de la materia
                if (grupo.getMateriaId() != null) {
                    Optional<Materia> materiaOpt = repositorioMateria.findById(grupo.getMateriaId());
                    if (materiaOpt.isPresent()) {
                        Materia materia = materiaOpt.get();
                        Map<String, Object> materiaInfo = new HashMap<>();
                        materiaInfo.put("materiaId", materia.getId());
                        materiaInfo.put("nombre", materia.getNombre());
                        materiaInfo.put("codigo", materia.getCodigo());
                        materiaInfo.put("creditos", materia.getCreditos());
                        materiaInfo.put("facultad", materia.getFacultad());
                        materiaInfo.put("esObligatoria", materia.isEsObligatoria());
                        grupoDetallado.put("materia", materiaInfo);
                    }
                }
                
                // 4. Obtener horarios específicos
                List<Map<String, Object>> horariosDetallados = new ArrayList<>();
                for (String horarioId : grupo.getHorarioIds()) {
                    Optional<Horario> horarioOpt = repositorioHorario.findById(horarioId);
                    if (horarioOpt.isPresent()) {
                        Horario horario = horarioOpt.get();
                        Map<String, Object> horarioInfo = new HashMap<>();
                        horarioInfo.put("horarioId", horario.getId());
                        horarioInfo.put("diaSemana", horario.getDiaSemana());
                        horarioInfo.put("horaInicio", horario.getHoraInicio());
                        horarioInfo.put("horaFin", horario.getHoraFin());
                        horarioInfo.put("salon", horario.getSalon());
                        horariosDetallados.add(horarioInfo);
                    }
                }
                grupoDetallado.put("horarios", horariosDetallados);
                gruposDetallados.add(grupoDetallado);
            }
        }
        
        horarioDetallado.put("grupos", gruposDetallados);
        horarioDetallado.put("totalGrupos", gruposDetallados.size());
        
        return horarioDetallado;
    }

    @Override
    public Map<String, Object> consultarResumenAcademicoCompleto(String estudianteId) {
        Map<String, Object> resumenCompleto = new HashMap<>();
        
        // 1. Información básica del estudiante
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            resumenCompleto.put("error", "Estudiante no encontrado");
            return resumenCompleto;
        }
        
        Estudiante estudiante = estudianteOpt.get();
        resumenCompleto.put("estudiante", Map.of(
            "id", estudiante.getId(),
            "nombre", estudiante.getNombre(),
            "codigo", estudiante.getCodigo(),
            "carrera", estudiante.getCarrera(),
            "semestre", estudiante.getSemestre(),
            "correoInstitucional", estudiante.getCorreoInstitucional()
        ));
        
        // 2. Horario del semestre actual
        List<Grupo> horarioActual = consultarHorarioSemestreActual(estudianteId);
        resumenCompleto.put("horarioSemestreActual", horarioActual);
        
        // 3. Materias de semestres anteriores
        List<Materia> materiasAnteriores = consultarMateriasSemestresAnteriores(estudianteId);
        resumenCompleto.put("materiasSemestresAnteriores", materiasAnteriores);
        
        // 4. Información del semáforo académico
        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforoOpt.isPresent()) {
            SemaforoAcademico semaforo = semaforoOpt.get();
            resumenCompleto.put("semaforoAcademico", Map.of(
                "creditosAprobados", semaforo.getCreditosAprobados(),
                "totalCreditosPlan", semaforo.getTotalCreditosPlan(),
                "materiasVistas", semaforo.getMateriasVistas(),
                "promedioAcumulado", semaforo.getPromedioAcumulado(),
                "grado", semaforo.getGrado()
            ));
        }
        
        // 5. Solicitudes de cambio
        List<SolicitudCambio> solicitudes = consultarSolicitudes(estudianteId);
        resumenCompleto.put("solicitudesCambio", solicitudes);
        
        // 6. Avance del plan de estudios
        Map<String, Object> avance = consultarAvancePlanEstudios(estudianteId);
        resumenCompleto.put("avancePlanEstudios", avance);
        
        return resumenCompleto;
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