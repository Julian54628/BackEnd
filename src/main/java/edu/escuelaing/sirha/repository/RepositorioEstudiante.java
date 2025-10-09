package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Estudiante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioEstudiante extends MongoRepository<Estudiante, String> {

    Optional<Estudiante> findByCodigo(String codigo);

    Optional<Estudiante> findByCodigoIgnoreCase(String codigo);

    List<Estudiante> findByCarrera(String carrera);

    List<Estudiante> findBySemestre(int semestre);

    List<Estudiante> findByCarreraAndSemestre(String carrera, int semestre);

    List<Estudiante> findByActivoTrue();

    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);

    List<Estudiante> findByPlanAcademicoId(String planAcademicoId);

    boolean existsByCodigo(String codigo);

    long countByCarrera(String carrera);

    long countBySemestreAndActivoTrue(int semestre);
}