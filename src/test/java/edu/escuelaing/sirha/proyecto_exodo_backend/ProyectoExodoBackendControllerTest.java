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

    @Mock
    private EstudianteService estudianteService;
    @Mock
    private SemaforoAcademicoService semaforoAcademicoService;
    @Mock
    private SolicitudCambioService solicitudCambioService;
    @Mock
    private GrupoService grupoService;
    @Mock
    private MateriaService materiaService;
    @Mock
    private AdministradorService administradorService;
    @Mock
    private DecanaturaService decanaturaService;
    @Mock
    private HorarioService horarioService;
    @Mock
    private PeriodoCambioService periodoService;
    @Mock
    private ProfesorService profesorService;
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private EstudiantesControlador estudiantesControlador;
    @InjectMocks
    private SemaforoAcademicoController semaforoAcademicoController;

    private AdministradorController administradorController;
    private DecanaturaController decanaturaController;
    private GrupoController grupoController;
    private HorarioController horarioController;
    private MateriaController materiaController;
    private PeriodoCambioController periodoCambioController;
    private ProfesorController profesorController;
    private SolicitudCambioController solicitudCambioController;
    private UsuarioController usuarioController;

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
        administradorController = new AdministradorController();
        administradorController.administradorService = administradorService;
        administradorController.solicitudCambioService = solicitudCambioService;
        administradorController.semaforoAcademicoService = semaforoAcademicoService;
        decanaturaController = new DecanaturaController();
        decanaturaController.decanaturaService = decanaturaService;
        decanaturaController.semaforoService = semaforoAcademicoService;
        grupoController = new GrupoController();
        grupoController.grupoService = grupoService;
        horarioController = new HorarioController();
        horarioController.horarioService = horarioService;
        materiaController = new MateriaController();
        materiaController.materiaService = materiaService;
        periodoCambioController = new PeriodoCambioController();
        periodoCambioController.periodoService = periodoService;
        profesorController = new ProfesorController();
        profesorController.profesorService = profesorService;
        solicitudCambioController = new SolicitudCambioController();
        solicitudCambioController.solicitudService = solicitudCambioService;
        usuarioController = new UsuarioController();
        usuarioController.usuarioService = usuarioService;

        estudiante = new Estudiante();
        estudiante.setId("Estudiante");
        estudiante.setCodigo("1000044519");
        estudiante.setNombre("Julian Andres");
        estudiante.setCarrera("Ingeniería de Sistemas");
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
        administrador = new Administrador();
        administrador.setId("Administrador");
        decanatura = new Decanatura();
        decanatura.setId("Decanos");
        horario = new Horario();
        horario.setId("horario1");
        periodoCambio = new PeriodoCambio();
        periodoCambio.setId("periodo2025-2");
        profesor = new Profesor();
        profesor.setId("profesor");
        usuario = new Usuario();
        usuario.setId("Usuario");
        usuario.setUsername("Julian");
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
        assertEquals(0, response.getBody());
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
    void testSolicitudCambioController_EliminarSolicitud() {
        doNothing().when(solicitudCambioService).eliminarSolicitud("Solicitud");
        ResponseEntity<Void> response = solicitudCambioController.eliminarSolicitud("Solicitud");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
        Optional<Grupo> result = grupoController.getById("grupo1");
        assertTrue(result.isPresent());
        assertEquals("grupo1", result.get().getId());
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
        Grupo result = grupoController.updateCupo("grupo1", 40);
        assertNotNull(result);
    }
    @Test
    void testGrupoController_VerificarCupoDisponible() {
        when(grupoService.verificarCupoDisponible("grupo1")).thenReturn(true);
        boolean result = grupoController.verificarCupoDisponible("grupo1");
        assertTrue(result);
    }
    @Test
    void testMateriaController_Crear() {
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);
        Materia result = materiaController.crear(materia);
        assertNotNull(result);
        assertEquals("AYSR", result.getId());
    }
    @Test
    void testMateriaController_BuscarPorId() {
        when(materiaService.buscarPorId("AYSR")).thenReturn(Optional.of(materia));
        Optional<Materia> result = materiaController.buscarPorId("AYSR");
        assertTrue(result.isPresent());
        assertEquals("AYSR", result.get().getId());
    }
    @Test
    void testMateriaController_InscribirEstudianteEnGrupo() {
        when(materiaService.inscribirEstudianteEnGrupo("grupo1", "Estudiante")).thenReturn(grupo);
        Grupo result = materiaController.inscribirEstudianteEnGrupo("grupo1", "Estudiante");
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
        Optional<Administrador> result = administradorController.getById("Administrador");
        assertTrue(result.isPresent());
        assertEquals("Administrador", result.get().getId());
    }
    @Test
    void testAdministradorController_Create() {
        when(administradorService.crear(any(Administrador.class))).thenReturn(administrador);
        Administrador result = administradorController.create(administrador);
        assertNotNull(result);
        assertEquals("Administrador", result.getId());
    }
    @Test
    void testAdministradorController_ModificarCupoGrupo() {
        when(administradorService.modificarCupoGrupo("grupo1", 30)).thenReturn(grupo);
        Grupo result = administradorController.modificarCupoGrupo("grupo1", 30);
        assertNotNull(result);
        assertEquals("grupo1", result.getId());
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
        Optional<Decanatura> result = decanaturaController.getById("Decanos");
        assertTrue(result.isPresent());
        assertEquals("Decanos", result.get().getId());
    }
    @Test
    void testDecanaturaController_Create() {
        when(decanaturaService.crear(any(Decanatura.class))).thenReturn(decanatura);
        Decanatura result = decanaturaController.create(decanatura);
        assertNotNull(result);
        assertEquals("Decanos", result.getId());
    }
    @Test
    void testDecanaturaController_Actualizar() {
        when(decanaturaService.actualizar(eq("Decanos"), any(Decanatura.class))).thenReturn(decanatura);
        Decanatura result = decanaturaController.actualizar("Decanos", decanatura);
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
        SolicitudCambio result = decanaturaController.revisarSolicitud("Solicitud", EstadoSolicitud.APROBADA, "Aprobada");
        assertNotNull(result);
        assertEquals("Solicitud", result.getId());
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
        Optional<Usuario> result = usuarioController.login("Julian", "password123");
        assertTrue(result.isPresent());
        assertEquals("Julian", result.get().getUsername());
    }
    @Test
    void testUsuarioController_Login_Fallido() {
        when(usuarioService.autenticar("usuarioinexistente", "contrasena")).thenReturn(Optional.empty());
        Optional<Usuario> result = usuarioController.login("usuarioinexistente", "contrasena");
        assertFalse(result.isPresent());
    }
    @Test
    void testUsuarioController_BuscarPorUsername() {
        when(usuarioService.buscarPorUsername("Julian")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioController.buscarPorUsername("Julian");
        assertTrue(result.isPresent());
        assertEquals("Julian", result.get().getUsername());
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
        Optional<Profesor> result = profesorController.buscarPorId("profesor");
        assertTrue(result.isPresent());
        assertEquals("profesor", result.get().getId());
    }
    @Test
    void testProfesorController_Crear() {
        when(profesorService.crear(any(Profesor.class))).thenReturn(profesor);
        Profesor result = profesorController.crear(profesor);
        assertNotNull(result);
        assertEquals("profesor", result.getId());
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
        Optional<Horario> result = horarioController.buscarPorId("horario1");
        assertTrue(result.isPresent());
        assertEquals("horario1", result.get().getId());
    }
    @Test
    void testHorarioController_ConsultarHorariosPorGrupo() {
        when(horarioService.consultarHorariosPorGrupo("grupo1")).thenReturn(Collections.singletonList(horario));
        List<Horario> result = horarioController.consultarHorariosPorGrupo("grupo1");
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
    @Test
    void testAdministradorController_Crear() {
        when(administradorService.crear(any(Administrador.class))).thenReturn(administrador);
        Administrador result = administradorController.create(administrador);
        assertNotNull(result);
        assertEquals("Administrador", result.getId());
    }
    @Test
    void testEstudiantesControlador_CrearEstudiante_DatosInvalidos() {
        Estudiante estudianteInvalido = new Estudiante();
        when(estudianteService.crear(any(Estudiante.class))).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {
            estudiantesControlador.crear(estudianteInvalido);
        });
    }
    @Test
    void testSolicitudCambioController_ActualizarEstado_EstadoInvalido() {
        ResponseEntity<SolicitudCambio> response = solicitudCambioController.actualizarEstado("Solicitud", "ESTADO_INVALIDO");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    @Test
    void testGrupoController_UpdateCupo_CupoInvalido() {
        when(grupoService.actualizarCupo("grupo1", -1)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> {grupoController.updateCupo("grupo1", -1);});
    }
    @Test
    void testDecanaturaController_RevisarSolicitud_SolicitudNoExistente() {
        when(decanaturaService.revisarSolicitud("solInexistente", EstadoSolicitud.APROBADA, "Aprobada"))
                .thenThrow(new RuntimeException("Solicitud no encontrada"));
        assertThrows(RuntimeException.class, () -> {
            decanaturaController.revisarSolicitud("solInexistente", EstadoSolicitud.APROBADA, "Aprobada");});
    }
    public boolean cambiarPassword(String userId, String newPassword) {
        try {
            usuarioService.cambiarPassword(userId, newPassword);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Test
    void testUsuarioController_TienePermiso() {
        when(usuarioService.tienePermiso("Usuario", "ACCION_TEST")).thenReturn(true);
        boolean resultado = usuarioController.tienePermiso("Usuario", "ACCION_TEST");
        assertTrue(resultado);
    }
    @Test
    void testProfesorController_ActualizarProfesor() {
        when(profesorService.actualizar(eq("profesor"), any(Profesor.class))).thenReturn(profesor);
        Profesor resultado = profesorController.actualizar("profesor", profesor);
        assertNotNull(resultado);
        assertEquals("profesor", resultado.getId());
    }
}