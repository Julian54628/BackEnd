package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioGrupo;
import edu.escuelaing.sirha.repository.RepositorioEstudiante;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GrupoServiceImpl implements GrupoService {

    private final RepositorioGrupo repositorioGrupo;
    private final RepositorioEstudiante repositorioEstudiante;

    @Autowired
    public GrupoServiceImpl(RepositorioGrupo repositorioGrupo,
                            RepositorioEstudiante repositorioEstudiante) {
        this.repositorioGrupo = repositorioGrupo;
        this.repositorioEstudiante = repositorioEstudiante;
    }

    @Override
    public Grupo crear(Grupo grupo) {
        return repositorioGrupo.save(grupo);
    }

    @Override
    public Optional<Grupo> buscarPorId(String id) {
        return repositorioGrupo.findById(id);
    }

    @Override
    public List<Grupo> listarTodos() {
        return repositorioGrupo.findAll();
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioGrupo.deleteById(id);
    }

    @Override
    public Grupo actualizarCupo(String grupoId, int nuevoCupo) {
        if (nuevoCupo <= 0) {
            throw new IllegalArgumentException("El cupo debe ser mayor a 0");
        }

        return repositorioGrupo.findById(grupoId)
                .map(grupo -> {
                    if (grupo.getEstudiantesInscritosIds().size() > nuevoCupo) {
                        throw new IllegalArgumentException("El nuevo cupo es menor al número de estudiantes inscritos");
                    }
                    grupo.setCupoMaximo(nuevoCupo);
                    return repositorioGrupo.save(grupo);
                })
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + grupoId));
    }

    @Override
    public boolean verificarCupoDisponible(String grupoId) {
        return repositorioGrupo.findById(grupoId)
                .map(grupo -> grupo.getEstudiantesInscritosIds().size() < grupo.getCupoMaximo())
                .orElse(false);
    }

    @Override
    public float consultarCargaAcademica(String grupoId) {
        return repositorioGrupo.findById(grupoId)
                .map(grupo -> {
                    int estudiantesInscritos = grupo.getEstudiantesInscritosIds().size();
                    int cupoMaximo = grupo.getCupoMaximo();
                    return cupoMaximo > 0 ? (float) estudiantesInscritos / cupoMaximo * 100 : 0;
                })
                .orElse(0f);
    }

    @Override
    public List<Estudiante> consultarEstudiantesInscritos(String grupoId) {
        return repositorioGrupo.findById(grupoId)
                .map(grupo -> grupo.getEstudiantesInscritosIds().stream()
                        .map(estudianteId -> repositorioEstudiante.findById(estudianteId).orElse(null))
                        .filter(estudiante -> estudiante != null)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Override
    public List<Grupo> obtenerGruposConAlertaCapacidad(double porcentajeAlerta) {
        return repositorioGrupo.findAll().stream()
                .filter(grupo -> grupo.getCupoMaximo() > 0)
                .filter(grupo -> {
                    double porcentajeOcupacion = ((double) grupo.getEstudiantesInscritosIds().size() / grupo.getCupoMaximo()) * 100;
                    return porcentajeOcupacion >= porcentajeAlerta;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Grupo> buscarPorMateria(String materiaId) {
        return repositorioGrupo.findByMateriaId(materiaId);
    }

    @Override
    public List<Grupo> buscarPorProfesor(String profesorId) {
        return repositorioGrupo.findByProfesorId(profesorId);
    }
}