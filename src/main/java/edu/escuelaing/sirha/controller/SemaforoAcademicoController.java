package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.EstadoSemaforo;
import edu.escuelaing.sirha.model.SemaforoVisualizacion;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

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
        return (resultado == null || resultado.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.ok(resultado);
    }

    @GetMapping("/estudiante/{estudianteId}/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> consultarSemaforoMateria(@PathVariable String estudianteId, @PathVariable String materiaId) {
        Optional<EstadoSemaforo> estado = semaforoAcademicoService.consultarSemaforoMateria(estudianteId, materiaId);
        return estado.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/{estudianteId}/actual")
    public ResponseEntity<Map<String, Object>> semestreActual(@PathVariable String estudianteId) {
        int semestre = semaforoAcademicoService.getSemestreActual(estudianteId);
        if (semestre <= 0) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("semestreActual", semestre);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/estudiante/{estudianteId}/anteriores")
    public ResponseEntity<Map<String, Object>> semestresAnteriores(@PathVariable String estudianteId) {
        Map<String, Object> res = semaforoAcademicoService.getForaneoEstudiante(estudianteId);
        return (res == null || res.isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.ok(res);
    }

    @GetMapping("/estudiante/{estudianteId}/completo")
    public ResponseEntity<SemaforoVisualizacion> semaforoCompleto(@PathVariable String estudianteId) {
        SemaforoVisualizacion vis = semaforoAcademicoService.obtenerSemaforoCompleto(estudianteId);
        return vis == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(vis);
    }

    @GetMapping("/estudiante/{estudianteId}/detallado")
    public ResponseEntity<SemaforoVisualizacion> semaforoDetallado(@PathVariable String estudianteId) {
        SemaforoVisualizacion vis = semaforoAcademicoService.obtenerSemaforoDetallado(estudianteId);
        return vis == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(vis);
    }
}