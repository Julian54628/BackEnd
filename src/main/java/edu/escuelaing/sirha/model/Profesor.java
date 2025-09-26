package edu.escuelaing.sirha.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Profesor")
public class Profesor {
    @Id
    private String id;
    private int idProfesor;
    private String nombre;
    private String correoInstitucional;
    private List<String> materiasAsignadasIds = new ArrayList<>(); // Solo IDs
    private List<String> gruposAsignadosIds = new ArrayList<>(); // Solo IDs

    public Profesor() {}

    public Profesor(int idProfesor, String nombre, String correoInstitucional) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.correoInstitucional = correoInstitucional;
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                correoInstitucional != null && correoInstitucional.contains("@") &&
                idProfesor > 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreoInstitucional() { return correoInstitucional; }
    public void setCorreoInstitucional(String correoInstitucional) { this.correoInstitucional = correoInstitucional; }
    public List<String> getMateriasAsignadasIds() { return materiasAsignadasIds; }
    public void setMateriasAsignadasIds(List<String> materiasAsignadasIds) { this.materiasAsignadasIds = materiasAsignadasIds; }
    public List<String> getGruposAsignadosIds() { return gruposAsignadosIds; }
    public void setGruposAsignadosIds(List<String> gruposAsignadosIds) { this.gruposAsignadosIds = gruposAsignadosIds; }
}