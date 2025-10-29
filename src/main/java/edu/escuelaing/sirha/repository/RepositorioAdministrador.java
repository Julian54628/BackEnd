package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Administrador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositorioAdministrador extends MongoRepository<Administrador, String> {
}