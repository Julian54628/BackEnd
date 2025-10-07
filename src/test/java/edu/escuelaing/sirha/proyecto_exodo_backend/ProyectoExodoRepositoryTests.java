package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/test_sirha",
        "spring.main.lazy-initialization=true"
})
class AllRepositoriesTest {

    @Autowired private RepositorioAdministrador repositorioAdministrador;
    @Autowired private RepositorioDecanatura repositorioDecanatura;
    @Autowired private RepositorioEstudiante repositorioEstudiante;
    @Autowired private RepositorioGrupo repositorioGrupo;
    @Autowired private RepositorioHorario repositorioHorario;
    @Autowired private RepositorioMateria repositorioMateria;
    @Autowired private RepositorioPeriodoCambio repositorioPeriodoCambio;
    @Autowired private RepositorioPlanAcademico repositorioPlanAcademico;
    @Autowired private RepositorioProfesor repositorioProfesor;
    @Autowired private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    @Autowired private RepositorioSolicitudCambio repositorioSolicitudCambio;
    @Autowired private RepositorioUsuario repositorioUsuario;

    @BeforeEach
    void setUp() {
        repositorioAdministrador.deleteAll();
        repositorioDecanatura.deleteAll();
        repositorioEstudiante.deleteAll();
        repositorioGrupo.deleteAll();
        repositorioHorario.deleteAll();
        repositorioMateria.deleteAll();
        repositorioPeriodoCambio.deleteAll();
        repositorioPlanAcademico.deleteAll();
        repositorioProfesor.deleteAll();
        repositorioSemaforoAcademico.deleteAll();
        repositorioSolicitudCambio.deleteAll();
        repositorioUsuario.deleteAll();
    }

    @Test
    void testRepositorioAdministrador() {
        Administrador admin = new Administrador();
        admin.setId("admin1");
        admin.setUsername("admin_user");
        admin.setCorreoInstitucional("admin@escuelaing.edu.co");
        admin.setActivo(true);
        repositorioAdministrador.save(admin);

        List<Administrador> activos = repositorioAdministrador.findByActivoTrue();
        assertFalse(activos.isEmpty());

        Optional<Administrador> encontrado = repositorioAdministrador.findByUsername("admin_user");
        assertTrue(encontrado.isPresent());

        assertTrue(repositorioAdministrador.existsByUsername("admin_user"));
    }

    @Test
    void testRepositorioDecanatura() {
        Decanatura decanatura = new Decanatura();
        decanatura.setId("dec1");
        decanatura.setIdDecanatura(1);
        decanatura.setNombre("Decanatura Ingeniería");
        decanatura.setFacultad("Ingeniería");
        decanatura.setUsername("dec_ing");
        decanatura.setCorreoInstitucional("decanatura@escuelaing.edu.co");
        decanatura.setActivo(true);
        repositorioDecanatura.save(decanatura);

        Optional<Decanatura> encontrado = repositorioDecanatura.findByIdDecanatura(1);
        assertTrue(encontrado.isPresent());

        List<Decanatura> porFacultad = repositorioDecanatura.findByFacultad("Ingeniería");
        assertFalse(porFacultad.isEmpty());

        assertTrue(repositorioDecanatura.findByUsername("dec_ing").isPresent());
        assertTrue(repositorioDecanatura.existsByFacultad("Ingeniería"));
    }

    @Test
    void testRepositorioEstudiante() {
        Estudiante estudiante = new Estudiante();
        estudiante.setId("est1");
        estudiante.setCodigo("202412345");
        estudiante.setNombre("Juan Pérez");
        estudiante.setCarrera("Ingeniería de Sistemas");
        estudiante.setSemestre(5);
        estudiante.setActivo(true);
        estudiante.setPlanAcademicoId("plan1");
        repositorioEstudiante.save(estudiante);

        Optional<Estudiante> encontrado = repositorioEstudiante.findByCodigo("202412345");
        assertTrue(encontrado.isPresent());

        List<Estudiante> porCarrera = repositorioEstudiante.findByCarrera("Ingeniería de Sistemas");
        assertFalse(porCarrera.isEmpty());

        List<Estudiante> activos = repositorioEstudiante.findByActivoTrue();
        assertFalse(activos.isEmpty());

        assertTrue(repositorioEstudiante.existsByCodigo("202412345"));
    }

