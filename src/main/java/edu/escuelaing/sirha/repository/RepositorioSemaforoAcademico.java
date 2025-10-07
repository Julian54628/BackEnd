package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.SemaforoAcademico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioSemaforoAcademico extends MongoRepository<SemaforoAcademico, String> {

    Optional<SemaforoAcademico> findByEstudianteId(String estudianteId);

    List<SemaforoAcademico> findByPlanAcademicoId(String planAcademicoId);

    List<SemaforoAcademico> findByGrado(String grado);

    List<SemaforoAcademico> findByCambioDePlanTrue();

    List<SemaforoAcademico> findByCreditosAprobadosGreaterThanEqual(int creditosMinimos);

    List<SemaforoAcademico> findByPromedioAcumuladoGreaterThanEqual(float promedioMinimo);

    List<SemaforoAcademico> findByMateriasVistasGreaterThanEqual(int materiasMinimas);

    @Query("{ $expr: { $gte: [{ $multiply: [{ $divide: ['$creditosAprobados', '$totalCreditosPlan'] }, 100] }, ?0] } }")
    List<SemaforoAcademico> findByProgresoMayorIgual(float porcentajeMinimo);

    @Query("{ 'promedioAcumulado': { $lt: ?0 }, 'materiasVistas': { $gte: ?1 } }")
    List<SemaforoAcademico> findEstudiantesEnRiesgo(float promedioMaximo, int materiasMinimas);

    long countByGrado(String grado);
    long countByCambioDePlanTrue();
    boolean existsByEstudianteId(String estudianteId);
}