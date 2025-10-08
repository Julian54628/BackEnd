package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import edu.escuelaing.sirha.model.Estudiante;
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

    @Override
    public Map<String, EstadoSemaforo> visualizarSemaforoEstudiante(String estudianteId) {
        Optional<SemaforoAcademico> optSemaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (!optSemaforo.isPresent()) {
            return Collections.emptyMap();
        }
        Map<String, EstadoMateria> historial = optSemaforo.get().getHistorialMaterias();
        return historial.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,entry -> mapearEstado(entry.getValue())));
    }

    @Override
    public Optional<EstadoSemaforo> consultarSemaforoMateria(String estudianteId, String materiaId) {
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
            // Buscar el estudiante
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

        Optional<SemaforoAcademico> semaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforo.isPresent()) {
            SemaforoAcademico sem = semaforo.get();

            resultado.put("materiasVistas", sem.getMateriasVistas());
            resultado.put("creditosAprobados", sem.getCreditosAprobados());
            resultado.put("promedio", sem.getPromedioAcumulado());

            Map<String, EstadoMateria> materias = sem.getHistorialMaterias();
            int aprobadas = 0;
            int reprobadas = 0;
            int inscritas = 0;

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

        } else {
            resultado.put("materiasVistas", 0);
            resultado.put("creditosAprobados", 0);
            resultado.put("promedio", 0.0);
            resultado.put("materiasAprobadas", 0);
            resultado.put("materiasReprobadas", 0);
            resultado.put("materiasInscritas", 0);
            resultado.put("totalMaterias", 0);
        }

        return resultado;
    }
}