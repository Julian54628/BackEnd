package edu.escuelaing.sirha.model;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Profesor")
public class Profesor extends Usuario {
    private int idProfesor;
    private String nombre;
    private List<String> materiasAsignadasIds = new ArrayList<>();
    private List<String> gruposAsignadosIds = new ArrayList<>();

    public Profesor() {
        super();
        setRol(Rol.PROFESOR);
    }

    public Profesor(int idProfesor, String nombre, String correoInstitucional) {
        super(0, nombre, "", correoInstitucional, Rol.PROFESOR);
        this.idProfesor = idProfesor;
        this.nombre = nombre;
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                getCorreoInstitucional() != null && getCorreoInstitucional().contains("@") &&
                idProfesor > 0;
    }

    public int getIdProfesor() { return idProfesor; }
    public void setIdProfesor(int idProfesor) { this.idProfesor = idProfesor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<String> getMateriasAsignadasIds() { return materiasAsignadasIds; }
    public void setMateriasAsignadasIds(List<String> materiasAsignadasIds) { this.materiasAsignadasIds = materiasAsignadasIds; }
    public List<String> getGruposAsignadosIds() { return gruposAsignadosIds; }
    public void setGruposAsignadosIds(List<String> gruposAsignadosIds) { this.gruposAsignadosIds = gruposAsignadosIds; }

    public void setFacultad(String ingenieria) {
    }
}