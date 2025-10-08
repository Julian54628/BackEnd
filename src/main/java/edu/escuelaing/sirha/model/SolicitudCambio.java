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
    private String decanaturaId;
    private TipoSolicitud tipoSolicitud;
    private TipoPrioridad tipoPrioridad;
    private String descripcion;
    private String justificacion;
    private String administradorId;

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
        this.tipoPrioridad = TipoPrioridad.NORMAL;
    }

    public SolicitudCambio(String estudianteId, String materiaOrigenId, String grupoOrigenId,
                           String materiaDestinoId, String grupoDestinoId, TipoSolicitud tipoSolicitud,
                           String descripcion, TipoPrioridad tipoPrioridad) {
        this();
        this.estudianteId = estudianteId;
        this.materiaOrigenId = materiaOrigenId;
        this.grupoOrigenId = grupoOrigenId;
        this.materiaDestinoId = materiaDestinoId;
        this.grupoDestinoId = grupoDestinoId;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion = descripcion;
        this.tipoPrioridad = tipoPrioridad;
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
    public String getDecanaturaId() { return decanaturaId; }
    public void setDecanaturaId(String decanaturaId) { this.decanaturaId = decanaturaId; }
    public TipoSolicitud getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) { this.tipoSolicitud = tipoSolicitud; }
    public TipoPrioridad getTipoPrioridad() { return tipoPrioridad; }
    public void setTipoPrioridad(TipoPrioridad tipoPrioridad) { this.tipoPrioridad = tipoPrioridad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getJustificacion() { return justificacion; }
    public void setJustificacion(String justificacion) { this.justificacion = justificacion; }
    public String getAdministradorId() { return administradorId; }
    public void setAdministradorId(String administradorId) { this.administradorId = administradorId; }
}