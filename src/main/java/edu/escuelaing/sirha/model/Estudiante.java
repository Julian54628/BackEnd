package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Estudiante")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Estudiante extends Usuario {
    private int idEstudiante;
    private String nombre;
    private String codigo;
    private String carrera;
    private int semestre;
    private String planAcademicoId;
    private String semaforoAcademicoId;
    private List<String> solicitudesIds = new ArrayList<>();
    private List<String> horariosIds = new ArrayList<>();

    public Estudiante(int idUsuario, String username, String passwordHash, String correoInstitucional,
                      int idEstudiante, String nombre, String codigo, String carrera, int semestre) {
        super(idUsuario, username, passwordHash, correoInstitucional, Rol.ESTUDIANTE);
        this.idEstudiante = idEstudiante;
        this.nombre = nombre;
        this.codigo = codigo;
        this.carrera = carrera;
        this.semestre = semestre;
    }

    public boolean esValido() {
        return super.esValido() && EstudianteValidator.isValid(this);
    }
}