package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.PeriodoCambioController;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.service.PeriodoCambioService;
import edu.escuelaing.sirha.repository.RepositorioPeriodoCambio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoExodoBackendApplicationTests {

    @Mock
    private PeriodoCambioService periodoService;

    @Mock
    private RepositorioPeriodoCambio periodoRepository;

    @InjectMocks
    private PeriodoCambioController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testListarTodosVacio() {
        when(periodoService.listarTodos()).thenReturn(Collections.emptyList());
        assertTrue(controller.getAll().isEmpty());
    }
    @Test
    void testListarTodosConDatos() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.listarTodos()).thenReturn(List.of(p));
        assertEquals(1, controller.getAll().size());
    }
    @Test
    void testBuscarPorIdExistente() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.buscarPorId("1")).thenReturn(Optional.of(p));
        assertTrue(controller.getById("1").isPresent());
    }
    @Test
    void testBuscarPorIdInexistente() {
        when(periodoService.buscarPorId("2")).thenReturn(Optional.empty());
        assertFalse(controller.getById("2").isPresent());
    }
    @Test
    void testObtenerPeriodoActivoExistente() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.obtenerPeriodoActivo()).thenReturn(Optional.of(p));
        assertTrue(controller.getActivo().isPresent());
    }
    @Test
    void testObtenerPeriodoActivoInexistente() {
        when(periodoService.obtenerPeriodoActivo()).thenReturn(Optional.empty());
        assertFalse(controller.getActivo().isPresent());
    }
    @Test
    void testCrearPeriodo() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.crear(p)).thenReturn(p);
        assertEquals(p, controller.create(p));
    }
    @Test
    void testActualizarPeriodo() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.actualizar("1", p)).thenReturn(p);
        assertEquals(p, controller.update("1", p));
    }
    @Test
    void testEliminarPeriodo() {
        doNothing().when(periodoService).eliminarPorId("1");
        controller.delete("1");
        verify(periodoService, times(1)).eliminarPorId("1");
    }
    @Test
    void testGuardarRepositorio() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoRepository.save(p)).thenReturn(p);
        assertEquals(p, periodoRepository.save(p));
    }
    @Test
    void testFindAllRepositorio() {
        when(periodoRepository.findAll()).thenReturn(List.of(new PeriodoCambio()));
        assertEquals(1, periodoRepository.findAll().size());
    }
    @Test
    void testFindByIdRepositorio() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoRepository.findById("1")).thenReturn(Optional.of(p));
        assertTrue(periodoRepository.findById("1").isPresent());
    }
    @Test
    void testDeleteByIdRepositorio() {
        doNothing().when(periodoRepository).deleteById("1");
        periodoRepository.deleteById("1");
        verify(periodoRepository, times(1)).deleteById("1");
    }
    @Test
    void testSaveAndFindByIdRepositorio() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoRepository.save(p)).thenReturn(p);
        when(periodoRepository.findById("1")).thenReturn(Optional.of(p));
        periodoRepository.save(p);
        assertTrue(periodoRepository.findById("1").isPresent());
    }
    @Test
    void testFindByIdRepositorioInexistente() {
        when(periodoRepository.findById("99")).thenReturn(Optional.empty());
        assertFalse(periodoRepository.findById("99").isPresent());
    }
    @Test
    void testModeloGettersSetters() {
        PeriodoCambio p = new PeriodoCambio();
        p.setId("abc");
        p.setNombre("nombre");
        p.setActivo(true);
        assertEquals("abc", p.getId());
        assertEquals("nombre", p.getNombre());
        assertTrue(p.isActivo());
    }
    @Test
    void testModeloComparacionReferencia() {
        PeriodoCambio p1 = new PeriodoCambio();
        PeriodoCambio p2 = p1;
        assertSame(p1, p2);
    }
    @Test
    void testModeloComparacionValores() {
        PeriodoCambio p1 = new PeriodoCambio();
        p1.setId("1");
        PeriodoCambio p2 = new PeriodoCambio();
        p2.setId("1");
        assertNotEquals(p1, p2);
    }
    @Test
    void testModeloToStringNotNull() {
        PeriodoCambio p = new PeriodoCambio();
        assertNotNull(p.toString());
    }
    @Test
    void testServicioCrearLanzaExcepcion() {
        PeriodoCambio p = new PeriodoCambio();
        when(periodoService.crear(p)).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> controller.create(p));
    }
}