package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Usuario;
import edu.escuelaing.sirha.model.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioUsuario extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByRolAndActivoTrue(Rol rol);

    boolean existsByUsername(String username);

    boolean existsByCorreoInstitucional(String correoInstitucional);

    List<Usuario> findByUsernameContainingIgnoreCase(String username);

    List<Usuario> findByActivoTrue();

    long countByRolAndActivoTrue(Rol rol);

    long countByRol(Rol rol);

    default Usuario guardarUsuario(Usuario usuario) {
        return save(usuario);
    }

    default boolean eliminarUsuarioSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<Usuario> obtenerUsuarioPorId(String id) {
        return findById(id);
    }

    default boolean existeUsuarioPorId(String id) {
        return existsById(id);
    }
}
