package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Profesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioProfesor extends MongoRepository<Profesor, String> {

    Optional<Profesor> findByIdProfesor(int idProfesor);

    List<Profesor> findByNombre(String nombre);

    List<Profesor> findByNombreContainingIgnoreCase(String nombre);

    Optional<Profesor> findByCorreoInstitucional(String correoInstitucional);

    List<Profesor> findByMateriasAsignadasIdsContaining(String materiaId);

    List<Profesor> findByGruposAsignadosIdsContaining(String grupoId);

    @Query("{ $expr: { $gt: [{ $size: '$gruposAsignadosIds' }, 0] } }")
    List<Profesor> findProfesoresConGruposAsignados();

    @Query("{ $or: [ { 'gruposAsignadosIds': { $exists: false } }, { $expr: { $eq: [{ $size: '$gruposAsignadosIds' }, 0] } } ] }")
    List<Profesor> findProfesoresSinGruposAsignados();

    boolean existsByCorreoInstitucional(String correoInstitucional);

    @Query("{ $expr: { $lt: [ { $add: [{ $size: '$materiasAsignadasIds' }, { $size: '$gruposAsignadosIds' }] }, ?0 ] } }")
    List<Profesor> findProfesoresDisponibles(int maxCarga);

    @Query(value = "{ $expr: { $gt: [{ $size: '$gruposAsignadosIds' }, 0] } }", count = true)
    long countProfesoresConGrupos();

    List<Profesor> findByActivoTrue();

    List<Profesor> findByFacultad(String ingenieria);
}