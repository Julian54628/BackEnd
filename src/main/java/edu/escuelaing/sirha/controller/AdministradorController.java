package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.Administrador;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @GetMapping
    public List<Administrador> getAll() {
        return administradorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Administrador> getById(@PathVariable String id) {
        return administradorService.buscarPorId(id);
    }

    @PostMapping
    public Administrador create(@RequestBody Administrador administrador) {
        return administradorService.crear(administrador);
    }

    @PutMapping("/grupo/{grupoId}/cupo")
    public Grupo modificarCupoGrupo(@PathVariable String grupoId, @RequestParam int nuevoCupo) {
        return administradorService.modificarCupoGrupo(grupoId, nuevoCupo);
    }

    @PostMapping("/periodo")
    public PeriodoCambio configurarPeriodo(@RequestBody PeriodoCambio periodo) {
        return administradorService.configurarPeriodo(periodo);
    }

    @GetMapping("/reportes")
    public List<SolicitudCambio> generarReportes() {
        return administradorService.generarReportes();
    }
}