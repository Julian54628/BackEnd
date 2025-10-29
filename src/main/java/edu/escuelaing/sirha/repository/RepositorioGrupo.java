package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Grupo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioGrupo extends MongoRepository<Grupo, String> {

    Optional<Grupo> findByIdGrupo(int idGrupo);

    List<Grupo> findByMateriaId(String materiaId);

    List<Grupo> findByProfesorId(String profesorId);

    List<Grupo> findByCupoMaximo(int cupoMaximo);

    List<Grupo> findByCupoMaximoGreaterThanEqual(int cupoMinimo);

    @Query("{ 'estudiantesInscritosIds': { $size: ?0 } }")
    List<Grupo> findByCantidadEstudiantes(int cantidad);

    @Query("{ $expr: { $gte: [ { $size: { $ifNull: ['$estudiantesInscritosIds', []] } }, ?0 ] } }")
    List<Grupo> findByEstudiantesInscritosCountGreaterThanEqual(int minSize);

    @Query("{ 'estudiantesInscritosIds': { $size: 0 } }")
    List<Grupo> findGruposVacios();

    List<Grupo> findByEstudiantesInscritosIdsContaining(String estudianteId);

    List<Grupo> findByHorarioIdsContaining(String horarioId);

    List<Grupo> findByPeriodoId(String periodoId);

    List<Grupo> findByMateriaIdAndProfesorId(String materiaId, String profesorId);

    boolean existsByIdGrupo(int idGrupo);

    List<Grupo> findByOrderByCupoMaximoDesc();

    List<Grupo> findByOrderByTotalSolicitudesDesc();

    default Grupo guardarGrupo(Grupo grupo) {
        return save(grupo);
    }

    default boolean eliminarGrupoSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<Grupo> obtenerGrupoPorId(String id) {
        return findById(id);
    }

    default boolean existeGrupoPorId(String id) {
        return existsById(id);
    }
}