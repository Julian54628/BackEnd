package edu.escuelaing.sirha.model;

import java.util.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Materia")
public class Materia {
    @Id
    private String id;
    private int idMateria;
    private String nombre;
    private String codigo;
    private int creditos;
    private String facultad;
    private boolean esObligatoria;
    private List<String> prerrequisitosIds = new ArrayList<>();
    private List<String> gruposIds = new ArrayList<>();

    public Materia() {}

    public Materia(int idMateria, String nombre, String codigo, int creditos, String facultad, boolean esObligatoria) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.facultad = facultad;
        this.esObligatoria = esObligatoria;
    }

    public boolean esValida() {
        return codigo != null && !codigo.trim().isEmpty() &&
                nombre != null && !nombre.trim().isEmpty() &&
                creditos > 0 && creditos <= 6 &&
                facultad != null && !facultad.trim().isEmpty();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public int getIdMateria() { return idMateria; }
    public void setIdMateria(int idMateria) { this.idMateria = idMateria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public int getCreditos() { return creditos; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
    public boolean isEsObligatoria() { return esObligatoria; }
    public void setEsObligatoria(boolean esObligatoria) { this.esObligatoria = esObligatoria; }
    public List<String> getPrerrequisitosIds() { return prerrequisitosIds; }
    public void setPrerrequisitosIds(List<String> prerrequisitosIds) { this.prerrequisitosIds = prerrequisitosIds; }
    public List<String> getGruposIds() { return gruposIds; }
    public void setGruposIds(List<String> gruposIds) { this.gruposIds = gruposIds; }
}