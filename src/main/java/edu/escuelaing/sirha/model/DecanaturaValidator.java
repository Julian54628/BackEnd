package edu.escuelaing.sirha.model;

public class DecanaturaValidator {
    public static boolean isValid(Decanatura decanatura) {
        return decanatura.getNombre() != null && !decanatura.getNombre().trim().isEmpty() &&
                decanatura.getFacultad() != null && !decanatura.getFacultad().trim().isEmpty() &&
                decanatura.getIdDecanatura() > 0;
    }

    public static String getErrorMessage() {
        return "Decanatura inválida: nombre, facultad e ID son requeridos";
    }
}