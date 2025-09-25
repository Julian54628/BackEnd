package edu.escuelaing.sirha.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Decanatura")

public class Decanatura extends Usuario {
    private int idDecanatura;
    private String nombre;
    private String facultad;

    public Decanatura(int idUsuario, String username, String passwordHash, int idDecanatura, String nombre, String facultad) {
        super(idUsuario, username, passwordHash, Rol.DECANATURA);
        this.idDecanatura = idDecanatura;
        this.nombre = nombre;
        this.facultad = facultad;
    }

    public List<SolicitudCambio> consultarSolicitudes(List<SolicitudCambio> solicitudes) {
        return solicitudes;
    }

    public void responderSolicitud(SolicitudCambio solicitud, EstadoSolicitud nuevoEstado, String respuesta) {
        solicitud.cambiarEstado(nuevoEstado);
        solicitud.registrarRespuesta(respuesta);
    }

    public void configurarPeriodoHabilitado(PeriodoCambio periodo, boolean activo) {
        periodo.setActivo(activo);
    }

    public void aprobarSolicitudEspecial(SolicitudCambio solicitud) {
        solicitud.cambiarEstado(EstadoSolicitud.APROBADA);
    }
}
