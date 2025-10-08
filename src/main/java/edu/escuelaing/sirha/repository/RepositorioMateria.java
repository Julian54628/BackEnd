package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Materia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioMateria extends MongoRepository<Materia, String> {

    Optional<Materia> findByCodigo(String codigo);

    Optional<Materia> findByNombre(String nombre);

    List<Materia> findByNombreContainingIgnoreCase(String nombre);

    List<Materia> findByFacultad(String facultad);

    List<Materia> findByEsObligatoria(boolean esObligatoria);

    List<Materia> findByCreditos(int creditos);

    List<Materia> findByCreditosGreaterThanEqual(int creditosMinimos);

    List<Materia> findByFacultadAndEsObligatoria(String facultad, boolean esObligatoria);

    List<Materia> findByPrerrequisitosIdsContaining(String prerrequisitosId);

    @Query("{ 'prerrequisitosIds': { $size: 0 } }")
    List<Materia> findMateriasSinPrerrequisitos();

    boolean existsByCodigo(String codigo);

    long countByFacultad(String facultad);

    List<Materia> findAllById(List<String> ids);
}