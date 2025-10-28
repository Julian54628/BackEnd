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

    List<Profesor> findByDepartamento(String departamento);

    List<Profesor> findByActivoTrue();

    List<Profesor> findByNombre(String nombre);

    List<Profesor> findByNombreContainingIgnoreCase(String nombre);

    Optional<Profesor> findByCorreoInstitucional(String correoInstitucional);

    List<Profesor> findByMateriasAsignadasIdsContaining(String materiaId);

    List<Profesor> findByGruposAsignadosIdsContaining(String grupoId);

    @Query("asignacion de grupos a profesores")
    List<Profesor> findProfesoresConGruposAsignados();

    @Query("profesores sin asignacion de grupos")
    List<Profesor> findProfesoresSinGruposAsignados();

    boolean existsByCorreoInstitucional(String correoInstitucional);

    boolean existsByIdProfesor(int idProfesor);

    long countByGruposAsignadosIdsIsNotEmpty();

    default Profesor guardarProfesor(Profesor profesor) {
        return save(profesor);
    }

    default boolean eliminarProfesorSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<Profesor> obtenerProfesorPorId(String id) {
        return findById(id);
    }

    default boolean existeProfesorPorId(String id) {
        return existsById(id);
    }
}