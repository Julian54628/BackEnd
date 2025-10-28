package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "PeriodoCambio")
@Data
@NoArgsConstructor
public class PeriodoCambio {
    @Id
    private String id;
    private int idPeriodo;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo = false;
    private String tipo;
    private String descripcion;

    public PeriodoCambio(int idPeriodo, String nombre, Date fechaInicio, Date fechaFin, String tipo) {
        this.idPeriodo = idPeriodo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                fechaInicio != null && fechaFin != null &&
                fechaInicio.before(fechaFin) &&
                tipo != null && !tipo.trim().isEmpty() &&
                idPeriodo > 0;
    }
}