    @Test
    void testRepositorioGrupo() {
        Grupo grupo = new Grupo();
        grupo.setId("grupo1");
        grupo.setIdGrupo(1);
        grupo.setMateriaId("mat1");
        grupo.setProfesorId("prof1");
        grupo.setCupoMaximo(30);
        grupo.setEstudiantesInscritosIds(Arrays.asList("est1", "est2"));
        grupo.setHorarioIds(Arrays.asList("hor1", "hor2"));
        repositorioGrupo.save(grupo);

        Optional<Grupo> encontrado = repositorioGrupo.findByIdGrupo(1);
        assertTrue(encontrado.isPresent());

        List<Grupo> porMateria = repositorioGrupo.findByMateriaId("mat1");
        assertFalse(porMateria.isEmpty());

        List<Grupo> porCupo = repositorioGrupo.findByCupoMaximoGreaterThanEqual(25);
        assertFalse(porCupo.isEmpty());

        List<Grupo> porEstudiante = repositorioGrupo.findByEstudiantesInscritosIdsContaining("est1");
        assertFalse(porEstudiante.isEmpty());

        assertTrue(repositorioGrupo.existsByIdGrupo(1));
    }

    @Test
    void testRepositorioMateria() {
        Materia materia = new Materia();
        materia.setId("mat1");
        materia.setCodigo("MAT101");
        materia.setNombre("Cálculo I");
        materia.setFacultad("Ingeniería");
        materia.setEsObligatoria(true);
        materia.setCreditos(4);
        materia.setPrerrequisitosIds(Arrays.asList("MAT100"));
        repositorioMateria.save(materia);

        Optional<Materia> encontrada = repositorioMateria.findByCodigo("MAT101");
        assertTrue(encontrada.isPresent());

        List<Materia> porNombre = repositorioMateria.findByNombreContainingIgnoreCase("cálculo");
        assertFalse(porNombre.isEmpty());

        List<Materia> porFacultad = repositorioMateria.findByFacultad("Ingeniería");
        assertFalse(porFacultad.isEmpty());

        List<Materia> obligatorias = repositorioMateria.findByEsObligatoria(true);
        assertFalse(obligatorias.isEmpty());

        assertTrue(repositorioMateria.existsByCodigo("MAT101"));
    }

    @Test
    void testRepositorioPeriodoCambio() {
        long currentTime = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;

        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setId("per1");
        periodo.setIdPeriodo(1);
        periodo.setNombre("Periodo 2024-1");
        periodo.setTipo("Cambio de Materias");
        periodo.setActivo(true);
        periodo.setFechaInicio(new Date(currentTime - dayInMillis));
        periodo.setFechaFin(new Date(currentTime + dayInMillis));
        repositorioPeriodoCambio.save(periodo);

        Optional<PeriodoCambio> encontrado = repositorioPeriodoCambio.findByIdPeriodo(1);
        assertTrue(encontrado.isPresent());

        List<PeriodoCambio> activos = repositorioPeriodoCambio.findByActivoTrue();
        assertFalse(activos.isEmpty());

        List<PeriodoCambio> porTipo = repositorioPeriodoCambio.findByTipo("Cambio de Materias");
        assertFalse(porTipo.isEmpty());

        List<PeriodoCambio> vigentes = repositorioPeriodoCambio.findPeriodosVigentesEnFecha(new Date());
        assertFalse(vigentes.isEmpty());

        assertTrue(repositorioPeriodoCambio.existsByActivoTrue());
    }

    @Test
    void testRepositorioPlanAcademico() {
        PlanAcademico plan = new PlanAcademico();
        plan.setId("plan1");
        plan.setIdPlan(1);
        plan.setNombre("Plan Ingeniería 2024");
        plan.setGrado("Pregrado");
        plan.setCreditosTotales(160);
        plan.setMateriasObligatoriasIds(Arrays.asList("mat1", "mat2"));
        plan.setMateriasElectivasIds(Arrays.asList("elec1", "elec2"));
        repositorioPlanAcademico.save(plan);

        Optional<PlanAcademico> encontrado = repositorioPlanAcademico.findByIdPlan(1);
        assertTrue(encontrado.isPresent());

        Optional<PlanAcademico> porNombre = repositorioPlanAcademico.findByNombre("Plan Ingeniería 2024");
        assertTrue(porNombre.isPresent());

        List<PlanAcademico> porGrado = repositorioPlanAcademico.findByGrado("Pregrado");
        assertFalse(porGrado.isEmpty());

        List<PlanAcademico> porCreditos = repositorioPlanAcademico.findByCreditosTotalesGreaterThanEqual(150);
        assertFalse(porCreditos.isEmpty());

        assertTrue(repositorioPlanAcademico.existsByNombre("Plan Ingeniería 2024"));
    }

