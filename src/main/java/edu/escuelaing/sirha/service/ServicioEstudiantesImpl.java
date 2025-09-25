package edu.escuelaing.sirha.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioEstudiantesImpl implements ServicioEstudiantes {

    private final RepositorioEstudiantes repo;

    public ServicioEstudiantesImpl(RepositorioEstudiantes repo) {
        this.repo = repo;
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        return repo.guardar(estudiante);
    }

    @Override
    public Optional<Estudiante> buscarPorCodigo(String codigo) {
        return repo.buscarPorCodigo(codigo);
    }

    @Override
    public Optional<Estudiante> buscarPorId(String id) {
        return repo.buscarPorId(id);
    }

    @Override
    public List<Estudiante> listarTodos() {
        return repo.listarTodos();
    }

    @Override
    public void eliminarPorId(String id) {
        repo.eliminarPorId(id);
    }
}
