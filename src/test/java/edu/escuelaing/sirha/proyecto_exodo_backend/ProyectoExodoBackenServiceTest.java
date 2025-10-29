package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static edu.escuelaing.sirha.model.Rol.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoExodoBackenServiceTest {

    @Mock private RepositorioUsuario repositorioUsuario;
    @Mock private RepositorioEstudiante repositorioEstudiante;
    @Mock private RepositorioGrupo repositorioGrupo;
    @Mock private RepositorioMateria repositorioMateria;
    @Mock private RepositorioSolicitudCambio repositorioSolicitudCambio;
    @Mock private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    @Mock private RepositorioDecanatura repositorioDecanatura;
    @Mock private RepositorioPeriodoCambio repositorioPeriodoCambio;
    @Mock private RepositorioProfesor repositorioProfesor;
    @Mock private RepositorioAdministrador repositorioAdministrador;

    @InjectMocks private UsuarioServiceImpl usuarioService;
    @InjectMocks private EstudianteServiceImpl estudianteService;
    @InjectMocks private MateriaServiceImpl materiaService;
    @InjectMocks private GrupoServiceImpl grupoService;
    @InjectMocks private SolicitudCambioServiceImpl solicitudCambioService;
    @InjectMocks private SemaforoAcademicoServiceImpl semaforoAcademicoService;
    @InjectMocks private DecanaturaServiceImpl decanaturaService;
    @InjectMocks private AdministradorServiceImpl administradorService;
    @InjectMocks private ProfesorServiceImpl profesorService;

    private Usuario usuario;
    private Estudiante estudiante;
    private Materia materia;
    private Grupo grupo;
    private SolicitudCambio solicitudCambio;
    private SemaforoAcademico semaforoAcademico;
    private Decanatura decanatura;
    private PeriodoCambio periodoCambio;
    private Profesor profesor;
    private Administrador administrador;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("1");
        usuario.setUsername("testuser");
        usuario.setPasswordHash("password");
        usuario.setActivo(true);
        usuario.setRol(Rol.ESTUDIANTE);

        estudiante = new Estudiante();
        estudiante.setId("est1");
        estudiante.setCodigo("202412345");
        estudiante.setNombre("Juan Perez");
        estudiante.setCarrera("Ingenieria");
        estudiante.setSemestre(5);

        materia = new Materia();
        materia.setId("mat1");
        materia.setCodigo("ISIS1001");
        materia.setNombre("Programacion I");
        materia.setCreditos(4);
        materia.setFacultad("Ingenieria");

        grupo = new Grupo();
        grupo.setId("grupo1");
        grupo.setCupoMaximo(30);
        grupo.setMateriaId("mat1");
        grupo.setProfesorId("prof1");
        grupo.setEstudiantesInscritosIds(new ArrayList<>(Arrays.asList("est1", "est2")));

        solicitudCambio = new SolicitudCambio();
        solicitudCambio.setId("sol1");
        solicitudCambio.setEstudianteId("est1");
        solicitudCambio.setMateriaOrigenId("mat1");
        solicitudCambio.setMateriaDestinoId("mat2");
        solicitudCambio.setEstado(EstadoSolicitud.PENDIENTE);
        solicitudCambio.setTipoPrioridad(TipoPrioridad.NORMAL);

        semaforoAcademico = new SemaforoAcademico();
        semaforoAcademico.setId("sem1");
        semaforoAcademico.setEstudianteId("est1");
        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("mat1", EstadoMateria.APROBADA);
        historial.put("mat2", EstadoMateria.REPROBADA);
        semaforoAcademico.setHistorialMaterias(historial);
        semaforoAcademico.setPromedioAcumulado(3.8f);

        decanatura = new Decanatura();
        decanatura.setId("dec1");
        decanatura.setNombre("Decanatura Ingenieria");
        decanatura.setFacultad("Ingenieria");
        decanatura.setEsAdministrador(false);

        periodoCambio = new PeriodoCambio();
        periodoCambio.setId("per1");
        periodoCambio.setNombre("Periodo 2024-1");
        periodoCambio.setFechaInicio(new Date());
        periodoCambio.setFechaFin(new Date(System.currentTimeMillis() + 86400000));
        periodoCambio.setActivo(true);

        profesor = new Profesor();
        profesor.setId("prof1");
        profesor.setNombre("Profesor Test");
        profesor.setUsername("Sistemas");
        profesor.setActivo(true);

        administrador = new Administrador();
        administrador.setId("admin1");
        administrador.setRol(ADMIN);
    }

    @Test
    void testUsuarioServiceAutenticarExitoso() {
        when(repositorioUsuario.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.autenticar("testuser", "password");

        assertTrue(resultado.isPresent());
        assertEquals("testuser", resultado.get().getUsername());
    }

    @Test
    void testUsuarioServiceAutenticarFallido() {
        when(repositorioUsuario.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.autenticar("testuser", "wrongpass");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testUsuarioServiceCrearUsuario() {
        when(repositorioUsuario.existsByUsername("testuser")).thenReturn(false);
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
    }

    @Test
    void testUsuarioServiceCrearUsuarioUsernameExistente() {
        when(repositorioUsuario.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.crearUsuario(usuario));
    }

    @Test
    void testEstudianteServiceCrearExitoso() {
        when(repositorioEstudiante.existsByCodigo("202412345")).thenReturn(false);
        when(repositorioEstudiante.save(any(Estudiante.class))).thenReturn(estudiante);

        Estudiante resultado = estudianteService.crear(estudiante);

        assertNotNull(resultado);
        assertEquals("202412345", resultado.getCodigo());
    }

    @Test
    void testEstudianteServiceCrearCodigoExistente() {
        when(repositorioEstudiante.existsByCodigo("202412345")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> estudianteService.crear(estudiante));
    }

    @Test
    void testEstudianteServiceBuscarPorCodigo() {
        when(repositorioEstudiante.findByCodigo("202412345")).thenReturn(Optional.of(estudiante));

        Optional<Estudiante> resultado = estudianteService.buscarPorCodigo("202412345");

        assertTrue(resultado.isPresent());
        assertEquals("202412345", resultado.get().getCodigo());
    }

    @Test
    void testEstudianteServiceListarTodos() {
        when(repositorioEstudiante.findAll()).thenReturn(Arrays.asList(estudiante));

        List<Estudiante> resultado = estudianteService.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void testEstudianteServiceCrearSolicitudCambio() {
        when(repositorioEstudiante.existsById("est1")).thenReturn(true);
        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitudCambio);

        SolicitudCambio resultado = estudianteService.crearSolicitudCambio("est1", "mat1", "grupo1", "mat2", "grupo2");

        assertNotNull(resultado);
        assertEquals("est1", resultado.getEstudianteId());
    }

    @Test
    void testMateriaServiceCrearExitoso() {
        when(repositorioMateria.existsByCodigo("ISIS1001")).thenReturn(false);
        when(repositorioMateria.save(any(Materia.class))).thenReturn(materia);

        Materia resultado = materiaService.crear(materia);

        assertNotNull(resultado);
        assertEquals("ISIS1001", resultado.getCodigo());
    }

    @Test
    void testMateriaServiceBuscarPorId() {
        when(repositorioMateria.findById("mat1")).thenReturn(Optional.of(materia));

        Optional<Materia> resultado = materiaService.buscarPorId("mat1");

        assertTrue(resultado.isPresent());
        assertEquals("mat1", resultado.get().getId());
    }

    @Test
    void testMateriaServiceConsultarGruposDisponibles() {
        when(repositorioGrupo.findByMateriaId("mat1")).thenReturn(Arrays.asList(grupo));

        List<Grupo> resultado = materiaService.consultarGruposDisponibles("mat1");

        assertFalse(resultado.isEmpty());
    }

    @Test
    void testMateriaServiceInscribirEstudianteEnGrupo() {
        when(repositorioGrupo.findById("grupo1")).thenReturn(Optional.of(grupo));
        when(repositorioEstudiante.findById("est3")).thenReturn(Optional.of(estudiante));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

        Grupo resultado = materiaService.inscribirEstudianteEnGrupo("grupo1", "est3");

        assertNotNull(resultado);
        assertTrue(resultado.getEstudiantesInscritosIds().contains("est3"));
    }

    @Test
    void testGrupoServiceCrear() {
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

        Grupo resultado = grupoService.crear(grupo);

        assertNotNull(resultado);
        assertEquals("grupo1", resultado.getId());
    }

    @Test
    void testGrupoServiceVerificarCupoDisponible() {
        when(repositorioGrupo.findById("grupo1")).thenReturn(Optional.of(grupo));

        boolean resultado = grupoService.verificarCupoDisponible("grupo1");

        assertTrue(resultado);
    }

    @Test
    void testGrupoServiceConsultarCargaAcademica() {
        when(repositorioGrupo.findById("grupo1")).thenReturn(Optional.of(grupo));

        float resultado = grupoService.consultarCargaAcademica("grupo1");

        assertTrue(resultado > 0);
    }

    @Test
    void testSolicitudCambioServiceCrearExitoso() {
        when(repositorioEstudiante.existsById("est1")).thenReturn(true);
        when(repositorioMateria.existsById("mat2")).thenReturn(true);
        when(repositorioPeriodoCambio.findByActivoTrue()).thenReturn(Arrays.asList(periodoCambio));
        when(repositorioSolicitudCambio.findByEstudianteIdAndMateriaDestinoIdAndEstadoIn(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitudCambio);

        SolicitudCambio resultado = solicitudCambioService.crear(solicitudCambio);

        assertNotNull(resultado);
        assertEquals("sol1", resultado.getId());
    }

    @Test
    void testSolicitudCambioServiceAprobarSolicitud() {
        when(repositorioSolicitudCambio.findById("sol1")).thenReturn(Optional.of(solicitudCambio));
        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitudCambio);

        SolicitudCambio resultado = solicitudCambioService.aprobarSolicitud("sol1", "Justificacion");

        assertNotNull(resultado);
        assertEquals(EstadoSolicitud.APROBADA, resultado.getEstado());
    }

    @Test
    void testSolicitudCambioServiceBuscarPorEstado() {
        when(repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE)).thenReturn(Arrays.asList(solicitudCambio));

        List<SolicitudCambio> resultado = solicitudCambioService.buscarPorEstado(EstadoSolicitud.PENDIENTE);

        assertFalse(resultado.isEmpty());
    }

    @Test
    void testSemaforoAcademicoServiceVisualizarSemaforo() {
        when(repositorioSemaforoAcademico.findByEstudianteId("est1")).thenReturn(Optional.of(semaforoAcademico));

        Map<String, EstadoSemaforo> resultado = semaforoAcademicoService.visualizarSemaforoEstudiante("est1");

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void testSemaforoAcademicoServiceGetSemestreActual() {
        when(repositorioEstudiante.findById("est1")).thenReturn(Optional.of(estudiante));

        int resultado = semaforoAcademicoService.getSemestreActual("est1");

        assertEquals(5, resultado);
    }

    @Test
    void testDecanaturaServiceRevisarSolicitud() {
        when(repositorioSolicitudCambio.findById("sol1")).thenReturn(Optional.of(solicitudCambio));
        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitudCambio);

        SolicitudCambio resultado = decanaturaService.revisarSolicitud("sol1", EstadoSolicitud.APROBADA, "Aprobada");

        assertNotNull(resultado);
        assertEquals(EstadoSolicitud.APROBADA, resultado.getEstado());
    }

    @Test
    void testDecanaturaServiceOtorgarPermisos() {
        when(repositorioDecanatura.findById("dec1")).thenReturn(Optional.of(decanatura));
        when(repositorioDecanatura.save(any(Decanatura.class))).thenReturn(decanatura);

        Decanatura resultado = decanaturaService.otorgarPermisosAdministrador("dec1");

        assertNotNull(resultado);
        assertTrue(resultado.tienePermisosAdministrador());
    }

    @Test
    void testAdministradorServiceCrear() {
        when(repositorioAdministrador.save(any(Administrador.class))).thenReturn(administrador);

        Administrador resultado = administradorService.crear(administrador);

        assertNotNull(resultado);
        assertEquals("admin1", resultado.getId());
    }

    @Test
    void testAdministradorServiceModificarCupoGrupo() {
        when(repositorioGrupo.findById("grupo1")).thenReturn(Optional.of(grupo));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

        Grupo resultado = administradorService.modificarCupoGrupo("grupo1", 35);

        assertNotNull(resultado);
        assertEquals(35, resultado.getCupoMaximo());
    }

    @Test
    void testAdministradorServiceModificarCupoGrupoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> administradorService.modificarCupoGrupo("grupo1", 0));
    }

    @Test
    void testAdministradorServiceConfigurarPeriodo() {
        when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodoCambio);

        PeriodoCambio resultado = administradorService.configurarPeriodo(periodoCambio);

        assertNotNull(resultado);
    }

    @Test
    void testAdministradorServiceGenerarReportes() {
        when(repositorioSolicitudCambio.findAll()).thenReturn(Arrays.asList(solicitudCambio));

        List<SolicitudCambio> resultado = administradorService.generarReportes();

        assertFalse(resultado.isEmpty());
    }

    @Test
    void testProfesorServiceCrearExitoso() {
        when(repositorioProfesor.existsByIdProfesor(anyInt())).thenReturn(false);
        when(repositorioProfesor.save(any(Profesor.class))).thenReturn(profesor);

        Profesor resultado = profesorService.crear(profesor);

        assertNotNull(resultado);
        assertEquals("prof1", resultado.getId());
    }

    @Test
    void testProfesorServiceConsultarGruposAsignados() {
        when(repositorioGrupo.findByProfesorId("prof1")).thenReturn(Arrays.asList(grupo));

        List<Grupo> resultado = profesorService.consultarGruposAsignados("prof1");

        assertFalse(resultado.isEmpty());
    }

    @Test
    void testProfesorServiceAsignarProfesorAGrupo() {
        when(repositorioProfesor.findById("prof1")).thenReturn(Optional.of(profesor));
        when(repositorioGrupo.findById("grupo1")).thenReturn(Optional.of(grupo));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);
        when(repositorioProfesor.save(any(Profesor.class))).thenReturn(profesor);

        Grupo resultado = profesorService.asignarProfesorAGrupo("prof1", "grupo1");

        assertNotNull(resultado);
        assertEquals("prof1", resultado.getProfesorId());
    }

    @Test
    void testEstudianteServiceBuscarPorIdNoEncontrado() {
        when(repositorioEstudiante.findById("est99")).thenReturn(Optional.empty());

        Optional<Estudiante> resultado = estudianteService.buscarPorId("est99");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testMateriaServiceBuscarPorCodigoNoEncontrado() {
        when(repositorioMateria.findByCodigo("CODIGO999")).thenReturn(Optional.empty());

        Optional<Materia> resultado = materiaService.buscarPorCodigo("CODIGO999");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testGrupoServiceBuscarPorIdNoEncontrado() {
        when(repositorioGrupo.findById("grupo99")).thenReturn(Optional.empty());

        Optional<Grupo> resultado = grupoService.buscarPorId("grupo99");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testSolicitudCambioServiceBuscarPorIdNoEncontrado() {
        when(repositorioSolicitudCambio.findById("sol99")).thenReturn(Optional.empty());

        Optional<SolicitudCambio> resultado = solicitudCambioService.buscarPorId("sol99");

        assertFalse(resultado.isPresent());
    }

    @Test
    void testAdministradorServiceModificarEstadoMateriaNoEncontrado() {
        when(repositorioSemaforoAcademico.findByEstudianteId("est99")).thenReturn(Optional.empty());

        Optional<SemaforoAcademico> resultado = administradorService.modificarEstadoMateriaSemaforo(
                "est99", "mat1", EstadoMateria.APROBADA);

        assertFalse(resultado.isPresent());
    }

}