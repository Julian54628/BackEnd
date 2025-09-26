package edu.escuelaing.sirha.model;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Usuario")
public class Usuario {
    @Id
    private String id;
    private int idUsuario;
    private String username;
    private String passwordHash;
    private String correoInstitucional;
    private Rol rol;
    private boolean activo;

    public Usuario() {
        this.activo = true;
    }

    public Usuario(int idUsuario, String username, String passwordHash, String correoInstitucional, Rol rol) {
        this();
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordHash = passwordHash;
        this.correoInstitucional = correoInstitucional;
        this.rol = rol;
    }

    public boolean esValido() {
        return username != null && !username.trim().isEmpty() &&
                correoInstitucional != null && correoInstitucional.contains("@") &&
                rol != null;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return idUsuario == usuario.idUsuario && Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, username);
    }
}