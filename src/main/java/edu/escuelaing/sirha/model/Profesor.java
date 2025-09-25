package edu.escuelaing.sirha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Profesor")

public class Profesor {
    private int idProfesor;
    private String nombre;
    private String correoInstitucional;
}