    @Test
    void testRepositorioSemaforoAcademico() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setId("sem1");
        semaforo.setEstudianteId("est1");
        semaforo.setPlanAcademicoId("plan1");
        semaforo.setGrado("Pregrado");
        semaforo.setCambioDePlan(false);
        semaforo.setCreditosAprobados(120);
        semaforo.setTotalCreditosPlan(160);
        semaforo.setPromedioAcumulado(4.2f);
        semaforo.setMateriasVistas(40);
        repositorioSemaforoAcademico.save(semaforo);

        Optional<SemaforoAcademico> encontrado = repositorioSemaforoAcademico.findByEstudianteId("est1");
        assertTrue(encontrado.isPresent());

        List<SemaforoAcademico> porPlan = repositorioSemaforoAcademico.findByPlanAcademicoId("plan1");
        assertFalse(porPlan.isEmpty());

        List<SemaforoAcademico> porCreditos = repositorioSemaforoAcademico.findByCreditosAprobadosGreaterThanEqual(100);
        assertFalse(porCreditos.isEmpty());

        List<SemaforoAcademico> porPromedio = repositorioSemaforoAcademico.findByPromedioAcumuladoGreaterThanEqual(4.0f);
        assertFalse(porPromedio.isEmpty());

        assertTrue(repositorioSemaforoAcademico.existsByEstudianteId("est1"));
    }

    @Test
    void testRepositorioSolicitudCambio() {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId("sol1");
        solicitud.setEstudianteId("est1");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setMateriaDestinoId("mat1");
        solicitud.setGrupoDestinoId("grupo1");
        solicitud.setFechaCreacion(new Date());
        solicitud.setPrioridad(1);
        repositorioSolicitudCambio.save(solicitud);

        List<SolicitudCambio> porEstudiante = repositorioSolicitudCambio.findByEstudianteId("est1");
        assertFalse(porEstudiante.isEmpty());

        List<SolicitudCambio> porEstado = repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE);
        assertFalse(porEstado.isEmpty());

        List<SolicitudCambio> porEstudianteYEstado = repositorioSolicitudCambio.findByEstudianteIdAndEstado("est1", EstadoSolicitud.PENDIENTE);
        assertFalse(porEstudianteYEstado.isEmpty());

        List<SolicitudCambio> porMateria = repositorioSolicitudCambio.findByMateriaDestinoId("mat1");
        assertFalse(porMateria.isEmpty());

        long count = repositorioSolicitudCambio.countByEstado(EstadoSolicitud.PENDIENTE);
        assertEquals(1, count);
    }

    @Test
    void testRepositorioUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId("user1");
        usuario.setUsername("usuario_test");
        usuario.setCorreoInstitucional("usuario@escuelaing.edu.co");
        usuario.setRol(Rol.ESTUDIANTE);
        usuario.setActivo(true);
        repositorioUsuario.save(usuario);

        Optional<Usuario> encontrado = repositorioUsuario.findByUsername("usuario_test");
        assertTrue(encontrado.isPresent());

        List<Usuario> porRol = repositorioUsuario.findByRol(Rol.ESTUDIANTE);
        assertFalse(porRol.isEmpty());

        List<Usuario> porRolYActivo = repositorioUsuario.findByRolAndActivoTrue(Rol.ESTUDIANTE);
        assertFalse(porRolYActivo.isEmpty());

        assertTrue(repositorioUsuario.existsByUsername("usuario_test"));

        long count = repositorioUsuario.countByRolAndActivoTrue(Rol.ESTUDIANTE);
        assertEquals(1, count);
    }
}