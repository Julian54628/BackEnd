package edu.escuelaing.sirha.model;

public class EstudianteValidator {
    public static boolean isValid(Estudiante estudiante) {
        return estudiante.getCodigo() != null && !estudiante.getCodigo().trim().isEmpty() &&
                estudiante.getNombre() != null && !estudiante.getNombre().trim().isEmpty() &&
                estudiante.getCarrera() != null && !estudiante.getCarrera().trim().isEmpty() &&
                estudiante.getSemestre() > 0 && estudiante.getSemestre() <= 12 &&
                estudiante.getIdEstudiante() > 0;
    }

    public static String getErrorMessage() {
        return "Estudiante inválido: código, nombre, carrera y semestre válido son requeridos";
    }
}