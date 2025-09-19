package edu.escuelaing.sirha.proyecto_exodo_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.escuelaing.sirha.Main;
import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.repository.RepositorioEstudiantes;
import edu.escuelaing.sirha.repository.RepositorioEstudiantesMemoria;
import edu.escuelaing.sirha.service.ServicioEstudiantesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class ProyectoExodoBackendApplicationTests {

    private RepositorioEstudiantes repoMock;
    private ServicioEstudiantesImpl servicioMock;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        repoMock = mock(RepositorioEstudiantes.class);
        servicioMock = new ServicioEstudiantesImpl(repoMock);
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

        when(repoMock.guardar(estudiante)).thenReturn(estudiante);

        Estudiante creado = servicioMock.crear(estudiante);

        assertNotNull(creado);
        assertEquals("20251234", creado.getCodigo());
        assertEquals("Julian Perez", creado.getNombre());
        verify(repoMock).guardar(estudiante);
    }

    @Test
    void buscarPorCodigo_CuandoExiste() {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20259876");
        estudiante.setNombre("Ana Rodriguez");

        when(repoMock.buscarPorCodigo("20259876")).thenReturn(Optional.of(estudiante));

        Optional<Estudiante> resultado = servicioMock.buscarPorCodigo("20259876");

        assertTrue(resultado.isPresent());
        assertEquals("Ana Rodriguez", resultado.get().getNombre());
    }

    @Test
    void buscarPorCodigo_CuandoNoExiste() {
        when(repoMock.buscarPorCodigo("999999")).thenReturn(Optional.empty());

        Optional<Estudiante> resultado = servicioMock.buscarPorCodigo("999999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarTodos_DeberiaRetornarLista() {
        Estudiante e1 = new Estudiante();
        Estudiante e2 = new Estudiante();
        when(repoMock.listarTodos()).thenReturn(List.of(e1, e2));

        List<Estudiante> lista = servicioMock.listarTodos();

        assertEquals(2, lista.size());
        verify(repoMock).listarTodos();
    }

    @Test
    void eliminarPorId_DeberiaEliminar() {
        servicioMock.eliminarPorId("uuid-98765");
        verify(repoMock).eliminarPorId("uuid-98765");
    }

    @Test
    void repositorioMemoria_GuardarYBuscarPorId() {
        RepositorioEstudiantesMemoria repo = new RepositorioEstudiantesMemoria();
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20256666");
        estudiante.setNombre("Carlos Gomez");

        Estudiante guardado = repo.guardar(estudiante);
        Optional<Estudiante> encontrado = repo.buscarPorId(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Carlos Gomez", encontrado.get().getNombre());
    }

    @Test
    void repositorioMemoria_BuscarPorCodigoYEliminar() {
        RepositorioEstudiantesMemoria repo = new RepositorioEstudiantesMemoria();
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20257777");
        estudiante.setNombre("Laura Martinez");

        Estudiante guardado = repo.guardar(estudiante);
        Optional<Estudiante> encontrado = repo.buscarPorCodigo("20257777");

        assertTrue(encontrado.isPresent());
        assertEquals("Laura Martinez", encontrado.get().getNombre());

        repo.eliminarPorId(guardado.getId());
        assertTrue(repo.buscarPorId(guardado.getId()).isEmpty());
    }

    @Test
    void controlador_CrearYObtenerEstudiante() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20258888");
        estudiante.setNombre("Pedro Ramirez");
        estudiante.setCarrera("Ingenieria Electronica");
        estudiante.setSemestre(4);
        estudiante.setSemaforo("Azul");

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value("20258888"));
    }

    @Test
    void controlador_ListarEstudiantes() throws Exception {
        mockMvc.perform(get("/api/estudiantes"))
                .andExpect(status().isOk());
    }
    @Test
    void controlador_BuscarPorCodigo_CuandoExiste() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20251111");
        estudiante.setNombre("Maria Lopez");
        estudiante.setCarrera("Industrial");
        estudiante.setSemestre(6);
        estudiante.setSemaforo("Rojo");

        // Primero lo creo
        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated());

        // Luego lo busco por código
        mockMvc.perform(get("/api/estudiantes/{codigo}", "20251111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maria Lopez"))
                .andExpect(jsonPath("$.codigo").value("20251111"));
    }

    @Test
    void controlador_BuscarPorCodigo_CuandoNoExiste() throws Exception {
        mockMvc.perform(get("/api/estudiantes/{codigo}", "999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void controlador_EliminarEstudiante() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("20252222");
        estudiante.setNombre("Jose Perez");
        estudiante.setCarrera("Civil");
        estudiante.setSemestre(3);
        estudiante.setSemaforo("Azul");

        String response = mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Estudiante creado = objectMapper.readValue(response, Estudiante.class);

        mockMvc.perform(delete("/api/estudiantes/{id}", creado.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/estudiantes/{codigo}", creado.getCodigo()))
                .andExpect(status().isNotFound());
    }

}