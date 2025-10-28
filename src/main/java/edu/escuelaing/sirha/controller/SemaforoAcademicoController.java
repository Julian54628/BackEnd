package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        if (resultado.isEmpty()) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> consultarSemaforoMateria(@PathVariable String estudianteId, @PathVariable String materiaId) {
        Optional<EstadoSemaforo> estado = semaforoAcademicoService.consultarSemaforoMateria(estudianteId, materiaId);
        if (estado.isPresent()) {
            return ResponseEntity.ok(estado.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/estudiante/{estudianteId}/foraneo")
    public ResponseEntity<Map<String, Object>> consultarForaneoEstudiante(@PathVariable String estudianteId) {
        try {
            Map<String, Object> foraneo = semaforoAcademicoService.getForaneoEstudiante(estudianteId);
            if (foraneo.get("semestreActual").equals(0)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(foraneo);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}