package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "PlanAcademico")
@Data
@NoArgsConstructor
public class PlanAcademico {
    @Id
    private String id;
    private int idPlan;
    private String nombre;
    private String grado;
    private int creditosTotales;
    private List<String> materiasObligatoriasIds = new ArrayList<>();
    private List<String> materiasElectivasIds = new ArrayList<>();

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
}