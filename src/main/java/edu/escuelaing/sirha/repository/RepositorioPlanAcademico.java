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

    @Query("{ '$where': 'this.materiasObligatoriasIds.length < ?0' }")
    List<PlanAcademico> findPlanesConMenosMateriasObligatorias(int maxMaterias);

    boolean existsByNombre(String nombre);

    long countByGrado(String grado);

    Optional<PlanAcademico> findFirstByGradoOrderByIdPlanDesc(String grado);

    @Query("{ '$where': 'this.materiasObligatoriasIds.length > ?0' }")
    List<PlanAcademico> findPlanesConMasMateriasObligatorias(int minMaterias);

    @Query("{ '$where': 'this.materiasElectivasIds.length < ?0' }")
    List<PlanAcademico> findPlanesConMenosMateriasElectivas(int maxMaterias);

    @Query("{ '$where': 'this.materiasElectivasIds.length > ?0' }")
    List<PlanAcademico> findPlanesConMasMateriasElectivas(int minMaterias);

    default List<PlanAcademico> findPlanesConRangoMateriasObligatorias(int min, int max) {
        return findAll().stream()
                .filter(plan -> plan.getMateriasObligatoriasIds() != null)
                .filter(plan -> {
                    int cantidad = plan.getMateriasObligatoriasIds().size();
                    return cantidad >= min && cantidad <= max;
                })
                .toList();
    }

    default List<PlanAcademico> findPlanesConRangoMateriasElectivas(int min, int max) {
        return findAll().stream()
                .filter(plan -> plan.getMateriasElectivasIds() != null)
                .filter(plan -> {
                    int cantidad = plan.getMateriasElectivasIds().size();
                    return cantidad >= min && cantidad <= max;
                })
                .toList();
    }

    default List<PlanAcademico> findPlanesConTotalMaterias(int totalMaterias) {
        return findAll().stream()
                .filter(plan -> {
                    int obligatorias = plan.getMateriasObligatoriasIds() != null ?
                            plan.getMateriasObligatoriasIds().size() : 0;
                    int electivas = plan.getMateriasElectivasIds() != null ?
                            plan.getMateriasElectivasIds().size() : 0;
                    return (obligatorias + electivas) == totalMaterias;
                })
                .toList();
    }

    default List<PlanAcademico> findPlanesConMasCreditosQue(int creditosMinimos) {
        return findAll().stream()
                .filter(plan -> plan.getCreditosTotales() > creditosMinimos)
                .toList();
    }

    default List<PlanAcademico> findPlanesConMenosCreditosQue(int creditosMaximos) {
        return findAll().stream()
                .filter(plan -> plan.getCreditosTotales() < creditosMaximos)
                .toList();
    }

    default List<PlanAcademico> findPlanesQueContenganMateria(String materiaId) {
        return findAll().stream()
                .filter(plan ->
                        (plan.getMateriasObligatoriasIds() != null &&
                                plan.getMateriasObligatoriasIds().contains(materiaId)) ||
                                (plan.getMateriasElectivasIds() != null &&
                                        plan.getMateriasElectivasIds().contains(materiaId))
                )
                .toList();
    }

    default List<PlanAcademico> findAllOrderByTotalMateriasDesc() {
        return findAll().stream()
                .sorted((p1, p2) -> {
                    int total1 = (p1.getMateriasObligatoriasIds() != null ? p1.getMateriasObligatoriasIds().size() : 0) +
                            (p1.getMateriasElectivasIds() != null ? p1.getMateriasElectivasIds().size() : 0);
                    int total2 = (p2.getMateriasObligatoriasIds() != null ? p2.getMateriasObligatoriasIds().size() : 0) +
                            (p2.getMateriasElectivasIds() != null ? p2.getMateriasElectivasIds().size() : 0);
                    return Integer.compare(total2, total1);
                })
                .toList();
    }

    default List<PlanAcademico> findAllOrderByCreditosDesc() {
        return findAll().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getCreditosTotales(), p1.getCreditosTotales()))
                .toList();
    }

    default boolean tieneMateriasObligatorias(String planId) {
        return findById(planId)
                .map(plan -> plan.getMateriasObligatoriasIds() != null &&
                        !plan.getMateriasObligatoriasIds().isEmpty())
                .orElse(false);
    }

    default boolean tieneMateriasElectivas(String planId) {
        return findById(planId)
                .map(plan -> plan.getMateriasElectivasIds() != null &&
                        !plan.getMateriasElectivasIds().isEmpty())
                .orElse(false);
    }

    default PlanAcademico guardarPlan(PlanAcademico plan) {
        return save(plan);
    }

    default boolean eliminarPlanSiExiste(String id) {
        if (existsById(id)) {
            deleteById(id);
            return true;
        }
        return false;
    }

    default Optional<PlanAcademico> obtenerPlanPorId(String id) {
        return findById(id);
    }

    default boolean existePlanPorId(String id) {
        return existsById(id);
    }
}