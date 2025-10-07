package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.*;
import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoExodoBackendControllerTest {

    @Mock private AdministradorService administradorService;
    @Mock private EstudianteService estudianteService;
    @Mock private GrupoService grupoService;
    @Mock private MateriaService materiaService;
    @Mock private PeriodoCambioService periodoService;
    @Mock private ProfesorService profesorService;
    @Mock private SolicitudCambioService solicitudService;
    @Mock private UsuarioService usuarioService;
    @Mock private DecanaturaService decanaturaService;

    @InjectMocks private AdministradorController administradorController;
    @InjectMocks private EstudiantesControlador controladorEstudiantes;
    @InjectMocks private GrupoController grupoController;
    @InjectMocks private MateriaController materiaController;
    @InjectMocks private PeriodoCambioController periodoController;
    @InjectMocks private ProfesorController profesorController;
    @InjectMocks private SolicitudCambioController solicitudController;
    @InjectMocks private UsuarioController usuarioController;
    @InjectMocks private DecanaturaController decanaturaController;

    private Administrador crearAdministrador() {
        Administrador admin = new Administrador();
        admin.setId("administrador");
        admin.setUsername("admin.lopez");
        return admin;
    }

    private Estudiante crearEstudiante() {
        Estudiante est = new Estudiante();
        est.setId("estudiante");
        est.setCodigo("2020100123");
        est.setNombre("Juan Pérez");
        return est;
    }

    private Grupo crearGrupo() {
        Grupo grupo = new Grupo();
        grupo.setId("grupo1");
        grupo.setCupoMaximo(30);
        return grupo;
    }

    private Materia crearMateria() {
        Materia materia = new Materia();
        materia.setId("aysr");
        materia.setCodigo("ayrs1");
        materia.setNombre("Programación red");
        return materia;
    }

    private PeriodoCambio crearPeriodo() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("periodo2");
        periodo.setNombre("2024-1");
        return periodo;
    }

    private Profesor crearProfesor() {
        Profesor profesor = new Profesor();
        profesor.setId("profesor");
        profesor.setIdProfesor(123);
        profesor.setNombre("Dr. Roberto Martínez");
        return profesor;
    }

    private SolicitudCambio crearSolicitud() {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solicitud1");
        solicitud.setEstudianteId("estudiante");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        return solicitud;
    }

    private Usuario crearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario2");
        usuario.setUsername("carlos.mendez");
        usuario.setRol(Rol.ESTUDIANTE);
        return usuario;
    }

    private Decanatura crearDecanatura() {
        Decanatura decanatura = new Decanatura();
        decanatura.setId("sistemas");
        decanatura.setUsername("decanatura.sistemas");
        return decanatura;
    }

    @Test
    void testAdministradorController() {
        Administrador admin = crearAdministrador();
        Grupo grupo = crearGrupo();
        PeriodoCambio periodo = crearPeriodo();
        SolicitudCambio solicitud = crearSolicitud();
        when(administradorService.listarTodos()).thenReturn(Arrays.asList(admin));
        when(administradorService.buscarPorId("administrador")).thenReturn(Optional.of(admin));
        when(administradorService.crear(any(Administrador.class))).thenReturn(admin);
        when(administradorService.modificarCupoGrupo("grupo1", 35)).thenReturn(grupo);
        when(administradorService.configurarPeriodo(any(PeriodoCambio.class))).thenReturn(periodo);
        when(administradorService.generarReportes()).thenReturn(Arrays.asList(solicitud));
        List<Administrador> admins = administradorController.getAll();
        assertFalse(admins.isEmpty());
        assertEquals("administrador", admins.get(0).getId());
        Optional<Administrador> adminEncontrado = administradorController.getById("administrador");
        assertTrue(adminEncontrado.isPresent());
        Administrador adminCreado = administradorController.create(admin);
        assertNotNull(adminCreado);
        Grupo grupoActualizado = administradorController.modificarCupoGrupo("grupo1", 35);
        assertNotNull(grupoActualizado);
        PeriodoCambio periodoCreado = administradorController.configurarPeriodo(periodo);
        assertNotNull(periodoCreado);
        List<SolicitudCambio> reportes = administradorController.generarReportes();
        assertFalse(reportes.isEmpty());
    }

    @Test
    void testControladorEstudiantes() {
        Estudiante estudiante = crearEstudiante();
        SolicitudCambio solicitud = crearSolicitud();
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteService.buscarPorCodigo("2020100123")).thenReturn(Optional.of(estudiante));
        when(estudianteService.listarTodos()).thenReturn(Arrays.asList(estudiante));
        doNothing().when(estudianteService).eliminarPorId("estudiante");
        when(estudianteService.actualizar(eq("estudiante"), any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteService.crearSolicitudCambio("estudiante", "mat1", "g1", "mat2", "g2")).thenReturn(solicitud);
        when(estudianteService.consultarSolicitudes("estudiante")).thenReturn(Arrays.asList(solicitud));
        ResponseEntity<Estudiante> responseCrear = controladorEstudiantes.crear(estudiante);
        assertEquals(HttpStatus.CREATED, responseCrear.getStatusCode());
        ResponseEntity<Estudiante> responseBuscar = controladorEstudiantes.buscarPorCodigo("2020100123");
        assertEquals(HttpStatus.OK, responseBuscar.getStatusCode());
        ResponseEntity<List<Estudiante>> responseListar = controladorEstudiantes.listarTodos();
        assertEquals(HttpStatus.OK, responseListar.getStatusCode());
        ResponseEntity<Void> responseEliminar = controladorEstudiantes.eliminar("estudiante");
        assertEquals(HttpStatus.NO_CONTENT, responseEliminar.getStatusCode());
        ResponseEntity<Estudiante> responseActualizar = controladorEstudiantes.actualizar("estudiante", estudiante);
        assertEquals(HttpStatus.OK, responseActualizar.getStatusCode());
        ResponseEntity<SolicitudCambio> responseSolicitud = controladorEstudiantes.crearSolicitudCambio("estudiante", "mat1", "g1", "mat2", "g2");
        assertEquals(HttpStatus.CREATED, responseSolicitud.getStatusCode());
        ResponseEntity<List<SolicitudCambio>> responseSolicitudes = controladorEstudiantes.consultarSolicitudes("estudiante");
        assertEquals(HttpStatus.OK, responseSolicitudes.getStatusCode());
    }

    @Test
    void testGrupoController() {
        Grupo grupo = crearGrupo();
        Estudiante estudiante = crearEstudiante();
        when(grupoService.listarTodos()).thenReturn(Arrays.asList(grupo));
        when(grupoService.buscarPorId("grupo1")).thenReturn(Optional.of(grupo));
        when(grupoService.crear(any(Grupo.class))).thenReturn(grupo);
        doNothing().when(grupoService).eliminarPorId("grupo1");
        when(grupoService.actualizarCupo("grupo1", 35)).thenReturn(grupo);
        when(grupoService.verificarCupoDisponible("grupo1")).thenReturn(true);
        when(grupoService.consultarCargaAcademica("grupo1")).thenReturn(15.5f);
        when(grupoService.consultarEstudiantesInscritos("grupo1")).thenReturn(Arrays.asList(estudiante));
        List<Grupo> grupos = grupoController.getAll();
        assertFalse(grupos.isEmpty());
        Optional<Grupo> grupoEncontrado = grupoController.getById("grupo1");
        assertTrue(grupoEncontrado.isPresent());
        Grupo grupoCreado = grupoController.create(grupo);
        assertNotNull(grupoCreado);
        grupoController.delete("grupo1");
        Grupo grupoActualizado = grupoController.updateCupo("grupo1", 35);
        assertNotNull(grupoActualizado);
        boolean cupoDisponible = grupoController.verificarCupoDisponible("grupo1");
        assertTrue(cupoDisponible);
        float carga = grupoController.consultarCargaAcademica("grupo1");
        assertEquals(15.5f, carga);
        List<Estudiante> estudiantes = grupoController.consultarEstudiantesInscritos("grupo1");
        assertFalse(estudiantes.isEmpty());
    }

    @Test
    void testMateriaController() {
        Materia materia = crearMateria();
        Grupo grupo = crearGrupo();

        when(materiaService.listarTodos()).thenReturn(Arrays.asList(materia));
        when(materiaService.buscarPorId("aysr")).thenReturn(Optional.of(materia));
        when(materiaService.buscarPorCodigo("ayrs1")).thenReturn(Optional.of(materia));
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);
        when(materiaService.actualizar(eq("aysr"), any(Materia.class))).thenReturn(materia);
        doNothing().when(materiaService).eliminarPorId("aysr");
        when(materiaService.consultarGruposDisponibles("aysr")).thenReturn(Arrays.asList(grupo));
        when(materiaService.verificarDisponibilidad("aysr")).thenReturn(true);
        List<Materia> materias = materiaController.listarTodos();
        assertFalse(materias.isEmpty());
        Optional<Materia> materiaEncontrada = materiaController.buscarPorId("aysr");
        assertTrue(materiaEncontrada.isPresent());
        Optional<Materia> materiaPorCodigo = materiaController.buscarPorCodigo("ayrs1");
        assertTrue(materiaPorCodigo.isPresent());
        Materia materiaCreada = materiaController.crear(materia);
        assertNotNull(materiaCreada);
        Materia materiaActualizada = materiaController.actualizar("aysr", materia);
        assertNotNull(materiaActualizada);
        materiaController.eliminarPorId("aysr");
        List<Grupo> grupos = materiaController.consultarGruposDisponibles("aysr");
        assertFalse(grupos.isEmpty());
        boolean disponible = materiaController.verificarDisponibilidad("aysr");
        assertTrue(disponible);
    }

    @Test
    void testPeriodoCambioController() {
        PeriodoCambio periodo = crearPeriodo();
        when(periodoService.listarTodos()).thenReturn(Arrays.asList(periodo));
        when(periodoService.buscarPorId("periodo2")).thenReturn(Optional.of(periodo));
        when(periodoService.obtenerPeriodoActivo()).thenReturn(Optional.of(periodo));
        when(periodoService.crear(any(PeriodoCambio.class))).thenReturn(periodo);
        when(periodoService.actualizar(eq("periodo2"), any(PeriodoCambio.class))).thenReturn(periodo);
        doNothing().when(periodoService).eliminarPorId("periodo2");
        List<PeriodoCambio> periodos = periodoController.getAll();
        assertFalse(periodos.isEmpty());
        Optional<PeriodoCambio> periodoEncontrado = periodoController.getById("periodo2");
        assertTrue(periodoEncontrado.isPresent());
        Optional<PeriodoCambio> periodoActivo = periodoController.getActivo();
        assertTrue(periodoActivo.isPresent());
        PeriodoCambio periodoCreado = periodoController.create(periodo);
        assertNotNull(periodoCreado);
        PeriodoCambio periodoActualizado = periodoController.update("periodo2", periodo);
        assertNotNull(periodoActualizado);
        periodoController.delete("periodo2");
    }

    @Test
    void testProfesorController() {
        Profesor profesor = crearProfesor();
        Grupo grupo = crearGrupo();
        when(profesorService.listarTodos()).thenReturn(Arrays.asList(profesor));
        when(profesorService.buscarPorId("profesor")).thenReturn(Optional.of(profesor));
        when(profesorService.buscarPorCodigo("123")).thenReturn(Optional.of(profesor));
        when(profesorService.crear(any(Profesor.class))).thenReturn(profesor);
        doNothing().when(profesorService).eliminarPorId("profesor");
        when(profesorService.consultarGruposAsignados("profesor")).thenReturn(Arrays.asList(grupo));
        List<Profesor> profesores = profesorController.listarTodos();
        assertFalse(profesores.isEmpty());
        Optional<Profesor> profesorEncontrado = profesorController.buscarPorId("profesor");
        assertTrue(profesorEncontrado.isPresent());
        Optional<Profesor> profesorPorCodigo = profesorController.buscarPorCodigo("123");
        assertTrue(profesorPorCodigo.isPresent());
        Profesor profesorCreado = profesorController.crear(profesor);
        assertNotNull(profesorCreado);
        profesorController.eliminarPorId("profesor");
        List<Grupo> grupos = profesorController.consultarGruposAsignados("profesor");
        assertFalse(grupos.isEmpty());
    }

    @Test
    void testSolicitudCambioController() {
        SolicitudCambio solicitud = crearSolicitud();
        when(solicitudService.listarTodos()).thenReturn(Arrays.asList(solicitud));
        when(solicitudService.buscarPorId("solicitud1")).thenReturn(Optional.of(solicitud));
        when(solicitudService.crear(any(SolicitudCambio.class))).thenReturn(solicitud);
        when(solicitudService.actualizarEstado("solicitud1", EstadoSolicitud.APROBADA)).thenReturn(solicitud);
        doNothing().when(solicitudService).eliminarPorId("solicitud1");
        when(solicitudService.buscarPorEstado(EstadoSolicitud.PENDIENTE)).thenReturn(Arrays.asList(solicitud));
        when(solicitudService.buscarPorEstudiante("estudiante")).thenReturn(Arrays.asList(solicitud));
        List<SolicitudCambio> solicitudes = solicitudController.listarTodos();
        assertFalse(solicitudes.isEmpty());
        Optional<SolicitudCambio> solicitudEncontrada = solicitudController.buscarPorId("solicitud1");
        assertTrue(solicitudEncontrada.isPresent());
        SolicitudCambio solicitudCreada = solicitudController.crear(solicitud);
        assertNotNull(solicitudCreada);
        SolicitudCambio solicitudActualizada = solicitudController.actualizarEstado("solicitud1", "APROBADA");
        assertNotNull(solicitudActualizada);
        solicitudController.eliminarPorId("solicitud1");
        List<SolicitudCambio> solicitudesPorEstado = solicitudController.buscarPorEstado("PENDIENTE");
        assertFalse(solicitudesPorEstado.isEmpty());
        List<SolicitudCambio> solicitudesPorEstudiante = solicitudController.buscarPorEstudiante("estudiante");
        assertFalse(solicitudesPorEstudiante.isEmpty());
    }


    @Test
    void testUsuarioController() {
        Usuario usuario = crearUsuario();
        when(usuarioService.autenticar("carlos.mendez", "password123")).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).cambiarPassword("usuario2", "nuevopass");
        when(usuarioService.buscarPorUsername("carlos.mendez")).thenReturn(Optional.of(usuario));
        when(usuarioService.tienePermiso("usuario2", "crear_solicitud")).thenReturn(true);
        Optional<Usuario> usuarioAutenticado = usuarioController.login("carlos.mendez", "password123");
        assertTrue(usuarioAutenticado.isPresent());
        usuarioController.cambiarPassword("usuario2", "nuevopass");
        Optional<Usuario> usuarioEncontrado = usuarioController.buscarPorUsername("carlos.mendez");
        assertTrue(usuarioEncontrado.isPresent());
        boolean tienePermiso = usuarioController.tienePermiso("usuario2", "crear_solicitud");
        assertTrue(tienePermiso);
        verify(usuarioService, times(1)).autenticar("carlos.mendez", "password123");
        verify(usuarioService, times(1)).cambiarPassword("usuario2", "nuevopass");
        verify(usuarioService, times(1)).buscarPorUsername("carlos.mendez");
        verify(usuarioService, times(1)).tienePermiso("usuario2", "crear_solicitud");
    }

    @Test
    void testDecanaturaController() {
        Decanatura decanatura = crearDecanatura();
        SolicitudCambio solicitud = crearSolicitud();
        when(decanaturaService.listarTodos()).thenReturn(Arrays.asList(decanatura));
        when(decanaturaService.buscarPorId("sistemas")).thenReturn(Optional.of(decanatura)); // Cambiado a "sistemas"
        when(decanaturaService.crear(any(Decanatura.class))).thenReturn(decanatura);
        when(decanaturaService.consultarSolicitudesPendientes()).thenReturn(Arrays.asList(solicitud));
        when(decanaturaService.revisarSolicitud("solicitud1", EstadoSolicitud.APROBADA, "Aprobada")).thenReturn(solicitud); // Cambiado a "solicitud1"
        doNothing().when(decanaturaService).aprobarSolicitudEspecial("solicitud1"); // Cambiado a "solicitud1"
        List<Decanatura> decanaturas = decanaturaController.getAll();
        assertFalse(decanaturas.isEmpty());
        Optional<Decanatura> decanaturaEncontrada = decanaturaController.getById("sistemas"); // Cambiado a "sistemas"
        assertTrue(decanaturaEncontrada.isPresent());
        Decanatura decanaturaCreada = decanaturaController.create(decanatura);
        assertNotNull(decanaturaCreada);
        List<SolicitudCambio> solicitudesPendientes = decanaturaController.consultarSolicitudesPendientes();
        assertFalse(solicitudesPendientes.isEmpty());
        SolicitudCambio solicitudRevisada = decanaturaController.revisarSolicitud("solicitud1", EstadoSolicitud.APROBADA, "Aprobada");
        assertNotNull(solicitudRevisada);
        decanaturaController.aprobarSolicitudEspecial("solicitud1");
        verify(decanaturaService, times(1)).aprobarSolicitudEspecial("solicitud1");
    }
}