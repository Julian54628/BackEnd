package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    private String id;
    private int idUsuario;
    private String username;
    private String passwordHash;
    private String correoInstitucional;
    private Rol rol;
    private boolean activo = true;

    public Usuario(int i, String nombre, String s, String correoInstitucional, Rol rol) {
    }

    public boolean esValido() {
        return UsuarioValidator.isValid(this);
    }
}