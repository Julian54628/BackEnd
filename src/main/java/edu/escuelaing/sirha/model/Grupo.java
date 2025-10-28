package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Grupo")
@Data
@NoArgsConstructor
public class Grupo {
    @Id
    private String id;
    private int idGrupo;
    private int cupoMaximo;
    private List<String> estudiantesInscritosIds = new ArrayList<>();
    private String profesorId;
    private List<String> horarioIds = new ArrayList<>();
    private String materiaId;
    private String periodoId;
    private int totalSolicitudes;

    public Grupo(int idGrupo, int cupoMaximo, String materiaId, String profesorId, String periodoId) {
        this.idGrupo = idGrupo;
        this.cupoMaximo = cupoMaximo;
        this.materiaId = materiaId;
        this.profesorId = profesorId;
        this.periodoId = periodoId;
    }

    public boolean esValido() {
        return idGrupo > 0 && cupoMaximo > 0 && cupoMaximo <= 50;
    }

    public int getCantidadInscritos() {
        return estudiantesInscritosIds.size();
    }
}