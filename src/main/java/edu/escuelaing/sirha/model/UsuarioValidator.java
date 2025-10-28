package edu.escuelaing.sirha.model;

public class UsuarioValidator {
    public static boolean isValid(Usuario usuario) {
        return usuario.getUsername() != null && !usuario.getUsername().trim().isEmpty() &&
                usuario.getPasswordHash() != null && !usuario.getPasswordHash().trim().isEmpty() &&
                usuario.getCorreoInstitucional() != null &&
                usuario.getCorreoInstitucional().contains("@") &&
                usuario.getRol() != null;
    }

    public static String getErrorMessage() {
        return "Usuario inválido: username, password, correo y rol son requeridos";
    }
}