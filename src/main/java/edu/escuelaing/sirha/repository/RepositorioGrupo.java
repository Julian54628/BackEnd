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

    @Query("{ $expr: { $gt: [{ $size: '$estudiantesInscritosIds' }, ?0] } }")
    List<Grupo> findByCantidadEstudiantesGreaterThan(int cantidad);

    @Query("{ $expr: { $lt: [{ $size: '$estudiantesInscritosIds' }, ?0] } }")
    List<Grupo> findByCantidadEstudiantesLessThan(int cantidad);

    @Query("{ 'estudiantesInscritosIds': { $size: 0 } }")
    List<Grupo> findGruposVacios();

    List<Grupo> findByEstudiantesInscritosIdsContaining(String estudianteId);

    List<Grupo> findByHorarioIdsContaining(String horarioId);

    boolean existsByIdGrupo(int idGrupo);

    List<Grupo> findByOrderByCupoMaximoDesc();
}