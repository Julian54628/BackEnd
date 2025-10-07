package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.controller.*;
import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class ProyectoExodoBackenServiceTest {

    private PeriodoCambioController periodoController;
    private ControladorEstudiantes estudianteController;
    private GrupoController grupoController;
    private MateriaController materiaController;
    private SolicitudCambioController solicitudController;
    private AdministradorController adminController;
    private DecanaturaController decanaturaController;
    private ProfesorController profesorController;
    private UsuarioController usuarioController;

    @BeforeEach
    void setup() {
        PeriodoCambioServiceImpl periodoService = new PeriodoCambioServiceImpl();
        EstudianteServiceImpl estudianteService = new EstudianteServiceImpl();
        GrupoServiceImpl grupoService = new GrupoServiceImpl();
        MateriaServiceImpl materiaService = new MateriaServiceImpl();
        SolicitudCambioServiceImpl solicitudService = new SolicitudCambioServiceImpl();
        AdministradorServiceImpl adminService = new AdministradorServiceImpl();
        DecanaturaServiceImpl decanaturaService = new DecanaturaServiceImpl();
        ProfesorServiceImpl profesorService = new ProfesorServiceImpl();
        UsuarioServiceImpl usuarioService = new UsuarioServiceImpl();
        periodoController = new PeriodoCambioController();
        periodoController.periodoService = periodoService;
        estudianteController = new ControladorEstudiantes(estudianteService);
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
    void periodoControllerGetAll() {
        PeriodoCambio p1 = new PeriodoCambio();
        p1.setId("1");
        periodoController.create(p1);
        List<PeriodoCambio> periodos = periodoController.getAll();
        assertFalse(periodos.isEmpty());
    }
    @Test
    void periodoControllerGetById() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("p-cont");
        periodoController.create(periodo);
        Optional<PeriodoCambio> encontrado = periodoController.getById("p-cont");
        assertTrue(encontrado.isPresent());
    }
    @Test
    void periodoControllerGetActivo() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("activo");
        periodo.setActivo(true);
        periodoController.create(periodo);
        Optional<PeriodoCambio> activo = periodoController.getActivo();
        assertTrue(activo.isPresent());
    }
    @Test
    void estudianteControllerCrear() {
        Estudiante est = new Estudiante();
        est.setId("est-cont");
        est.setCodigo("202400001");
        ResponseEntity<Estudiante> respuesta = estudianteController.crear(est);
        assertEquals(201, respuesta.getStatusCodeValue());
    }
    @Test
    void estudianteControllerBuscarPorCodigo() {
        Estudiante est = new Estudiante();
        est.setId("est-buscar");
        est.setCodigo("202400002");
        estudianteController.crear(est);
        ResponseEntity<Estudiante> respuesta = estudianteController.buscarPorCodigo("202400002");
        assertEquals(200, respuesta.getStatusCodeValue());
    }
    @Test
    void estudianteControllerCrearSolicitud() {
        Estudiante est = new Estudiante();
        est.setId("est-sol-cont");
        estudianteController.crear(est);
        ResponseEntity<SolicitudCambio> respuesta = estudianteController.crearSolicitudCambio(
                "est-sol-cont", "mat1", "g1", "mat2", "g2");
        assertEquals(201, respuesta.getStatusCodeValue());
    }
    @Test
    void grupoControllerCreate() {
        Grupo grupo = new Grupo();
        grupo.setId("g-cont");
        grupo.setCupoMaximo(30);
        Grupo creado = grupoController.create(grupo);
        assertNotNull(creado);
    }
    @Test
    void grupoControllerUpdateCupo() {
        Grupo grupo = new Grupo();
        grupo.setId("g-update");
        grupo.setCupoMaximo(20);
        grupoController.create(grupo);
        Grupo actualizado = grupoController.updateCupo("g-update", 40);
        assertNotNull(actualizado);
    }
    @Test
    void materiaControllerCrear() {
        Materia materia = new Materia();
        materia.setId("m-cont");
        materia.setCodigo("CONT101");
        Materia creada = materiaController.crear(materia);
        assertNotNull(creada);
    }
    @Test
    void materiaControllerListar() {
        Materia m1 = new Materia();
        m1.setId("m1-cont");
        materiaController.crear(m1);
        List<Materia> materias = materiaController.listarTodos();
        assertFalse(materias.isEmpty());
    }
    @Test
    void solicitudControllerCrear() {
        SolicitudCambio sol = new SolicitudCambio();
        sol.setId("sol-cont");
        SolicitudCambio creada = solicitudController.crear(sol);
        assertNotNull(creada);
    }
    @Test
    void solicitudControllerListar() {
        SolicitudCambio sol = new SolicitudCambio();
        sol.setId("sol-list");
        solicitudController.crear(sol);
        List<SolicitudCambio> solicitudes = solicitudController.listarTodos();
        assertFalse(solicitudes.isEmpty());
    }
    @Test
    void adminControllerCreate() {
        Administrador admin = new Administrador();
        admin.setId("admin-cont");
        Administrador creado = adminController.create(admin);
        assertNotNull(creado);
    }
    @Test
    void decanaturaControllerCreate() {
        Decanatura dec = new Decanatura();
        dec.setId("dec-cont");
        Decanatura creada = decanaturaController.create(dec);
        assertNotNull(creada);
    }
    @Test
    void profesorControllerCrear() {
        Profesor prof = new Profesor();
        prof.setId("prof-cont");
        Profesor creado = profesorController.crear(prof);
        assertNotNull(creado);
    }
    @Test
    void usuarioControllerLogin() {
        Optional<Usuario> login = usuarioController.login("user", "pass");
        assertNotNull(login);
    }
    @Test
    void testControllersNotFound() {
        Optional<PeriodoCambio> periodo = periodoController.getById("no-existe");
        assertFalse(periodo.isPresent());
        Optional<Materia> materia = materiaController.buscarPorId("no-existe");
        assertFalse(materia.isPresent());
        ResponseEntity<Estudiante> estudiante = estudianteController.buscarPorCodigo("no-existe");
        assertEquals(404, estudiante.getStatusCodeValue());
    }
    @Test
    void testControllersEliminar() {
        Materia materia = new Materia();
        materia.setId("mat-elim");
        materiaController.crear(materia);
        materiaController.eliminarPorId("mat-elim");
        Optional<Materia> eliminada = materiaController.buscarPorId("mat-elim");
        assertFalse(eliminada.isPresent());
    }
}
