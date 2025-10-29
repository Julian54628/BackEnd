package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioSemaforoAcademico;
import edu.escuelaing.sirha.repository.RepositorioEstudiante;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SemaforoAcademicoServiceImpl implements SemaforoAcademicoService {

    private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    private RepositorioEstudiante repositorioEstudiante;

    public SemaforoAcademicoServiceImpl() {
        this.repositorioSemaforoAcademico = null;
        this.repositorioEstudiante = null;
    }

    @Autowired
    public SemaforoAcademicoServiceImpl(RepositorioSemaforoAcademico repositorioSemaforoAcademico,
                                        RepositorioEstudiante repositorioEstudiante) {
        this.repositorioSemaforoAcademico = repositorioSemaforoAcademico;
        this.repositorioEstudiante = repositorioEstudiante;
    }

    @Override
    public Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId) {
        if (repositorioSemaforoAcademico == null) return Collections.emptyMap();

        return repositorioSemaforoAcademico.findByEstudianteId(estudianteId)
                .map(semaforo -> {
                    Map<String, EstadoMateria> historial = semaforo.getHistorialMaterias();
                    if (historial == null) return Collections.<String, EstadoSemaforo>emptyMap();
                    return historial.entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> mapearEstado(entry.getValue())
                            ));
                })
                .orElse(Collections.emptyMap());
    }

    @Override
    public Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId) {
        if (repositorioSemaforoAcademico == null) return Optional.empty();

        return repositorioSemaforoAcademico.findByEstudianteId(estudianteId)
                .map(semaforo -> {
                    Map<String, EstadoMateria> historial = semaforo.getHistorialMaterias();
                    return historial == null ? null : historial.get(materiaId);
                })
                .map(this::mapearEstado);
    }

    private EstadoSemaforo mapearEstado(EstadoMateria estadoMateria) {
        if (estadoMateria == null) return EstadoSemaforo.AZUL;
        return switch (estadoMateria) {
            case APROBADA -> EstadoSemaforo.VERDE;
            case INSCRITA, PENDIENTE -> EstadoSemaforo.AZUL;
            case REPROBADA, CANCELADA -> EstadoSemaforo.ROJO;
            default -> EstadoSemaforo.AZUL;
        };
    }

    @Override
    public int getSemestreActual(String estudianteId) {
        if (repositorioEstudiante == null) return 0;
        return repositorioEstudiante.findById(estudianteId)
                .map(Estudiante::getSemestre)
                .orElse(0);
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

        if (repositorioSemaforoAcademico == null) {
            resultado.put("materiasAprobadas", 0L);
            resultado.put("materiasReprobadas", 0L);
            resultado.put("materiasInscritas", 0L);
            resultado.put("totalMaterias", 0);
            resultado.put("creditosAprobados", 0);
            resultado.put("promedio", 0.0);
            return resultado;
        }

        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforoOpt.isPresent()) {
            SemaforoAcademico semaforo = semaforoOpt.get();
            Map<String, EstadoMateria> materias = semaforo.getHistorialMaterias();
            if (materias == null) materias = Collections.emptyMap();

            long aprobadas = materias.values().stream().filter(e -> e == EstadoMateria.APROBADA).count();
            long reprobadas = materias.values().stream().filter(e -> e == EstadoMateria.REPROBADA).count();
            long inscritas = materias.values().stream().filter(e -> e == EstadoMateria.INSCRITA).count();

            resultado.put("materiasAprobadas", aprobadas);
            resultado.put("materiasReprobadas", reprobadas);
            resultado.put("materiasInscritas", inscritas);
            resultado.put("totalMaterias", materias.size());
            resultado.put("creditosAprobados", semaforo.getCreditosAprobados());
            resultado.put("promedio", semaforo.getPromedioAcumulado());
        } else {
            resultado.put("materiasAprobadas", 0L);
            resultado.put("materiasReprobadas", 0L);
            resultado.put("materiasInscritas", 0L);
            resultado.put("totalMaterias", 0);
            resultado.put("creditosAprobados", 0);
            resultado.put("promedio", 0.0);
        }

        return resultado;
    }

    @Override
    public SemaforoVisualizacion obtenerSemaforoCompleto(String estudianteId) {
        return construirSemaforoVisualizacion(estudianteId, false);
    }

    @Override
    public SemaforoVisualizacion obtenerSemaforoDetallado(String estudianteId) {
        return construirSemaforoVisualizacion(estudianteId, true);
    }

    @Override
    public boolean tieneProblemasAcademicos(String estudianteId) {
        Map<String, Object> foraneo = getForaneoEstudiante(estudianteId);
        Number reprobadasNum = (Number) foraneo.getOrDefault("materiasReprobadas", 0L);
        Number promedioNum = (Number) foraneo.getOrDefault("promedio", 0.0);

        long reprobadas = reprobadasNum.longValue();
        double promedio = promedioNum.doubleValue();

        return reprobadas > 3 || promedio < 3.0;
    }

    private SemaforoVisualizacion construirSemaforoVisualizacion(String estudianteId, boolean detallado) {
        Map<String, Object> foraneo = getForaneoEstudiante(estudianteId);
        Optional<SemaforoAcademico> semaforoOpt = (repositorioSemaforoAcademico == null)
                ? Optional.empty()
                : repositorioSemaforoAcademico.findByEstudianteId(estudianteId);

        SemaforoVisualizacion vis = new SemaforoVisualizacion();
        vis.setEstudianteId(estudianteId);
        vis.setSemestreActual(((Number) foraneo.getOrDefault("semestreActual", 0)).intValue());
        vis.setCreditosCompletados(((Number) foraneo.getOrDefault("creditosAprobados", 0)).intValue());
        vis.setPromedioAcumulado(((Number) foraneo.getOrDefault("promedio", 0.0)).floatValue());
        vis.setMateriasAprobadas(((Number) foraneo.getOrDefault("materiasAprobadas", 0L)).intValue());
        vis.setMateriasReprobadas(((Number) foraneo.getOrDefault("materiasReprobadas", 0L)).intValue());
        vis.setMateriasCursando(((Number) foraneo.getOrDefault("materiasInscritas", 0L)).intValue());
        vis.setTotalMateriasPlan(((Number) foraneo.getOrDefault("totalMaterias", 0)).intValue());

        if (semaforoOpt.isPresent()) {
            SemaforoAcademico semaforo = semaforoOpt.get();
            int totalCreditosPlan = semaforo.getTotalCreditosPlan();
            vis.setTotalCreditosPlan(totalCreditosPlan);

            int creditosCompletados = vis.getCreditosCompletados();
            vis.setCreditosFaltantes(Math.max(0, totalCreditosPlan - creditosCompletados));

            float progreso = totalCreditosPlan > 0 ? (creditosCompletados * 100.0f) / totalCreditosPlan : 0f;
            vis.setPorcentajeProgreso(progreso);
        }

        return vis;
    }
}

