package edu.escuelaing.sirha.model;

import java.util.Objects;

public class Usuario {
    private int idUsuario;
    private String username;
    private String passwordHash;
    private Rol rol;

    public Usuario(int idUsuario, String username, String passwordHash, Rol rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public boolean autenticar(String username, String passwordHash) {
        return Objects.equals(this.username, username) &&
                Objects.equals(this.passwordHash, passwordHash);
    }

    public boolean tienePermiso(String accion) {
        switch (rol) {
            case ADMIN:
                return true;
            case DECANATURA:
                return !accion.equalsIgnoreCase("gestionarUsuarios");
            case ESTUDIANTE:
                return accion.equalsIgnoreCase("crearSolicitud") ||
                        accion.equalsIgnoreCase("consultarHorario");
            default:
                return false;
        }
    }
    public Rol getRol() {
        return rol;
    }
}

