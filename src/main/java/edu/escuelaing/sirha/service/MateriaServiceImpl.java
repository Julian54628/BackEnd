package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MateriaServiceImpl implements MateriaService {

    private final RepositorioMateria repositorioMateria;
    private final RepositorioGrupo repositorioGrupo;
    private final RepositorioEstudiante repositorioEstudiante;

    @Autowired
    public MateriaServiceImpl(RepositorioMateria repositorioMateria,
                              RepositorioGrupo repositorioGrupo,
                              RepositorioEstudiante repositorioEstudiante) {
        this.repositorioMateria = repositorioMateria;
        this.repositorioGrupo = repositorioGrupo;
        this.repositorioEstudiante = repositorioEstudiante;
    }

    @Override
    public Materia crear(Materia materia) {
        if (repositorioMateria.existsByCodigo(materia.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una materia con el código: " + materia.getCodigo());
        }
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
        if (!repositorioMateria.existsById(id)) {
            throw new IllegalArgumentException("Materia no encontrada: " + id);
        }
        materia.setId(id);
        return repositorioMateria.save(materia);
    }

    @Override
    public List<Grupo> consultarGruposDisponibles(String materiaId) {
        return repositorioGrupo.findByMateriaId(materiaId).stream()
                .filter(grupo -> grupo.getEstudiantesInscritosIds().size() < grupo.getCupoMaximo())
                .collect(Collectors.toList());
    }

    @Override
    public boolean verificarDisponibilidad(String materiaId) {
        return !consultarGruposDisponibles(materiaId).isEmpty();
    }

    @Override
    public void modificarCuposMateria(String materiaId, int nuevoCupo) {
        if (nuevoCupo <= 0) {
            throw new IllegalArgumentException("El cupo debe ser mayor a 0");
        }

        List<Grupo> grupos = repositorioGrupo.findByMateriaId(materiaId);
        for (Grupo grupo : grupos) {
            if (grupo.getEstudiantesInscritosIds().size() > nuevoCupo) {
                throw new IllegalArgumentException("El nuevo cupo es menor al número de estudiantes inscritos en el grupo: " + grupo.getId());
            }
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
        return repositorioGrupo.findByMateriaId(materiaId).stream()
                .mapToInt(grupo -> grupo.getEstudiantesInscritosIds().size())
                .sum();
    }

    @Override
    public Grupo inscribirEstudianteEnGrupo(String grupoId, String estudianteId) {
        Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
        Optional<Estudiante> estOpt = repositorioEstudiante.findById(estudianteId);

        if (grupoOpt.isEmpty() || estOpt.isEmpty()) {
            throw new IllegalArgumentException("Grupo o estudiante no encontrado");
        }

        Grupo grupo = grupoOpt.get();
        if (grupo.getEstudiantesInscritosIds().contains(estudianteId)) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en este grupo");
        }

        if (grupo.getEstudiantesInscritosIds().size() >= grupo.getCupoMaximo()) {
            throw new IllegalArgumentException("No hay cupo disponible en este grupo");
        }

        grupo.getEstudiantesInscritosIds().add(estudianteId);
        return repositorioGrupo.save(grupo);
    }

    @Override
    public Grupo retirarEstudianteDeGrupo(String grupoId, String estudianteId) {
        return repositorioGrupo.findById(grupoId)
                .map(grupo -> {
                    grupo.getEstudiantesInscritosIds().remove(estudianteId);
                    return repositorioGrupo.save(grupo);
                })
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + grupoId));
    }

    @Override
    public boolean asignarMateriaAEstudiante(String materiaId, String estudianteId) {
        return repositorioMateria.existsById(materiaId) && repositorioEstudiante.existsById(estudianteId);
    }

    @Override
    public boolean retirarMateriaDeEstudiante(String materiaId, String estudianteId) {
        return repositorioMateria.existsById(materiaId) && repositorioEstudiante.existsById(estudianteId);
    }

    @Override
    public List<Materia> buscarPorFacultad(String facultad) {
        return repositorioMateria.findByFacultad(facultad);
    }

    @Override
    public List<Materia> buscarPorCreditos(int creditos) {
        return repositorioMateria.findByCreditos(creditos);
    }
}