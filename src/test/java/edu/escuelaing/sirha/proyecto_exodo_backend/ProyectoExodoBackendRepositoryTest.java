package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import java.sql.Time;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ProyectoExodoBackendRepositoryTest {
    @Autowired private RepositorioAdministrador repositorioAdministrador;
    @Autowired private RepositorioDecanatura repositorioDecanatura;
    @Autowired private RepositorioEstudiante repositorioEstudiante;
    @Autowired private RepositorioGrupo repositorioGrupo;
    @Autowired private RepositorioHorario repositorioHorario;
    @Autowired private RepositorioMateria repositorioMateria;
    @Autowired private RepositorioPeriodoCambio repositorioPeriodoCambio;
    @Autowired private RepositorioPlanAcademico repositorioPlanAcademico;
    @Autowired private RepositorioSolicitudCambio repositorioSolicitudCambio;
    @Test
    void testFindByActivoTrueAdministrador() {
        Administrador admin1 = new Administrador(1, "carlos.admin", "hash123", "carlos.ramirez@escuelaing.edu.co");
        Administrador admin2 = new Administrador(2, "ana.admin", "hash456", "ana.lopez@escuelaing.edu.co");
        admin2.setActivo(false);
        repositorioAdministrador.save(admin1);
        repositorioAdministrador.save(admin2);
        List<Administrador> activos = repositorioAdministrador.findByActivoTrue();
        assertEquals(1, activos.size());
        assertEquals("carlos.admin", activos.get(0).getUsername());
    }
    @Test
    void testFindByUsernameAdministrador() {
        Administrador admin = new Administrador(3, "maria.sistema", "pass789", "maria.gonzalez@escuelaing.edu.co");
        repositorioAdministrador.save(admin);
        Optional<Administrador> encontrado = repositorioAdministrador.findByUsername("maria.sistema");
        assertTrue(encontrado.isPresent());
        assertEquals("maria.gonzalez@escuelaing.edu.co", encontrado.get().getCorreoInstitucional());
    }
    @Test
    void testExistsByCorreoAdministrador() {
        Administrador admin = new Administrador(4, "pedro.admin", "hash321", "pedro.martinez@escuelaing.edu.co");
        repositorioAdministrador.save(admin);
        assertTrue(repositorioAdministrador.existsByCorreoInstitucional("pedro.martinez@escuelaing.edu.co"));
        assertFalse(repositorioAdministrador.existsByCorreoInstitucional("inexistente@escuelaing.edu.co"));
    }
    @Test
    void testFindByIdDecanatura() {
        Decanatura decano = new Decanatura(3, "sandra.admin", "hash789",
                "sandra.morales@escuelaing.edu.co", 205, "Decanatura Sistemas", "Sistemas");
        repositorioDecanatura.save(decano);
        Optional<Decanatura> encontrado = repositorioDecanatura.findByIdDecanatura(205);
        assertTrue(encontrado.isPresent());
        assertEquals("sandra.admin", encontrado.get().getUsername());
    }
    @Test
    void testFindByCodigoEstudiante() {
        Estudiante estudiante = new Estudiante(1, "camilo.estudiante", "hash123",
                "camilo.hernandez@mail.escuelaing.edu.co", 2001, "Camilo Hernández", "2020112345",
                "Ingeniería de Sistemas", 5);
        repositorioEstudiante.save(estudiante);
        Optional<Estudiante> encontrado = repositorioEstudiante.findByCodigo("2020112345");
        assertTrue(encontrado.isPresent());
        assertEquals("Camilo Hernández", encontrado.get().getNombre());
    }
    @Test
    void testFindByMateriaIdGrupo() {
        Grupo g1 = new Grupo(1, 25, "calculo123", "prof456");
        Grupo g2 = new Grupo(2, 30, "calculo123", "prof789");
        Grupo g3 = new Grupo(3, 20, "algebra456", "prof123");
        repositorioGrupo.save(g1);
        repositorioGrupo.save(g2);
        repositorioGrupo.save(g3);
        List<Grupo> gruposCalculo = repositorioGrupo.findByMateriaId("calculo123");
        assertEquals(2, gruposCalculo.size());
    }
    @Test
    void testFindByDiaSemanaHorario() {
        Horario h1 = new Horario(1, "Lunes", Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), "Aula 101");
        Horario h2 = new Horario(2, "Lunes", Time.valueOf("10:00:00"), Time.valueOf("12:00:00"), "Aula 102");
        Horario h3 = new Horario(3, "Martes", Time.valueOf("14:00:00"), Time.valueOf("16:00:00"), "Lab 201");
        repositorioHorario.save(h1);
        repositorioHorario.save(h2);
        repositorioHorario.save(h3);
        List<Horario> horariosLunes = repositorioHorario.findByDiaSemana("Lunes");
        assertEquals(2, horariosLunes.size());
    }
    @Test
    void testFindBySalonHorario() {
        Horario horario = new Horario(4, "Miércoles", Time.valueOf("16:00:00"), Time.valueOf("18:00:00"), "Auditorio A");
        repositorioHorario.save(horario);
        List<Horario> enAuditorio = repositorioHorario.findBySalon("Auditorio A");
        assertEquals(1, enAuditorio.size());
        assertEquals("Miércoles", enAuditorio.get(0).getDiaSemana());
    }
    @Test
    void testFindByCodigoMateria() {
        Materia materia = new Materia(1, "Cálculo Diferencial", "CALC101", 4, "Matemáticas", true);
        repositorioMateria.save(materia);
        Optional<Materia> encontrada = repositorioMateria.findByCodigo("CALC101");
        assertTrue(encontrada.isPresent());
        assertEquals("Cálculo Diferencial", encontrada.get().getNombre());
    }
    @Test
    void testFindByFacultadAndObligatoriaMateria() {
        Materia m1 = new Materia(2, "Programación I", "PROG101", 3, "Ingeniería", true);
        Materia m2 = new Materia(3, "Base de Datos", "BD201", 3, "Ingeniería", true);
        Materia m3 = new Materia(4, "Electiva Técnica", "ELEC301", 2, "Ingeniería", false);
        repositorioMateria.save(m1);
        repositorioMateria.save(m2);
        repositorioMateria.save(m3);
        List<Materia> obligatorias = repositorioMateria.findByFacultadAndEsObligatoria("Ingeniería", true);
        assertEquals(2, obligatorias.size());
    }
    @Test
    void testFindMateriasSinPrerrequisitos() {
        Materia sinPrerreq = new Materia(5, "Introducción Ingeniería", "INTRO101", 2, "Ingeniería", true);
        repositorioMateria.save(sinPrerreq);
        List<Materia> sinPrerrequisitos = repositorioMateria.findMateriasSinPrerrequisitos();
        assertEquals(1, sinPrerrequisitos.size());
        assertEquals("Introducción Ingeniería", sinPrerrequisitos.get(0).getNombre());
    }
    @Test
    void testCountByGradoPlanAcademico() {
        PlanAcademico m1 = new PlanAcademico(5, "Maestría IA", "MAESTRIA", 50);
        PlanAcademico m2 = new PlanAcademico(6, "Maestría Datos", "MAESTRIA", 45);
        repositorioPlanAcademico.save(m1);
        repositorioPlanAcademico.save(m2);
        long cantidadMaestrias = repositorioPlanAcademico.countByGrado("MAESTRIA");
        assertEquals(2, cantidadMaestrias);
    }
    @Test
    void testFindByEstudianteIdSolicitudCambio() {
        SolicitudCambio s1 = new SolicitudCambio("est123", "matOrigen1", "grupoOrigen1", "matDestino1", "grupoDestino1");
        SolicitudCambio s2 = new SolicitudCambio("est123", "matOrigen2", "grupoOrigen2", "matDestino2", "grupoDestino2");
        SolicitudCambio s3 = new SolicitudCambio("est456", "matOrigen3", "grupoOrigen3", "matDestino3", "grupoDestino3");
        repositorioSolicitudCambio.save(s1);
        repositorioSolicitudCambio.save(s2);
        repositorioSolicitudCambio.save(s3);
        List<SolicitudCambio> solicitudesEst123 = repositorioSolicitudCambio.findByEstudianteId("est123");
        assertEquals(2, solicitudesEst123.size());
    }
    @Test
    void testFindByEstadoSolicitudCambio() {
        SolicitudCambio pendiente = new SolicitudCambio("est789", null, null, "materia123", "grupo456");
        SolicitudCambio aprobada = new SolicitudCambio("est321", null, null, "materia789", "grupo123");
        aprobada.setEstado(EstadoSolicitud.APROBADA);
        repositorioSolicitudCambio.save(pendiente);
        repositorioSolicitudCambio.save(aprobada);
        List<SolicitudCambio> pendientes = repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE);
        assertEquals(1, pendientes.size());
        assertEquals("est789", pendientes.get(0).getEstudianteId());
    }
    @Test
    void testCountByEstadoSolicitudCambio() {
        SolicitudCambio s1 = new SolicitudCambio("est111", null, null, "mat111", "grupo111");
        s1.setEstado(EstadoSolicitud.EN_REVISION);
        SolicitudCambio s2 = new SolicitudCambio("est222", null, null, "mat222", "grupo222");
        s2.setEstado(EstadoSolicitud.EN_REVISION);
        repositorioSolicitudCambio.save(s1);
        repositorioSolicitudCambio.save(s2);
        long cantidadEnRevision = repositorioSolicitudCambio.countByEstado(EstadoSolicitud.EN_REVISION);
        assertEquals(2, cantidadEnRevision);
    }
}
