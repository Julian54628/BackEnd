package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.model.SemaforoVisualizacion;
import edu.escuelaing.sirha.repository.RepositorioSemaforoAcademico;
import edu.escuelaing.sirha.repository.RepositorioEstudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class SemaforoAcademicoServiceImpl implements SemaforoAcademicoService {
    private final RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    private final RepositorioEstudiante repositorioEstudiante;

    @Autowired
    public SemaforoAcademicoServiceImpl(RepositorioSemaforoAcademico repositorioSemaforoAcademico,
                                        RepositorioEstudiante repositorioEstudiante) {
        this.repositorioSemaforoAcademico = repositorioSemaforoAcademico;
        this.repositorioEstudiante = repositorioEstudiante;
    }

    public SemaforoAcademicoServiceImpl() {
        this.repositorioSemaforoAcademico = null;
        this.repositorioEstudiante = null;
    }

    @Override
    public Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId) {
        if (repositorioSemaforoAcademico == null) {
            return Collections.emptyMap();
        }
        Optional<SemaforoAcademico> optSemaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (!optSemaforo.isPresent()) {
            return Collections.emptyMap();
        }
        Map<String, EstadoMateria> historial = optSemaforo.get().getHistorialMaterias();
        return historial.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> mapearEstado(entry.getValue())));
    }

    @Override
    public Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId) {
        if (repositorioSemaforoAcademico == null) {
            return Optional.empty();
        }
        Optional<SemaforoAcademico> optSemaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (optSemaforo.isPresent()) {
            EstadoMateria estadoMateria = optSemaforo.get().getHistorialMaterias().get(materiaId);
            if (estadoMateria != null) {
                return Optional.of(mapearEstado(estadoMateria));
            }
        }
        return Optional.empty();
    }

    private EstadoSemaforo mapearEstado(EstadoMateria estadoMateria) {
        switch (estadoMateria) {
            case APROBADA:
                return EstadoSemaforo.VERDE;
            case INSCRITA:
            case PENDIENTE:
                return EstadoSemaforo.AZUL;
            case REPROBADA:
            case CANCELADA:
                return EstadoSemaforo.ROJO;
            default:
                return EstadoSemaforo.AZUL;
        }
    }

    @Override
    public int getSemestreActual(String estudianteId) {
        try {
            if (repositorioEstudiante == null) {
                return 0;
            }
            Optional<Estudiante> estudiante = repositorioEstudiante.findById(estudianteId);
            if (estudiante.isPresent()) {
                return estudiante.get().getSemestre();
            }
        } catch (Exception e) {
            System.out.println("Error buscando estudiante: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public Map<String, Object> getForaneoEstudiante(String estudianteId) {
        Map<String, Object> resultado = new HashMap<>();

        int semestreActual = getSemestreActual(estudianteId);
        resultado.put("semestreActual", semestreActual);

        List<Integer> semestresAnteriores = new ArrayList<>();
        for (int i = 1; i < semestreActual; i++) {
            semestresAnteriores.add(i);
        }
        resultado.put("semestresAnteriores", semestresAnteriores);

        Optional<SemaforoAcademico> semaforo = Optional.empty();
        if (repositorioSemaforoAcademico != null) {
            semaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        }
        if (semaforo.isPresent()) {
            SemaforoAcademico sem = semaforo.get();

            int aprobadas = 0;
            int reprobadas = 0;
            int inscritas = 0;

            Map<String, EstadoMateria> materias = sem.getHistorialMaterias();
            for (EstadoMateria estado : materias.values()) {
                if (estado == EstadoMateria.APROBADA) {
                    aprobadas++;
                } else if (estado == EstadoMateria.REPROBADA) {
                    reprobadas++;
                } else if (estado == EstadoMateria.INSCRITA) {
                    inscritas++;
                }
            }

            resultado.put("materiasAprobadas", aprobadas);
            resultado.put("materiasReprobadas", reprobadas);
            resultado.put("materiasInscritas", inscritas);
            resultado.put("totalMaterias", materias.size());
        }

        return resultado;
    }

    @Override
    public SemaforoVisualizacion obtenerSemaforoCompleto(String estudianteId) {
        Map<String, EstadoSemaforo> estados = visualizarSemaforoEstudiante(estudianteId);
        Map<String, Object> foraneo = getForaneoEstudiante(estudianteId);

        SemaforoVisualizacion vis = new SemaforoVisualizacion();
        vis.setEstudianteId(estudianteId);

        Object semestreActual = foraneo.get("semestreActual");
        if (semestreActual instanceof Number) {
            vis.setSemestreActual(((Number) semestreActual).intValue());
        }

        Object creditosAprobados = foraneo.get("creditosAprobados");
        if (creditosAprobados instanceof Number) {
            vis.setCreditosCompletados(((Number) creditosAprobados).intValue());
        }

        Object promedio = foraneo.get("promedio");
        if (promedio instanceof Number) {
            vis.setPromedioAcumulado(((Number) promedio).floatValue());
        }

        Object materiasAprobadas = foraneo.get("materiasAprobadas");
        if (materiasAprobadas instanceof Number) {
            vis.setMateriasAprobadas(((Number) materiasAprobadas).intValue());
        }

        Object materiasReprobadas = foraneo.get("materiasReprobadas");
        if (materiasReprobadas instanceof Number) {
            vis.setMateriasReprobadas(((Number) materiasReprobadas).intValue());
        }

        Object materiasInscritas = foraneo.get("materiasInscritas");
        if (materiasInscritas instanceof Number) {
            vis.setMateriasCursando(((Number) materiasInscritas).intValue());
        }

        Object totalMaterias = foraneo.get("totalMaterias");
        if (totalMaterias instanceof Number) {
            vis.setTotalMateriasPlan(((Number) totalMaterias).intValue());
        }

        if (repositorioSemaforoAcademico != null) {
            Optional<SemaforoAcademico> semOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
            if (semOpt.isPresent()) {
                SemaforoAcademico sem = semOpt.get();
                int totalCreditosPlan = sem.getTotalCreditosPlan();
                vis.setTotalCreditosPlan(totalCreditosPlan);
                int creditosCompletados = vis.getCreditosCompletados();
                int faltantes = totalCreditosPlan > 0 ? Math.max(0, totalCreditosPlan - creditosCompletados) : 0;
                vis.setCreditosFaltantes(faltantes);
                float progreso = totalCreditosPlan > 0 ? (creditosCompletados * 100.0f) / totalCreditosPlan : 0f;
                vis.setPorcentajeProgreso(progreso);
            }
        }

        return vis;
    }

    @Override
    public SemaforoVisualizacion obtenerSemaforoDetallado(String estudianteId) {
        Map<String, EstadoSemaforo> estados = visualizarSemaforoEstudiante(estudianteId);
        Map<String, Object> foraneo = getForaneoEstudiante(estudianteId);

        SemaforoVisualizacion vis = new SemaforoVisualizacion();
        vis.setEstudianteId(estudianteId);

        Object semestreActual = foraneo.get("semestreActual");
        if (semestreActual instanceof Number) {
            vis.setSemestreActual(((Number) semestreActual).intValue());
        }

        Object creditosAprobados = foraneo.get("creditosAprobados");
        if (creditosAprobados instanceof Number) {
            vis.setCreditosCompletados(((Number) creditosAprobados).intValue());
        }

        Object promedio = foraneo.get("promedio");
        if (promedio instanceof Number) {
            vis.setPromedioAcumulado(((Number) promedio).floatValue());
        }

        Object materiasAprobadas = foraneo.get("materiasAprobadas");
        if (materiasAprobadas instanceof Number) {
            vis.setMateriasAprobadas(((Number) materiasAprobadas).intValue());
        }

        Object materiasReprobadas = foraneo.get("materiasReprobadas");
        if (materiasReprobadas instanceof Number) {
            vis.setMateriasReprobadas(((Number) materiasReprobadas).intValue());
        }

        Object materiasInscritas = foraneo.get("materiasInscritas");
        if (materiasInscritas instanceof Number) {
            vis.setMateriasCursando(((Number) materiasInscritas).intValue());
        }

        Object totalMaterias = foraneo.get("totalMaterias");
        if (totalMaterias instanceof Number) {
            vis.setTotalMateriasPlan(((Number) totalMaterias).intValue());
        }

        if (repositorioSemaforoAcademico != null) {
            Optional<SemaforoAcademico> semOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
            if (semOpt.isPresent()) {
                SemaforoAcademico sem = semOpt.get();
                int totalCreditosPlan = sem.getTotalCreditosPlan();
                vis.setTotalCreditosPlan(totalCreditosPlan);
                int creditosCompletados = vis.getCreditosCompletados();
                int faltantes = totalCreditosPlan > 0 ? Math.max(0, totalCreditosPlan - creditosCompletados) : 0;
                vis.setCreditosFaltantes(faltantes);
                float progreso = totalCreditosPlan > 0 ? (creditosCompletados * 100.0f) / totalCreditosPlan : 0f;
                vis.setPorcentajeProgreso(progreso);
            }
        }

        return vis;
    }
}