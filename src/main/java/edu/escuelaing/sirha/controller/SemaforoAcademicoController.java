package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/semaforo")
public class SemaforoAcademicoController {

    private final SemaforoAcademicoService semaforoAcademicoService;

    @Autowired
    public SemaforoAcademicoController(SemaforoAcademicoService semaforoAcademicoService) {
        this.semaforoAcademicoService = semaforoAcademicoService;
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<Map<String, EstadoSemaforo>> visualizarSemaforoEstudiante(@PathVariable String estudianteId) {
        Map<String, EstadoSemaforo> resultado = semaforoAcademicoService.visualizarSemaforoEstudiante(estudianteId);
        return resultado.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultado);
    }

    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> consultarSemaforoMateria(@PathVariable String estudianteId, @PathVariable String materiaId) {
        Optional<EstadoSemaforo> estado = semaforoAcademicoService.consultarSemaforoMateria(estudianteId, materiaId);
        return estado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/{estudianteId}/foraneo")
    public ResponseEntity<Map<String, Object>> consultarForaneoEstudiante(@PathVariable String estudianteId) {
        try {
            Map<String, Object> foraneo = semaforoAcademicoService.getForaneoEstudiante(estudianteId);
            return foraneo.get("semestreActual").equals(0) ? ResponseEntity.notFound().build() : ResponseEntity.ok(foraneo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}/completo")
    public ResponseEntity<?> obtenerSemaforoCompleto(@PathVariable String estudianteId) {
        try {
            Object semaforo = semaforoAcademicoService.obtenerSemaforoCompleto(estudianteId);
            return ResponseEntity.ok(semaforo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}/detallado")
    public ResponseEntity<?> obtenerSemaforoDetallado(@PathVariable String estudianteId) {
        try {
            Object semaforo = semaforoAcademicoService.obtenerSemaforoDetallado(estudianteId);
            return ResponseEntity.ok(semaforo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}/semestre-actual")
    public ResponseEntity<Integer> getSemestreActual(@PathVariable String estudianteId) {
        try {
            int semestre = semaforoAcademicoService.getSemestreActual(estudianteId);
            return semestre > 0 ? ResponseEntity.ok(semestre) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}/promedio")
    public ResponseEntity<Double> obtenerPromedioEstudiante(@PathVariable String estudianteId) {
        try {
            Double promedio = semaforoAcademicoService.obtenerPromedioEstudiante(estudianteId);
            return promedio != null ? ResponseEntity.ok(promedio) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}/progreso")
    public ResponseEntity<Map<String, Object>> obtenerProgresoAcademico(@PathVariable String estudianteId) {
        try {
            Map<String, Object> progreso = semaforoAcademicoService.obtenerProgresoAcademico(estudianteId);
            return ResponseEntity.ok(progreso);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}