package edu.escuelaing.sirha.model;

import java.sql.Time;

public class HorarioValidator {
    public static boolean isValid(Horario horario) {
        return horario.getDiaSemana() != null && !horario.getDiaSemana().trim().isEmpty() &&
                horario.getHoraInicio() != null && horario.getHoraFin() != null &&
                horario.getHoraInicio().before(horario.getHoraFin()) &&
                horario.getSalon() != null && !horario.getSalon().trim().isEmpty() &&
                horario.getIdHorario() > 0;
    }

    public static String getErrorMessage() {
        return "Horario inválido: día, horas válidas, salón e ID son requeridos";
    }
}