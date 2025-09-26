package edu.escuelaing.sirha.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SolicitudCambio")
public class SolicitudCambio {
    @Id
    private String id;
    private int idSolicitud;
    private Date fechaCreacion;
    private EstadoSolicitud estado;
    private int prioridad;
    private String observaciones;
    private Date fechaRespuesta;
    private String respuesta;

    private String estudianteId;
    private String materiaOrigenId;
    private String grupoOrigenId;
    private String materiaDestinoId;
    private String grupoDestinoId;

    public SolicitudCambio() {
        this.fechaCreacion = new Date();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public SolicitudCambio(String estudianteId, String materiaOrigenId, String grupoOrigenId,
                           String materiaDestinoId, String grupoDestinoId) {
        this();
        this.estudianteId = estudianteId;
        this.materiaOrigenId = materiaOrigenId;
        this.grupoOrigenId = grupoOrigenId;
        this.materiaDestinoId = materiaDestinoId;
        this.grupoDestinoId = grupoDestinoId;
    }

    public boolean esValida() {
        return estudianteId != null && !estudianteId.trim().isEmpty() &&
                materiaDestinoId != null && !materiaDestinoId.trim().isEmpty() &&
                grupoDestinoId != null && !grupoDestinoId.trim().isEmpty() &&
                estado != null;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(int idSolicitud) { this.idSolicitud = idSolicitud; }
    public Date getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Date getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(Date fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
    public String getEstudianteId() { return estudianteId; }
    public void setEstudianteId(String estudianteId) { this.estudianteId = estudianteId; }
    public String getMateriaOrigenId() { return materiaOrigenId; }
    public void setMateriaOrigenId(String materiaOrigenId) { this.materiaOrigenId = materiaOrigenId; }
    public String getGrupoOrigenId() { return grupoOrigenId; }
    public void setGrupoOrigenId(String grupoOrigenId) { this.grupoOrigenId = grupoOrigenId; }
    public String getMateriaDestinoId() { return materiaDestinoId; }
    public void setMateriaDestinoId(String materiaDestinoId) { this.materiaDestinoId = materiaDestinoId; }
    public String getGrupoDestinoId() { return grupoDestinoId; }
    public void setGrupoDestinoId(String grupoDestinoId) { this.grupoDestinoId = grupoDestinoId; }
}