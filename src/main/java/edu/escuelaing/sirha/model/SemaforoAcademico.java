package edu.escuelaing.sirha.model;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SemaforoAcademico")
public class SemaforoAcademico {
    @Id
    private String id;
    private String estudianteId;
    private String grado;
    private String planAcademicoId;
    private boolean cambioDePlan;
    private String planAnteriorId;
    private int creditosAprobados;
    private int totalCreditosPlan;
    private int materiasVistas;
    private int totalMateriasDelPlan;
    private float promedioAcumulado;
    private Map<String, EstadoMateria> historialMaterias = new HashMap<>();

    public SemaforoAcademico() {}

    public SemaforoAcademico(String estudianteId, String grado, String planAcademicoId) {
        this.estudianteId = estudianteId;
        this.grado = grado;
        this.planAcademicoId = planAcademicoId;
        this.cambioDePlan = false;
        this.promedioAcumulado = 0.0f;
    }

    public boolean esValido() {
        return estudianteId != null && !estudianteId.trim().isEmpty() &&
                grado != null && (grado.equals("PREGRADO") || grado.equals("MAESTRIA")) &&
                creditosAprobados >= 0 && materiasVistas >= 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
    public String getPlanAcademicoId() { return planAcademicoId; }
    public void setPlanAcademicoId(String planAcademicoId) { this.planAcademicoId = planAcademicoId; }
    public boolean isCambioDePlan() { return cambioDePlan; }
    public void setCambioDePlan(boolean cambioDePlan) { this.cambioDePlan = cambioDePlan; }
    public String getPlanAnteriorId() { return planAnteriorId; }
    public void setPlanAnteriorId(String planAnteriorId) { this.planAnteriorId = planAnteriorId; }
    public int getCreditosAprobados() { return creditosAprobados; }
    public void setCreditosAprobados(int creditosAprobados) { this.creditosAprobados = creditosAprobados; }
    public int getTotalCreditosPlan() { return totalCreditosPlan; }
    public void setTotalCreditosPlan(int totalCreditosPlan) { this.totalCreditosPlan = totalCreditosPlan; }
    public int getMateriasVistas() { return materiasVistas; }
    public void setMateriasVistas(int materiasVistas) { this.materiasVistas = materiasVistas; }
    public int getTotalMateriasDelPlan() { return totalMateriasDelPlan; }
    public void setTotalMateriasDelPlan(int totalMateriasDelPlan) { this.totalMateriasDelPlan = totalMateriasDelPlan; }
    public float getPromedioAcumulado() { return promedioAcumulado; }
    public void setPromedioAcumulado(float promedioAcumulado) { this.promedioAcumulado = promedioAcumulado; }
    public Map<String, EstadoMateria> getHistorialMaterias() { return historialMaterias; }
    public void setHistorialMaterias(Map<String, EstadoMateria> historialMaterias) { this.historialMaterias = historialMaterias; }
}