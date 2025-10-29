package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.*;
import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.sql.Time;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        AdministradorController.class,
        DecanaturaController.class,
        EstudiantesControlador.class,
        GrupoController.class,
        HorarioController.class,
        MateriaController.class,
        PeriodoCambioController.class,
        ProfesorController.class,
        SemaforoAcademicoController.class,
        SolicitudCambioController.class,
        UsuarioController.class
})
public class ProyectoExodoBackendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorService administradorService;

    @MockBean
    private DecanaturaService decanaturaService;

    @MockBean
    private EstudianteService estudianteService;

    @MockBean
    private GrupoService grupoService;

    @MockBean
    private HorarioService horarioService;

    @MockBean
    private MateriaService materiaService;

    @MockBean
    private PeriodoCambioService periodoService;

    @MockBean
    private ProfesorService profesorService;

    @MockBean
    private SemaforoAcademicoService semaforoService;

    @MockBean
    private SolicitudCambioService solicitudService;

    @MockBean
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Administrador admin;
    private Estudiante estudiante;
    private Decanatura decanatura;
    private Profesor profesor;
    private Materia materia;
    private Grupo grupo;
    private Horario horario;
    private SolicitudCambio solicitud;
    private PeriodoCambio periodo;
    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        admin = new Administrador(1, "admin", "password", "admin@escuelaing.edu.co");
        estudiante = new Estudiante(1, "estudiante", "pass", "est@escuelaing.edu.co",
                1001, "Juan Perez", "20231001", "Ingeniería", 3);
        decanatura = new Decanatura(2, "decano", "pass", "decano@escuelaing.edu.co",
                1, "Decano Carlos", "Ingeniería");
        profesor = new Profesor(1001, "Profesor Ana", "ana@escuelaing.edu.co");
        materia = new Materia(101, "Cálculo I", "CAL101", 4, "Ingeniería", true);
        grupo = new Grupo(1, 30, "MAT101", "PROF001", "PER001");
        horario = new Horario(1, "Lunes", Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), "A101");
        solicitud = new SolicitudCambio("EST001", "MAT101", "GRP001", "MAT102", "GRP002");
        periodo = new PeriodoCambio(1, "Periodo 2023-2", new Date(), new Date(), "ACADEMICO");
        usuario = new Usuario(1, "usuario", "password", "user@escuelaing.edu.co", Rol.ESTUDIANTE);
    }

    @Test
    public void testGetAllAdministradores() throws Exception {
        when(administradorService.listarTodos()).thenReturn(Arrays.asList(admin));

        mockMvc.perform(get("/api/administradores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    public void testGetAdministradorById() throws Exception {
        when(administradorService.buscarPorId("1")).thenReturn(Optional.of(admin));

        mockMvc.perform(get("/api/administradores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    public void testGetAdministradorByIdNotFound() throws Exception {
        when(administradorService.buscarPorId("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/administradores/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAdministrador() throws Exception {
        when(administradorService.crear(any(Administrador.class))).thenReturn(admin);

        mockMvc.perform(post("/api/administradores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    public void testModificarCupoGrupo() throws Exception {
        when(administradorService.modificarCupoGrupo(anyString(), anyInt())).thenReturn(grupo);

        mockMvc.perform(put("/api/administradores/grupo/1/cupo")
                        .param("nuevoCupo", "35"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllDecanaturas() throws Exception {
        when(decanaturaService.listarTodos()).thenReturn(Arrays.asList(decanatura));

        mockMvc.perform(get("/api/decanaturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Decano Carlos"));
    }

    @Test
    public void testGetDecanaturaById() throws Exception {
        when(decanaturaService.buscarPorId("1")).thenReturn(Optional.of(decanatura));

        mockMvc.perform(get("/api/decanaturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Decano Carlos"));
    }

    @Test
    public void testCreateDecanatura() throws Exception {
        when(decanaturaService.crear(any(Decanatura.class))).thenReturn(decanatura);

        mockMvc.perform(post("/api/decanaturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decanatura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Decano Carlos"));
    }

    @Test
    public void testConsultarSolicitudesPendientes() throws Exception {
        when(decanaturaService.consultarSolicitudesPendientes()).thenReturn(Arrays.asList(solicitud));

        mockMvc.perform(get("/api/decanaturas/solicitudes/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estudianteId").value("EST001"));
    }

    @Test
    public void testCreateEstudiante() throws Exception {
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    public void testGetEstudianteByCodigo() throws Exception {
        when(estudianteService.buscarPorCodigo("20231001")).thenReturn(Optional.of(estudiante));

        mockMvc.perform(get("/api/estudiantes/codigo/20231001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("20231001"));
    }

    @Test
    public void testGetEstudianteById() throws Exception {
        when(estudianteService.buscarPorId("1")).thenReturn(Optional.of(estudiante));

        mockMvc.perform(get("/api/estudiantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    public void testGetAllEstudiantes() throws Exception {
        when(estudianteService.listarTodos()).thenReturn(Arrays.asList(estudiante));

        mockMvc.perform(get("/api/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }

    @Test
    public void testDeleteEstudiante() throws Exception {
        doNothing().when(estudianteService).eliminarPorId("1");

        mockMvc.perform(delete("/api/estudiantes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testVerMiSemaforo() throws Exception {
        Map<String, EstadoSemaforo> semaforo = new HashMap<>();
        semaforo.put("MAT101", EstadoSemaforo.VERDE);
        when(semaforoService.visualizarSemaforoEstudiante("1")).thenReturn(semaforo);

        mockMvc.perform(get("/api/estudiantes/1/semaforo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.MAT101").value("VERDE"));
    }

    @Test
    public void testGetAllGrupos() throws Exception {
        when(grupoService.listarTodos()).thenReturn(Arrays.asList(grupo));

        mockMvc.perform(get("/api/grupos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idGrupo").value(1));
    }

    @Test
    public void testGetGrupoById() throws Exception {
        when(grupoService.buscarPorId("1")).thenReturn(Optional.of(grupo));

        mockMvc.perform(get("/api/grupos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cupoMaximo").value(30));
    }

    @Test
    public void testCreateGrupo() throws Exception {
        when(grupoService.crear(any(Grupo.class))).thenReturn(grupo);

        mockMvc.perform(post("/api/grupos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grupo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idGrupo").value(1));
    }

    @Test
    public void testUpdateCupoGrupo() throws Exception {
        when(grupoService.actualizarCupo(anyString(), anyInt())).thenReturn(grupo);

        mockMvc.perform(put("/api/grupos/1/cupo")
                        .param("nuevoCupo", "35"))
                .andExpect(status().isOk());
    }

    @Test
    public void testVerificarCupoDisponible() throws Exception {
        when(grupoService.verificarCupoDisponible("1")).thenReturn(true);

        mockMvc.perform(get("/api/grupos/1/cupo-disponible"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testCreateHorario() throws Exception {
        when(horarioService.crear(any(Horario.class))).thenReturn(horario);

        mockMvc.perform(post("/api/horarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diaSemana").value("Lunes"));
    }

    @Test
    public void testGetHorarioById() throws Exception {
        when(horarioService.buscarPorId("1")).thenReturn(Optional.of(horario));

        mockMvc.perform(get("/api/horarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salon").value("A101"));
    }

    @Test
    public void testGetAllHorarios() throws Exception {
        when(horarioService.listarTodos()).thenReturn(Arrays.asList(horario));

        mockMvc.perform(get("/api/horarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idHorario").value(1));
    }

    @Test
    public void testCreateMateria() throws Exception {
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);

        mockMvc.perform(post("/api/materias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cálculo I"));
    }

    @Test
    public void testGetMateriaById() throws Exception {
        when(materiaService.buscarPorId("1")).thenReturn(Optional.of(materia));

        mockMvc.perform(get("/api/materias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("CAL101"));
    }

    @Test
    public void testGetMateriaByCodigo() throws Exception {
        when(materiaService.buscarPorCodigo("CAL101")).thenReturn(Optional.of(materia));

        mockMvc.perform(get("/api/materias/codigo/CAL101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cálculo I"));
    }

    @Test
    public void testGetAllMaterias() throws Exception {
        when(materiaService.listarTodos()).thenReturn(Arrays.asList(materia));

        mockMvc.perform(get("/api/materias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].creditos").value(4));
    }

    @Test
    public void testGetAllPeriodos() throws Exception {
        when(periodoService.listarTodos()).thenReturn(Arrays.asList(periodo));

        mockMvc.perform(get("/api/periodos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Periodo 2023-2"));
    }

    @Test
    public void testGetAllProfesores() throws Exception {
        when(profesorService.listarTodos()).thenReturn(Arrays.asList(profesor));

        mockMvc.perform(get("/api/profesores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Profesor Ana"));
    }

    @Test
    public void testGetProfesorById() throws Exception {
        when(profesorService.buscarPorId("1")).thenReturn(Optional.of(profesor));

        mockMvc.perform(get("/api/profesores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Profesor Ana"));
    }

    @Test
    public void testCreateProfesor() throws Exception {
        when(profesorService.crear(any(Profesor.class))).thenReturn(profesor);

        mockMvc.perform(post("/api/profesores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProfesor").value(1001));
    }

    @Test
    public void testVisualizarSemaforoEstudiante() throws Exception {
        Map<String, EstadoSemaforo> semaforo = new HashMap<>();
        semaforo.put("MAT101", EstadoSemaforo.VERDE);
        when(semaforoService.visualizarSemaforoEstudiante("1")).thenReturn(semaforo);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.MAT101").value("VERDE"));
    }

    @Test
    public void testConsultarSemaforoMateria() throws Exception {
        when(semaforoService.consultarSemaforoMateria("1", "MAT101")).thenReturn(Optional.of(EstadoSemaforo.VERDE));

        mockMvc.perform(get("/api/v1/semaforo/estudiante/1/materia/MAT101"))
                .andExpect(status().isOk())
                .andExpect(content().string("\"VERDE\""));
    }

    @Test
    public void testSemestreActual() throws Exception {
        when(semaforoService.getSemestreActual("1")).thenReturn(3);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/1/actual"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semestreActual").value(3));
    }

    @Test
    public void testGetAllSolicitudes() throws Exception {
        when(solicitudService.obtenerTodasLasSolicitudes()).thenReturn(Arrays.asList(solicitud));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estudianteId").value("EST001"));
    }

    @Test
    public void testGetSolicitudById() throws Exception {
        when(solicitudService.obtenerSolicitudPorId("1")).thenReturn(Optional.of(solicitud));

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    public void testCreateSolicitud() throws Exception {
        when(solicitudService.crearSolicitud(any(SolicitudCambio.class))).thenReturn(solicitud);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("EST001"));
    }

    @Test
    public void testUpdateEstadoSolicitud() throws Exception {
        when(solicitudService.actualizarEstado(anyString(), any(EstadoSolicitud.class))).thenReturn(solicitud);

        mockMvc.perform(put("/api/solicitudes/1/cambiar-estado")
                        .param("estado", "APROBADA"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        when(usuarioService.autenticar("usuario", "password")).thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/api/usuarios/login")
                        .param("username", "usuario")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario"));
    }

    @Test
    public void testLoginUnauthorized() throws Exception {
        when(usuarioService.autenticar("usuario", "wrong")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/usuarios/login")
                        .param("username", "usuario")
                        .param("password", "wrong"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCambiarPassword() throws Exception {
        doNothing().when(usuarioService).cambiarPassword("1", "newpassword");

        mockMvc.perform(put("/api/usuarios/1/password")
                        .param("nuevoPassword", "newpassword"))
                .andExpect(status().isOk());
    }

    @Test
    public void testBuscarPorUsername() throws Exception {
        when(usuarioService.buscarPorUsername("usuario")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/username/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario"));
    }

    @Test
    public void testTienePermiso() throws Exception {
        when(usuarioService.tienePermiso("1", "WRITE")).thenReturn(true);

        mockMvc.perform(get("/api/usuarios/1/permiso")
                        .param("accion", "WRITE"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testCreateEstudianteBadRequest() throws Exception {
        when(estudianteService.crear(any(Estudiante.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEstudianteNotFound() throws Exception {
        when(estudianteService.actualizar(anyString(), any(Estudiante.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/estudiantes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(estudiante)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEstudianteNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(estudianteService).eliminarPorId("999");

        mockMvc.perform(delete("/api/estudiantes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSemaforoEstudianteNoContent() throws Exception {
        when(semaforoService.visualizarSemaforoEstudiante("999")).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/estudiantes/999/semaforo"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateCupoGrupoBadRequest() throws Exception {
        when(grupoService.actualizarCupo(anyString(), anyInt())).thenThrow(new IllegalArgumentException("Cupo inválido"));

        mockMvc.perform(put("/api/grupos/1/cupo")
                        .param("nuevoCupo", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void testFiltrarSolicitudes() throws Exception {
        when(solicitudService.obtenerTodasLasSolicitudes()).thenReturn(Arrays.asList(solicitud));

        mockMvc.perform(get("/api/solicitudes/filtrar")
                        .param("decanaturaId", "1")
                        .param("ordenarPor", "fecha")
                        .param("orden", "asc"))
                .andExpect(status().isOk());
    }

    @Test
    public void testResponderSolicitudAprobar() throws Exception {
        when(solicitudService.aprobarSolicitud(anyString(), anyString())).thenReturn(solicitud);

        Map<String, Object> action = new HashMap<>();
        action.put("accion", "APROBAR");
        action.put("justificacion", "Aprobado por pruebas");

        mockMvc.perform(post("/api/solicitudes/1/responder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(action)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCupoGrupoInternalError() throws Exception {
        when(grupoService.actualizarCupo(anyString(), anyInt())).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(put("/api/grupos/1/cupo")
                        .param("nuevoCupo", "35"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testConsultarSemaforoMateriaNotFound() throws Exception {
        when(semaforoService.consultarSemaforoMateria("999", "MAT999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/semaforo/estudiante/999/materia/MAT999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSemestreActualNotFound() throws Exception {
        when(semaforoService.getSemestreActual("999")).thenReturn(0);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/999/actual"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSemestresAnteriores() throws Exception {
        Map<String, Object> foraneo = new HashMap<>();
        foraneo.put("semestres", Arrays.asList(1, 2));
        when(semaforoService.getForaneoEstudiante("1")).thenReturn(foraneo);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/1/anteriores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.semestres").exists());
    }

    @Test
    public void testSemestresAnterioresNoContent() throws Exception {
        when(semaforoService.getForaneoEstudiante("999")).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/v1/semaforo/estudiante/999/anteriores"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSemaforoCompleto() throws Exception {
        SemaforoVisualizacion semaforoVis = new SemaforoVisualizacion();
        semaforoVis.setEstudianteId("1");
        semaforoVis.setNombreEstudiante("Juan Perez");

        when(semaforoService.obtenerSemaforoCompleto("1")).thenReturn(semaforoVis);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/1/completo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("1"));
    }

    @Test
    public void testSemaforoCompletoNoContent() throws Exception {
        when(semaforoService.obtenerSemaforoCompleto("999")).thenReturn(null);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/999/completo"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSemaforoDetallado() throws Exception {
        SemaforoVisualizacion semaforoVis = new SemaforoVisualizacion();
        semaforoVis.setEstudianteId("1");

        when(semaforoService.obtenerSemaforoDetallado("1")).thenReturn(semaforoVis);

        mockMvc.perform(get("/api/v1/semaforo/estudiante/1/detallado"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSemestreActualEstudiante() throws Exception {
        when(semaforoService.getSemestreActual("1")).thenReturn(3);

        mockMvc.perform(get("/api/estudiantes/1/semestre-actual"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    public void testGetSemestreActualEstudianteNotFound() throws Exception {
        when(semaforoService.getSemestreActual("999")).thenReturn(0);

        mockMvc.perform(get("/api/estudiantes/999/semestre-actual"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSemestreActualEstudianteError() throws Exception {
        when(semaforoService.getSemestreActual("1")).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/estudiantes/1/semestre-actual"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAssignMateriaToStudent() throws Exception {
        when(estudianteService.assignMateriaToStudent("1", "MAT101")).thenReturn("Materia asignada");

        mockMvc.perform(post("/api/estudiantes/1/materia/MAT101"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveMateriaFromStudent() throws Exception {
        doNothing().when(estudianteService).removeMateriaFromStudent("1", "MAT101");

        mockMvc.perform(delete("/api/estudiantes/1/materia/MAT101"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetCurrentSemesterForum() throws Exception {
        when(estudianteService.getCurrentSemesterForum("1")).thenReturn("Foro actual");

        mockMvc.perform(get("/api/estudiantes/1/foro/actual"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPastSemestersForum() throws Exception {
        when(estudianteService.getPastSemestersForum("1")).thenReturn("Foros anteriores");

        mockMvc.perform(get("/api/estudiantes/1/foro/anteriores"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentFullInfo() throws Exception {
        when(estudianteService.getStudentFullInfo("1")).thenReturn("Info completa");

        mockMvc.perform(get("/api/estudiantes/1/info"))
                .andExpect(status().isOk());
    }

    @Test
    public void testConfigurarPeriodo() throws Exception {
        when(administradorService.configurarPeriodo(any(PeriodoCambio.class))).thenReturn(periodo);

        mockMvc.perform(post("/api/administradores/periodo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(periodo)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGenerarReportes() throws Exception {
        when(administradorService.generarReportes()).thenReturn(Arrays.asList(solicitud));

        mockMvc.perform(get("/api/administradores/reportes"))
                .andExpect(status().isOk());
    }

    @Test
    public void testModificarEstadoMateriaSemaforo() throws Exception {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        when(administradorService.modificarEstadoMateriaSemaforo("1", "MAT101", EstadoMateria.APROBADA))
                .thenReturn(Optional.of(semaforo));

        mockMvc.perform(put("/api/administradores/semaforo/estudiante/1/materia/MAT101")
                        .param("nuevoEstado", "APROBADA"))
                .andExpect(status().isOk());
    }

    @Test
    public void testModificarEstadoMateriaSemaforoNotFound() throws Exception {
        when(administradorService.modificarEstadoMateriaSemaforo("999", "MAT999", EstadoMateria.APROBADA))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/administradores/semaforo/estudiante/999/materia/MAT999")
                        .param("nuevoEstado", "APROBADA"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testListCasosExcepcionalesError() throws Exception {
        when(administradorService.listCasosExcepcionales()).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/administradores/casos-excepcionales"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAprobarCasoExcepcional() throws Exception {
        when(administradorService.aprobarCasoEspecial(anyLong(), anyMap())).thenReturn("Caso aprobado");

        mockMvc.perform(post("/api/administradores/casos-excepcionales/1/aprobar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }


    @Test
    public void testRechazarCasoExcepcional() throws Exception {
        when(administradorService.rechazarCasoEspecial(anyLong(), anyMap())).thenReturn("Caso rechazado");

        mockMvc.perform(post("/api/administradores/casos-excepcionales/1/rechazar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSolicitarInfoCasoExcepcional() throws Exception {
        when(administradorService.solicitarInfoCasoEspecial(anyLong(), anyMap())).thenReturn("Info solicitada");

        Map<String, Object> info = new HashMap<>();
        info.put("campo", "valor");

        mockMvc.perform(post("/api/administradores/casos-excepcionales/1/solicitar-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizarDecanatura() throws Exception {
        when(decanaturaService.actualizar(eq("1"), any(Decanatura.class))).thenReturn(decanatura);

        mockMvc.perform(put("/api/decanaturas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decanatura)))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizarDecanaturaNotFound() throws Exception {
        when(decanaturaService.actualizar(eq("999"), any(Decanatura.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/decanaturas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(decanatura)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRevisarSolicitud() throws Exception {
        when(decanaturaService.revisarSolicitud("1", EstadoSolicitud.APROBADA, "Aprobada"))
                .thenReturn(solicitud);

        mockMvc.perform(put("/api/decanaturas/solicitudes/1/revisar")
                        .param("estado", "APROBADA")
                        .param("respuesta", "Aprobada"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRevisarSolicitudNotFound() throws Exception {
        when(decanaturaService.revisarSolicitud("999", EstadoSolicitud.APROBADA, "Aprobada"))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/decanaturas/solicitudes/999/revisar")
                        .param("estado", "APROBADA")
                        .param("respuesta", "Aprobada"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGroupsWithHighLoad() throws Exception {
        when(grupoService.obtenerGruposConAlertaCapacidad(90.0)).thenReturn(Arrays.asList(grupo));

        mockMvc.perform(get("/api/grupos/alerts")
                        .param("threshold", "90"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCupoInfoNotFound() throws Exception {
        when(grupoService.buscarPorId("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/grupos/999/cupo-info"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testConsultarEstudiantesInscritos() throws Exception {
        when(grupoService.consultarEstudiantesInscritos("1")).thenReturn(Arrays.asList(estudiante));

        mockMvc.perform(get("/api/grupos/1/estudiantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }

    @Test
    public void testTotalInscritos() throws Exception {
        when(grupoService.consultarEstudiantesInscritos("1")).thenReturn(Arrays.asList(estudiante, estudiante));

        mockMvc.perform(get("/api/grupos/1/inscritos/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2));
    }

    @Test
    public void testGruposPorMateria() throws Exception {
        when(grupoService.buscarPorMateria("MAT101")).thenReturn(Arrays.asList(grupo));

        mockMvc.perform(get("/api/grupos/por-materia/MAT101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].materiaId").value("MAT101"));
    }

    @Test
    public void testGruposPorProfesor() throws Exception {
        when(grupoService.buscarPorProfesor("PROF001")).thenReturn(Arrays.asList(grupo));

        mockMvc.perform(get("/api/grupos/por-profesor/PROF001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].profesorId").value("PROF001"));
    }

    @Test
    public void testDeleteGrupo() throws Exception {
        doNothing().when(grupoService).eliminarPorId("1");

        mockMvc.perform(delete("/api/grupos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteGrupoNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(grupoService).eliminarPorId("999");

        mockMvc.perform(delete("/api/grupos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateHorario() throws Exception {
        when(horarioService.actualizar(eq("1"), any(Horario.class))).thenReturn(horario);

        mockMvc.perform(put("/api/horarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horario)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateHorarioNotFound() throws Exception {
        when(horarioService.actualizar(eq("999"), any(Horario.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/horarios/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horario)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteHorario() throws Exception {
        doNothing().when(horarioService).eliminarPorId("1");

        mockMvc.perform(delete("/api/horarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteHorarioNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(horarioService).eliminarPorId("999");

        mockMvc.perform(delete("/api/horarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateMateria() throws Exception {
        when(materiaService.actualizar(eq("1"), any(Materia.class))).thenReturn(materia);

        mockMvc.perform(put("/api/materias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materia)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMateriaNotFound() throws Exception {
        when(materiaService.actualizar(eq("999"), any(Materia.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/materias/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materia)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMateria() throws Exception {
        doNothing().when(materiaService).eliminarPorId("1");

        mockMvc.perform(delete("/api/materias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteMateriaNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(materiaService).eliminarPorId("999");

        mockMvc.perform(delete("/api/materias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testInscribirEstudianteEnGrupo() throws Exception {
        when(materiaService.inscribirEstudianteEnGrupo("GRP001", "EST001")).thenReturn(grupo);

        mockMvc.perform(post("/api/materias/grupos/GRP001/inscribir/EST001"))
                .andExpect(status().isOk());
    }

    @Test
    public void testInscribirEstudianteEnGrupoBadRequest() throws Exception {
        when(materiaService.inscribirEstudianteEnGrupo("GRP001", "EST001"))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/api/materias/grupos/GRP001/inscribir/EST001"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCapacidadMateria() throws Exception {
        when(materiaService.consultarTotalInscritosPorMateria("MAT101")).thenReturn(25);
        when(materiaService.verificarDisponibilidad("MAT101")).thenReturn(true);
        when(materiaService.consultarGruposDisponibles("MAT101")).thenReturn(Arrays.asList(grupo));

        mockMvc.perform(get("/api/materias/MAT101/capacidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materiaId").value("MAT101"))
                .andExpect(jsonPath("$.inscritos").value(25))
                .andExpect(jsonPath("$.tieneDisponibilidad").value(true))
                .andExpect(jsonPath("$.gruposDisponibles").value(1));
    }

    @Test
    public void testGetCapacidadMateriaNotFound() throws Exception {
        when(materiaService.consultarTotalInscritosPorMateria("MAT999"))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/api/materias/MAT999/capacidad"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCuposMateria() throws Exception {
        doNothing().when(materiaService).modificarCuposMateria("MAT101", 40);

        Map<String, Object> payload = new HashMap<>();
        payload.put("nuevoCupo", 40);

        mockMvc.perform(put("/api/materias/MAT101/cupo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCuposMateriaBadRequest() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("nuevoCupo", "invalido");

        mockMvc.perform(put("/api/materias/MAT101/cupo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetProfesorByCodigo() throws Exception {
        when(profesorService.buscarPorCodigo("PROF001")).thenReturn(Optional.of(profesor));

        mockMvc.perform(get("/api/profesores/codigo/PROF001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Profesor Ana"));
    }

    @Test
    public void testUpdateProfesor() throws Exception {
        when(profesorService.actualizar(eq("1"), any(Profesor.class))).thenReturn(profesor);

        mockMvc.perform(put("/api/profesores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesor)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProfesorNotFound() throws Exception {
        when(profesorService.actualizar(eq("999"), any(Profesor.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/profesores/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profesor)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteProfesor() throws Exception {
        doNothing().when(profesorService).eliminarPorId("1");

        mockMvc.perform(delete("/api/profesores/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProfesorNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(profesorService).eliminarPorId("999");

        mockMvc.perform(delete("/api/profesores/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteSolicitud() throws Exception {
        doNothing().when(solicitudService).eliminarSolicitud("1");

        mockMvc.perform(delete("/api/solicitudes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteSolicitudNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(solicitudService).eliminarSolicitud("999");

        mockMvc.perform(delete("/api/solicitudes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEliminarPorIdSolicitud() throws Exception {
        doNothing().when(solicitudService).eliminarSolicitud("1");

        mockMvc.perform(delete("/api/solicitudes/eliminar/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFiltrarSolicitudesPorPrioridad() throws Exception {
        when(solicitudService.obtenerSolicitudesPorPrioridad(TipoPrioridad.ESPECIAL))
                .thenReturn(Arrays.asList(solicitud));

        mockMvc.perform(get("/api/solicitudes/por-prioridad")
                        .param("prioridad", "ESPECIAL"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFiltrarSolicitudesPrioridadInvalida() throws Exception {
        mockMvc.perform(get("/api/solicitudes/por-prioridad")
                        .param("prioridad", "INVALIDA"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDecisionHistory() throws Exception {
        when(solicitudService.obtenerHistorialPorSolicitud("1"))
                .thenReturn(Arrays.asList("PENDIENTE", "APROBADA"));

        mockMvc.perform(get("/api/solicitudes/1/decisiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("PENDIENTE"));
    }

    @Test
    public void testUpdateSolicitud() throws Exception {
        solicitud.setId("1");
        when(solicitudService.actualizar(any(SolicitudCambio.class))).thenReturn(solicitud);

        mockMvc.perform(put("/api/solicitudes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateSolicitudIdMismatch() throws Exception {
        solicitud.setId("2");

        mockMvc.perform(put("/api/solicitudes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitud)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testResponderSolicitudRechazar() throws Exception {
        when(solicitudService.rechazarSolicitud(anyString(), anyString())).thenReturn(solicitud);

        Map<String, Object> action = new HashMap<>();
        action.put("accion", "RECHAZAR");
        action.put("justificacion", "Rechazado por pruebas");

        mockMvc.perform(post("/api/solicitudes/1/responder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(action)))
                .andExpect(status().isOk());
    }

    @Test
    public void testResponderSolicitudSolicitarInfo() throws Exception {
        when(solicitudService.actualizarEstadoSolicitud(anyString(), any(EstadoSolicitud.class), anyString(), anyString()))
                .thenReturn(solicitud);

        Map<String, Object> action = new HashMap<>();
        action.put("accion", "SOLICITAR_INFO");
        action.put("justificacion", "Se necesita más información");

        mockMvc.perform(post("/api/solicitudes/1/responder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(action)))
                .andExpect(status().isOk());
    }

    @Test
    public void testResponderSolicitudAccionInvalida() throws Exception {
        Map<String, Object> action = new HashMap<>();
        action.put("accion", "INVALIDA");

        mockMvc.perform(post("/api/solicitudes/1/responder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(action)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsuarioById() throws Exception {
        when(usuarioService.buscarPorId("1")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("usuario"));
    }

    @Test
    public void testGetUsuarioByIdNotFound() throws Exception {
        when(usuarioService.buscarPorId("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCambiarPasswordNotFound() throws Exception {
        doThrow(new IllegalArgumentException()).when(usuarioService).cambiarPassword("999", "newpass");

        mockMvc.perform(put("/api/usuarios/999/password")
                        .param("nuevoPassword", "newpass"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBuscarPorUsernameNotFound() throws Exception {
        when(usuarioService.buscarPorUsername("inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/username/inexistente"))
                .andExpect(status().isNotFound());
    }
}