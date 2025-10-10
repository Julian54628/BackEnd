package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProyectoExodoBackenServiceTest {

    @Mock private RepositorioAdministrador repositorioAdministrador;
    @Mock private RepositorioDecanatura repositorioDecanatura;
    @Mock private RepositorioEstudiante repositorioEstudiante;
    @Mock private RepositorioGrupo repositorioGrupo;
    @Mock private RepositorioHorario repositorioHorario;
    @Mock private RepositorioMateria repositorioMateria;
    @Mock private RepositorioPeriodoCambio repositorioPeriodoCambio;
    @Mock private RepositorioPlanAcademico repositorioPlanAcademico;
    @Mock private RepositorioProfesor repositorioProfesor;
    @Mock private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    @Mock private RepositorioSolicitudCambio repositorioSolicitudCambio;
    @Mock private RepositorioUsuario repositorioUsuario;

    @InjectMocks private AdministradorServiceImpl administradorService;
    @InjectMocks private DecanaturaServiceImpl decanaturaService;
    @InjectMocks private EstudianteServiceImpl estudianteService;
    @InjectMocks private GrupoServiceImpl grupoService;
    @InjectMocks private HorarioServiceImpl horarioService;
    @InjectMocks private MateriaServiceImpl materiaService;
    @InjectMocks private PeriodoCambioServiceImpl periodoService;
    @InjectMocks private ProfesorServiceImpl profesorService;
    @InjectMocks private SemaforoAcademicoServiceImpl semaforoService;
    @InjectMocks private SolicitudCambioServiceImpl solicitudService;
    @InjectMocks private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== ADMINISTRADOR SERVICE TESTS ==========
    @Test
    void testAdministradorServiceCRUD() {
        Administrador admin = new Administrador();
        admin.setId("admin1");
        admin.setUsername("adminUser");

        when(repositorioAdministrador.save(any(Administrador.class))).thenReturn(admin);
        when(repositorioAdministrador.findById("admin1")).thenReturn(Optional.of(admin));
        when(repositorioAdministrador.findAll()).thenReturn(Arrays.asList(admin));
        doNothing().when(repositorioAdministrador).deleteById("admin1");

        Administrador creado = administradorService.crear(admin);
        assertNotNull(creado);
        assertEquals("admin1", creado.getId());

        Optional<Administrador> encontrado = administradorService.buscarPorId("admin1");
        assertTrue(encontrado.isPresent());

        List<Administrador> todos = administradorService.listarTodos();
        assertFalse(todos.isEmpty());

        // Test eliminar
        //assertDoesNotThrow(() -> administradorService.eliminarPorId("admin1"));
    }

    @Test
    void testAdministradorModificarEstadoMateriaSemaforo() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setId("semaforo1");
        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("materia1", EstadoMateria.PENDIENTE);
        semaforo.setHistorialMaterias(historial);

        when(repositorioSemaforoAcademico.findByEstudianteId("estudiante1")).thenReturn(Optional.of(semaforo));
        when(repositorioSemaforoAcademico.save(any(SemaforoAcademico.class))).thenReturn(semaforo);

        Optional<SemaforoAcademico> resultado = administradorService.modificarEstadoMateriaSemaforo(
                "estudiante1", "materia1", EstadoMateria.APROBADA);

        assertTrue(resultado.isPresent());
        assertEquals(EstadoMateria.APROBADA, resultado.get().getHistorialMaterias().get("materia1"));
    }

    // ========== DECANATURA SERVICE TESTS ==========
    @Test
    void testDecanaturaServiceCompleto() {
        // Configurar datos de prueba
        Decanatura decanatura = new Decanatura();
        decanatura.setId("dec1");
        decanatura.setNombre("Decanatura Sistemas");
        decanatura.setFacultad("Ingeniería");
        decanatura.setEsAdministrador(false);
        decanatura.setActivo(true);

        SolicitudCambio solicitud1 = new SolicitudCambio();
        solicitud1.setId("sol1");
        solicitud1.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud1.setDecanaturaId("dec1");
        solicitud1.setPrioridad(1);

        SolicitudCambio solicitud2 = new SolicitudCambio();
        solicitud2.setId("sol2");
        solicitud2.setEstado(EstadoSolicitud.APROBADA);
        solicitud2.setDecanaturaId("dec1");
        solicitud2.setPrioridad(2);

        when(repositorioDecanatura.save(any(Decanatura.class))).thenReturn(decanatura);
        when(repositorioDecanatura.findById("dec1")).thenReturn(Optional.of(decanatura));
        when(repositorioDecanatura.findAll()).thenReturn(Arrays.asList(decanatura));
        when(repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE)).thenReturn(Arrays.asList(solicitud1));
        when(repositorioSolicitudCambio.findById("sol1")).thenReturn(Optional.of(solicitud1));
        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitud1);
        when(repositorioSolicitudCambio.findAll()).thenReturn(Arrays.asList(solicitud1, solicitud2));

        Decanatura creada = decanaturaService.crear(decanatura);
        assertNotNull(creada);

        Optional<Decanatura> encontrada = decanaturaService.buscarPorId("dec1");
        assertTrue(encontrada.isPresent());

        List<Decanatura> todas = decanaturaService.listarTodos();
        assertFalse(todas.isEmpty());

        Decanatura actualizada = decanaturaService.actualizar("dec1", decanatura);
        assertNotNull(actualizada);

        when(repositorioDecanatura.findById("noExiste")).thenReturn(Optional.empty());
        Decanatura noActualizada = decanaturaService.actualizar("noExiste", decanatura);
        assertNull(noActualizada);
        assertDoesNotThrow(() -> decanaturaService.eliminarPorId("dec1"));

        List<SolicitudCambio> pendientes = decanaturaService.consultarSolicitudesPendientes();
        assertFalse(pendientes.isEmpty());

        SolicitudCambio revisada = decanaturaService.revisarSolicitud("sol1", EstadoSolicitud.APROBADA, "Aprobada");
        assertNotNull(revisada);
        assertEquals(EstadoSolicitud.APROBADA, revisada.getEstado());

        when(repositorioSolicitudCambio.findById("noExiste")).thenReturn(Optional.empty());
        SolicitudCambio noRevisada = decanaturaService.revisarSolicitud("noExiste", EstadoSolicitud.APROBADA, "Aprobada");
        assertNull(noRevisada);

        assertDoesNotThrow(() -> decanaturaService.aprobarSolicitudEspecial("sol1"));

        Decanatura conPermisos = decanaturaService.otorgarPermisosAdministrador("dec1");
        assertNotNull(conPermisos);
        assertTrue(conPermisos.isEsAdministrador());

        Decanatura sinPermisos = decanaturaService.revocarPermisosAdministrador("dec1");
        assertNotNull(sinPermisos);
        assertFalse(sinPermisos.isEsAdministrador());

        List<SolicitudCambio> porPrioridad = decanaturaService.consultarSolicitudesPorDecanaturaYPrioridad("dec1");
        assertNotNull(porPrioridad);

        List<SolicitudCambio> porFecha = decanaturaService.consultarSolicitudesPorDecanaturaYFechaLlegada("dec1");
        assertNotNull(porFecha);

        Map<String, Object> tasas = decanaturaService.consultarTasaAprobacionRechazo("dec1");
        assertNotNull(tasas);
        assertTrue(tasas.containsKey("totalSolicitudes"));
        assertTrue(tasas.containsKey("aprobadas"));
        assertTrue(tasas.containsKey("rechazadas"));
        assertTrue(tasas.containsKey("tasaAprobacion"));
        assertTrue(tasas.containsKey("tasaRechazo"));
    }

    @Test
    void testDecanaturaServiceCasosBorde() {
        when(repositorioDecanatura.findById("noExiste")).thenReturn(Optional.empty());

        Decanatura sinPermisos = decanaturaService.otorgarPermisosAdministrador("noExiste");
        assertNull(sinPermisos);

        Decanatura noRevocados = decanaturaService.revocarPermisosAdministrador("noExiste");
        assertNull(noRevocados);

        when(repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE)).thenReturn(new ArrayList<>());
        List<SolicitudCambio> pendientesVacias = decanaturaService.consultarSolicitudesPendientes();
        assertTrue(pendientesVacias.isEmpty());

        when(repositorioSolicitudCambio.findAll()).thenReturn(new ArrayList<>());
        Map<String, Object> tasasVacias = decanaturaService.consultarTasaAprobacionRechazo("dec1");
        assertNotNull(tasasVacias);
        assertEquals(0L, tasasVacias.get("totalSolicitudes"));
        assertEquals(0.0, tasasVacias.get("tasaAprobacion"));
    }

    // ========== ESTUDIANTE SERVICE TESTS ==========
    @Test
    void testEstudianteServiceCompleto() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("est1");
        estudiante.setCodigo("202410001");

        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solEst1");

        when(repositorioEstudiante.save(any(Estudiante.class))).thenReturn(estudiante);
        when(repositorioEstudiante.findByCodigo("202410001")).thenReturn(Optional.of(estudiante));
        when(repositorioEstudiante.findById("est1")).thenReturn(Optional.of(estudiante));
        when(repositorioEstudiante.findAll()).thenReturn(Arrays.asList(estudiante));

        Estudiante creado = estudianteService.crear(estudiante);
        assertNotNull(creado);

        Optional<Estudiante> porCodigo = estudianteService.buscarPorCodigo("202410001");
        assertTrue(porCodigo.isPresent());


        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitud);
        SolicitudCambio solicitudCreada = estudianteService.crearSolicitudCambio(
                "est1", "mat1", "grp1", "mat2", "grp2");
        assertNotNull(solicitudCreada);

        when(repositorioSolicitudCambio.findByEstudianteId("est1")).thenReturn(Arrays.asList(solicitud));
        List<SolicitudCambio> solicitudes = estudianteService.consultarSolicitudes("est1");
        assertFalse(solicitudes.isEmpty());
    }

    @Test
    void testGrupoServiceCompleto() {
        Grupo grupo = new Grupo();
        grupo.setId("grp1");
        grupo.setCupoMaximo(30);
        grupo.setEstudiantesInscritosIds(Arrays.asList("est1", "est2"));

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est1");
        estudiante.setNombre("Estudiante 1");

        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);
        when(repositorioGrupo.findById("grp1")).thenReturn(Optional.of(grupo));
        when(repositorioGrupo.findAll()).thenReturn(Arrays.asList(grupo));

        Grupo creado = grupoService.crear(grupo);
        assertNotNull(creado);

        Optional<Grupo> encontrado = grupoService.buscarPorId("grp1");
        assertTrue(encontrado.isPresent());

        boolean cupoDisponible = grupoService.verificarCupoDisponible("grp1");
        assertTrue(cupoDisponible);

        Grupo actualizado = grupoService.actualizarCupo("grp1", 40);
        assertNotNull(actualizado);

        float carga = grupoService.consultarCargaAcademica("grp1");
        assertTrue(carga > 0);

        List<Grupo> gruposAlerta = grupoService.obtenerGruposConAlertaCapacidad(50.0);
        assertNotNull(gruposAlerta);
    }

    // ========== HORARIO SERVICE TESTS ==========
    @Test
    void testHorarioServiceCompleto() {
        Horario horario = new Horario();
        horario.setId("hor1");
        horario.setDiaSemana("Lunes");
        horario.setHoraInicio(Time.valueOf("08:00:00"));
        horario.setHoraFin(Time.valueOf("10:00:00"));

        when(repositorioHorario.save(any(Horario.class))).thenReturn(horario);
        when(repositorioHorario.findById("hor1")).thenReturn(Optional.of(horario));
        when(repositorioHorario.findAll()).thenReturn(Arrays.asList(horario));
        when(repositorioHorario.findByGrupoId("grp1")).thenReturn(Arrays.asList(horario));

        Horario creado = horarioService.crear(horario);
        assertNotNull(creado);

        Optional<Horario> encontrado = horarioService.buscarPorId("hor1");
        assertTrue(encontrado.isPresent());

        List<Horario> horariosGrupo = horarioService.consultarHorariosPorGrupo("grp1");
        assertFalse(horariosGrupo.isEmpty());
    }

    // ========== MATERIA SERVICE TESTS ==========
    @Test
    void testMateriaServiceCompleto() {
        // Crear materia VÁLIDA
        Materia materia = new Materia();
        materia.setId("mat1");
        materia.setCodigo("MAT101");
        materia.setNombre("Matemáticas I");
        materia.setCreditos(3);
        materia.setFacultad("Ingeniería");
        materia.setEsObligatoria(true);

        Grupo grupo = new Grupo();
        grupo.setId("grpMat1");
        grupo.setCupoMaximo(30);
        grupo.setEstudiantesInscritosIds(new ArrayList<>());

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est1");
        estudiante.setCodigo("202410001");

        when(repositorioMateria.save(any(Materia.class))).thenReturn(materia);
        when(repositorioMateria.findById("mat1")).thenReturn(Optional.of(materia));
        when(repositorioMateria.findByCodigo("MAT101")).thenReturn(Optional.of(materia));
        when(repositorioMateria.findAll()).thenReturn(Arrays.asList(materia));

        when(repositorioGrupo.findByMateriaId("mat1")).thenReturn(Arrays.asList(grupo));
        when(repositorioGrupo.findById("grp1")).thenReturn(Optional.of(grupo));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);
        when(repositorioEstudiante.findById("est1")).thenReturn(Optional.of(estudiante));

        Materia creada = materiaService.crear(materia);
        assertNotNull(creada);

        Optional<Materia> porCodigo = materiaService.buscarPorCodigo("MAT101");
        assertTrue(porCodigo.isPresent());

        List<Grupo> grupos = materiaService.consultarGruposDisponibles("mat1");
        assertFalse(grupos.isEmpty());

        boolean disponible = materiaService.verificarDisponibilidad("mat1");
        assertTrue(disponible);

        Grupo grupoInscrito = materiaService.inscribirEstudianteEnGrupo("grp1", "est1");
        assertNotNull(grupoInscrito);

        Grupo grupoRetirado = materiaService.retirarEstudianteDeGrupo("grp1", "est1");
        assertNotNull(grupoRetirado);

        int totalInscritos = materiaService.consultarTotalInscritosPorMateria("mat1");
        assertTrue(totalInscritos >= 0);

        boolean asignada = materiaService.asignarMateriaAEstudiante("mat1", "est1");
        assertTrue(asignada);
        boolean retirada = materiaService.retirarMateriaDeEstudiante("mat1", "est1");
        assertTrue(retirada);
        assertDoesNotThrow(() -> materiaService.modificarCuposMateria("mat1", 40));
        List<Grupo> nuevosGrupos = Arrays.asList(grupo);
        Materia materiaConGrupos = materiaService.registrarMateriaConGrupos(materia, nuevosGrupos);
        assertNotNull(materiaConGrupos);
    }

    // ========== PERIODO CAMBIO SERVICE TESTS ==========
    @Test
    void testPeriodoCambioServiceCompleto() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("per1");
        periodo.setNombre("2024-1");
        periodo.setActivo(true);
        when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodo);
        when(repositorioPeriodoCambio.findById("per1")).thenReturn(Optional.of(periodo));
        when(repositorioPeriodoCambio.findAll()).thenReturn(Arrays.asList(periodo));
        when(repositorioPeriodoCambio.existsByActivoTrue()).thenReturn(true);
        when(repositorioPeriodoCambio.findByActivoTrue()).thenReturn(Arrays.asList(periodo));
        PeriodoCambio creado = periodoService.crear(periodo);
        assertNotNull(creado);
        Optional<PeriodoCambio> encontrado = periodoService.buscarPorId("per1");
        assertTrue(encontrado.isPresent());
        boolean activo = periodoService.estaPeriodoActivo();
        assertTrue(activo);
        Optional<PeriodoCambio> periodoActivo = periodoService.obtenerPeriodoActivo();
        assertTrue(periodoActivo.isPresent());
    }

    // ========== PROFESOR SERVICE TESTS ==========
    @Test
    void testProfesorServiceCompleto() {
        // Configurar datos de prueba
        Profesor profesor = new Profesor();
        profesor.setId("prof1");
        profesor.setIdProfesor(1001);
        profesor.setNombre("Profesor Test");
        profesor.setCorreoInstitucional("profesor@escuela.edu");
        profesor.setUsername("profesor.test");
        profesor.setPasswordHash("password123");
        profesor.setActivo(true);
        profesor.setMateriasAsignadasIds(Arrays.asList("mat1", "mat2"));
        profesor.setGruposAsignadosIds(new ArrayList<>());

        Grupo grupo = new Grupo();
        grupo.setId("grp1");
        grupo.setProfesorId(null);

        Grupo grupoConProfesor = new Grupo();
        grupoConProfesor.setId("grp2");
        grupoConProfesor.setProfesorId("prof1");

        when(repositorioProfesor.save(any(Profesor.class))).thenReturn(profesor);
        when(repositorioProfesor.findById("prof1")).thenReturn(Optional.of(profesor));
        when(repositorioProfesor.findAll()).thenReturn(Arrays.asList(profesor));
        when(repositorioGrupo.findByProfesorId("prof1")).thenReturn(Arrays.asList(grupoConProfesor));
        when(repositorioGrupo.findById("grp1")).thenReturn(Optional.of(grupo));
        when(repositorioGrupo.findById("grp2")).thenReturn(Optional.of(grupoConProfesor));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo, grupoConProfesor);
        when(repositorioProfesor.findAll()).thenReturn(Arrays.asList(profesor));

        Profesor creado = profesorService.crear(profesor);
        assertNotNull(creado);

        Optional<Profesor> encontrado = profesorService.buscarPorId("prof1");
        assertTrue(encontrado.isPresent());

        Optional<Profesor> porCodigo = profesorService.buscarPorCodigo("1001");
        assertTrue(porCodigo.isPresent());

        List<Profesor> todos = profesorService.listarTodos();
        assertFalse(todos.isEmpty());

        Profesor actualizado = profesorService.actualizar("prof1", profesor);
        assertNotNull(actualizado);

        when(repositorioProfesor.findById("noExiste")).thenReturn(Optional.empty());
        Profesor nuevoProfesor = profesorService.actualizar("noExiste", profesor);
        assertNotNull(nuevoProfesor); // Debería crear uno nuevo

        assertDoesNotThrow(() -> profesorService.eliminarPorId("prof1"));

        List<Grupo> gruposAsignados = profesorService.consultarGruposAsignados("prof1");
        assertFalse(gruposAsignados.isEmpty());

        Grupo grupoAsignado = profesorService.asignarProfesorAGrupo("prof1", "grp1");
        assertNotNull(grupoAsignado);
        assertEquals("prof1", grupoAsignado.getProfesorId());

        when(repositorioGrupo.findById("noExiste")).thenReturn(Optional.empty());
        Grupo noAsignado = profesorService.asignarProfesorAGrupo("prof1", "noExiste");
        assertNull(noAsignado);

        when(repositorioProfesor.findById("noExiste")).thenReturn(Optional.empty());
        Grupo noAsignadoProfesor = profesorService.asignarProfesorAGrupo("noExiste", "grp1");
        assertNull(noAsignadoProfesor);

        Grupo grupoRetirado = profesorService.retirarProfesorDeGrupo("grp2");
        assertNotNull(grupoRetirado);
        assertNull(grupoRetirado.getProfesorId());

        when(repositorioGrupo.findById("noExiste")).thenReturn(Optional.empty());
        Grupo noRetirado = profesorService.retirarProfesorDeGrupo("noExiste");
        assertNull(noRetirado);
    }

    @Test
    void testProfesorServiceBuscarPorCodigo() {
        Profesor profesor = new Profesor();
        profesor.setId("prof1");
        profesor.setIdProfesor(1001);
        profesor.setNombre("Profesor Test");

        when(repositorioProfesor.findAll()).thenReturn(Arrays.asList(profesor));

        Optional<Profesor> encontrado = profesorService.buscarPorCodigo("1001");
        assertTrue(encontrado.isPresent());

        Optional<Profesor> noEncontrado = profesorService.buscarPorCodigo("9999");
        assertFalse(noEncontrado.isPresent());

        Optional<Profesor> codigoNull = profesorService.buscarPorCodigo(null);
        assertFalse(codigoNull.isPresent());
    }

    // ========== SEMAFORO ACADEMICO SERVICE TESTS ==========
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

        when(repositorioSemaforoAcademico.findByEstudianteId("est1")).thenReturn(Optional.of(semaforo));
        when(repositorioEstudiante.findById("est1")).thenReturn(Optional.of(estudiante));
        when(repositorioSemaforoAcademico.save(any(SemaforoAcademico.class))).thenReturn(semaforo);

        Map<String, EstadoSemaforo> semaforoVisual = semaforoService.visualizarSemaforoEstudiante("est1");
        assertFalse(semaforoVisual.isEmpty());
        assertEquals(5, semaforoVisual.size());

        Optional<EstadoSemaforo> estadoMat1 = semaforoService.consultarSemaforoMateria("est1", "mat1");
        assertTrue(estadoMat1.isPresent());
        assertEquals(EstadoSemaforo.VERDE, estadoMat1.get());

        Optional<EstadoSemaforo> estadoMat2 = semaforoService.consultarSemaforoMateria("est1", "mat2");
        assertTrue(estadoMat2.isPresent());
        assertEquals(EstadoSemaforo.ROJO, estadoMat2.get());

        Optional<EstadoSemaforo> estadoMat3 = semaforoService.consultarSemaforoMateria("est1", "mat3");
        assertTrue(estadoMat3.isPresent());
        assertEquals(EstadoSemaforo.AZUL, estadoMat3.get());

        Optional<EstadoSemaforo> estadoNoExiste = semaforoService.consultarSemaforoMateria("est1", "matNoExiste");
        assertFalse(estadoNoExiste.isPresent());

        when(repositorioSemaforoAcademico.findByEstudianteId("estSinSemaforo")).thenReturn(Optional.empty());
        Map<String, EstadoSemaforo> semaforoVacio = semaforoService.visualizarSemaforoEstudiante("estSinSemaforo");
        assertTrue(semaforoVacio.isEmpty());

        int semestre = semaforoService.getSemestreActual("est1");
        assertEquals(5, semestre);

        when(repositorioEstudiante.findById("estNoExiste")).thenReturn(Optional.empty());
        int semestreNoEncontrado = semaforoService.getSemestreActual("estNoExiste");
        assertEquals(0, semestreNoEncontrado);

        Map<String, Object> foraneo = semaforoService.getForaneoEstudiante("est1");
        assertNotNull(foraneo);
        assertEquals(5, foraneo.get("semestreActual"));
        assertEquals(1, foraneo.get("materiasAprobadas")); // Solo mat1 está como APROBADA
        assertEquals(1, foraneo.get("materiasReprobadas")); // mat2 REPROBADA y mat5 CANCELADA
        assertEquals(1, foraneo.get("materiasInscritas")); // mat3 INSCRITA
        assertEquals(5, foraneo.get("totalMaterias"));

        Map<String, Object> foraneoSinSemaforo = semaforoService.getForaneoEstudiante("estSinSemaforo");
        assertNotNull(foraneoSinSemaforo);
        assertEquals(0, foraneoSinSemaforo.get("semestreActual"));

        SemaforoVisualizacion semaforoCompleto = semaforoService.obtenerSemaforoCompleto("est1");
        assertNotNull(semaforoCompleto);
        assertEquals("est1", semaforoCompleto.getEstudianteId());
        assertEquals(5, semaforoCompleto.getSemestreActual());

        SemaforoVisualizacion semaforoDetallado = semaforoService.obtenerSemaforoDetallado("est1");
        assertNotNull(semaforoDetallado);
        assertEquals("est1", semaforoDetallado.getEstudianteId());

        SemaforoAcademicoServiceImpl serviceSinRepositorio = new SemaforoAcademicoServiceImpl();
        Map<String, EstadoSemaforo> resultadoNull = serviceSinRepositorio.visualizarSemaforoEstudiante("est1");
        assertTrue(resultadoNull.isEmpty());

        Optional<EstadoSemaforo> estadoNull = serviceSinRepositorio.consultarSemaforoMateria("est1", "mat1");
        assertFalse(estadoNull.isPresent());

        int semestreNull = serviceSinRepositorio.getSemestreActual("est1");
        assertEquals(0, semestreNull);
    }

    @Test
    void testSemaforoAcademicoMapeoEstados() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setEstudianteId("estTest");

        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("matAprobada", EstadoMateria.APROBADA);
        historial.put("matInscrita", EstadoMateria.INSCRITA);
        historial.put("matPendiente", EstadoMateria.PENDIENTE);
        historial.put("matReprobada", EstadoMateria.REPROBADA);
        historial.put("matCancelada", EstadoMateria.CANCELADA);
        semaforo.setHistorialMaterias(historial);

        when(repositorioSemaforoAcademico.findByEstudianteId("estTest")).thenReturn(Optional.of(semaforo));

        Map<String, EstadoSemaforo> resultado = semaforoService.visualizarSemaforoEstudiante("estTest");

        assertEquals(EstadoSemaforo.VERDE, resultado.get("matAprobada"));
        assertEquals(EstadoSemaforo.AZUL, resultado.get("matInscrita"));
        assertEquals(EstadoSemaforo.AZUL, resultado.get("matPendiente"));
        assertEquals(EstadoSemaforo.ROJO, resultado.get("matReprobada"));
        assertEquals(EstadoSemaforo.ROJO, resultado.get("matCancelada"));
    }

    // NUEVA PRUEBA: Test específico para SemaforoVisualizacion
    @Test
    void testSemaforoVisualizacionCalculos() {
        // Configurar semáforo académico con datos específicos
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setId("sem2");
        semaforo.setEstudianteId("est2");
        semaforo.setCreditosAprobados(45);
        semaforo.setTotalCreditosPlan(180);
        semaforo.setPromedioAcumulado(3.8f);

        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("mat1", EstadoMateria.APROBADA);
        historial.put("mat2", EstadoMateria.APROBADA);
        historial.put("mat3", EstadoMateria.APROBADA);
        historial.put("mat4", EstadoMateria.REPROBADA);
        semaforo.setHistorialMaterias(historial);

        Estudiante estudiante = new Estudiante();
        estudiante.setId("est2");
        estudiante.setSemestre(6);

        when(repositorioSemaforoAcademico.findByEstudianteId("est2")).thenReturn(Optional.of(semaforo));
        when(repositorioEstudiante.findById("est2")).thenReturn(Optional.of(estudiante));

        Map<String, Object> foraneo = semaforoService.getForaneoEstudiante("est2");
        assertNotNull(foraneo);
        assertEquals(6, foraneo.get("semestreActual"));
        assertEquals(3, foraneo.get("materiasAprobadas"));
        assertEquals(1, foraneo.get("materiasReprobadas"));
        assertEquals(4, foraneo.get("totalMaterias"));

        SemaforoVisualizacion visualizacion = semaforoService.obtenerSemaforoCompleto("est2");
        assertNotNull(visualizacion);
        assertEquals("est2", visualizacion.getEstudianteId());
        assertEquals(6, visualizacion.getSemestreActual());

        assertTrue(visualizacion.getCreditosCompletados() >= 0);
        assertTrue(visualizacion.getCreditosFaltantes() >= 0);
        assertTrue(visualizacion.getPorcentajeProgreso() >= 0);
    }

    // NUEVA PRUEBA: Test para casos de error y borde
    @Test
    void testSemaforoAcademicoCasosBorde() {
        SemaforoAcademicoServiceImpl service = new SemaforoAcademicoServiceImpl();

        Map<String, Object> foraneoNull = service.getForaneoEstudiante("cualquierId");
        assertNotNull(foraneoNull);

        SemaforoVisualizacion completoNull = service.obtenerSemaforoCompleto("cualquierId");
        assertNotNull(completoNull);

        SemaforoVisualizacion detalladoNull = service.obtenerSemaforoDetallado("cualquierId");
        assertNotNull(detalladoNull);

        SemaforoAcademico semaforoSinHistorial = new SemaforoAcademico();
        semaforoSinHistorial.setId("semVacio");
        semaforoSinHistorial.setEstudianteId("estVacio");
        semaforoSinHistorial.setHistorialMaterias(new HashMap<>());

        Estudiante estudianteVacio = new Estudiante();
        estudianteVacio.setId("estVacio");
        estudianteVacio.setSemestre(1);

        when(repositorioSemaforoAcademico.findByEstudianteId("estVacio")).thenReturn(Optional.of(semaforoSinHistorial));
        when(repositorioEstudiante.findById("estVacio")).thenReturn(Optional.of(estudianteVacio));

        Map<String, EstadoSemaforo> semaforoVacio = semaforoService.visualizarSemaforoEstudiante("estVacio");
        assertTrue(semaforoVacio.isEmpty());

        Map<String, Object> foraneoVacio = semaforoService.getForaneoEstudiante("estVacio");
        assertNotNull(foraneoVacio);
        assertEquals(0, foraneoVacio.get("materiasAprobadas"));
        assertEquals(0, foraneoVacio.get("materiasReprobadas"));
        assertEquals(0, foraneoVacio.get("materiasInscritas"));
        assertEquals(0, foraneoVacio.get("totalMaterias"));
    }

    // ========== SOLICITUD CAMBIO SERVICE TESTS ==========
    @Test
    void testSolicitudCambioServiceCompleto() {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("sol1");
        solicitud.setEstudianteId("est1");
        solicitud.setMateriaDestinoId("mat1");
        solicitud.setGrupoDestinoId("grp1");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setDescripcion("Solicitud de cambio válida");
        solicitud.setTipoPrioridad(TipoPrioridad.NORMAL);

        Materia materia = new Materia();
        materia.setId("mat1");
        materia.setFacultad("Ingeniería");

        Decanatura decanatura = new Decanatura();
        decanatura.setId("dec1");

        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitud);
        when(repositorioSolicitudCambio.findById("sol1")).thenReturn(Optional.of(solicitud));
        when(repositorioSolicitudCambio.findAll()).thenReturn(Arrays.asList(solicitud));
        when(repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE)).thenReturn(Arrays.asList(solicitud));
        when(repositorioSolicitudCambio.findByEstudianteId("est1")).thenReturn(Arrays.asList(solicitud));
        when(repositorioMateria.findById("mat1")).thenReturn(Optional.of(materia));
        when(repositorioDecanatura.findByFacultad("Ingeniería")).thenReturn(Arrays.asList(decanatura));

        SolicitudCambio creada = solicitudService.crearSolicitud(solicitud);
        assertNotNull(creada);

        boolean valida = solicitudService.validarSolicitud(solicitud);
        assertTrue(valida);

        SolicitudCambio actualizada = solicitudService.actualizarEstadoSolicitud(
                "sol1", EstadoSolicitud.APROBADA, "Aprobada", "Justificación");
        assertNotNull(actualizada);

        Map<String, Object> estadisticas = solicitudService.obtenerEstadisticasSolicitudes();
        assertNotNull(estadisticas);

        List<SolicitudCambio> porEstado = solicitudService.obtenerSolicitudesPorEstado(EstadoSolicitud.PENDIENTE);
        assertFalse(porEstado.isEmpty());

        List<SolicitudCambio> porEstudiante = solicitudService.obtenerSolicitudesPorEstudiante("est1");
        assertFalse(porEstudiante.isEmpty());

        SolicitudCambio aprobada = solicitudService.aprobarSolicitud("sol1", "Justificación aprobación");
        assertNotNull(aprobada);

        SolicitudCambio rechazada = solicitudService.rechazarSolicitud("sol1", "Justificación rechazo");
        assertNotNull(rechazada);
    }

    // ========== USUARIO SERVICE TESTS ==========
    @Test
    void testUsuarioServiceCompleto() {
        Usuario usuario = new Usuario();
        usuario.setId("user1");
        usuario.setUsername("testUser");
        usuario.setPasswordHash("password123");
        usuario.setActivo(true);
        usuario.setRol(Rol.ESTUDIANTE);

        when(repositorioUsuario.findByUsername("testUser")).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.findById("user1")).thenReturn(Optional.of(usuario));
        when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

        Optional<Usuario> autenticado = usuarioService.autenticar("testUser", "password123");
        assertTrue(autenticado.isPresent());

        Optional<Usuario> porUsername = usuarioService.buscarPorUsername("testUser");
        assertTrue(porUsername.isPresent());

        boolean tienePermiso = usuarioService.tienePermiso("user1", "consulta");
        assertTrue(tienePermiso);

        assertDoesNotThrow(() -> usuarioService.cambiarPassword("user1", "newPassword"));
    }

    // ========== TEST CASOS BORDE Y ERROR ==========
    @Test
    void testCasosBordeYErrores() {
        when(repositorioEstudiante.findById("noExiste")).thenReturn(Optional.empty());
        Optional<Estudiante> noEncontrado = estudianteService.buscarPorId("noExiste");
        assertFalse(noEncontrado.isPresent());

        when(repositorioSemaforoAcademico.findByEstudianteId("sinDatos")).thenReturn(Optional.empty());
        Map<String, EstadoSemaforo> semaforoVacio = semaforoService.visualizarSemaforoEstudiante("sinDatos");
        assertTrue(semaforoVacio.isEmpty());

        SolicitudCambio solicitudInvalida = new SolicitudCambio();
        boolean esValida = solicitudService.validarSolicitud(solicitudInvalida);
        assertFalse(esValida);

        when(repositorioUsuario.findByUsername("userFalso")).thenReturn(Optional.empty());
        Optional<Usuario> authFallida = usuarioService.autenticar("userFalso", "pass");
        assertFalse(authFallida.isPresent());
    }
}
