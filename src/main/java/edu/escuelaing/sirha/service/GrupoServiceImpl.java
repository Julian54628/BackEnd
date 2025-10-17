package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.model.Grupo;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GrupoServiceImpl implements GrupoService {

    private final Map<String, Grupo> grupos = new HashMap<>();
    private final Map<String, List<Estudiante>> estudiantesPorGrupo = new HashMap<>();

    @Override
    public Grupo crear(Grupo grupo) {
        grupos.put(grupo.getId(), grupo);
        estudiantesPorGrupo.putIfAbsent(grupo.getId(), new ArrayList<>());
        return grupo;
    }

    @Override
    public Optional<Grupo> buscarPorId(String id) {
        return Optional.ofNullable(grupos.get(id));
    }

    @Override
    public List<Grupo> listarTodos() {
        return new ArrayList<>(grupos.values());
    }

    @Override
    public void eliminarPorId(String id) {
        grupos.remove(id);
        estudiantesPorGrupo.remove(id);
    }

    @Override
    public Grupo actualizarCupo(String grupoId, int nuevoCupo) {
        Grupo grupo = grupos.get(grupoId);
        if (grupo != null) {
            grupo.setCupoMaximo(nuevoCupo);
        }
        return grupo;
    }

    @Override
    public boolean verificarCupoDisponible(String grupoId) {
        Grupo grupo = grupos.get(grupoId);
        if (grupo != null) {
            int inscritos = grupo.getEstudiantesInscritosIds().size();
            return inscritos < grupo.getCupoMaximo();
        }
        return false;
    }

    @Override
    public float consultarCargaAcademica(String grupoId) {
        Grupo grupo = grupos.get(grupoId);
        if (grupo != null) {
            int estudiantesInscritos = grupo.getEstudiantesInscritosIds().size();
            int cupoMaximo = grupo.getCupoMaximo();
            if (cupoMaximo > 0) {
                return (float) estudiantesInscritos / cupoMaximo * 100;
            }
        }
        return 0;
    }

    @Override
    public List<Estudiante> consultarEstudiantesInscritos(String grupoId) {
        return new ArrayList<>(estudiantesPorGrupo.getOrDefault(grupoId, new ArrayList<>()));
    }

    @Override
    public List<Grupo> obtenerGruposConAlertaCapacidad(double porcentajeAlerta) {
        List<Grupo> todosLosGrupos = listarTodos();
        List<Grupo> gruposEnAlerta = new ArrayList<>();
        
        for (Grupo grupo : todosLosGrupos) {
            if (grupo.getCupoMaximo() > 0) {
                double porcentajeOcupacion = ((double) grupo.getEstudiantesInscritosIds().size() / grupo.getCupoMaximo()) * 100;
                if (porcentajeOcupacion >= porcentajeAlerta) {
                    gruposEnAlerta.add(grupo);
                }
            }
        }
        
        return gruposEnAlerta;
    }
}
