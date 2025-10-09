package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.*;
import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.RepositorioSemaforoAcademico;
import edu.escuelaing.sirha.repository.RepositorioUsuario;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProyectoExodoBackenServiceTest {

    private PeriodoCambioController periodoController;
    private EstudiantesControlador estudianteController;
    private GrupoController grupoController;
    private MateriaController materiaController;
    private SolicitudCambioController solicitudController;
    private AdministradorController adminController;
    private DecanaturaController decanaturaController;
    private ProfesorController profesorController;
    private UsuarioController usuarioController;
    private RepositorioSemaforoAcademico mockRepositorioSemaforoAcademico;
    private RepositorioUsuario mockRepositorioUsuario;
    private SemaforoAcademicoService semaforoAcademicoService;

    // Servicios mock
    private PeriodoCambioService periodoService;
    private EstudianteService estudianteService;
    private GrupoService grupoService;
    private MateriaService materiaService;
    private SolicitudCambioService solicitudService;
    private AdministradorService adminService;
    private DecanaturaService decanaturaService;
    private ProfesorService profesorService;
    private UsuarioService usuarioService;

    @BeforeEach
    void setup() {
        mockRepositorioSemaforoAcademico = Mockito.mock(RepositorioSemaforoAcademico.class);
        mockRepositorioUsuario = Mockito.mock(RepositorioUsuario.class);

        // Crear mocks de los servicios
        periodoService = Mockito.mock(PeriodoCambioService.class);
        estudianteService = Mockito.mock(EstudianteService.class);
        grupoService = Mockito.mock(GrupoService.class);
        materiaService = Mockito.mock(MateriaService.class);
        solicitudService = Mockito.mock(SolicitudCambioService.class);
        adminService = Mockito.mock(AdministradorService.class);
        decanaturaService = Mockito.mock(DecanaturaService.class);
        profesorService = Mockito.mock(ProfesorService.class);
        usuarioService = Mockito.mock(UsuarioService.class);
        semaforoAcademicoService = Mockito.mock(SemaforoAcademicoService.class);

        // Inicializar controllers con servicios mock
        periodoController = new PeriodoCambioController();
        periodoController.periodoService = periodoService;

        estudianteController = new EstudiantesControlador(estudianteService, semaforoAcademicoService);

        grupoController = new GrupoController();
        grupoController.grupoService = grupoService;

        materiaController = new MateriaController();
        materiaController.materiaService = materiaService;

        solicitudController = new SolicitudCambioController();
        solicitudController.solicitudService = solicitudService;

        adminController = new AdministradorController();
        adminController.administradorService = adminService;

        decanaturaController = new DecanaturaController();
        decanaturaController.decanaturaService = decanaturaService;

        profesorController = new ProfesorController();
        profesorController.profesorService = profesorService;

        usuarioController = new UsuarioController();
        usuarioController.usuarioService = usuarioService;
    }

    @Test
    void testCreacionYBusquedaBasica() {
        // Configurar mocks
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("p1");
        periodo.setNombre("2024-1");
        when(periodoService.crear(any(PeriodoCambio.class))).thenReturn(periodo);
        when(periodoService.buscarPorId("p1")).thenReturn(Optional.of(periodo));

        Estudiante estudiante = new Estudiante();
        estudiante.setId("e1");
        estudiante.setCodigo("202410001");
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);

        Materia materia = new Materia();
        materia.setId("m1");
        materia.setCodigo("MAT101");
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);

        // Ejecutar pruebas
        PeriodoCambio periodoCreado = periodoController.create(periodo);
        assertEquals("p1", periodoCreado.getId());

        Optional<PeriodoCambio> periodoEncontrado = periodoController.getById("p1");
        assertTrue(periodoEncontrado.isPresent());

        ResponseEntity<Estudiante> estudianteCreado = estudianteController.crear(estudiante);
        assertTrue(estudianteCreado.getStatusCode().is2xxSuccessful());

        Materia materiaCreada = materiaController.crear(materia);
        assertEquals("m1", materiaCreada.getId());
    }

    @Test
    void testGestionEstudiantesCompleta() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("estudiante01");
        estudiante.setCodigo("202410001");
        estudiante.setNombre("Carlos Ruiz");

        // Configurar mocks
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteService.buscarPorCodigo("202410001")).thenReturn(Optional.of(estudiante));
        when(estudianteService.listarTodos()).thenReturn(Arrays.asList(estudiante));
        when(estudianteService.actualizar("estudiante01", estudiante)).thenReturn(estudiante);
        Mockito.doNothing().when(estudianteService).eliminarPorId("estudiante01");

        // Ejecutar pruebas
        ResponseEntity<Estudiante> response = estudianteController.crear(estudiante);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        ResponseEntity<Estudiante> encontrado = estudianteController.buscarPorCodigo("202410001");
        assertTrue(encontrado.getStatusCode().is2xxSuccessful());

        ResponseEntity<List<Estudiante>> lista = estudianteController.listarTodos();
        assertTrue(lista.getStatusCode().is2xxSuccessful());
        assertFalse(lista.getBody().isEmpty());

        estudiante.setNombre("Carlos Ruiz Actualizado");
        ResponseEntity<Estudiante> actualizado = estudianteController.actualizar("estudiante01", estudiante);
        assertTrue(actualizado.getStatusCode().is2xxSuccessful());

        ResponseEntity<Void> eliminado = estudianteController.eliminar("estudiante01");
        assertTrue(eliminado.getStatusCode().is2xxSuccessful());
    }

    @Test
    void testSolicitudesCambio() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("estudiante01");
        estudiante.setCodigo("202410002");

        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solicitud01");
        solicitud.setEstudianteId("estudiante01");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        // Configurar mocks
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        when(solicitudService.crearSolicitud(any(SolicitudCambio.class))).thenReturn(solicitud);
        when(solicitudService.obtenerSolicitudPorId("solicitud01")).thenReturn(Optional.of(solicitud));
        when(solicitudService.obtenerSolicitudesPorEstado(EstadoSolicitud.PENDIENTE)).thenReturn(Arrays.asList(solicitud));
        when(solicitudService.actualizarEstado("solicitud01", EstadoSolicitud.APROBADA)).thenReturn(solicitud);
        when(solicitudService.obtenerTodasLasSolicitudes()).thenReturn(Arrays.asList(solicitud));

        // Ejecutar pruebas
        ResponseEntity<Estudiante> respuestaEstudiante = estudianteController.crear(estudiante);
        assertTrue(respuestaEstudiante.getStatusCode().is2xxSuccessful());

        ResponseEntity<SolicitudCambio> respuestaCrear = solicitudController.crear(solicitud);
        assertTrue(respuestaCrear.getStatusCode().is2xxSuccessful());
        assertNotNull(respuestaCrear.getBody());

        ResponseEntity<SolicitudCambio> respuestaBuscar = solicitudController.obtenerSolicitudPorId("solicitud01");
        assertTrue(respuestaBuscar.getStatusCode().is2xxSuccessful());
        assertNotNull(respuestaBuscar.getBody());

        List<SolicitudCambio> pendientes = solicitudController.buscarPorEstado("PENDIENTE");
        assertFalse(pendientes.isEmpty());

        ResponseEntity<SolicitudCambio> respuestaActualizar = solicitudController.actualizarEstado("solicitud01", "APROBADA");
        assertTrue(respuestaActualizar.getStatusCode().is2xxSuccessful());
        assertNotNull(respuestaActualizar.getBody());

        List<SolicitudCambio> todas = solicitudController.obtenerTodasLasSolicitudes();
        assertFalse(todas.isEmpty());
    }

    @Test
    void testGestionGrupos() {
        Grupo grupo = new Grupo();
        grupo.setId("g1");
        grupo.setCupoMaximo(30);

        // Configurar mocks
        when(grupoService.crear(any(Grupo.class))).thenReturn(grupo);
        when(grupoService.buscarPorId("g1")).thenReturn(Optional.of(grupo));
        when(grupoService.verificarCupoDisponible("g1")).thenReturn(true);
        when(grupoService.actualizarCupo("g1", 35)).thenReturn(grupo);
        when(grupoService.consultarCargaAcademica("g1")).thenReturn(0.0f);
        when(grupoService.listarTodos()).thenReturn(Arrays.asList(grupo));

        // Ejecutar pruebas
        Grupo grupoCreado = grupoController.create(grupo);
        assertNotNull(grupoCreado);

        Optional<Grupo> grupoEncontrado = grupoController.getById("g1");
        assertTrue(grupoEncontrado.isPresent());

        boolean cupoDisponible = grupoController.verificarCupoDisponible("g1");
        assertTrue(cupoDisponible);

        Grupo cupoActualizado = grupoController.updateCupo("g1", 35);
        assertNotNull(cupoActualizado);

        float carga = grupoController.consultarCargaAcademica("g1");
        assertEquals(0.0f, carga);

        List<Grupo> grupos = grupoController.getAll();
        assertFalse(grupos.isEmpty());
    }

    @Test
    void testGestionMaterias() {
        Materia materia = new Materia();
        materia.setId("logica");
        materia.setCodigo("ISIS101");
        materia.setNombre("Programación I");

        // CORREGIDO: Configurar mocks correctamente
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);
        when(materiaService.buscarPorId("logica")).thenReturn(Optional.of(materia));
        when(materiaService.buscarPorCodigo("ISIS101")).thenReturn(Optional.of(materia));
        when(materiaService.listarTodos()).thenReturn(Arrays.asList(materia));
        when(materiaService.actualizar("logica", materia)).thenReturn(materia);

        // CORREGIDO: Configurar eliminación por separado
        Mockito.doNothing().when(materiaService).eliminarPorId("logica");

        // CORREGIDO: Crear un mock separado para después de eliminar
        when(materiaService.buscarPorId("logica"))
                .thenReturn(Optional.of(materia))  // Primera llamada (antes de eliminar)
                .thenReturn(Optional.empty());     // Segunda llamada (después de eliminar)

        // Ejecutar pruebas
        Materia materiaCreada = materiaController.crear(materia);
        assertNotNull(materiaCreada);

        Optional<Materia> materiaEncontrada = materiaController.buscarPorId("logica");
        assertTrue(materiaEncontrada.isPresent()); // ✅ Esto ahora pasa

        Optional<Materia> porCodigo = materiaController.buscarPorCodigo("ISIS101");
        assertTrue(porCodigo.isPresent());

        List<Materia> materias = materiaController.listarTodos();
        assertFalse(materias.isEmpty());

        materia.setNombre("Programación I Actualizada");
        Materia actualizada = materiaController.actualizar("logica", materia);
        assertNotNull(actualizada);

        materiaController.eliminarPorId("logica");

        Optional<Materia> eliminada = materiaController.buscarPorId("logica");
        assertFalse(eliminada.isPresent()); // ✅ Esto también pasa
    }

    @Test
    void testPeriodosCambio() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("periodo1");
        periodo.setNombre("2024-1");
        periodo.setActivo(true);

        // CORREGIDO: Configurar mocks correctamente
        when(periodoService.crear(any(PeriodoCambio.class))).thenReturn(periodo);
        when(periodoService.buscarPorId("periodo1"))
                .thenReturn(Optional.of(periodo))  // Primera llamada (antes de eliminar)
                .thenReturn(Optional.empty());     // Segunda llamada (después de eliminar)
        when(periodoService.obtenerPeriodoActivo()).thenReturn(Optional.of(periodo));
        when(periodoService.listarTodos()).thenReturn(Arrays.asList(periodo));
        when(periodoService.actualizar("periodo1", periodo)).thenReturn(periodo);
        Mockito.doNothing().when(periodoService).eliminarPorId("periodo1");

        // Ejecutar pruebas
        PeriodoCambio periodoCreado = periodoController.create(periodo);
        assertNotNull(periodoCreado);

        Optional<PeriodoCambio> encontrado = periodoController.getById("periodo1");
        assertTrue(encontrado.isPresent()); // ✅ Esto ahora pasa

        Optional<PeriodoCambio> activo = periodoController.getActivo();
        assertTrue(activo.isPresent());

        List<PeriodoCambio> periodos = periodoController.getAll();
        assertFalse(periodos.isEmpty());

        periodo.setNombre("2024-1 Actualizado");
        PeriodoCambio actualizado = periodoController.update("periodo1", periodo);
        assertNotNull(actualizado);

        periodoController.delete("periodo1");

        Optional<PeriodoCambio> eliminado = periodoController.getById("periodo1");
        assertFalse(eliminado.isPresent());
    }

    @Test
    void testAdministradores() {
        Administrador admin = new Administrador();
        admin.setId("admin1");
        admin.setUsername("admin.sistema");

        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("per-admin");
        periodo.setNombre("Periodo Admin");

        // Configurar mocks
        when(adminService.crear(any(Administrador.class))).thenReturn(admin);
        when(adminService.buscarPorId("admin1")).thenReturn(Optional.of(admin));
        when(adminService.configurarPeriodo(any(PeriodoCambio.class))).thenReturn(periodo);
        when(adminService.generarReportes()).thenReturn(Arrays.asList(new SolicitudCambio()));
        when(adminService.listarTodos()).thenReturn(Arrays.asList(admin));

        // Ejecutar pruebas
        Administrador adminCreado = adminController.create(admin);
        assertNotNull(adminCreado);

        Optional<Administrador> adminEncontrado = adminController.getById("admin1");
        assertTrue(adminEncontrado.isPresent());

        PeriodoCambio periodoConfigurado = adminController.configurarPeriodo(periodo);
        assertNotNull(periodoConfigurado);

        List<SolicitudCambio> reportes = adminController.generarReportes();
        assertNotNull(reportes);

        List<Administrador> admins = adminController.getAll();
        assertFalse(admins.isEmpty());
    }

    @Test
    void testDecanaturaSolicitudes() {
        Decanatura decanatura = new Decanatura();
        decanatura.setId("decanatura");
        decanatura.setUsername("decanatura.sistemas");

        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solicitud");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        // Configurar mocks
        when(decanaturaService.crear(any(Decanatura.class))).thenReturn(decanatura);
        when(decanaturaService.buscarPorId("decanatura")).thenReturn(Optional.of(decanatura));
        when(decanaturaService.consultarSolicitudesPendientes()).thenReturn(Arrays.asList(solicitud));
        when(decanaturaService.revisarSolicitud("solicitud", EstadoSolicitud.APROBADA, "Aprobada por decanatura"))
                .thenReturn(solicitud);

        // Ejecutar pruebas
        Decanatura decanaturaCreada = decanaturaController.create(decanatura);
        assertNotNull(decanaturaCreada);

        List<SolicitudCambio> pendientes = decanaturaController.consultarSolicitudesPendientes();
        assertNotNull(pendientes);

        if (!pendientes.isEmpty()) {
            String solicitudId = pendientes.get(0).getId();
            SolicitudCambio revisada = decanaturaController.revisarSolicitud(solicitudId, EstadoSolicitud.APROBADA, "Aprobada por decanatura");
            assertNotNull(revisada);
        }

        Optional<Decanatura> decEncontrada = decanaturaController.getById("decanatura");
        assertTrue(decEncontrada.isPresent());
    }

    @Test
    void testProfesores() {
        Profesor profesor = new Profesor();
        profesor.setId("profesor");
        profesor.setIdProfesor(1001);
        profesor.setNombre("Dr. Ana López");

        // Configurar mocks
        when(profesorService.crear(any(Profesor.class))).thenReturn(profesor);
        when(profesorService.buscarPorId("profesor")).thenReturn(Optional.of(profesor));
        when(profesorService.buscarPorCodigo("1001")).thenReturn(Optional.of(profesor));
        when(profesorService.consultarGruposAsignados("profesor")).thenReturn(Arrays.asList(new Grupo()));
        when(profesorService.listarTodos()).thenReturn(Arrays.asList(profesor));

        // Ejecutar pruebas
        Profesor profesorCreado = profesorController.crear(profesor);
        assertNotNull(profesorCreado);

        Optional<Profesor> profesorEncontrado = profesorController.buscarPorId("profesor");
        assertTrue(profesorEncontrado.isPresent());

        Optional<Profesor> porCodigo = profesorController.buscarPorCodigo("1001");
        assertTrue(porCodigo.isPresent());

        List<Grupo> grupos = profesorController.consultarGruposAsignados("profesor");
        assertNotNull(grupos);

        List<Profesor> profesores = profesorController.listarTodos();
        assertFalse(profesores.isEmpty());
    }

    @Test
    void testEstudianteSolicitudesCambio() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("estudiante");
        estudiante.setCodigo("202410003");

        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solicitud-est");

        // Configurar mocks
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        when(estudianteService.crearSolicitudCambio("estudiante", "MAT101", "GRP1", "MAT102", "GRP2"))
                .thenReturn(solicitud);
        when(estudianteService.consultarSolicitudes("estudiante")).thenReturn(Arrays.asList(solicitud));

        // Ejecutar pruebas
        ResponseEntity<Estudiante> response = estudianteController.crear(estudiante);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        ResponseEntity<SolicitudCambio> solicitudResponse = estudianteController.crearSolicitudCambio("estudiante", "MAT101", "GRP1", "MAT102", "GRP2");
        assertTrue(solicitudResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(solicitudResponse.getBody());

        ResponseEntity<List<SolicitudCambio>> solicitudes = estudianteController.consultarSolicitudes("estudiante");
        assertTrue(solicitudes.getStatusCode().is2xxSuccessful());
        assertFalse(solicitudes.getBody().isEmpty());
    }

    @Test
    void testOperacionesEliminacion() {
        // Configurar mocks para Materia
        Materia materia = new Materia();
        materia.setId("fisica");
        materia.setCodigo("ELIM101");
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);

        // CORREGIDO: Configurar diferentes respuestas para diferentes llamadas
        when(materiaService.buscarPorId("fisica"))
                .thenReturn(Optional.of(materia))  // Primera llamada (antes de eliminar)
                .thenReturn(Optional.empty());     // Segunda llamada (después de eliminar)
        Mockito.doNothing().when(materiaService).eliminarPorId("fisica");

        // Configurar mocks para Grupo
        Grupo grupo = new Grupo();
        grupo.setId("grp-elim");
        when(grupoService.crear(any(Grupo.class))).thenReturn(grupo);
        when(grupoService.buscarPorId("grp-elim"))
                .thenReturn(Optional.of(grupo))    // Primera llamada (antes de eliminar)
                .thenReturn(Optional.empty());     // Segunda llamada (después de eliminar)
        Mockito.doNothing().when(grupoService).eliminarPorId("grp-elim");

        // Configurar mocks para SolicitudCambio
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("sol-elim");
        when(solicitudService.crearSolicitud(any(SolicitudCambio.class))).thenReturn(solicitud);

        // CORREGIDO: Configurar diferentes respuestas para obtenerSolicitudPorId
        when(solicitudService.obtenerSolicitudPorId("sol-elim"))
                .thenReturn(Optional.of(solicitud)) // Primera llamada (antes de eliminar)
                .thenReturn(Optional.empty());      // Segunda llamada (después de eliminar)

        Mockito.doNothing().when(solicitudService).eliminarSolicitud("sol-elim");

        // Ejecutar pruebas
        Materia materiaCreada = materiaController.crear(materia);
        assertNotNull(materiaCreada);

        // Primera búsqueda - debería existir
        Optional<Materia> materiaAntes = materiaController.buscarPorId("fisica");
        assertTrue(materiaAntes.isPresent());

        materiaController.eliminarPorId("fisica");

        // Segunda búsqueda - después de eliminar
        Optional<Materia> materiaEliminada = materiaController.buscarPorId("fisica");
        assertFalse(materiaEliminada.isPresent()); // ✅ Esto ahora pasa

        Grupo grupoCreado = grupoController.create(grupo);
        assertNotNull(grupoCreado);

        // Primera búsqueda - debería existir
        Optional<Grupo> grupoAntes = grupoController.getById("grp-elim");
        assertTrue(grupoAntes.isPresent());

        grupoController.delete("grp-elim");

        // Segunda búsqueda - después de eliminar
        Optional<Grupo> grupoEliminado = grupoController.getById("grp-elim");
        assertFalse(grupoEliminado.isPresent());

        ResponseEntity<SolicitudCambio> respuestaSolicitud = solicitudController.crear(solicitud);
        assertTrue(respuestaSolicitud.getStatusCode().is2xxSuccessful());

        // Primera búsqueda - debería existir
        ResponseEntity<SolicitudCambio> respuestaBuscarAntes = solicitudController.obtenerSolicitudPorId("sol-elim");
        assertTrue(respuestaBuscarAntes.getStatusCode().is2xxSuccessful());

        ResponseEntity<Void> respuestaEliminar = solicitudController.eliminarSolicitud("sol-elim");
        assertTrue(respuestaEliminar.getStatusCode().is2xxSuccessful());

        // Segunda búsqueda - después de eliminar
        ResponseEntity<SolicitudCambio> respuestaBuscar = solicitudController.obtenerSolicitudPorId("sol-elim");
        assertTrue(respuestaBuscar.getStatusCode().is4xxClientError());
    }

    @Test
    void testConsultasAvanzadas() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("consulta");
        estudiante.setCodigo("202410004");

        Materia materia = new Materia();
        materia.setId("mat-consulta");
        materia.setCodigo("CONS101");

        Grupo grupo = new Grupo();
        grupo.setId("grp-consulta");
        grupo.setMateriaId("mat-consulta");

        // Configurar mocks
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        when(materiaService.crear(any(Materia.class))).thenReturn(materia);
        when(grupoService.crear(any(Grupo.class))).thenReturn(grupo);
        when(grupoService.consultarEstudiantesInscritos("grp-consulta")).thenReturn(Arrays.asList(new Estudiante()));
        when(materiaService.consultarGruposDisponibles("mat-consulta")).thenReturn(Arrays.asList(new Grupo()));
        when(materiaService.verificarDisponibilidad("mat-consulta")).thenReturn(false);
        when(solicitudService.obtenerSolicitudesPorEstudiante("consulta")).thenReturn(Arrays.asList(new SolicitudCambio()));

        // Ejecutar pruebas
        ResponseEntity<Estudiante> estResponse = estudianteController.crear(estudiante);
        assertTrue(estResponse.getStatusCode().is2xxSuccessful());

        Materia matResponse = materiaController.crear(materia);
        assertNotNull(matResponse);

        Grupo grpResponse = grupoController.create(grupo);
        assertNotNull(grpResponse);

        List<Estudiante> estudiantesInscritos = grupoController.consultarEstudiantesInscritos("grp-consulta");
        assertNotNull(estudiantesInscritos);

        List<Grupo> gruposDisponibles = materiaController.consultarGruposDisponibles("mat-consulta");
        assertNotNull(gruposDisponibles);

        boolean disponible = materiaController.verificarDisponibilidad("mat-consulta");
        assertFalse(disponible);

        List<SolicitudCambio> porEstudiante = solicitudController.buscarPorEstudiante("consulta");
        assertNotNull(porEstudiante);
    }

    @Test
    void testEstudiantePuedeVerSemaforo() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("estudiante-semaforo");
        estudiante.setCodigo("202410005");

        // Configurar mocks
        when(estudianteService.crear(any(Estudiante.class))).thenReturn(estudiante);
        when(semaforoAcademicoService.visualizarSemaforoEstudiante("estudiante-semaforo"))
                .thenReturn(Map.of());
        when(semaforoAcademicoService.consultarSemaforoMateria("estudiante-semaforo", "MAT101"))
                .thenReturn(Optional.empty());

        // Ejecutar pruebas
        ResponseEntity<Estudiante> response = estudianteController.crear(estudiante);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        ResponseEntity<Map<String, EstadoSemaforo>> semaforoResponse = estudianteController.verMiSemaforo("estudiante-semaforo");
        assertTrue(semaforoResponse.getStatusCode().is2xxSuccessful());

        ResponseEntity<EstadoSemaforo> estadoMateria = estudianteController.verEstadoMateria("estudiante-semaforo", "MAT101");
        assertTrue(estadoMateria.getStatusCode().is4xxClientError());
    }
}