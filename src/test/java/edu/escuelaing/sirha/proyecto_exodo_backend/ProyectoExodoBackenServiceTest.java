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

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setup() {
        mockRepositorioSemaforoAcademico = Mockito.mock(RepositorioSemaforoAcademico.class);
        mockRepositorioUsuario = Mockito.mock(RepositorioUsuario.class);

        PeriodoCambioServiceImpl periodoService = new PeriodoCambioServiceImpl();
        EstudianteServiceImpl estudianteService = new EstudianteServiceImpl();
        GrupoServiceImpl grupoService = new GrupoServiceImpl();
        MateriaServiceImpl materiaService = new MateriaServiceImpl();
        SolicitudCambioServiceImpl solicitudService = new SolicitudCambioServiceImpl();
        AdministradorServiceImpl adminService = new AdministradorServiceImpl(mockRepositorioSemaforoAcademico);
        DecanaturaServiceImpl decanaturaService = new DecanaturaServiceImpl();
        ProfesorServiceImpl profesorService = new ProfesorServiceImpl();
        UsuarioServiceImpl usuarioService = new UsuarioServiceImpl(mockRepositorioUsuario);
        semaforoAcademicoService = new SemaforoAcademicoServiceImpl(mockRepositorioSemaforoAcademico);
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
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("p1");
        periodo.setNombre("2024-1");
        PeriodoCambio periodoCreado = periodoController.create(periodo);
        assertEquals("p1", periodoCreado.getId());
        Optional<PeriodoCambio> periodoEncontrado = periodoController.getById("p1");
        assertTrue(periodoEncontrado.isPresent());
        Estudiante estudiante = new Estudiante();
        estudiante.setId("e1");
        estudiante.setCodigo("202410001");
        ResponseEntity<Estudiante> estudianteCreado = estudianteController.crear(estudiante);
        assertEquals(201, estudianteCreado.getStatusCodeValue());
        Materia materia = new Materia();
        materia.setId("m1");
        materia.setCodigo("MAT101");
        Materia materiaCreada = materiaController.crear(materia);
        assertEquals("m1", materiaCreada.getId());
    }

    @Test
    void testGestionEstudiantesCompleta() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("estudiante01");
        estudiante.setCodigo("202410001");
        estudiante.setNombre("Carlos Ruiz");
        ResponseEntity<Estudiante> response = estudianteController.crear(estudiante);
        assertEquals(201, response.getStatusCodeValue());
        ResponseEntity<Estudiante> encontrado = estudianteController.buscarPorCodigo("202410001");
        assertEquals(200, encontrado.getStatusCodeValue());
        ResponseEntity<List<Estudiante>> lista = estudianteController.listarTodos();
        assertEquals(200, lista.getStatusCodeValue());
        assertFalse(lista.getBody().isEmpty());
        estudiante.setNombre("Carlos Ruiz Actualizado");
        ResponseEntity<Estudiante> actualizado = estudianteController.actualizar("estudiante01", estudiante);
        assertEquals(200, actualizado.getStatusCodeValue());
        ResponseEntity<Void> eliminado = estudianteController.eliminar("estudiante01");
        assertEquals(204, eliminado.getStatusCodeValue());
    }

    @Test
    void testSolicitudesCambio() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("estudiante01");
        estudiante.setCodigo("202410002");
        estudianteController.crear(estudiante);
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solicitud01");
        solicitud.setEstudianteId("estudiante01");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        SolicitudCambio solicitudCreada = solicitudController.crear(solicitud);
        assertNotNull(solicitudCreada);
        Optional<SolicitudCambio> encontrada = solicitudController.buscarPorId("solicitud01");
        assertTrue(encontrada.isPresent());
        List<SolicitudCambio> pendientes = solicitudController.buscarPorEstado("PENDIENTE");
        assertFalse(pendientes.isEmpty());
        SolicitudCambio actualizada = solicitudController.actualizarEstado("solicitud01", "APROBADA");
        assertNotNull(actualizada);
        List<SolicitudCambio> todas = solicitudController.listarTodos();
        assertFalse(todas.isEmpty());
    }

    @Test
    void testGestionGrupos() {
        Grupo grupo = new Grupo();
        grupo.setId("g1");
        grupo.setCupoMaximo(30);
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
        Materia materiaCreada = materiaController.crear(materia);
        assertNotNull(materiaCreada);
        Optional<Materia> materiaEncontrada = materiaController.buscarPorId("logica");
        assertTrue(materiaEncontrada.isPresent());
        Optional<Materia> porCodigo = materiaController.buscarPorCodigo("ISIS101");
        assertTrue(porCodigo.isPresent());
        List<Materia> materias = materiaController.listarTodos();
        assertFalse(materias.isEmpty());
        materia.setNombre("Programación I Actualizada");
        Materia actualizada = materiaController.actualizar("logica", materia);
        assertNotNull(actualizada);
        materiaController.eliminarPorId("logica");
        Optional<Materia> eliminada = materiaController.buscarPorId("logica");
        assertFalse(eliminada.isPresent());
    }

    @Test
    void testPeriodosCambio() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("periodo1");
        periodo.setNombre("2024-1");
        periodo.setActivo(true);
        PeriodoCambio periodoCreado = periodoController.create(periodo);
        assertNotNull(periodoCreado);
        Optional<PeriodoCambio> encontrado = periodoController.getById("periodo1");
        assertTrue(encontrado.isPresent());
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
        Administrador adminCreado = adminController.create(admin);
        assertNotNull(adminCreado);
        Optional<Administrador> adminEncontrado = adminController.getById("admin1");
        assertTrue(adminEncontrado.isPresent());
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("per-admin");
        periodo.setNombre("Periodo Admin");
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
        Decanatura decanaturaCreada = decanaturaController.create(decanatura);
        assertNotNull(decanaturaCreada);
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("solicitud");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitudController.crear(solicitud);
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
        estudianteController.crear(estudiante);
        ResponseEntity<SolicitudCambio> solicitud = estudianteController.crearSolicitudCambio("estudiante", "MAT101", "GRP1", "MAT102", "GRP2");
        assertEquals(201, solicitud.getStatusCodeValue());
        assertNotNull(solicitud.getBody());
        ResponseEntity<List<SolicitudCambio>> solicitudes = estudianteController.consultarSolicitudes("estudiante");
        assertEquals(200, solicitudes.getStatusCodeValue());
        assertFalse(solicitudes.getBody().isEmpty());
    }
    @Test
    void testOperacionesEliminacion() {
        Materia materia = new Materia();
        materia.setId("fisica");
        materia.setCodigo("ELIM101");
        materiaController.crear(materia);
        materiaController.eliminarPorId("fisica");
        Optional<Materia> materiaEliminada = materiaController.buscarPorId("fisica");
        assertFalse(materiaEliminada.isPresent());
        Grupo grupo = new Grupo();
        grupo.setId("grp-elim");
        grupoController.create(grupo);
        grupoController.delete("grp-elim");
        Optional<Grupo> grupoEliminado = grupoController.getById("grp-elim");
        assertFalse(grupoEliminado.isPresent());
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("sol-elim");
        solicitudController.crear(solicitud);
        solicitudController.eliminarPorId("sol-elim");
        Optional<SolicitudCambio> solicitudEliminada = solicitudController.buscarPorId("sol-elim");
        assertFalse(solicitudEliminada.isPresent());
    }
    @Test
    void testConsultasAvanzadas() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("consulta");
        estudiante.setCodigo("202410004");
        estudianteController.crear(estudiante);
        Materia materia = new Materia();
        materia.setId("mat-consulta");
        materia.setCodigo("CONS101");
        materiaController.crear(materia);
        Grupo grupo = new Grupo();
        grupo.setId("grp-consulta");
        grupo.setMateriaId("mat-consulta");
        grupoController.create(grupo);
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
        estudianteController.crear(estudiante);
        ResponseEntity<Map<String, EstadoSemaforo>> semaforoResponse = estudianteController.verMiSemaforo("estudiante-semaforo");
        assertEquals(204, semaforoResponse.getStatusCodeValue());
        ResponseEntity<EstadoSemaforo> estadoMateria = estudianteController.verEstadoMateria("estudiante-semaforo", "MAT101");
        assertEquals(404, estadoMateria.getStatusCodeValue());
    }
}