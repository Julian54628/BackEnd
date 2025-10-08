package edu.escuelaing.sirha.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Grupo")
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

    public Grupo() {}

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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdGrupo() { return idGrupo; }
    public void setIdGrupo(int idGrupo) { this.idGrupo = idGrupo; }
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    public List<String> getEstudiantesInscritosIds() { return estudiantesInscritosIds; }
    public void setEstudiantesInscritosIds(List<String> estudiantesInscritosIds) { this.estudiantesInscritosIds = estudiantesInscritosIds; }
    public String getProfesorId() { return profesorId; }
    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }
    public List<String> getHorarioIds() { return horarioIds; }
    public void setHorarioIds(List<String> horarioIds) { this.horarioIds = horarioIds; }
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    public String getPeriodoId() { return periodoId; }
    public void setPeriodoId(String periodoId) { this.periodoId = periodoId; }
    public int getTotalSolicitudes() { return totalSolicitudes; }
    public void setTotalSolicitudes(int totalSolicitudes) { this.totalSolicitudes = totalSolicitudes; }
}