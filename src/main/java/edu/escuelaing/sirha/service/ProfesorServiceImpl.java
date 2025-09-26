package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Profesor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    private final Map<String, Profesor> profesores = new HashMap<>();
    private final Map<String, Grupo> grupos = new HashMap<>();

    @Override
    public Profesor crear(Profesor profesor) {
        profesores.put(profesor.getId(), profesor);
        return profesor;
    }

    @Override
    public Optional<Profesor> buscarPorId(String id) {
        return Optional.ofNullable(profesores.get(id));
    }

    @Override
    public Optional<Profesor> buscarPorCodigo(String codigo) {
        return profesores.values().stream().filter(p -> String.valueOf(p.getIdProfesor()).equals(codigo)).findFirst();
    }

    @Override
    public List<Profesor> listarTodos() {
        return new ArrayList<>(profesores.values());
    }

    @Override
    public void eliminarPorId(String id) {
        profesores.remove(id);
    }

    @Override
    public List<Grupo> consultarGruposAsignados(String profesorId) {
        List<Grupo> asignados = new ArrayList<>();
        for (Grupo grupo : grupos.values()) {
            if (profesorId.equals(grupo.getProfesorId())) {
                asignados.add(grupo);
            }
        }
        return asignados;
    }
}
