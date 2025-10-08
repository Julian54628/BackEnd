package edu.escuelaing.sirha.model;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PlanAcademico")
public class PlanAcademico {
    @Id
    private String id;
    private int idPlan;
    private String nombre;
    private String grado;
    private int creditosTotales;
    private List<String> materiasObligatoriasIds = new ArrayList<>();
    private List<String> materiasElectivasIds = new ArrayList<>();

    public PlanAcademico() {}

    public PlanAcademico(int idPlan, String nombre, String grado, int creditosTotales) {
        this.idPlan = idPlan;
        this.nombre = nombre;
        this.grado = grado;
        this.creditosTotales = creditosTotales;
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                grado != null && (grado.equals("PREGRADO") || grado.equals("MAESTRIA")) &&
                creditosTotales > 0 && idPlan > 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdPlan() { return idPlan; }
    public void setIdPlan(int idPlan) { this.idPlan = idPlan; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
    public int getCreditosTotales() { return creditosTotales; }
    public void setCreditosTotales(int creditosTotales) { this.creditosTotales = creditosTotales; }
    public List<String> getMateriasObligatoriasIds() { return materiasObligatoriasIds; }
    public void setMateriasObligatoriasIds(List<String> materiasObligatoriasIds) { this.materiasObligatoriasIds = materiasObligatoriasIds; }
    public List<String> getMateriasElectivasIds() { return materiasElectivasIds; }
    public void setMateriasElectivasIds(List<String> materiasElectivasIds) { this.materiasElectivasIds = materiasElectivasIds; }
}