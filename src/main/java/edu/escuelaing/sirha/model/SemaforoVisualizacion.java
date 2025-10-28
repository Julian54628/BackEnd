package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class SemaforoVisualizacion {
    private String estudianteId;
    private String nombreEstudiante;
    private String codigoEstudiante;
    private String carrera;
    private int semestreActual;
    private String grado;

    private int creditosActuales;
    private int creditosCompletados;
    private int creditosFaltantes;
    private int creditosMaximosSemestre;
    private int totalCreditosPlan;

    private int materiasAprobadas;
    private int materiasReprobadas;
    private int materiasCursando;
    private int materiasPendientes;
    private int totalMateriasPlan;

    private float promedioAcumulado;
    private float porcentajeProgreso;

    private List<MateriaSemaforo> materiasAzules;
    private List<MateriaSemaforo> materiasVerdes;
    private List<MateriaSemaforo> materiasRojas;
    private List<MateriaSemaforo> materiasBlancas;

    private String estadoAcademico;
    private int semestresRestantes;
    private boolean puedeGraduarse;
}