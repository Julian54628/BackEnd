package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Decanatura")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Decanatura extends Usuario {
    private int idDecanatura;
    private String nombre;
    private String facultad;
    private boolean esAdministrador = false;

    public Decanatura(int idUsuario, String username, String passwordHash, String correo,int idDecanatura, String nombre, String facultad) {
        super(idUsuario, username, passwordHash, correo, Rol.DECANATURA);
        this.idDecanatura = idDecanatura;
        this.nombre = nombre;
        this.facultad = facultad;
    }

    public Decanatura(int idUsuario, String username, String passwordHash, String correo, int idDecanatura, String nombre, String facultad, boolean esAdministrador) {
        super(idUsuario, username, passwordHash, correo, Rol.DECANATURA);
        this.idDecanatura = idDecanatura;
        this.nombre = nombre;
        this.facultad = facultad;
        this.esAdministrador = esAdministrador;
    }

    public boolean esValida() {
        return super.esValido() && DecanaturaValidator.isValid(this);
    }

    public boolean tienePermisosAdministrador() {
        return this.esAdministrador;
    }

    public void convertirEnAdministrador() {
        this.esAdministrador = true;
    }

    public void removerPermisosAdministrador() {
        this.esAdministrador = false;
    }
}