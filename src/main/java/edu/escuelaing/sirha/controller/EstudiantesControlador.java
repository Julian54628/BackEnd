package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.EstudianteService;
import edu.escuelaing.sirha.service.SemaforoAcademicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudiantesControlador {

    private final EstudianteService estudianteService;
    private final SemaforoAcademicoService semaforoAcademicoService;

    @Autowired
    public EstudiantesControlador(EstudianteService estudianteService, SemaforoAcademicoService semaforoAcademicoService) {
        this.estudianteService = estudianteService;
        this.semaforoAcademicoService = semaforoAcademicoService;
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        try {
            Estudiante creado = estudianteService.crear(estudiante);
            return ResponseEntity.created(URI.create("/api/estudiantes/" + creado.getId())).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Estudiante> buscarPorCodigo(@PathVariable String codigo) {
        return estudianteService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> buscarPorId(@PathVariable String id) {
        return estudianteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Estudiante> listarTodos() {
        return estudianteService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        try {
            estudianteService.eliminarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        try {
            Estudiante actualizado = estudianteService.actualizar(id, estudiante);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/solicitudes")
    public ResponseEntity<SolicitudCambio> crearSolicitudCambio(
            @PathVariable String id,
            @RequestParam String materiaOrigenId,
            @RequestParam String grupoOrigenId,
            @RequestParam String materiaDestinoId,
            @RequestParam String grupoDestinoId) {
        try {
            SolicitudCambio solicitud = estudianteService.crearSolicitudCambio(id, materiaOrigenId, grupoOrigenId, materiaDestinoId, grupoDestinoId);
            return ResponseEntity.created(URI.create("/api/estudiantes/" + id + "/solicitudes/" + solicitud.getId())).body(solicitud);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/solicitudes")
    public List<SolicitudCambio> consultarSolicitudes(@PathVariable String id) {
        return estudianteService.consultarSolicitudes(id);
    }

    @GetMapping("/{id}/semaforo")
    public ResponseEntity<Map<String, EstadoSemaforo>> verMiSemaforo(@PathVariable String id) {
        Map<String, EstadoSemaforo> semaforo = semaforoAcademicoService.visualizarSemaforoEstudiante(id);
        return semaforo.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(semaforo);
    }

    @GetMapping("/{id}/semaforo/materia/{materiaId}")
    public ResponseEntity<EstadoSemaforo> verEstadoMateria(@PathVariable String id, @PathVariable String materiaId) {
        return semaforoAcademicoService.consultarSemaforoMateria(id, materiaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/semestre-actual")
    public ResponseEntity<Integer> getSemestreActual(@PathVariable String id) {
        try {
            int semestre = semaforoAcademicoService.getSemestreActual(id);
            return semestre > 0 ? ResponseEntity.ok(semestre) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/semestres-anteriores")
    public ResponseEntity<List<Integer>> getSemestresAnteriores(@PathVariable String id) {
        try {
            Map<String, Object> foraneo = semaforoAcademicoService.getForaneoEstudiante(id);
            @SuppressWarnings("unchecked")
            List<Integer> semestres = (List<Integer>) foraneo.get("semestresAnteriores");
            return ResponseEntity.ok(semestres);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/{id}/foraneo")
    public ResponseEntity<Map<String, Object>> getForaneoCompleto(@PathVariable String id) {
        try {
            Map<String, Object> foraneo = semaforoAcademicoService.getForaneoEstudiante(id);
            return ResponseEntity.ok(foraneo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/informacion-academica")
    public ResponseEntity<Map<String, Object>> getInformacionAcademica(@PathVariable String id) {
        try {
            Map<String, Object> informacion = new HashMap<>();
            Optional<Estudiante> estudianteOpt = estudianteService.buscarPorId(id);
            if (estudianteOpt.isPresent()) {
                Estudiante estudiante = estudianteOpt.get();
                informacion.put("codigo", estudiante.getCodigo());
                informacion.put("nombre", estudiante.getNombre());
                informacion.put("semestre", estudiante.getSemestre());
                informacion.put("programa", estudiante.getCarrera());
            }
            Map<String, Object> foraneo = semaforoAcademicoService.getForaneoEstudiante(id);
            informacion.putAll(foraneo);
            return ResponseEntity.ok(informacion);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/carrera/{carrera}")
    public List<Estudiante> buscarPorCarrera(@PathVariable String carrera) {
        return estudianteService.buscarPorCarrera(carrera);
    }

    @GetMapping("/semestre/{semestre}")
    public List<Estudiante> buscarPorSemestre(@PathVariable int semestre) {
        return estudianteService.buscarPorSemestre(semestre);
    }
}