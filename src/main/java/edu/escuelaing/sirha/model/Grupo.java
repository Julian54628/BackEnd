package edu.escuelaing.sirha.model;

import java.util.ArrayList;
import java.util.List;

public class Grupo {
    private int idGrupo;
    private int cupoMaximo;
    private List<Estudiante> estudiantesInscritos = new ArrayList<>();

    public boolean verificarCupoDisponible() {
        return estudiantesInscritos.size() < cupoMaximo;
    }

    public void agregarEstudiante(Estudiante e) {
        if (verificarCupoDisponible()) {
            estudiantesInscritos.add(e);
        }
    }

    public float consultarCarga() {
        if (cupoMaximo == 0) return 0;
        return (float) estudiantesInscritos.size() / cupoMaximo * 100;
    }

    public void setCupoMaximo(int nuevoCupo) {
        this.cupoMaximo = nuevoCupo;
    }
}

