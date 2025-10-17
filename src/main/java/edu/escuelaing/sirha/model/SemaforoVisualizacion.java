package edu.escuelaing.sirha.model;

import java.util.List;

public class SemaforoVisualizacion {
    private String estudianteId;
    private String nombreEstudiante;
    private String codigoEstudiante;
    private String carrera;
    private int semestreActual;
    private String grado;
    
    private int creditosActuales;
    private int creditosCompletados;
    private int creditosFaltantes;
    private int creditosMaximosSemestre;
    private int totalCreditosPlan;
    
    private int materiasAprobadas;
    private int materiasReprobadas;
    private int materiasCursando;
    private int materiasPendientes;
    private int totalMateriasPlan;
    
    private float promedioAcumulado;
    private float porcentajeProgreso;
    
    private List<MateriaSemaforo> materiasAzules;
    private List<MateriaSemaforo> materiasVerdes;
    private List<MateriaSemaforo> materiasRojas;
    private List<MateriaSemaforo> materiasBlancas;
    
    private String estadoAcademico;
    private int semestresRestantes;
    private boolean puedeGraduarse;
    
    public SemaforoVisualizacion() {}

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
