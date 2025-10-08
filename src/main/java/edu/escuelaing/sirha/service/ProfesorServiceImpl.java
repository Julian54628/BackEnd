package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Profesor;
import edu.escuelaing.sirha.repository.RepositorioGrupo;
import edu.escuelaing.sirha.repository.RepositorioProfesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private RepositorioProfesor repositorioProfesor;

    @Autowired
    private RepositorioGrupo repositorioGrupo;

    @Override
    public Profesor crear(Profesor profesor) {
        return repositorioProfesor.save(profesor);
    }

    @Override
    public Optional<Profesor> buscarPorId(String id) {
        return repositorioProfesor.findById(id);
    }

    @Override
    public Optional<Profesor> buscarPorCodigo(String codigo) {
        return repositorioProfesor.findAll().stream()
                .filter(p -> String.valueOf(p.getIdProfesor()).equals(codigo))
                .findFirst();
    }

    @Override
    public List<Profesor> listarTodos() {
        return repositorioProfesor.findAll();
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
        if (profesorOpt.isPresent() && grupoOpt.isPresent()) {
            Profesor profesor = profesorOpt.get();
            Grupo grupo = grupoOpt.get();
            grupo.setProfesorId(profesorId);
            profesor.getGruposAsignadosIds().add(grupoId);
            repositorioGrupo.save(grupo);
            repositorioProfesor.save(profesor);
            return grupo;
        }
        return null;
    }

    @Override
    public Grupo retirarProfesorDeGrupo(String grupoId) {
        Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
        if (grupoOpt.isPresent()) {
            Grupo grupo = grupoOpt.get();
            String profesorId = grupo.getProfesorId();
            if (profesorId != null) {
                Optional<Profesor> profesorOpt = repositorioProfesor.findById(profesorId);
                if (profesorOpt.isPresent()) {
                    Profesor profesor = profesorOpt.get();
                    profesor.getGruposAsignadosIds().remove(grupoId);
                    repositorioProfesor.save(profesor);
                }
            }
            grupo.setProfesorId(null);
            return repositorioGrupo.save(grupo);
        }
        return null;
    }
}