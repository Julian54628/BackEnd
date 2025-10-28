package edu.escuelaing.sirha.repository;

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

    @Query("{ 'diaSemana': ?0, 'horaInicio': { $lt: ?2 }, 'horaFin': { $gt: ?1 } }")
    List<Horario> findHorariosQueSeCruzan(String diaSemana, Time horaInicio, Time horaFin);

    @Query("{ 'salon': ?0, 'diaSemana': ?1, 'horaInicio': { $lt: ?3 }, 'horaFin': { $gt: ?2 } }")
    List<Horario> findHorariosConflictivosEnSalon(String salon, String diaSemana, Time horaInicio, Time horaFin);

    long countByDiaSemana(String diaSemana);
    long countBySalon(String salon);

    @Query("{ 'diaSemana': ?0, 'salon': ?1, 'horaInicio': { $lt: ?3 }, 'horaFin': { $gt: ?2 } }")
    boolean existsHorarioConflictivo(String diaSemana, String salon, Time horaInicio, Time horaFin);

    boolean existsByIdHorario(int idHorario);

    default Horario guardarHorario(Horario horario) {
        return save(horario);
    }

    default boolean eliminarHorarioSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<Horario> obtenerHorarioPorId(String id) {
        return findById(id);
    }

    default boolean existeHorarioPorId(String id) {
        return existsById(id);
    }
}