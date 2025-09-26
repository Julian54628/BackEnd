package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioAdministrador extends MongoRepository<Administrador, String> {

    List<Administrador> findByActivoTrue();

    Optional<Administrador> findByUsername(String username);

    Optional<Administrador> findByCorreoInstitucional(String correoInstitucional);

    boolean existsByUsername(String username);

    boolean existsByCorreoInstitucional(String correoInstitucional);

    long countByActivoTrue();
}