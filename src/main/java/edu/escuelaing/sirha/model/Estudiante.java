package edu.escuelaing.sirha.model;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "Estudiante")
public class Estudiante extends Usuario {
    @Id
    private String id;
    private int idEstudiante;
    private String nombre;
    private String codigo;
    private String carrera;
    private int semestre;
    private String planAcademicoId; // Solo referencia por ID
    private String semaforoAcademicoId; // Solo referencia por ID
    private List<String> solicitudesIds = new ArrayList<>(); // Solo IDs
    private List<String> horariosIds = new ArrayList<>(); // Solo IDs

    public Estudiante() {
        super();
    }

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
        return super.esValido() &&
                codigo != null && !codigo.trim().isEmpty() &&
                nombre != null && !nombre.trim().isEmpty() &&
                semestre > 0 && semestre <= 12;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public String getPlanAcademicoId() { return planAcademicoId; }
    public void setPlanAcademicoId(String planAcademicoId) { this.planAcademicoId = planAcademicoId; }
    public String getSemaforoAcademicoId() { return semaforoAcademicoId; }
    public void setSemaforoAcademicoId(String semaforoAcademicoId) { this.semaforoAcademicoId = semaforoAcademicoId; }
    public List<String> getSolicitudesIds() { return solicitudesIds; }
    public void setSolicitudesIds(List<String> solicitudesIds) { this.solicitudesIds = solicitudesIds; }
    public List<String> getHorariosIds() { return horariosIds; }
    public void setHorariosIds(List<String> horariosIds) { this.horariosIds = horariosIds; }
}