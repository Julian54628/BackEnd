package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.Horario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioHorario extends MongoRepository<Horario, String> {

    Optional<Horario> findByIdHorario(int idHorario);

    List<Horario> findByDiaSemana(String diaSemana);

    List<Horario> findByDiaSemanaIgnoreCase(String diaSemana);

    List<Horario> findByMateriaId(String materiaId);

    List<Horario> findByGrupoId(String grupoId);

    List<Horario> findBySalon(String salon);

    List<Horario> findByHoraInicio(Time horaInicio);

    List<Horario> findByHoraInicioBetween(Time horaInicio, Time horaFin);

    @Query("{ 'diaSemana': ?0, '$nor': [ { 'horaFin': { $lte: ?1 } }, { 'horaInicio': { $gte: ?2 } } ] }")
    List<Horario> findHorariosQueSeCruzan(String diaSemana, Time horaInicio, Time horaFin);

    @Query("{ 'salon': ?0, 'diaSemana': ?1, '$nor': [ { 'horaFin': { $lte: ?2 } }, { 'horaInicio': { $gte: ?3 } } ] }")
    List<Horario> findHorariosConflictivosEnSalon(String salon, String diaSemana, Time horaInicio, Time horaFin);

    long countByDiaSemana(String diaSemana);

    long countBySalon(String salon);

    @Query("{ 'diaSemana': ?0, 'salon': ?1, '$nor': [ { 'horaFin': { $lte: ?2 } }, { 'horaInicio': { $gte: ?3 } } ] }")
    boolean existsHorarioConflictivo(String diaSemana, String salon, Time horaInicio, Time horaFin);

    boolean existsByIdHorario(int idHorario);
}