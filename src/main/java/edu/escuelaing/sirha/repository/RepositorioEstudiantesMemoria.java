package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Estudiante;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RepositorioEstudiantesMemoria implements RepositorioEstudiantes {

    private final Map<String, Estudiante> almacenamiento = new ConcurrentHashMap<>();

    @Override
    public Estudiante guardar(Estudiante estudiante) {
        if (estudiante.getId() == null || estudiante.getId().isBlank()) {
            estudiante.setId(UUID.randomUUID().toString());
        }
        almacenamiento.put(estudiante.getId(), estudiante);
        return estudiante;
    }

    @Override
    public Optional<Estudiante> buscarPorCodigo(String codigo) {
        return almacenamiento.values().stream()
                .filter(e -> e.getCodigo() != null && e.getCodigo().equals(codigo))
                .findFirst();
    }

    @Override
    public Optional<Estudiante> buscarPorId(String id) {
        return Optional.ofNullable(almacenamiento.get(id));
    }

    @Override
    public List<Estudiante> listarTodos() {
        return new ArrayList<>(almacenamiento.values());
    }

    @Override
    public void eliminarPorId(String id) {
        almacenamiento.remove(id);
    }
}
