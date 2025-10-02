package edu.escuelaing.sirha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Decanatura")
public class Decanatura extends Usuario {

    private int idDecanatura;
    private String nombre;
    private String facultad;

    public Decanatura() {
        super();
    }

    public Decanatura(int idUsuario, String username, String passwordHash, String correo,
                      int idDecanatura, String nombre, String facultad) {
        super(idUsuario, username, passwordHash, correo, Rol.DECANATURA);
        this.idDecanatura = idDecanatura;
        this.nombre = nombre;
        this.facultad = facultad;
    }

    public boolean esValida() {
        return super.esValido() &&
                nombre != null && !nombre.trim().isEmpty() &&
                facultad != null && !facultad.trim().isEmpty() &&
                idDecanatura > 0;
    }

    public int getIdDecanatura() { return idDecanatura; }
    public void setIdDecanatura(int idDecanatura) { this.idDecanatura = idDecanatura; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
}