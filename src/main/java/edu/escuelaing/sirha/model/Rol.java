package edu.escuelaing.sirha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Rol")

public enum Rol {
    ESTUDIANTE,
    DECANATURA,
    ADMIN
}

