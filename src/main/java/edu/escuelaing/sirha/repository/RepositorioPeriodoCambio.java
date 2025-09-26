package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.PeriodoCambio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioPeriodoCambio extends MongoRepository<PeriodoCambio, String> {

    Optional<PeriodoCambio> findByIdPeriodo(int idPeriodo);

    Optional<PeriodoCambio> findByNombre(String nombre);

    List<PeriodoCambio> findByActivoTrue();

    List<PeriodoCambio> findByTipo(String tipo);

    List<PeriodoCambio> findByTipoAndActivoTrue(String tipo);

    @Query("{ 'fechaInicio': { $lte: ?0 }, 'fechaFin': { $gte: ?0 } }")
    List<PeriodoCambio> findPeriodosVigentesEnFecha(Date fecha);

    @Query("{ 'activo': true, 'fechaInicio': { $lte: ?0 }, 'fechaFin': { $gte: ?0 } }")
    Optional<PeriodoCambio> findPeriodoActivoEnFecha(Date fecha);

    @Query("{ 'fechaInicio': { $gt: ?0 } }")
    List<PeriodoCambio> findPeriodosFuturos(Date fechaReferencia);

    @Query("{ 'fechaFin': { $lt: ?0 } }")
    List<PeriodoCambio> findPeriodosPasados(Date fechaReferencia);

    Optional<PeriodoCambio> findFirstByOrderByFechaInicioDesc();

    boolean existsByActivoTrue();

    long countByActivoTrue();

    long countByTipo(String tipo);
}