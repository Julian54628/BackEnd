package edu.escuelaing.sirha.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Administrador")
public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(int idUsuario, String username, String passwordHash, String correo) {
        super(idUsuario, username, passwordHash, correo, Rol.ADMIN);
    }

}