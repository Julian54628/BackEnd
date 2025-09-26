package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.PeriodoCambioController;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.service.PeriodoCambioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PeriodoCambioControllerTest {

    @Mock
    private PeriodoCambioService periodoService;

    @InjectMocks
    private PeriodoCambioController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(periodoService.listarTodos()).thenReturn(Collections.emptyList());
        List<PeriodoCambio> result = controller.getAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetById() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.buscarPorId("1")).thenReturn(Optional.of(p));
        Optional<PeriodoCambio> result = controller.getById("1");
        assertTrue(result.isPresent());
        assertEquals(p, result.get());
    }

    @Test
    void testGetActivo() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.obtenerPeriodoActivo()).thenReturn(Optional.of(p));
        Optional<PeriodoCambio> result = controller.getActivo();
        assertTrue(result.isPresent());
        assertEquals(p, result.get());
    }

    @Test
    void testCreate() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.crear(p)).thenReturn(p);
        PeriodoCambio result = controller.create(p);
        assertEquals(p, result);
    }

    @Test
    void testUpdate() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.actualizar("1", p)).thenReturn(p);
        PeriodoCambio result = controller.update("1", p);
        assertEquals(p, result);
    }

    @Test
    void testDelete() {
        doNothing().when(periodoService).eliminarPorId("1");
        controller.delete("1");
        verify(periodoService, times(1)).eliminarPorId("1");
    }
}