package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioHorario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private RepositorioHorario repositorioHorario;

    @Override
    public Horario crear(Horario horario) {
        return repositorioHorario.save(horario);
    }

    @Override
    public Optional<Horario> buscarPorId(String id) {
        return repositorioHorario.findById(id);
    }

    @Override
    public List<Horario> listarTodos() {
        return repositorioHorario.findAll();
    }

    @Override
    public Horario actualizar(String id, Horario horario) {
        horario.setId(id);
        return repositorioHorario.save(horario);
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioHorario.deleteById(id);
    }

    @Override
    public List<Horario> consultarHorariosPorGrupo(String grupoId) {
        return repositorioHorario.findByGrupoId(grupoId);
    }
}