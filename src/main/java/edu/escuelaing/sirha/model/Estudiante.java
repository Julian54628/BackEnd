package edu.escuelaing.sirha.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Estudiante")

public class Estudiante extends Usuario {
    private int idEstudiante;
    private String nombre;
    private String codigo;
    private String carrera;
    private int semestre;
    private String correoInstitucional;
    private String planEstudios;

    private List<SolicitudCambio> solicitudes = new ArrayList<>();
    private List<Horario> horarios = new ArrayList<>();
    private SemaforoAcademico semaforo;

    public Estudiante(int idUsuario, String username, String passwordHash, int idEstudiante, String nombre, String codigo) {
        super(idUsuario, username, passwordHash, Rol.ESTUDIANTE);
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public SolicitudCambio crearSolicitudCambio(Materia origen, Grupo gOrigen, Materia destino, Grupo gDestino) {
        SolicitudCambio solicitud = new SolicitudCambio(this, origen, gOrigen, destino, gDestino);
        solicitudes.add(solicitud);
        return solicitud;
    }

    public List<Horario> consultarHorarioActual() {
        return horarios;
    }

    public SemaforoAcademico consultarSemaforoAcademico() {
        return semaforo;
    }

    public List<SolicitudCambio> consultarHistorialSolicitudes() {
        return solicitudes;
    }
}
