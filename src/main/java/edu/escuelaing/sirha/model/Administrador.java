package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Administrador")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Administrador extends Usuario {

    public Administrador(int idUsuario, String username, String passwordHash, String correo) {
        super(idUsuario, username, passwordHash, correo, Rol.ADMIN);
    }
}