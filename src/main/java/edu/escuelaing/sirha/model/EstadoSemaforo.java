package edu.escuelaing.sirha.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "EstadoSemaforo")

public enum EstadoSemaforo {
    VERDE,
    AZUL,
    ROJO
}

