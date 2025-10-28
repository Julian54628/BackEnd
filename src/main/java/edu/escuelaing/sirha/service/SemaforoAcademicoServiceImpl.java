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

    private final RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    private final RepositorioEstudiante repositorioEstudiante;

    @Autowired
    public SemaforoAcademicoServiceImpl(RepositorioSemaforoAcademico repositorioSemaforoAcademico,
                                        RepositorioEstudiante repositorioEstudiante) {
        this.repositorioSemaforoAcademico = repositorioSemaforoAcademico;
        this.repositorioEstudiante = repositorioEstudiante;
    }

    @Override
    public Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId) {
        return repositorioSemaforoAcademico.findByEstudianteId(estudianteId)
                .map(semaforo -> semaforo.getHistorialMaterias().entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> mapearEstado(entry.getValue())
                        )))
                .orElse(Collections.emptyMap());
    }

    @Override
    public Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId) {
        return repositorioSemaforoAcademico.findByEstudianteId(estudianteId)
                .map(semaforo -> semaforo.getHistorialMaterias().get(materiaId))
                .map(this::mapearEstado);
    }

    private EstadoSemaforo mapearEstado(EstadoMateria estadoMateria) {
        return switch (estadoMateria) {
            case APROBADA -> EstadoSemaforo.VERDE;
            case INSCRITA, PENDIENTE -> EstadoSemaforo.AZUL;
            case REPROBADA, CANCELADA -> EstadoSemaforo.ROJO;
            default -> EstadoSemaforo.AZUL;
        };
    }

    @Override
    public int getSemestreActual(String estudianteId) {
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

        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforoOpt.isPresent()) {
            SemaforoAcademico semaforo = semaforoOpt.get();
            Map<String, EstadoMateria> materias = semaforo.getHistorialMaterias();

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
            resultado.put("materiasAprobadas", 0);
            resultado.put("materiasReprobadas", 0);
            resultado.put("materiasInscritas", 0);
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
        long reprobadas = (long) foraneo.getOrDefault("materiasReprobadas", 0L);
        double promedio = (double) foraneo.getOrDefault("promedio", 0.0);

        return reprobadas > 3 || promedio < 3.0;
    }

    private SemaforoVisualizacion construirSemaforoVisualizacion(String estudianteId, boolean detallado) {
        Map<String, Object> foraneo = getForaneoEstudiante(estudianteId);
        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);

        SemaforoVisualizacion vis = new SemaforoVisualizacion();
        vis.setEstudianteId(estudianteId);
        vis.setSemestreActual((int) foraneo.get("semestreActual"));
        vis.setCreditosCompletados(((Number) foraneo.get("creditosAprobados")).intValue());
        vis.setPromedioAcumulado(((Number) foraneo.get("promedio")).floatValue());
        vis.setMateriasAprobadas(((Number) foraneo.get("materiasAprobadas")).intValue());
        vis.setMateriasReprobadas(((Number) foraneo.get("materiasReprobadas")).intValue());
        vis.setMateriasCursando(((Number) foraneo.get("materiasInscritas")).intValue());
        vis.setTotalMateriasPlan(((Number) foraneo.get("totalMaterias")).intValue());

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