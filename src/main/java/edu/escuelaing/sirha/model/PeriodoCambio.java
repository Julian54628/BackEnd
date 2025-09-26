package edu.escuelaing.sirha.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "PeriodoCambio")
public class PeriodoCambio {
    @Id
    private String id;
    private int idPeriodo;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean activo;
    private String tipo; // "ORDINARIO", "EXTRAORDINARIO"
    private String descripcion;

    public PeriodoCambio() {
        this.activo = false;
    }

    public PeriodoCambio(int idPeriodo, String nombre, Date fechaInicio, Date fechaFin, String tipo) {
        this();
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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdPeriodo() { return idPeriodo; }
    public void setIdPeriodo(int idPeriodo) { this.idPeriodo = idPeriodo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}