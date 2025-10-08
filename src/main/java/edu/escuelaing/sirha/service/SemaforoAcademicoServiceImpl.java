package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SemaforoAcademicoServiceImpl implements SemaforoAcademicoService {
    private final RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    private final RepositorioEstudiante repositorioEstudiante;
    private final RepositorioMateria repositorioMateria;
    private final RepositorioGrupo repositorioGrupo;
    private final RepositorioPlanAcademico repositorioPlanAcademico;
    
    @Autowired
    public SemaforoAcademicoServiceImpl(RepositorioSemaforoAcademico repositorioSemaforoAcademico,
                                      RepositorioEstudiante repositorioEstudiante,
                                      RepositorioMateria repositorioMateria,
                                      RepositorioGrupo repositorioGrupo,
                                      RepositorioPlanAcademico repositorioPlanAcademico) {
        this.repositorioSemaforoAcademico = repositorioSemaforoAcademico;
        this.repositorioEstudiante = repositorioEstudiante;
        this.repositorioMateria = repositorioMateria;
        this.repositorioGrupo = repositorioGrupo;
        this.repositorioPlanAcademico = repositorioPlanAcademico;
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
    
    @Override
    public SemaforoVisualizacion obtenerSemaforoCompleto(String estudianteId) {
        SemaforoVisualizacion semaforo = new SemaforoVisualizacion();
        
        // 1. Obtener información del estudiante
        Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return semaforo; // Retorna semáforo vacío si no existe el estudiante
        }
        
        Estudiante estudiante = estudianteOpt.get();
        semaforo.setEstudianteId(estudianteId);
        semaforo.setNombreEstudiante(estudiante.getNombre());
        semaforo.setCodigoEstudiante(estudiante.getCodigo());
        semaforo.setCarrera(estudiante.getCarrera());
        semaforo.setSemestreActual(estudiante.getSemestre());
        
        // 2. Obtener semáforo académico
        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforoOpt.isEmpty()) {
            return semaforo; // Retorna semáforo básico si no existe
        }
        
        SemaforoAcademico semaforoAcademico = semaforoOpt.get();
        semaforo.setGrado(semaforoAcademico.getGrado());
        semaforo.setPromedioAcumulado(semaforoAcademico.getPromedioAcumulado());
        semaforo.setCreditosCompletados(semaforoAcademico.getCreditosAprobados());
        semaforo.setTotalCreditosPlan(semaforoAcademico.getTotalCreditosPlan());
        semaforo.setCreditosFaltantes(semaforoAcademico.getTotalCreditosPlan() - semaforoAcademico.getCreditosAprobados());
        semaforo.setCreditosMaximosSemestre(18); // Máximo por semestre
        
        // 3. Calcular créditos actuales (del semestre actual)
        int creditosActuales = calcularCreditosSemestreActual(estudiante);
        semaforo.setCreditosActuales(creditosActuales);
        
        // 4. Obtener plan académico
        if (estudiante.getPlanAcademicoId() != null) {
            Optional<PlanAcademico> planOpt = repositorioPlanAcademico.findById(estudiante.getPlanAcademicoId());
            if (planOpt.isPresent()) {
                PlanAcademico plan = planOpt.get();
                semaforo.setTotalMateriasPlan(plan.getMateriasObligatoriasIds().size() + plan.getMateriasElectivasIds().size());
            }
        }
        
        // 5. Procesar materias por colores
        procesarMateriasPorColores(semaforo, estudiante, semaforoAcademico);
        
        // 6. Calcular estadísticas adicionales
        calcularEstadisticasAdicionales(semaforo);
        
        return semaforo;
    }
    
    @Override
    public SemaforoVisualizacion obtenerSemaforoDetallado(String estudianteId) {
        SemaforoVisualizacion semaforo = obtenerSemaforoCompleto(estudianteId);
        
        // Agregar información adicional para vista detallada
        semaforo.setSemestresRestantes(calcularSemestresRestantes(semaforo));
        semaforo.setPuedeGraduarse(verificarElegibilidadGraduacion(semaforo));
        semaforo.setEstadoAcademico(determinarEstadoAcademico(semaforo));
        
        return semaforo;
    }
    
    private int calcularCreditosSemestreActual(Estudiante estudiante) {
        int creditosActuales = 0;
        for (String grupoId : estudiante.getHorariosIds()) {
            Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
            if (grupoOpt.isPresent()) {
                Grupo grupo = grupoOpt.get();
                if (grupo.getMateriaId() != null) {
                    Optional<Materia> materiaOpt = repositorioMateria.findById(grupo.getMateriaId());
                    if (materiaOpt.isPresent()) {
                        creditosActuales += materiaOpt.get().getCreditos();
                    }
                }
            }
        }
        return creditosActuales;
    }
    
    private void procesarMateriasPorColores(SemaforoVisualizacion semaforo, Estudiante estudiante, SemaforoAcademico semaforoAcademico) {
        List<MateriaSemaforo> materiasAzules = new ArrayList<>();
        List<MateriaSemaforo> materiasVerdes = new ArrayList<>();
        List<MateriaSemaforo> materiasRojas = new ArrayList<>();
        List<MateriaSemaforo> materiasBlancas = new ArrayList<>();
        
        // Obtener todas las materias del plan académico
        Set<String> materiasPlan = new HashSet<>();
        if (estudiante.getPlanAcademicoId() != null) {
            Optional<PlanAcademico> planOpt = repositorioPlanAcademico.findById(estudiante.getPlanAcademicoId());
            if (planOpt.isPresent()) {
                PlanAcademico plan = planOpt.get();
                materiasPlan.addAll(plan.getMateriasObligatoriasIds());
                materiasPlan.addAll(plan.getMateriasElectivasIds());
            }
        }
        
        // Procesar materias del historial
        Map<String, EstadoMateria> historial = semaforoAcademico.getHistorialMaterias();
        for (Map.Entry<String, EstadoMateria> entry : historial.entrySet()) {
            String materiaId = entry.getKey();
            EstadoMateria estado = entry.getValue();
            
            Optional<Materia> materiaOpt = repositorioMateria.findById(materiaId);
            if (materiaOpt.isPresent()) {
                Materia materia = materiaOpt.get();
                MateriaSemaforo materiaSemaforo = new MateriaSemaforo(
                    materia.getId(),
                    materia.getNombre(),
                    materia.getCodigo(),
                    materia.getCreditos(),
                    materia.getFacultad(),
                    materia.isEsObligatoria(),
                    estado
                );
                
                switch (estado) {
                    case INSCRITA:
                    case PENDIENTE:
                        materiasAzules.add(materiaSemaforo);
                        break;
                    case APROBADA:
                        materiasVerdes.add(materiaSemaforo);
                        break;
                    case REPROBADA:
                    case CANCELADA:
                        materiasRojas.add(materiaSemaforo);
                        break;
                }
            }
        }
        
        // Procesar materias no vistas (blancas)
        for (String materiaId : materiasPlan) {
            if (!historial.containsKey(materiaId)) {
                Optional<Materia> materiaOpt = repositorioMateria.findById(materiaId);
                if (materiaOpt.isPresent()) {
                    Materia materia = materiaOpt.get();
                    MateriaSemaforo materiaSemaforo = new MateriaSemaforo(
                        materia.getId(),
                        materia.getNombre(),
                        materia.getCodigo(),
                        materia.getCreditos(),
                        materia.getFacultad(),
                        materia.isEsObligatoria(),
                        EstadoMateria.PENDIENTE
                    );
                    materiaSemaforo.setColor("BLANCO");
                    materiasBlancas.add(materiaSemaforo);
                }
            }
        }
        
        semaforo.setMateriasAzules(materiasAzules);
        semaforo.setMateriasVerdes(materiasVerdes);
        semaforo.setMateriasRojas(materiasRojas);
        semaforo.setMateriasBlancas(materiasBlancas);
    }
    
    private void calcularEstadisticasAdicionales(SemaforoVisualizacion semaforo) {
        // Contar materias por estado
        semaforo.setMateriasAprobadas(semaforo.getMateriasVerdes().size());
        semaforo.setMateriasReprobadas(semaforo.getMateriasRojas().size());
        semaforo.setMateriasCursando(semaforo.getMateriasAzules().size());
        semaforo.setMateriasPendientes(semaforo.getMateriasBlancas().size());
        
        // Calcular porcentaje de progreso
        if (semaforo.getTotalCreditosPlan() > 0) {
            float porcentaje = (float) semaforo.getCreditosCompletados() / semaforo.getTotalCreditosPlan() * 100;
            semaforo.setPorcentajeProgreso(porcentaje);
        }
    }
    
    private int calcularSemestresRestantes(SemaforoVisualizacion semaforo) {
        if (semaforo.getCreditosFaltantes() <= 0) return 0;
        
        // Asumiendo que el estudiante toma 15 créditos por semestre en promedio
        int creditosPromedioSemestre = 15;
        return (int) Math.ceil((float) semaforo.getCreditosFaltantes() / creditosPromedioSemestre);
    }
    
    private boolean verificarElegibilidadGraduacion(SemaforoVisualizacion semaforo) {
        return semaforo.getCreditosCompletados() >= semaforo.getTotalCreditosPlan() && 
               semaforo.getPromedioAcumulado() >= 3.0f;
    }
    
    private String determinarEstadoAcademico(SemaforoVisualizacion semaforo) {
        if (semaforo.getPromedioAcumulado() >= 4.0f) {
            return "EXCELENTE";
        } else if (semaforo.getPromedioAcumulado() >= 3.0f) {
            return "REGULAR";
        } else {
            return "EN_RIESGO";
        }
    }
}