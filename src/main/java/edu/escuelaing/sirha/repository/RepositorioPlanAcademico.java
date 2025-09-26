package edu.escuelaing.sirha.repository;

import edu.escuelaing.sirha.model.PlanAcademico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioPlanAcademico extends MongoRepository<PlanAcademico, String> {

    Optional<PlanAcademico> findByIdPlan(int idPlan);

    Optional<PlanAcademico> findByNombre(String nombre);

    List<PlanAcademico> findByGrado(String grado);

    List<PlanAcademico> findByNombreContainingIgnoreCase(String nombre);

    List<PlanAcademico> findByCreditosTotales(int creditosTotales);

    List<PlanAcademico> findByCreditosTotalesGreaterThanEqual(int creditosMinimos);

    List<PlanAcademico> findByMateriasObligatoriasIdsContaining(String materiaId);

    List<PlanAcademico> findByMateriasElectivasIdsContaining(String materiaId);

    @Query("{ $expr: { $lt: [{ $size: '$materiasObligatoriasIds' }, ?0] } }")
    List<PlanAcademico> findPlanesConMenosMateriasObligatorias(int maxMaterias);

    boolean existsByNombre(String nombre);

    long countByGrado(String grado);

    Optional<PlanAcademico> findFirstByGradoOrderByIdPlanDesc(String grado);
}