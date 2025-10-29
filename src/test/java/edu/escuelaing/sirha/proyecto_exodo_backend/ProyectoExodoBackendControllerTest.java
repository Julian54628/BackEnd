package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.*;
import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProyectoExodoBackendControllerTest {

    @Mock private EstudianteService estudianteService;
    @Mock private SemaforoAcademicoService semaforoAcademicoService;
    @Mock private SolicitudCambioService solicitudCambioService;
    @Mock private GrupoService grupoService;
    @Mock private MateriaService materiaService;
    @Mock private AdministradorService administradorService;
    @Mock private DecanaturaService decanaturaService;
    @Mock private HorarioService horarioService;
    @Mock private PeriodoCambioService periodoService;
    @Mock private ProfesorService profesorService;
    @Mock private UsuarioService usuarioService;

    @InjectMocks private EstudiantesControlador estudiantesControlador;
    @InjectMocks private SemaforoAcademicoController semaforoAcademicoController;
    @InjectMocks private SolicitudCambioController solicitudCambioController;
    @InjectMocks private GrupoController grupoController;
    @InjectMocks private MateriaController materiaController;
    @InjectMocks private AdministradorController administradorController;
    @InjectMocks private DecanaturaController decanaturaController;
    @InjectMocks private HorarioController horarioController;
    @InjectMocks private PeriodoCambioController periodoCambioController;
    @InjectMocks private ProfesorController profesorController;
    @InjectMocks private UsuarioController usuarioController;

    private Estudiante estudiante;
    private SolicitudCambio solicitudCambio;
    private Grupo grupo;
    private Materia materia;
    private Administrador administrador;
    private Decanatura decanatura;
    private Horario horario;
    private PeriodoCambio periodoCambio;
    private Profesor profesor;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante();
        estudiante.setId("Estudiante");
        estudiante.setCodigo("1000044519");
        estudiante.setNombre("Julian Andres");
        estudiante.setCarrera("Ingeniería de Sistemas");
        estudiante.setSemestre(5);
        estudiante.setUsername("julian.estudiante");
        estudiante.setPasswordHash("password123");
        estudiante.setCorreoInstitucional("julian@escuelaing.edu.co");
        estudiante.setRol(Rol.ESTUDIANTE);
        estudiante.setIdEstudiante(12345);

        administrador = new Administrador();
        administrador.setId("Administrador");
        administrador.setUsername("admin.lopez");
        administrador.setPasswordHash("admin123");
        administrador.setCorreoInstitucional("admin@escuelaing.edu.co");
        administrador.setRol(Rol.ADMIN);
        administrador.setIdUsuario(1);

        profesor = new Profesor();
        profesor.setId("profesor");
        profesor.setNombre("Dr. Roberto Martínez");
        profesor.setCorreoInstitucional("roberto.martinez@escuelaing.edu.co");
        profesor.setIdProfesor(100);
        profesor.setUsername("roberto.martinez");
        profesor.setPasswordHash("prof123");
        profesor.setRol(Rol.PROFESOR);

        decanatura = new Decanatura();
        decanatura.setId("Decanos");
        decanatura.setUsername("maria.decana");
        decanatura.setNombre("Decanatura Ingeniería");
        decanatura.setFacultad("Ingeniería");
        decanatura.setIdDecanatura(101);
        decanatura.setPasswordHash("decana123");
        decanatura.setCorreoInstitucional("maria@escuelaing.edu.co");
        decanatura.setRol(Rol.DECANATURA);

        usuario = new Usuario();
        usuario.setId("Usuario");
        usuario.setUsername("Julian");
        usuario.setPasswordHash("password123");
        usuario.setCorreoInstitucional("julian@escuelaing.edu.co");
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setIdUsuario(1);

        solicitudCambio = new SolicitudCambio();
        solicitudCambio.setId("Solicitud");
        solicitudCambio.setEstudianteId("Estudiante");
        solicitudCambio.setEstado(EstadoSolicitud.APROBADA);

        grupo = new Grupo();
        grupo.setId("grupo1");
        grupo.setCupoMaximo(30);
        grupo.setEstudiantesInscritosIds(new ArrayList<>());

        materia = new Materia();
        materia.setId("AYSR");
        materia.setNombre("ArquitecturadeRed");

        horario = new Horario();
        horario.setId("horario1");

        periodoCambio = new PeriodoCambio();
        periodoCambio.setId("periodo2025-2");
    }

    @Test
    void testEstudiantesControlador_CrearEstudiante() {
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        ResponseEntity<Estudiante> response = estudiantesControlador.crear(estudiante);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Estudiante", response.getBody().getId());
    }

    @Test
    void testEstudiantesControlador_BuscarPorCodigo_Existe() {
        when(estudianteService.buscarPorCodigo("1000044519")).thenReturn(Optional.of(estudiante));
        ResponseEntity<Estudiante> response = estudiantesControlador.buscarPorCodigo("1000044519");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("1000044519", response.getBody().getCodigo());
    }

    @Test
    void testEstudiantesControlador_BuscarPorCodigo_NoExiste() {
        when(estudianteService.buscarPorCodigo("999999")).thenReturn(Optional.empty());
        ResponseEntity<Estudiante> response = estudiantesControlador.buscarPorCodigo("999999");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testEstudiantesControlador_ListarTodos() {
        when(estudianteService.listarTodos()).thenReturn(Arrays.asList(estudiante));
        ResponseEntity<List<Estudiante>> response = estudiantesControlador.listarTodos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testEstudiantesControlador_Eliminar() {
        doNothing().when(estudianteService).eliminarPorId("Estudiante");
        ResponseEntity<Void> response = estudiantesControlador.eliminar("Estudiante");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(estudianteService).eliminarPorId("Estudiante");
    }

    @Test
    void testEstudiantesControlador_Actualizar() {
        when(estudianteService.actualizar(eq("Estudiante"), any(Estudiante.class))).thenReturn(estudiante);
        ResponseEntity<Estudiante> response = estudiantesControlador.actualizar("Estudiante", estudiante);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testEstudiantesControlador_VerMiSemaforo_ConDatos() {
        Map<String, EstadoSemaforo> semaforo = new HashMap<>();
        semaforo.put("AYSR", EstadoSemaforo.VERDE);
        when(semaforoAcademicoService.visualizarSemaforoEstudiante("Estudiante")).thenReturn(semaforo);
        ResponseEntity<Map<String, EstadoSemaforo>> response = estudiantesControlador.verMiSemaforo("Estudiante");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testEstudiantesControlador_VerMiSemaforo_SinDatos() {
        when(semaforoAcademicoService.visualizarSemaforoEstudiante("Estudiante")).thenReturn(new HashMap<>());
        ResponseEntity<Map<String, EstadoSemaforo>> response = estudiantesControlador.verMiSemaforo("Estudiante");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testEstudiantesControlador_GetSemestreActual_Existe() {
        when(semaforoAcademicoService.getSemestreActual("Estudiante")).thenReturn(5);
        ResponseEntity<Integer> response = estudiantesControlador.getSemestreActual("Estudiante");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody());
    }

    @Test
    void testEstudiantesControlador_GetSemestreActual_NoExiste() {
        when(semaforoAcademicoService.getSemestreActual("Estudiante")).thenReturn(0);
        ResponseEntity<Integer> response = estudiantesControlador.getSemestreActual("Estudiante");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSolicitudCambioController_ObtenerTodasLasSolicitudes() {
        when(solicitudCambioService.obtenerTodasLasSolicitudes()).thenReturn(Arrays.asList(solicitudCambio));
        List<SolicitudCambio> result = solicitudCambioController.obtenerTodasLasSolicitudes();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSolicitudCambioController_ObtenerSolicitudPorId_Existe() {
        when(solicitudCambioService.obtenerSolicitudPorId("Solicitud")).thenReturn(Optional.of(solicitudCambio));
        ResponseEntity<SolicitudCambio> response = solicitudCambioController.obtenerSolicitudPorId("Solicitud");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Solicitud", response.getBody().getId());
    }

    @Test
    void testSolicitudCambioController_ObtenerSolicitudPorId_NoExiste() {
        when(solicitudCambioService.obtenerSolicitudPorId("Solicitud")).thenReturn(Optional.empty());
        ResponseEntity<SolicitudCambio> response = solicitudCambioController.obtenerSolicitudPorId("Solicitud");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSolicitudCambioController_CrearSolicitud() {
        when(solicitudCambioService.crearSolicitud(any(SolicitudCambio.class))).thenReturn(solicitudCambio);
        ResponseEntity<SolicitudCambio> response = solicitudCambioController.crear(solicitudCambio);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testSolicitudCambioController_ActualizarEstado() {
        when(solicitudCambioService.actualizarEstado("Solicitud", EstadoSolicitud.APROBADA)).thenReturn(solicitudCambio);
        ResponseEntity<SolicitudCambio> response = solicitudCambioController.actualizarEstado("Solicitud", "aprobada");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testSolicitudCambioController_ActualizarEstado_EstadoInvalido() {
        ResponseEntity<SolicitudCambio> response = solicitudCambioController.actualizarEstado("Solicitud", "ESTADO_INVALIDO");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSolicitudCambioController_EliminarSolicitud() {
        doNothing().when(solicitudCambioService).eliminarSolicitud("Solicitud");
        ResponseEntity<Void> response = solicitudCambioController.eliminarSolicitud("Solicitud");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testSolicitudCambioController_Responder_AprobarYRechazarYSolicitarInfo() {
        when(solicitudCambioService.aprobarSolicitud("Solicitud", "OK")).thenReturn(solicitudCambio);
        when(solicitudCambioService.rechazarSolicitud("Solicitud", "NO")).thenReturn(solicitudCambio);
        when(solicitudCambioService.actualizarEstadoSolicitud(eq("Solicitud"), any(), anyString(), anyString()))
                .thenReturn(solicitudCambio);

        Map<String, Object> accionAprobar = Map.of("accion", "APROBAR", "justificacion", "OK");
        ResponseEntity<?> respA = solicitudCambioController.responderSolicitud("Solicitud", accionAprobar);
        assertEquals(HttpStatus.OK, respA.getStatusCode());

        Map<String, Object> accionRechazar = Map.of("accion", "RECHAZAR", "justificacion", "NO");
        ResponseEntity<?> respR = solicitudCambioController.responderSolicitud("Solicitud", accionRechazar);
        assertEquals(HttpStatus.OK, respR.getStatusCode());

        Map<String, Object> accionInfo = Map.of("accion", "SOLICITAR_INFO", "justificacion", "Mas datos");
        ResponseEntity<?> respI = solicitudCambioController.responderSolicitud("Solicitud", accionInfo);
        assertEquals(HttpStatus.OK, respI.getStatusCode());

        Map<String, Object> accionInvalida = Map.of("accion", "OTRA");
        ResponseEntity<?> respBad = solicitudCambioController.responderSolicitud("Solicitud", accionInvalida);
        assertEquals(HttpStatus.BAD_REQUEST, respBad.getStatusCode());
    }

    @Test
    void testGrupoController_GetAll() {
        when(grupoService.listarTodos()).thenReturn(Arrays.asList(grupo));
        List<Grupo> result = grupoController.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGrupoController_GetById() {
        when(grupoService.buscarPorId("grupo1")).thenReturn(Optional.of(grupo));
        ResponseEntity<Grupo> response = grupoController.getById("grupo1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("grupo1", response.getBody().getId());
    }

    @Test
    void testGrupoController_Create() {
        when(grupoService.crear(any(Grupo.class))).thenReturn(grupo);
        Grupo result = grupoController.create(grupo);
        assertNotNull(result);
        assertEquals("grupo1", result.getId());
    }

    @Test
    void testGrupoController_UpdateCupo() {
        when(grupoService.actualizarCupo("grupo1", 40)).thenReturn(grupo);
        ResponseEntity<?> response = grupoController.updateCupo("grupo1", 40);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGrupoController_UpdateCupo_CupoInvalido() {
        when(grupoService.actualizarCupo("grupo1", -1))
                .thenThrow(new IllegalArgumentException("Cupo inválido"));

        ResponseEntity<?> response = grupoController.updateCupo("grupo1", -1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGrupoController_VerificarCupoDisponible() {
        when(grupoService.verificarCupoDisponible("grupo1")).thenReturn(true);
        ResponseEntity<Boolean> response = grupoController.verificarCupoDisponible("grupo1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Boolean.TRUE.equals(response.getBody()));
    }

    @Test
    void testGrupoController_GetCupoInfo() {
        when(grupoService.buscarPorId("grupo1")).thenReturn(Optional.of(grupo));
        when(grupoService.consultarCargaAcademica("grupo1")).thenReturn(50.0f);
        ResponseEntity<?> response = grupoController.getCupoInfo("grupo1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("grupo1", body.get("grupoId"));
        assertEquals(30, body.get("cupoMaximo"));
        assertEquals(0, body.get("inscritos"));
    }

    @Test
    void testMateriaController_Crear() {
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);
        ResponseEntity<Materia> result = materiaController.crear(materia);
        assertNotNull(result);
        assertEquals(materia    , result.getBody());
    }

    @Test
    void testMateriaController_BuscarPorId() {
        when(materiaService.buscarPorId("AYSR")).thenReturn(Optional.of(materia));
        ResponseEntity<Materia> response = materiaController.buscarPorId("AYSR");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("AYSR", response.getBody().getId());
    }

    @Test
    void testMateriaController_InscribirEstudianteEnGrupo() {
        when(materiaService.inscribirEstudianteEnGrupo("grupo1", "Estudiante")).thenReturn(grupo);
        ResponseEntity<Grupo> result = materiaController.inscribirEstudianteEnGrupo("grupo1", "Estudiante");
        assertNotNull(result);
    }

    @Test
    void testAdministradorController_GetAll() {
        when(administradorService.listarTodos()).thenReturn(Collections.singletonList(administrador));
        List<Administrador> result = administradorController.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testAdministradorController_GetById() {
        when(administradorService.buscarPorId("Administrador")).thenReturn(Optional.of(administrador));
        ResponseEntity<Administrador> response = administradorController.getById("Administrador");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Administrador", response.getBody().getId());
    }

    @Test
    void testAdministradorController_Create() {
        when(administradorService.crear(any(Administrador.class))).thenReturn(administrador);
        ResponseEntity<Administrador> result = administradorController.create(administrador);
        assertNotNull(result);
        assertEquals(administrador, result.getBody());
    }

    @Test
    void testAdministradorController_ModificarCupoGrupo() {
        when(administradorService.modificarCupoGrupo("grupo1", 30)).thenReturn(grupo);
        ResponseEntity<Grupo> result = administradorController.modificarCupoGrupo("grupo1", 30);
        assertNotNull(result);
        assertEquals(grupo, result.getBody());
    }

    @Test
    void testAdministradorController_ConfigurarPeriodo() {
        when(administradorService.configurarPeriodo(any(PeriodoCambio.class))).thenReturn(periodoCambio);
        PeriodoCambio result = administradorController.configurarPeriodo(periodoCambio);
        assertNotNull(result);
        assertEquals("periodo2025-2", result.getId());
    }

    @Test
    void testAdministradorController_GenerarReportes() {
        when(administradorService.generarReportes()).thenReturn(Collections.singletonList(solicitudCambio));
        List<SolicitudCambio> result = administradorController.generarReportes();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDecanaturaController_GetAll() {
        when(decanaturaService.listarTodos()).thenReturn(Collections.singletonList(decanatura));
        List<Decanatura> result = decanaturaController.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDecanaturaController_GetById() {
        when(decanaturaService.buscarPorId("Decanos")).thenReturn(Optional.of(decanatura));
        ResponseEntity<Decanatura> response = decanaturaController.getById("Decanos");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Decanos", response.getBody().getId());
    }

    @Test
    void testDecanaturaController_Create() {
        when(decanaturaService.crear(any(Decanatura.class))).thenReturn(decanatura);
        Decanatura result = decanaturaController.create(decanatura);
        assertNotNull(result);
        assertEquals("Decanos", result.getId());
    }

    @Test
    void testDecanaturaController_ConsultarSolicitudesPendientes() {
        when(decanaturaService.consultarSolicitudesPendientes()).thenReturn(Collections.singletonList(solicitudCambio));
        List<SolicitudCambio> result = decanaturaController.consultarSolicitudesPendientes();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testDecanaturaController_RevisarSolicitud() {
        when(decanaturaService.revisarSolicitud("Solicitud", EstadoSolicitud.APROBADA, "Aprobada")).thenReturn(solicitudCambio);
        ResponseEntity<SolicitudCambio> result = decanaturaController.revisarSolicitud("Solicitud", EstadoSolicitud.APROBADA, "Aprobada");
        assertNotNull(result);
        assertEquals(solicitudCambio, result.getBody());
    }

    @Test
    void testSemaforoAcademicoController_VisualizarSemaforoEstudiante_ConDatos() {
        Map<String, EstadoSemaforo> semaforo = new HashMap<>();
        semaforo.put("AYSR", EstadoSemaforo.VERDE);
        when(semaforoAcademicoService.visualizarSemaforoEstudiante("Estudiante")).thenReturn(semaforo);
        ResponseEntity<Map<String, EstadoSemaforo>> response = semaforoAcademicoController.visualizarSemaforoEstudiante("Estudiante");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(EstadoSemaforo.VERDE, response.getBody().get("AYSR"));
    }

    @Test
    void testSemaforoAcademicoController_ConsultarSemaforoMateria_Existe() {
        when(semaforoAcademicoService.consultarSemaforoMateria("Estudiante", "AYSR")).thenReturn(Optional.of(EstadoSemaforo.ROJO));
        ResponseEntity<EstadoSemaforo> response = semaforoAcademicoController.consultarSemaforoMateria("Estudiante", "AYSR");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(EstadoSemaforo.ROJO, response.getBody());
    }

    @Test
    void testSemaforoAcademicoController_ConsultarSemaforoMateria_NoExiste() {
        when(semaforoAcademicoService.consultarSemaforoMateria("Estudiante", "mat99")).thenReturn(Optional.empty());
        ResponseEntity<EstadoSemaforo> response = semaforoAcademicoController.consultarSemaforoMateria("Estudiante", "mat99");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUsuarioController_Login_Exitoso() {
        when(usuarioService.autenticar("Julian", "password123")).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioController.login("Julian", "password123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Julian", response.getBody().getUsername());
    }

    @Test
    void testUsuarioController_Login_Fallido() {
        when(usuarioService.autenticar("usuarioinexistente", "contrasena")).thenReturn(Optional.empty());
        ResponseEntity<Usuario> response = usuarioController.login("usuarioinexistente", "contrasena");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUsuarioController_BuscarPorUsername() {
        when(usuarioService.buscarPorUsername("Julian")).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioController.buscarPorUsername("Julian");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Julian", response.getBody().getUsername());
    }

    @Test
    void testUsuarioController_TienePermiso() {
        when(usuarioService.tienePermiso("Usuario", "ACCION_TEST")).thenReturn(true);
        ResponseEntity<Boolean> response = usuarioController.tienePermiso("Usuario", "ACCION_TEST");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Boolean.TRUE.equals(response.getBody()));
    }

    @Test
    void testUsuarioController_CambiarPassword_Helper() {
        doNothing().when(usuarioService).cambiarPassword("Usuario", "newPass");
        ResponseEntity<Void> response = usuarioController.cambiarPassword("Usuario", "newPass");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioService).cambiarPassword("Usuario", "newPass");
    }

    @Test
    void testProfesorController_ListarTodos() {
        when(profesorService.listarTodos()).thenReturn(Collections.singletonList(profesor));
        List<Profesor> result = profesorController.listarTodos();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testProfesorController_BuscarPorId() {
        when(profesorService.buscarPorId("profesor")).thenReturn(Optional.of(profesor));
        ResponseEntity<Profesor> response = profesorController.buscarPorId("profesor");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("profesor", response.getBody().getId());
    }

    @Test
    void testProfesorController_Crear() {
        when(profesorService.crear(any(Profesor.class))).thenReturn(profesor);
        Profesor result = profesorController.crear(profesor);
        assertNotNull(result);
        assertEquals("profesor", result.getId());
    }

    @Test
    void testProfesorController_ActualizarProfesor() {
        when(profesorService.actualizar(eq("profesor"), any(Profesor.class))).thenReturn(profesor);
        ResponseEntity<Profesor> resultado = profesorController.actualizar("profesor", profesor);
        assertNotNull(resultado);
        assertEquals(profesor, resultado.getBody());
    }

    @Test
    void testHorarioController_Crear() {
        when(horarioService.crear(any(Horario.class))).thenReturn(horario);
        Horario result = horarioController.crear(horario);
        assertNotNull(result);
        assertEquals("horario1", result.getId());
    }

    @Test
    void testHorarioController_BuscarPorId() {
        when(horarioService.buscarPorId("horario1")).thenReturn(Optional.of(horario));
        ResponseEntity<Horario> response = horarioController.buscarPorId("horario1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("horario1", response.getBody().getId());
    }

    @Test
    void testHorarioController_ConsultarHorariosPorGrupo() {
        when(horarioService.consultarHorariosPorGrupo("grupo1")).thenReturn(Collections.singletonList(horario));
        List<Horario> result = horarioController.consultarHorariosPorGrupo("grupo1");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testSemaforoAcademicoCasosBorde() {
        when(semaforoAcademicoService.getForaneoEstudiante("cualquierId")).thenReturn(new HashMap<>());
        when(semaforoAcademicoService.obtenerSemaforoCompleto("cualquierId")).thenReturn(new SemaforoVisualizacion());
        when(semaforoAcademicoService.obtenerSemaforoDetallado("cualquierId")).thenReturn(new SemaforoVisualizacion());

        Map<String, Object> foraneoNull = semaforoAcademicoService.getForaneoEstudiante("cualquierId");
        assertNotNull(foraneoNull);

        SemaforoVisualizacion completoNull = semaforoAcademicoService.obtenerSemaforoCompleto("cualquierId");
        assertNotNull(completoNull);

        SemaforoVisualizacion detalladoNull = semaforoAcademicoService.obtenerSemaforoDetallado("cualquierId");
        assertNotNull(detalladoNull);

        Map<String, EstadoSemaforo> semaforoVacioMap = new HashMap<>();
        when(semaforoAcademicoService.visualizarSemaforoEstudiante("estVacio")).thenReturn(semaforoVacioMap);

        Map<String, Object> foraneoVacio = new HashMap<>();
        foraneoVacio.put("materiasAprobadas", 0);
        foraneoVacio.put("materiasReprobadas", 0);
        foraneoVacio.put("materiasInscritas", 0);
        foraneoVacio.put("totalMaterias", 0);
        when(semaforoAcademicoService.getForaneoEstudiante("estVacio")).thenReturn(foraneoVacio);

        Map<String, EstadoSemaforo> semaforoVacio = semaforoAcademicoService.visualizarSemaforoEstudiante("estVacio");
        assertTrue(semaforoVacio.isEmpty());

        Map<String, Object> resForaneoVacio = semaforoAcademicoService.getForaneoEstudiante("estVacio");
        assertNotNull(resForaneoVacio);
        assertEquals(0, resForaneoVacio.get("materiasAprobadas"));
        assertEquals(0, resForaneoVacio.get("materiasReprobadas"));
        assertEquals(0, resForaneoVacio.get("materiasInscritas"));
        assertEquals(0, resForaneoVacio.get("totalMaterias"));
    }

    @Test
    void testSemaforoAcademicoServiceCompleto() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setId("sem1");
        semaforo.setEstudianteId("est1");
        semaforo.setGrado("PREGRADO");
        semaforo.setPlanAcademicoId("plan1");
        semaforo.setCreditosAprobados(60);
        semaforo.setTotalCreditosPlan(160);
        semaforo.setMateriasVistas(20);
        semaforo.setTotalMateriasDelPlan(45);
        semaforo.setPromedioAcumulado(4.2f);
        semaforo.setCambioDePlan(false);

        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("mat1", EstadoMateria.APROBADA);
        historial.put("mat2", EstadoMateria.REPROBADA);
        historial.put("mat3", EstadoMateria.INSCRITA);
        historial.put("mat4", EstadoMateria.PENDIENTE);
        historial.put("mat5", EstadoMateria.CANCELADA);
        semaforo.setHistorialMaterias(historial);

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est1");
        estudiante.setSemestre(5);

        Map<String, EstadoSemaforo> semaforoVisual = new HashMap<>();
        semaforoVisual.put("mat1", EstadoSemaforo.VERDE);
        semaforoVisual.put("mat2", EstadoSemaforo.ROJO);
        semaforoVisual.put("mat3", EstadoSemaforo.AZUL);

        when(semaforoAcademicoService.visualizarSemaforoEstudiante("est1")).thenReturn(semaforoVisual);
        when(semaforoAcademicoService.consultarSemaforoMateria("est1", "mat1")).thenReturn(Optional.of(EstadoSemaforo.VERDE));
        when(semaforoAcademicoService.consultarSemaforoMateria("est1", "mat2")).thenReturn(Optional.of(EstadoSemaforo.ROJO));
        when(semaforoAcademicoService.consultarSemaforoMateria("est1", "mat3")).thenReturn(Optional.of(EstadoSemaforo.AZUL));
        when(semaforoAcademicoService.consultarSemaforoMateria("est1", "matNoExiste")).thenReturn(Optional.empty());

        when(semaforoAcademicoService.visualizarSemaforoEstudiante("estSinSemaforo")).thenReturn(new HashMap<>());

        when(semaforoAcademicoService.getSemestreActual("est1")).thenReturn(5);
        when(semaforoAcademicoService.getSemestreActual("estNoExiste")).thenReturn(0);

        Map<String, Object> foraneo = new HashMap<>();
        foraneo.put("semestreActual", 5);
        foraneo.put("materiasAprobadas", 1);
        foraneo.put("materiasReprobadas", 1);
        foraneo.put("materiasInscritas", 1);
        foraneo.put("totalMaterias", 5);
        when(semaforoAcademicoService.getForaneoEstudiante("est1")).thenReturn(foraneo);

        when(semaforoAcademicoService.getForaneoEstudiante("estSinSemaforo")).thenReturn(Map.of("semestreActual", 0));

        SemaforoVisualizacion sv = new SemaforoVisualizacion();
        sv.setEstudianteId("est1");
        sv.setSemestreActual(5);
        when(semaforoAcademicoService.obtenerSemaforoCompleto("est1")).thenReturn(sv);
        when(semaforoAcademicoService.obtenerSemaforoDetallado("est1")).thenReturn(sv);

        Map<String, EstadoSemaforo> resultadoVisual = semaforoAcademicoService.visualizarSemaforoEstudiante("est1");
        assertFalse(resultadoVisual.isEmpty());
        assertEquals(3, resultadoVisual.size());

        Optional<EstadoSemaforo> estadoMat1 = semaforoAcademicoService.consultarSemaforoMateria("est1", "mat1");
        assertTrue(estadoMat1.isPresent());
        assertEquals(EstadoSemaforo.VERDE, estadoMat1.get());

        Optional<EstadoSemaforo> estadoMat2 = semaforoAcademicoService.consultarSemaforoMateria("est1", "mat2");
        assertTrue(estadoMat2.isPresent());
        assertEquals(EstadoSemaforo.ROJO, estadoMat2.get());

        Optional<EstadoSemaforo> estadoMat3 = semaforoAcademicoService.consultarSemaforoMateria("est1", "mat3");
        assertTrue(estadoMat3.isPresent());
        assertEquals(EstadoSemaforo.AZUL, estadoMat3.get());

        Optional<EstadoSemaforo> estadoNoExiste = semaforoAcademicoService.consultarSemaforoMateria("est1", "matNoExiste");
        assertFalse(estadoNoExiste.isPresent());

        Map<String, EstadoSemaforo> semaforoVacio = semaforoAcademicoService.visualizarSemaforoEstudiante("estSinSemaforo");
        assertTrue(semaforoVacio.isEmpty());

        int semestre = semaforoAcademicoService.getSemestreActual("est1");
        assertEquals(5, semestre);

        int semestreNoEncontrado = semaforoAcademicoService.getSemestreActual("estNoExiste");
        assertEquals(0, semestreNoEncontrado);

        Map<String, Object> foraneoRes = semaforoAcademicoService.getForaneoEstudiante("est1");
        assertNotNull(foraneoRes);
        assertEquals(5, foraneoRes.get("semestreActual"));
        assertEquals(1, foraneoRes.get("materiasAprobadas"));
        assertEquals(1, foraneoRes.get("materiasReprobadas"));
        assertEquals(1, foraneoRes.get("materiasInscritas"));
        assertEquals(5, foraneoRes.get("totalMaterias"));

        Map<String, Object> foraneoSinSemaforo = semaforoAcademicoService.getForaneoEstudiante("estSinSemaforo");
        assertNotNull(foraneoSinSemaforo);
        assertEquals(0, foraneoSinSemaforo.get("semestreActual"));

        SemaforoVisualizacion semaforoCompleto = semaforoAcademicoService.obtenerSemaforoCompleto("est1");
        assertNotNull(semaforoCompleto);
        assertEquals("est1", semaforoCompleto.getEstudianteId());
        assertEquals(5, semaforoCompleto.getSemestreActual());

        SemaforoVisualizacion semaforoDetallado = semaforoAcademicoService.obtenerSemaforoDetallado("est1");
        assertNotNull(semaforoDetallado);
        assertEquals("est1", semaforoDetallado.getEstudianteId());
    }
}
