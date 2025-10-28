package edu.escuelaing.sirha.model;

public class MateriaValidator {
    public static boolean isValid(Materia materia) {
        return materia.getCodigo() != null && !materia.getCodigo().trim().isEmpty() &&
                materia.getNombre() != null && !materia.getNombre().trim().isEmpty() &&
                materia.getCreditos() > 0 && materia.getCreditos() <= 6 &&
                materia.getFacultad() != null && !materia.getFacultad().trim().isEmpty() &&
                materia.getIdMateria() > 0;
    }

    public static String getErrorMessage() {
        return "Materia inválida: código, nombre, créditos (1-6) y facultad son requeridos";
    }
}