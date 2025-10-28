package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.sql.Time;
import java.util.Objects;

@Document(collection = "Horario")
@Data
@NoArgsConstructor
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

    public Horario(int idHorario, String diaSemana, Time horaInicio, Time horaFin, String salon) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.salon = salon;
    }

    public boolean esValido() {
        return HorarioValidator.isValid(this);
    }

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