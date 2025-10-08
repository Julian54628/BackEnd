package edu.escuelaing.sirha.model;

import java.util.List;

public class SemaforoVisualizacion {
    private String estudianteId;
    private String nombreEstudiante;
    private String codigoEstudiante;
    private String carrera;
    private int semestreActual;
    private String grado;
    
    // Estadísticas de créditos
    private int creditosActuales; // Créditos del semestre actual
    private int creditosCompletados; // Créditos de materias aprobadas
    private int creditosFaltantes; // Créditos restantes del plan
    private int creditosMaximosSemestre; // Máximo de créditos por semestre (18)
    private int totalCreditosPlan; // Total de créditos del plan académico
    
    // Estadísticas de materias
    private int materiasAprobadas;
    private int materiasReprobadas;
    private int materiasCursando;
    private int materiasPendientes;
    private int totalMateriasPlan;
    
    // Promedio y progreso
    private float promedioAcumulado;
    private float porcentajeProgreso; // Porcentaje de avance en el plan
    
    // Materias por color
    private List<MateriaSemaforo> materiasAzules; // Cursando actualmente
    private List<MateriaSemaforo> materiasVerdes; // Aprobadas
    private List<MateriaSemaforo> materiasRojas; // Reprobadas
    private List<MateriaSemaforo> materiasBlancas; // No vistas
    
    // Información adicional
    private String estadoAcademico; // "EN_RIESGO", "REGULAR", "EXCELENTE"
    private int semestresRestantes;
    private boolean puedeGraduarse;
    
    public SemaforoVisualizacion() {}

    // Getters y Setters
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    
    public int getSemestreActual() { return semestreActual; }
    public void setSemestreActual(int semestreActual) { this.semestreActual = semestreActual; }
    
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
    
    public int getCreditosActuales() { return creditosActuales; }
    public void setCreditosActuales(int creditosActuales) { this.creditosActuales = creditosActuales; }
    
    public int getCreditosCompletados() { return creditosCompletados; }
    public void setCreditosCompletados(int creditosCompletados) { this.creditosCompletados = creditosCompletados; }
    
    public int getCreditosFaltantes() { return creditosFaltantes; }
    public void setCreditosFaltantes(int creditosFaltantes) { this.creditosFaltantes = creditosFaltantes; }
    
    public int getCreditosMaximosSemestre() { return creditosMaximosSemestre; }
    public void setCreditosMaximosSemestre(int creditosMaximosSemestre) { this.creditosMaximosSemestre = creditosMaximosSemestre; }
    
    public int getTotalCreditosPlan() { return totalCreditosPlan; }
    public void setTotalCreditosPlan(int totalCreditosPlan) { this.totalCreditosPlan = totalCreditosPlan; }
    
    public int getMateriasAprobadas() { return materiasAprobadas; }
    public void setMateriasAprobadas(int materiasAprobadas) { this.materiasAprobadas = materiasAprobadas; }
    
    public int getMateriasReprobadas() { return materiasReprobadas; }
    public void setMateriasReprobadas(int materiasReprobadas) { this.materiasReprobadas = materiasReprobadas; }
    
    public int getMateriasCursando() { return materiasCursando; }
    public void setMateriasCursando(int materiasCursando) { this.materiasCursando = materiasCursando; }
    
    public int getMateriasPendientes() { return materiasPendientes; }
    public void setMateriasPendientes(int materiasPendientes) { this.materiasPendientes = materiasPendientes; }
    
    public int getTotalMateriasPlan() { return totalMateriasPlan; }
    public void setTotalMateriasPlan(int totalMateriasPlan) { this.totalMateriasPlan = totalMateriasPlan; }
    
    public float getPromedioAcumulado() { return promedioAcumulado; }
    public void setPromedioAcumulado(float promedioAcumulado) { this.promedioAcumulado = promedioAcumulado; }
    
    public float getPorcentajeProgreso() { return porcentajeProgreso; }
    public void setPorcentajeProgreso(float porcentajeProgreso) { this.porcentajeProgreso = porcentajeProgreso; }
    
    public List<MateriaSemaforo> getMateriasAzules() { return materiasAzules; }
    public void setMateriasAzules(List<MateriaSemaforo> materiasAzules) { this.materiasAzules = materiasAzules; }
    
    public List<MateriaSemaforo> getMateriasVerdes() { return materiasVerdes; }
    public void setMateriasVerdes(List<MateriaSemaforo> materiasVerdes) { this.materiasVerdes = materiasVerdes; }
    
    public List<MateriaSemaforo> getMateriasRojas() { return materiasRojas; }
    public void setMateriasRojas(List<MateriaSemaforo> materiasRojas) { this.materiasRojas = materiasRojas; }
    
    public List<MateriaSemaforo> getMateriasBlancas() { return materiasBlancas; }
    public void setMateriasBlancas(List<MateriaSemaforo> materiasBlancas) { this.materiasBlancas = materiasBlancas; }
    
    public String getEstadoAcademico() { return estadoAcademico; }
    public void setEstadoAcademico(String estadoAcademico) { this.estadoAcademico = estadoAcademico; }
    
    public int getSemestresRestantes() { return semestresRestantes; }
    public void setSemestresRestantes(int semestresRestantes) { this.semestresRestantes = semestresRestantes; }
    
    public boolean isPuedeGraduarse() { return puedeGraduarse; }
    public void setPuedeGraduarse(boolean puedeGraduarse) { this.puedeGraduarse = puedeGraduarse; }
}
