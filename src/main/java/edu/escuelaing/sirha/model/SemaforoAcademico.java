package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "SemaforoAcademico")
@Data
@NoArgsConstructor
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
}