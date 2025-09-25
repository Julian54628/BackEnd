package edu.escuelaing.sirha.model;

import java.util.ArrayList;
import java.util.List;

public class Materia {
    private int idMateria;
    private String nombre;
    private String codigo;
    private int creditos;
    private String facultad;
    private boolean esObligatoria;
    private List<Grupo> grupos = new ArrayList<>();

    public List<Grupo> obtenerGrupos() {
        return grupos;
    }

    public boolean consultarDisponibilidad() {
        return grupos.stream().anyMatch(Grupo::verificarCupoDisponible);
    }
}
