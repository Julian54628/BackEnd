package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.Main;
import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.repository.RepositorioEstudiantes;
import edu.escuelaing.sirha.service.ServicioEstudiantesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Main.class)
class ProyectoExodoBackendApplicationTests {

    private RepositorioEstudiantes repo;
    private ServicioEstudiantesImpl servicio;

    @BeforeEach
    void setUp() {
        repo = mock(RepositorioEstudiantes.class);
        servicio = new ServicioEstudiantesImpl(repo);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void crearEstudiante_DeberiaGuardarse() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20251234");
        estudiante.setNombre("Julian Perez");
        estudiante.setCarrera("Ingenieria de Sistemas");
        estudiante.setSemestre(5);
        estudiante.setSemaforo("Verde");

        when(repo.guardar(estudiante)).thenReturn(estudiante);

        Estudiante creado = servicio.crear(estudiante);

        assertNotNull(creado);
        assertEquals("20251234", creado.getCodigo());
        assertEquals("Julian Perez", creado.getNombre());
        verify(repo).guardar(estudiante);
    }

    @Test
    void buscarPorCodigo_CuandoExiste() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20259876");
        estudiante.setNombre("Ana Rodriguez");
        estudiante.setCarrera("Ingenieria Industrial");
        estudiante.setSemestre(3);
        estudiante.setSemaforo("Azul");

        when(repo.buscarPorCodigo("20259876")).thenReturn(Optional.of(estudiante));

        Optional<Estudiante> resultado = servicio.buscarPorCodigo("20259876");

        assertTrue(resultado.isPresent());
        assertEquals("Ana Rodriguez", resultado.get().getNombre());
        assertEquals("Ingenieria Industrial", resultado.get().getCarrera());
    }

    @Test
    void buscarPorCodigo_CuandoNoExiste() {
        when(repo.buscarPorCodigo("999999")).thenReturn(Optional.empty());

        Optional<Estudiante> resultado = servicio.buscarPorCodigo("999999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscarPorId_DeberiaRetornarEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("uuid-12345");
        estudiante.setCodigo("20251111");
        estudiante.setNombre("Carlos Gomez");
        estudiante.setCarrera("Ingenieria Civil");
        estudiante.setSemestre(7);
        estudiante.setSemaforo("Rojo");

        when(repo.buscarPorId("uuid-12345")).thenReturn(Optional.of(estudiante));

        Optional<Estudiante> resultado = servicio.buscarPorId("uuid-12345");

        assertTrue(resultado.isPresent());
        assertEquals("Carlos Gomez", resultado.get().getNombre());
        assertEquals(7, resultado.get().getSemestre());
    }

    @Test
    void listarTodos_DeberiaRetornarLista() {
        Estudiante e1 = new Estudiante();
        e1.setCodigo("20253333");
        e1.setNombre("Laura Martinez");
        e1.setCarrera("Ingenieria Electronica");
        e1.setSemestre(2);
        e1.setSemaforo("Azul");

        Estudiante e2 = new Estudiante();
        e2.setCodigo("20254444");
        e2.setNombre("Pedro Ramirez");
        e2.setCarrera("Ingenieria de Sistemas");
        e2.setSemestre(6);
        e2.setSemaforo("Verde");

        when(repo.listarTodos()).thenReturn(List.of(e1, e2));

        List<Estudiante> lista = servicio.listarTodos();

        assertEquals(2, lista.size());
        assertEquals("Laura Martinez", lista.get(0).getNombre());
        assertEquals("Pedro Ramirez", lista.get(1).getNombre());
        verify(repo).listarTodos();
    }

    @Test
    void eliminarPorId_DeberiaEliminar() {
        servicio.eliminarPorId("uuid-98765");
        verify(repo).eliminarPorId("uuid-98765");
    }
}
