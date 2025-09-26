// ===== ADMINISTRADOR.JAVA =====
package edu.escuelaing.sirha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Administrador")
public class Administrador extends Usuario {
    @Id
    private String id;

    public Administrador() {
        super();
    }

    public Administrador(int idUsuario, String username, String passwordHash, String correo) {
        super(idUsuario, username, passwordHash, correo, Rol.ADMIN);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}