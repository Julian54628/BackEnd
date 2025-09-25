package edu.escuelaing.sirha.model;

import java.sql.Time;

public class Horario {
    private int idHorario;
    private String diaSemana;
    private Time horaInicio;
    private Time horaFin;
    private String salon;

    public boolean hayCruceCon(Horario h) {
        if (!this.diaSemana.equalsIgnoreCase(h.diaSemana)) {
            return false;
        }
        return !(horaFin.before(h.horaInicio) || horaInicio.after(h.horaFin));
    }
}

