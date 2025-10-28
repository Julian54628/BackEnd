package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioProfesor;
import edu.escuelaing.sirha.repository.RepositorioGrupo;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfesorServiceImpl implements ProfesorService {

    private final RepositorioProfesor repositorioProfesor;
    private final RepositorioGrupo repositorioGrupo;

    @Autowired
    public ProfesorServiceImpl(RepositorioProfesor repositorioProfesor,
                               RepositorioGrupo repositorioGrupo) {
        this.repositorioProfesor = repositorioProfesor;
        this.repositorioGrupo = repositorioGrupo;
    }

    @Override
    public Profesor crear(Profesor profesor) {
        if (repositorioProfesor.existsByIdProfesor(profesor.getIdProfesor())) {
            throw new IllegalArgumentException("Ya existe un profesor con el ID: " + profesor.getIdProfesor());
        }
        return repositorioProfesor.save(profesor);
    }

    @Override
    public Optional<Profesor> buscarPorId(String id) {
        return repositorioProfesor.findById(id);
    }

    @Override
    public Optional<Profesor> buscarPorCodigo(String codigo) {
        return repositorioProfesor.findByIdProfesor(Integer.parseInt(codigo));
    }

    @Override
    public List<Profesor> listarTodos() {
        return repositorioProfesor.findAll();
    }

    @Override
    public Profesor actualizar(String id, Profesor profesor) {
        if (!repositorioProfesor.existsById(id)) {
            throw new IllegalArgumentException("Profesor no encontrado: " + id);
        }
        profesor.setId(id);
        return repositorioProfesor.save(profesor);
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioProfesor.deleteById(id);
    }

    @Override
    public List<Grupo> consultarGruposAsignados(String profesorId) {
        return repositorioGrupo.findByProfesorId(profesorId);
    }

    @Override
    public Grupo asignarProfesorAGrupo(String profesorId, String grupoId) {
        Optional<Profesor> profesorOpt = repositorioProfesor.findById(profesorId);
        Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);

        if (profesorOpt.isEmpty() || grupoOpt.isEmpty()) {
            throw new IllegalArgumentException("Profesor o grupo no encontrado");
        }

        Profesor profesor = profesorOpt.get();
        Grupo grupo = grupoOpt.get();

        grupo.setProfesorId(profesorId);
        profesor.getGruposAsignadosIds().add(grupoId);

        repositorioGrupo.save(grupo);
        repositorioProfesor.save(profesor);

        return grupo;
    }

    @Override
    public Grupo retirarProfesorDeGrupo(String grupoId) {
        return repositorioGrupo.findById(grupoId)
                .map(grupo -> {
                    String profesorId = grupo.getProfesorId();
                    if (profesorId != null) {
                        repositorioProfesor.findById(profesorId)
                                .ifPresent(profesor -> {
                                    profesor.getGruposAsignadosIds().remove(grupoId);
                                    repositorioProfesor.save(profesor);
                                });
                    }
                    grupo.setProfesorId(null);
                    return repositorioGrupo.save(grupo);
                })
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + grupoId));
    }

    @Override
    public List<Profesor> buscarPorDepartamento(String departamento) {
        return repositorioProfesor.findByDepartamento(departamento);
    }

    @Override
    public List<Profesor> buscarActivos() {
        return repositorioProfesor.findByActivoTrue();
    }
}