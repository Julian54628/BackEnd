package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Decanatura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioDecanatura extends MongoRepository<Decanatura, String> {

    Optional<Decanatura> findByIdDecanatura(int idDecanatura);

    List<Decanatura> findByFacultad(String facultad);

    Optional<Decanatura> findByNombre(String nombre);

    List<Decanatura> findByNombreContainingIgnoreCase(String nombre);

    List<Decanatura> findByActivoTrue();

    List<Decanatura> findByFacultadAndActivoTrue(String facultad);

    Optional<Decanatura> findByUsername(String username);

    Optional<Decanatura> findByCorreoInstitucional(String correoInstitucional);

    List<Decanatura> findByEsAdministradorTrue();

    List<Decanatura> findByEsAdministradorFalse();

    boolean existsByFacultad(String facultad);

    boolean existsByUsername(String username);

    boolean existsByIdDecanatura(int idDecanatura);

    long countByFacultad(String facultad);

    long countByActivoTrue();

    default Decanatura guardarDecanatura(Decanatura decanatura) {
        return save(decanatura);
    }

    default boolean eliminarDecanaturaSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<Decanatura> obtenerDecanaturaPorId(String id) {
        return findById(id);
    }

    default boolean existeDecanaturaPorId(String id) {
        return existsById(id);
    }
}