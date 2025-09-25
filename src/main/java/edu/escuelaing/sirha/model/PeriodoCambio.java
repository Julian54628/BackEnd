package edu.escuelaing.sirha.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "PeriodoCambio")

public class PeriodoCambio {
    private int idPeriodo;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;

    public boolean estaHabilitado(Date fecha) {
        return activo && !fecha.before(fechaInicio) && !fecha.after(fechaFin);
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

