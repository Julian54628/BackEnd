package edu.escuelaing.sirha.model;

import java.util.HashMap;
import java.util.Map;

public class SemaforoAcademico {
    private Map<Materia, EstadoSemaforo> mapaMaterias = new HashMap<>();

    public EstadoSemaforo consultarEstado(Materia m) {
        return mapaMaterias.getOrDefault(m, EstadoSemaforo.ROJO);
    }

    public float calcularAvance() {
        if (mapaMaterias.isEmpty()) return 0;
        long verdes = mapaMaterias.values().stream().filter(e -> e == EstadoSemaforo.VERDE).count();
        return (float) verdes / mapaMaterias.size() * 100;
    }
}

