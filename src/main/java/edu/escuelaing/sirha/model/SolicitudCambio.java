package edu.escuelaing.sirha.model;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "SolicitudCambio")

public class SolicitudCambio {
    private static int contador = 0;
    private int idSolicitud;
    private Date fechaCreacion;
    private EstadoSolicitud estado;
    private int prioridad;
    private String observaciones;
    private Date fechaRespuesta;
    private String respuesta;
    private Estudiante estudiante;
    private Materia materiaOrigen;
    private Grupo grupoOrigen;
    private Materia materiaDestino;
    private Grupo grupoDestino;

    public SolicitudCambio(Estudiante estudiante, Materia materiaOrigen, Grupo grupoOrigen, Materia materiaDestino, Grupo grupoDestino) {
        this.idSolicitud = ++contador;
        this.fechaCreacion = new Date();
        this.estado = EstadoSolicitud.PENDIENTE;
        this.estudiante = estudiante;
        this.materiaOrigen = materiaOrigen;
        this.grupoOrigen = grupoOrigen;
        this.materiaDestino = materiaDestino;
        this.grupoDestino = grupoDestino;
    }

    public void asignarPrioridad() {
        this.prioridad = (int) (System.currentTimeMillis() % 10000);
    }

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void registrarRespuesta(String respuesta) {
        this.fechaRespuesta = new Date();
        this.respuesta = respuesta;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }
}

