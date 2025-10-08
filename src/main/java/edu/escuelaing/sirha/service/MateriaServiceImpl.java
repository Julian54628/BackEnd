package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Materia;
import edu.escuelaing.sirha.repository.RepositorioGrupo;
import edu.escuelaing.sirha.repository.RepositorioMateria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Autowired
    private RepositorioMateria repositorioMateria;

    @Autowired
    private RepositorioGrupo repositorioGrupo;

    @Override
    public Materia crear(Materia materia) {
        return repositorioMateria.save(materia);
    }

    @Override
    public Optional<Materia> buscarPorId(String id) {
        return repositorioMateria.findById(id);
    }

    @Override
    public Optional<Materia> buscarPorCodigo(String codigo) {
        return repositorioMateria.findByCodigo(codigo);
    }

    @Override
    public List<Materia> listarTodos() {
        return repositorioMateria.findAll();
    }

    @Override
    public void eliminarPorId(String id) {
        repositorioMateria.deleteById(id);
    }

    @Override
    public Materia actualizar(String id, Materia materia) {
        materia.setId(id);
        return repositorioMateria.save(materia);
    }

    @Override
    public List<Grupo> consultarGruposDisponibles(String materiaId) {
        return repositorioGrupo.findByMateriaId(materiaId);
    }

    @Override
    public boolean verificarDisponibilidad(String materiaId) {
        List<Grupo> grupos = repositorioGrupo.findByMateriaId(materiaId);
        for (Grupo grupo : grupos) {
            int estudiantesInscritos = grupo.getEstudiantesInscritosIds().size();
            int cupoMaximo = grupo.getCupoMaximo();
            if (estudiantesInscritos < cupoMaximo) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void modificarCuposMateria(String materiaId, int nuevoCupo) {
        List<Grupo> grupos = repositorioGrupo.findByMateriaId(materiaId);
        for (Grupo grupo : grupos) {
            grupo.setCupoMaximo(nuevoCupo);
            repositorioGrupo.save(grupo);
        }
    }

    @Override
    public Materia registrarMateriaConGrupos(Materia materia, List<Grupo> grupos) {
        Materia materiaCreada = repositorioMateria.save(materia);
        for (Grupo grupo : grupos) {
            grupo.setMateriaId(materiaCreada.getId());
            Grupo grupoCreado = repositorioGrupo.save(grupo);
            materiaCreada.getGruposIds().add(grupoCreado.getId());
        }
        return repositorioMateria.save(materiaCreada);
    }

    @Override
    public int consultarTotalInscritosPorMateria(String materiaId) {
        List<Grupo> grupos = repositorioGrupo.findByMateriaId(materiaId);
        return grupos.stream().mapToInt(grupo -> grupo.getEstudiantesInscritosIds().size()).sum();
    }
}