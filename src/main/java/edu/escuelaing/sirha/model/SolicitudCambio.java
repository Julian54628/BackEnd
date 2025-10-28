package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "SolicitudCambio")
@Data
@NoArgsConstructor
public class SolicitudCambio {
    @Id
    private String id;
    private int idSolicitud;
    private Date fechaCreacion = new Date();
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
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
    private TipoPrioridad tipoPrioridad;
    private String justificacion;
    private String administradorId;
    private List<String> historialEstados = new ArrayList<>();
    private String descripcion;

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
                           String materiaDestinoId, String grupoDestinoId,
                           String descripcion, TipoPrioridad tipoPrioridad) {
        this();
        this.estudianteId = estudianteId;
        this.materiaOrigenId = materiaOrigenId;
        this.grupoOrigenId = grupoOrigenId;
        this.materiaDestinoId = materiaDestinoId;
        this.grupoDestinoId = grupoDestinoId;
        this.descripcion = descripcion;
        this.tipoPrioridad = tipoPrioridad;
    }

    public boolean esValida() {
        return estudianteId != null && !estudianteId.trim().isEmpty() &&
                materiaDestinoId != null && !materiaDestinoId.trim().isEmpty() &&
                grupoDestinoId != null && !grupoDestinoId.trim().isEmpty() &&
                estado != null;
    }

    public void addHistorialEstado(String estado) {
        if (this.historialEstados == null) {
            this.historialEstados = new ArrayList<>();
        }
        this.historialEstados.add(estado);
    }
}