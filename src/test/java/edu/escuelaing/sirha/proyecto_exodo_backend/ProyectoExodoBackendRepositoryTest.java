package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/testdb"
})
class ProyectoExodoBackendRepositoryTest {

    @Autowired
    private RepositorioAdministrador repositorioAdministrador;

    @Autowired
    private RepositorioDecanatura repositorioDecanatura;

    @Autowired
    private RepositorioEstudiante repositorioEstudiante;

    @Autowired
    private RepositorioGrupo repositorioGrupo;

    @Autowired
    private RepositorioHorario repositorioHorario;

    @Autowired
    private RepositorioMateria repositorioMateria;

    @Autowired
    private RepositorioPeriodoCambio repositorioPeriodoCambio;

    @Autowired
    private RepositorioPlanAcademico repositorioPlanAcademico;

    @Autowired
    private RepositorioProfesor repositorioProfesor;

    @Autowired
    private RepositorioSemaforoAcademico repositorioSemaforoAcademico;

    @Autowired
    private RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Test
    void testAdministradorSimple() {
        repositorioAdministrador.deleteAll();
        Administrador admin = new Administrador();
        admin.setUsername("admin1");
        admin.setCorreoInstitucional("admin1@test.com");
        admin.setActivo(true);
        repositorioAdministrador.save(admin);
        assertTrue(repositorioAdministrador.findByUsername("admin1").isPresent());
        assertTrue(repositorioAdministrador.findByCorreoInstitucional("admin1@test.com").isPresent());
        assertTrue(repositorioAdministrador.existsByUsername("admin1"));
        assertTrue(repositorioAdministrador.countByActivoTrue() >= 1);
    }
    @Test
    void testDecanaturaSimple() {
        repositorioDecanatura.deleteAll();
        Decanatura decanatura = new Decanatura();
        decanatura.setIdDecanatura(100);
        decanatura.setNombre("Decanatura Test");
        decanatura.setFacultad("Ingenieria");
        decanatura.setUsername("decana1");
        decanatura.setCorreoInstitucional("decana@test.com");
        decanatura.setActivo(true);
        repositorioDecanatura.save(decanatura);
        assertTrue(repositorioDecanatura.findByIdDecanatura(100).isPresent());
        assertFalse(repositorioDecanatura.findByFacultad("Ingenieria").isEmpty());
        assertTrue(repositorioDecanatura.findByActivoTrue().size() >= 1);
    }
    @Test
    void testEstudianteSimple() {
        repositorioEstudiante.deleteAll();
        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo("EST001");
        estudiante.setNombre("Estudiante Test");
        estudiante.setCarrera("Sistemas");
        estudiante.setSemestre(3);
        estudiante.setActivo(true);
        repositorioEstudiante.save(estudiante);
        assertTrue(repositorioEstudiante.findByCodigo("EST001").isPresent());
        assertFalse(repositorioEstudiante.findByCarrera("Sistemas").isEmpty());
        assertTrue(repositorioEstudiante.existsByCodigo("EST001"));
        assertTrue(repositorioEstudiante.countByCarrera("Sistemas") >= 1);
    }
    @Test
    void testGrupoSimple() {
        repositorioGrupo.deleteAll();
        Grupo grupo = new Grupo();
        grupo.setIdGrupo(200);
        grupo.setCupoMaximo(25);
        repositorioGrupo.save(grupo);
        assertTrue(repositorioGrupo.findByIdGrupo(200).isPresent());
        assertFalse(repositorioGrupo.findByCupoMaximo(25).isEmpty());
        assertTrue(repositorioGrupo.existsByIdGrupo(200));
    }
    @Test
    void testHorarioSimple() {
        repositorioHorario.deleteAll();
        Horario horario = new Horario();
        horario.setIdHorario(300);
        horario.setDiaSemana("Lunes");
        horario.setSalon("A101");
        repositorioHorario.save(horario);
        assertTrue(repositorioHorario.findByIdHorario(300).isPresent());
        assertFalse(repositorioHorario.findByDiaSemana("Lunes").isEmpty());
        assertTrue(repositorioHorario.existsByIdHorario(300));
    }
    @Test
    void testMateriaSimple() {
        repositorioMateria.deleteAll();
        Materia materia = new Materia();
        materia.setCodigo("MAT001");
        materia.setNombre("Matemáticas");
        materia.setFacultad("Ingenieria");
        materia.setEsObligatoria(true);
        materia.setCreditos(3);
        repositorioMateria.save(materia);
        assertTrue(repositorioMateria.findByCodigo("MAT001").isPresent());
        assertFalse(repositorioMateria.findByFacultad("Ingenieria").isEmpty());
        assertTrue(repositorioMateria.existsByCodigo("MAT001"));
        assertTrue(repositorioMateria.countByFacultad("Ingenieria") >= 1);
    }
    @Test
    void testPeriodoCambioSimple() {
        repositorioPeriodoCambio.deleteAll();
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setIdPeriodo(400);
        periodo.setNombre("Periodo Test");
        periodo.setTipo("Regular");
        periodo.setActivo(true);
        repositorioPeriodoCambio.save(periodo);
        assertTrue(repositorioPeriodoCambio.findByIdPeriodo(400).isPresent());
        assertFalse(repositorioPeriodoCambio.findByActivoTrue().isEmpty());
        assertTrue(repositorioPeriodoCambio.countByActivoTrue() >= 1);
        List<PeriodoCambio> porTipo = repositorioPeriodoCambio.findByTipo("Regular");
        assertFalse(porTipo.isEmpty());
    }
    @Test
    void testPlanAcademicoSimple() {
        repositorioPlanAcademico.deleteAll();
        PlanAcademico plan = new PlanAcademico();
        plan.setIdPlan(500);
        plan.setNombre("Plan Test");
        plan.setGrado("Pregrado");
        plan.setCreditosTotales(150);
        repositorioPlanAcademico.save(plan);
        assertTrue(repositorioPlanAcademico.findByIdPlan(500).isPresent());
        assertFalse(repositorioPlanAcademico.findByGrado("Pregrado").isEmpty());
        assertTrue(repositorioPlanAcademico.existsByNombre("Plan Test"));
    }
    @Test
    void testProfesorSimple() {
        repositorioProfesor.deleteAll();
        Profesor profesor = new Profesor();
        profesor.setIdProfesor(600);
        profesor.setNombre("Profesor Test");
        profesor.setCorreoInstitucional("profesor@test.com");
        repositorioProfesor.save(profesor);
        assertTrue(repositorioProfesor.findByIdProfesor(600).isPresent());
        assertFalse(repositorioProfesor.findByNombre("Profesor Test").isEmpty());
        assertTrue(repositorioProfesor.existsByCorreoInstitucional("profesor@test.com"));
    }
    @Test
    void testSemaforoAcademicoSimple() {
        repositorioSemaforoAcademico.deleteAll();
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setEstudianteId("EST001");
        semaforo.setGrado("Pregrado");
        semaforo.setCambioDePlan(false);
        repositorioSemaforoAcademico.save(semaforo);
        assertTrue(repositorioSemaforoAcademico.findByEstudianteId("EST001").isPresent());
        assertFalse(repositorioSemaforoAcademico.findByGrado("Pregrado").isEmpty());
        assertTrue(repositorioSemaforoAcademico.existsByEstudianteId("EST001"));
    }
    @Test
    void testSolicitudCambioSimple() {
        repositorioSolicitudCambio.deleteAll();
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setEstudianteId("EST001");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        repositorioSolicitudCambio.save(solicitud);
        assertFalse(repositorioSolicitudCambio.findByEstudianteId("EST001").isEmpty());
        assertFalse(repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE).isEmpty());
        assertTrue(repositorioSolicitudCambio.countByEstado(EstadoSolicitud.PENDIENTE) >= 1);
    }
    @Test
    void testUsuarioSimple() {
        repositorioUsuario.deleteAll();
        Usuario usuario = new Usuario();
        usuario.setUsername("user1");
        usuario.setCorreoInstitucional("user1@test.com");
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setActivo(true);
        repositorioUsuario.save(usuario);
        assertTrue(repositorioUsuario.findByUsername("user1").isPresent());
        assertFalse(repositorioUsuario.findByRol(Rol.ESTUDIANTE).isEmpty());
        assertTrue(repositorioUsuario.existsByUsername("user1"));
        assertTrue(repositorioUsuario.countByRol(Rol.ESTUDIANTE) >= 1);
    }
    @Test
    void testEmptyResults() {
        assertFalse(repositorioEstudiante.findByCodigo("NOEXISTE").isPresent());
        assertFalse(repositorioMateria.findByCodigo("NOEXISTE").isPresent());
        assertFalse(repositorioUsuario.findByUsername("NOEXISTE").isPresent());
        assertFalse(repositorioEstudiante.existsByCodigo("NOEXISTE"));
        assertTrue(repositorioEstudiante.findByCarrera("NOEXISTE").isEmpty());
    }
    @Test
    void testCountOperations() {
        repositorioEstudiante.deleteAll();
        repositorioUsuario.deleteAll();
        Estudiante e = new Estudiante();
        e.setCodigo("COUNT001");
        e.setCarrera("Ingenieria");
        e.setActivo(true);
        repositorioEstudiante.save(e);
        Usuario u = new Usuario();
        u.setUsername("countuser");
        u.setRol(Rol.ESTUDIANTE);
        u.setActivo(true);
        repositorioUsuario.save(u);
        assertTrue(repositorioEstudiante.countByCarrera("Ingenieria") >= 1);
        assertTrue(repositorioUsuario.countByRol(Rol.ESTUDIANTE) >= 1);
    }
    @Test
    void testMultipleRepositories() {
        repositorioEstudiante.deleteAll();
        repositorioMateria.deleteAll();
        repositorioUsuario.deleteAll();
        Estudiante e = new Estudiante();
        e.setCodigo("MULTI001");
        repositorioEstudiante.save(e);
        Materia m = new Materia();
        m.setCodigo("MULTI001");
        repositorioMateria.save(m);
        Usuario u = new Usuario();
        u.setUsername("multiuser");
        repositorioUsuario.save(u);
        assertTrue(repositorioEstudiante.findByCodigo("MULTI001").isPresent());
        assertTrue(repositorioMateria.findByCodigo("MULTI001").isPresent());
        assertTrue(repositorioUsuario.findByUsername("multiuser").isPresent());
    }
}
