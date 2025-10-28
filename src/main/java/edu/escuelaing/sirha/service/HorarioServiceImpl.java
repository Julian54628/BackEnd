package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.RepositorioHorario;
import edu.escuelaing.sirha.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HorarioServiceImpl implements HorarioService {

    private final RepositorioHorario repositorioHorario;

    @Autowired
    public HorarioServiceImpl(RepositorioHorario repositorioHorario) {
        this.repositorioHorario = repositorioHorario;
    }

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
        if (!repositorioHorario.existsById(id)) {
            throw new IllegalArgumentException("Horario no encontrado: " + id);
        }
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

    @Override
    public List<Horario> consultarHorariosPorDia(String diaSemana) {
        return repositorioHorario.findByDiaSemanaIgnoreCase(diaSemana);
    }

    @Override
    public List<Horario> consultarHorariosPorSalon(String salon) {
        return repositorioHorario.findBySalon(salon);
    }
}