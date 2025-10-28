package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Profesor")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Profesor extends Usuario {
    private int idProfesor;
    private String nombre;
    private List<String> materiasAsignadasIds = new ArrayList<>();
    private List<String> gruposAsignadosIds = new ArrayList<>();

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

    public void setFacultad(String ingenieria) {
        // Método mantenido por compatibilidad
    }
}