package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import edu.escuelaing.sirha.model.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProyectoExodoBackendControllerTest {

        private PeriodoCambioServiceImpl periodoService;
        private EstudianteServiceImpl estudianteService;
        private GrupoServiceImpl grupoService;
        private MateriaServiceImpl materiaService;
        private SolicitudCambioServiceImpl solicitudService;
        private AdministradorServiceImpl adminService;
        private DecanaturaServiceImpl decanaturaService;
        private ProfesorServiceImpl profesorService;
        private UsuarioServiceImpl usuarioService;

        @BeforeEach
        void setup() {
            periodoService = new PeriodoCambioServiceImpl();
            estudianteService = new EstudianteServiceImpl();
            grupoService = new GrupoServiceImpl();
            materiaService = new MateriaServiceImpl();
            solicitudService = new SolicitudCambioServiceImpl();
            adminService = new AdministradorServiceImpl();
            decanaturaService = new DecanaturaServiceImpl();
            profesorService = new ProfesorServiceImpl();
            usuarioService = new UsuarioServiceImpl();
        }
        @Test
        void periodoCrearYBuscar() {
            PeriodoCambio periodo = new PeriodoCambio();
            periodo.setId("p1");
            periodo.setNombre("Test Periodo");
            PeriodoCambio creado = periodoService.crear(periodo);
            assertNotNull(creado);
            Optional<PeriodoCambio> encontrado = periodoService.buscarPorId("p1");
            assertTrue(encontrado.isPresent());
            assertEquals("Test Periodo", encontrado.get().getNombre());
        }
        @Test
        void periodoListarTodos() {
            PeriodoCambio p1 = new PeriodoCambio();
            p1.setId("1");
            periodoService.crear(p1);
            PeriodoCambio p2 = new PeriodoCambio();
            p2.setId("2");
            periodoService.crear(p2);
            List<PeriodoCambio> periodos = periodoService.listarTodos();
            assertEquals(2, periodos.size());
        }
        @Test
        void periodoEliminar() {
            PeriodoCambio periodo = new PeriodoCambio();
            periodo.setId("eliminar");
            periodoService.crear(periodo);
            periodoService.eliminarPorId("eliminar");
            Optional<PeriodoCambio> eliminado = periodoService.buscarPorId("eliminar");
            assertFalse(eliminado.isPresent());
        }
        @Test
        void estudianteCrearYActualizar() {
            Estudiante est = new Estudiante();
            est.setId("e1");
            est.setNombre("Juan");
            Estudiante creado = estudianteService.crear(est);
            assertNotNull(creado);
            Estudiante actualizado = new Estudiante();
            actualizado.setNombre("Pedro");
            estudianteService.actualizar("e1", actualizado);
            Optional<Estudiante> encontrado = estudianteService.buscarPorId("e1");
            assertTrue(encontrado.isPresent());
            assertEquals("Pedro", encontrado.get().getNombre());
        }
        @Test
        void estudianteBuscarPorCodigo() {
            Estudiante est = new Estudiante();
            est.setId("e2");
            est.setCodigo("202410001");
            estudianteService.crear(est);
            Optional<Estudiante> encontrado = estudianteService.buscarPorCodigo("202410001");
            assertTrue(encontrado.isPresent());
        }
        @Test
        void estudianteCrearSolicitud() {
            Estudiante est = new Estudiante();
            est.setId("est-sol");
            estudianteService.crear(est);
            SolicitudCambio sol = estudianteService.crearSolicitudCambio("est-sol", "mat1", "g1", "mat2", "g2");
            assertNotNull(sol);
            assertEquals("est-sol", sol.getEstudianteId());
        }
        @Test
        void grupoCrearYVerificarCupo() {
            Grupo grupo = new Grupo();
            grupo.setId("g1");
            grupo.setCupoMaximo(25);
            grupoService.crear(grupo);
            boolean disponible = grupoService.verificarCupoDisponible("g1");
            assertTrue(disponible);
        }
        @Test
        void grupoActualizarCupo() {
            Grupo grupo = new Grupo();
            grupo.setId("g2");
            grupo.setCupoMaximo(20);
            grupoService.crear(grupo);
            Grupo actualizado = grupoService.actualizarCupo("g2", 30);
            assertNotNull(actualizado);
            assertEquals(30, actualizado.getCupoMaximo());
        }
        @Test
        void materiaCrearYBuscarPorCodigo() {
            Materia materia = new Materia();
            materia.setId("m1");
            materia.setCodigo("MAT101");
            materiaService.crear(materia);
            Optional<Materia> encontrada = materiaService.buscarPorCodigo("MAT101");
            assertTrue(encontrada.isPresent());
        }
        @Test
        void solicitudCrearYBuscarPorEstado() {
            SolicitudCambio sol = new SolicitudCambio();
            sol.setId("sol1");
            sol.setEstado(EstadoSolicitud.PENDIENTE);
            solicitudService.crear(sol);
            List<SolicitudCambio> pendientes = solicitudService.buscarPorEstado(EstadoSolicitud.PENDIENTE);
            assertFalse(pendientes.isEmpty());
        }
        @Test
        void solicitudActualizarEstado() {
            SolicitudCambio sol = new SolicitudCambio();
            sol.setId("sol2");
            sol.setEstado(EstadoSolicitud.PENDIENTE);
            solicitudService.crear(sol);
            solicitudService.actualizarEstado("sol2", EstadoSolicitud.APROBADA);
            Optional<SolicitudCambio> actualizada = solicitudService.buscarPorId("sol2");
            assertTrue(actualizada.isPresent());
            assertEquals(EstadoSolicitud.APROBADA, actualizada.get().getEstado());
        }
        @Test
        void adminCrearYListar() {
            Administrador admin = new Administrador();
            admin.setId("admin1");
            adminService.crear(admin);
            List<Administrador> admins = adminService.listarTodos();
            assertFalse(admins.isEmpty());
        }
        @Test
        void decanaturaCrearYConsultarSolicitudes() {
            Decanatura dec = new Decanatura();
            dec.setId("dec1");
            decanaturaService.crear(dec);
            List<SolicitudCambio> pendientes = decanaturaService.consultarSolicitudesPendientes();
            assertNotNull(pendientes);
        }
        @Test
        void profesorCrearYBuscarPorCodigo() {
            Profesor prof = new Profesor();
            prof.setId("prof1");
            prof.setIdProfesor(123);
            profesorService.crear(prof);
            Optional<Profesor> encontrado = profesorService.buscarPorCodigo("123");
            assertTrue(encontrado.isPresent());
        }
        @Test
        void usuarioAutenticar() {
            Usuario user = new Usuario();
            user.setUsername("test");
            user.setPasswordHash("pass");
            usuarioService.cambiarPassword("test", "pass");
            Optional<Usuario> autenticado = usuarioService.autenticar("test", "pass");
            assertNotNull(autenticado);
        }
    }