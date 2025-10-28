package edu.escuelaing.sirha.model;

import java.util.List;

public class SemaforoVisualizacionBuilder {
    private final SemaforoVisualizacion semaforo;

    public SemaforoVisualizacionBuilder() {
        this.semaforo = new SemaforoVisualizacion();
    }

    public SemaforoVisualizacionBuilder withStudentInfo(String estudianteId, String nombre,
                                                        String codigo, String carrera, int semestre, String grado) {
        semaforo.setEstudianteId(estudianteId);
        semaforo.setNombreEstudiante(nombre);
        semaforo.setCodigoEstudiante(codigo);
        semaforo.setCarrera(carrera);
        semaforo.setSemestreActual(semestre);
        semaforo.setGrado(grado);
        return this;
    }

    public SemaforoVisualizacionBuilder withCreditsInfo(int actuales, int completados,
                                                        int faltantes, int total, int maximosSemestre) {
        semaforo.setCreditosActuales(actuales);
        semaforo.setCreditosCompletados(completados);
        semaforo.setCreditosFaltantes(faltantes);
        semaforo.setTotalCreditosPlan(total);
        semaforo.setCreditosMaximosSemestre(maximosSemestre);
        return this;
    }

    public SemaforoVisualizacionBuilder withSubjectsByColor(List<MateriaSemaforo> azules,
                                                            List<MateriaSemaforo> verdes,
                                                            List<MateriaSemaforo> rojas,
                                                            List<MateriaSemaforo> blancas) {
        semaforo.setMateriasAzules(azules);
        semaforo.setMateriasVerdes(verdes);
        semaforo.setMateriasRojas(rojas);
        semaforo.setMateriasBlancas(blancas);
        return this;
    }

    public SemaforoVisualizacionBuilder withAcademicStatus(float promedio, boolean puedeGraduarse,
                                                           String estadoAcademico, int semestresRestantes) {
        semaforo.setPromedioAcumulado(promedio);
        semaforo.setPuedeGraduarse(puedeGraduarse);
        semaforo.setEstadoAcademico(estadoAcademico);
        semaforo.setSemestresRestantes(semestresRestantes);
        return this;
    }

    public SemaforoVisualizacion build() {
        calculateSubjectStatistics();
        calculateProgressPercentage();
        return semaforo;
    }

    private void calculateSubjectStatistics() {
        if (semaforo.getMateriasAzules() != null) {
            semaforo.setMateriasCursando(semaforo.getMateriasAzules().size());
        }
        if (semaforo.getMateriasVerdes() != null) {
            semaforo.setMateriasAprobadas(semaforo.getMateriasVerdes().size());
        }
        if (semaforo.getMateriasRojas() != null) {
            semaforo.setMateriasReprobadas(semaforo.getMateriasRojas().size());
        }

        int totalMaterias = semaforo.getMateriasAprobadas() +
                semaforo.getMateriasCursando() +
                semaforo.getMateriasReprobadas();
        semaforo.setTotalMateriasPlan(totalMaterias);
    }

    private void calculateProgressPercentage() {
        if (semaforo.getTotalCreditosPlan() > 0) {
            float progreso = (semaforo.getCreditosCompletados() / (float) semaforo.getTotalCreditosPlan()) * 100;
            semaforo.setPorcentajeProgreso(progreso);
        }
    }
}