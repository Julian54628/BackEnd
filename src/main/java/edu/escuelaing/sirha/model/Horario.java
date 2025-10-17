package edu.escuelaing.sirha.model;

import java.sql.Time;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Horario")
public class Horario {
    @Id
    private String id;
    private int idHorario;
    private String diaSemana;
    private Time horaInicio;
    private Time horaFin;
    private String salon;
    private String materiaId;
    private String grupoId;

    public Horario() {}

    public Horario(int idHorario, String diaSemana, Time horaInicio, Time horaFin, String salon) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.salon = salon;
    }

    public boolean esValido() {
        return diaSemana != null && !diaSemana.trim().isEmpty() &&
                horaInicio != null && horaFin != null &&
                horaInicio.before(horaFin) &&
                salon != null && !salon.trim().isEmpty();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdHorario() { return idHorario; }
    public void setIdHorario(int idHorario) { this.idHorario = idHorario; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public Time getHoraInicio() { return horaInicio; }
    public void setHoraInicio(Time horaInicio) { this.horaInicio = horaInicio; }
    public Time getHoraFin() { return horaFin; }
    public void setHoraFin(Time horaFin) { this.horaFin = horaFin; }
    public String getSalon() { return salon; }
    public void setSalon(String salon) { this.salon = salon; }
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    public String getGrupoId() { return grupoId; }
    public void setGrupoId(String grupoId) { this.grupoId = grupoId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horario horario = (Horario) o;
        return idHorario == horario.idHorario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHorario);
    }
}