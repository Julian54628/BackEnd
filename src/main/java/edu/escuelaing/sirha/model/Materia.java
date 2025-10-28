package edu.escuelaing.sirha.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Materia")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public Materia(int idMateria, String nombre, String codigo, int creditos, String facultad, boolean esObligatoria) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.facultad = facultad;
        this.esObligatoria = esObligatoria;
    }

    public boolean esValida() {
        return MateriaValidator.isValid(this);
    }
}