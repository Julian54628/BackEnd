package edu.escuelaing.sirha.model;

import java.util.List;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String username, String passwordHash) {
        super(idUsuario, username, passwordHash, Rol.ADMIN);
    }

    public void modificarCupoGrupo(Grupo g, int nuevoCupo) {
        g.setCupoMaximo(nuevoCupo);
    }

    public void configurarPeriodoCambio(PeriodoCambio p, boolean activo) {
        p.setActivo(activo);
    }

    public void verReportesGlobales(List<SolicitudCambio> solicitudes) {
        long aprobadas = solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count();
        long rechazadas = solicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count();
        System.out.println("Reportes Globales:");
        System.out.println("Aprobadas: " + aprobadas);
        System.out.println("Rechazadas: " + rechazadas);
    }
}

