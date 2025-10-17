package edu.escuelaing.sirha.model;

public class MateriaSemaforo {
    private String materiaId;
    private String nombre;
    private String codigo;
    private int creditos;
    private String facultad;
    private boolean esObligatoria;
    private EstadoMateria estado;
    private String color;
    private String semestre;
    private float nota;
    private String observaciones;
    
    public MateriaSemaforo() {}
    
    public MateriaSemaforo(String materiaId, String nombre, String codigo, int creditos, 
                          String facultad, boolean esObligatoria, EstadoMateria estado) {
        this.materiaId = materiaId;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.facultad = facultad;
        this.esObligatoria = esObligatoria;
        this.estado = estado;
        this.color = determinarColor(estado);
    }
    
    private String determinarColor(EstadoMateria estado) {
        switch (estado) {
            case INSCRITA:
            case PENDIENTE:
                return "AZUL";
            case APROBADA:
                return "VERDE";
            case REPROBADA:
            case CANCELADA:
                return "ROJO";
            default:
                return "BLANCO";
        }
    }
    
    public String getMateriaId() { return materiaId; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    
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
    
    public EstadoMateria getEstado() { return estado; }
    public void setEstado(EstadoMateria estado) { 
        this.estado = estado; 
        this.color = determinarColor(estado);
    }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }
    
    public float getNota() { return nota; }
    public void setNota(float nota) { this.nota = nota; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
