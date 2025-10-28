package edu.escuelaing.sirha.model;

public class UsuarioFactory {

    private UsuarioFactory() {}

    public static Usuario crearAdministrador(int idUsuario, String username, String passwordHash, String correo) {
        return new Administrador(idUsuario, username, passwordHash, correo);
    }

    public static Estudiante crearEstudiante(int idUsuario, String username, String passwordHash, String correo,
                                             int idEstudiante, String nombre, String codigo, String carrera, int semestre) {
        return new Estudiante(idUsuario, username, passwordHash, correo, idEstudiante, nombre, codigo, carrera, semestre);
    }

    public static Decanatura crearDecanatura(int idUsuario, String username, String passwordHash, String correo,
                                             int idDecanatura, String nombre, String facultad) {
        return new Decanatura(idUsuario, username, passwordHash, correo, idDecanatura, nombre, facultad);
    }

    public static Decanatura crearDecanaturaConPermisos(int idUsuario, String username, String passwordHash, String correo,
                                                        int idDecanatura, String nombre, String facultad, boolean esAdministrador) {
        return new Decanatura(idUsuario, username, passwordHash, correo, idDecanatura, nombre, facultad, esAdministrador);
    }

    public static Profesor crearProfesor(int idProfesor, String nombre, String correoInstitucional) {
        return new Profesor(idProfesor, nombre, correoInstitucional);
    }
}