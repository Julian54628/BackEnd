package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Materia;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MateriaServiceImpl implements MateriaService {

    private final Map<String, Materia> materias = new HashMap<>();
    private final Map<String, Grupo> grupos = new HashMap<>();

    @Override
    public Materia crear(Materia materia) {
        materias.put(materia.getId(), materia);
        return materia;
    }

    @Override
    public Optional<Materia> buscarPorId(String id) {
        return Optional.ofNullable(materias.get(id));
    }

    @Override
    public Optional<Materia> buscarPorCodigo(String codigo) {
        return materias.values().stream().filter(m -> codigo.equals(m.getCodigo())).findFirst();
    }

    @Override
    public List<Materia> listarTodos() {
        return new ArrayList<>(materias.values());
    }

    @Override
    public void eliminarPorId(String id) {
        materias.remove(id);
    }

    @Override
    public Materia actualizar(String id, Materia materia) {
        materias.put(id, materia);
        return materia;
    }

    @Override
    public List<Grupo> consultarGruposDisponibles(String materiaId) {
        List<Grupo> disponibles = new ArrayList<>();
        for (Grupo grupo : grupos.values()) {
            if (materiaId.equals(grupo.getMateriaId())) {
                disponibles.add(grupo);
            }
        }
        return disponibles;
    }

    @Override
    public boolean verificarDisponibilidad(String materiaId) {
        for (Grupo grupo : grupos.values()) {
            if (materiaId.equals(grupo.getMateriaId())) {
                int estudiantesInscritos = grupo.getEstudiantesInscritosIds().size();
                int cupoMaximo = grupo.getCupoMaximo();
                if (estudiantesInscritos < cupoMaximo) {
                    return true;
                }
            }
        }
        return false;
    }
}
