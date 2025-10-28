package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MateriaSemaforo {
    private String materiaId;
    private String nombre;
    private String codigo;
    private int creditos;
    private String facultad;
    private boolean esObligatoria;
    private EstadoMateria estado;
    private String color;
    private String semestre;
    private float nota;
    private String observaciones;

    public MateriaSemaforo(String materiaId, String nombre, String codigo, int creditos,
                           String facultad, boolean esObligatoria, EstadoMateria estado) {
        this.materiaId = materiaId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.facultad = facultad;
        this.esObligatoria = esObligatoria;
        this.estado = estado;
        this.color = determinarColor(estado);
    }

    private String determinarColor(EstadoMateria estado) {
        switch (estado) {
            case INSCRITA:
            case PENDIENTE:
                return "AZUL";
            case APROBADA:
                return "VERDE";
            case REPROBADA:
            case CANCELADA:
                return "ROJO";
            default:
                return "BLANCO";
        }
    }

    public void setEstado(EstadoMateria estado) {
        this.estado = estado;
        this.color = determinarColor(estado);
    }
}