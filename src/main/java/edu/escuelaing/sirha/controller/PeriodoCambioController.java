package edu.escuelaing.sirha.controller;

import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.service.PeriodoCambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/periodos")
public class PeriodoCambioController {

    @Autowired
    private PeriodoCambioService periodoService;

    @GetMapping
    public List<PeriodoCambio> getAll() {
        return periodoService.listarTodos();
    }

}