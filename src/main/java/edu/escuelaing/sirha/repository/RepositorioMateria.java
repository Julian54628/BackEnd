package edu.escuelaing.sirha.repository;

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

    default Materia guardarMateria(Materia materia) {
        return save(materia);
    }

    default boolean eliminarMateriaSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<Materia> obtenerMateriaPorId(String id) {
        return findById(id);
    }

    default boolean existeMateriaPorId(String id) {
        return existsById(id);
    }
}