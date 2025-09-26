package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProyectoExodoBackendModelTest {
    @Test
    void testAdministradorConstructorCompleto() {
        Administrador admin = new Administrador(1, "admin.lopez", "hash123", "admin.lopez@escuelaing.edu.co");
        assertEquals("admin.lopez", admin.getUsername());
        assertEquals("hash123", admin.getPasswordHash());
        assertEquals(Rol.ADMIN, admin.getRol());
        assertTrue(admin.isActivo());
    }
    @Test
    void testAdministradorSettersGetters() {
        Administrador admin = new Administrador();
        admin.setId("507f1f77bcf86cd799439011");

        assertEquals("507f1f77bcf86cd799439011", admin.getId());
        assertNotNull(admin);
    }
    @Test
    void testDecanaturaConstructorCompleto() {
        Decanatura decanatura = new Decanatura(1, "maria.decana", "pass456",
                "maria.rodriguez@escuelaing.edu.co", 101, "Decanatura Ingeniería", "Ingeniería");

        assertEquals(101, decanatura.getIdDecanatura());
        assertEquals("Decanatura Ingeniería", decanatura.getNombre());
        assertEquals("Ingeniería", decanatura.getFacultad());
        assertEquals(Rol.DECANATURA, decanatura.getRol());
    }
    @Test
    void testDecanaturaEsValida() {
        Decanatura decanatura = new Decanatura(1, "ana.decanato", "hash789",
                "ana.martinez@escuelaing.edu.co", 102, "Decanatura Sistemas", "Sistemas");
        assertTrue(decanatura.esValida());

        decanatura.setNombre("");
        assertFalse(decanatura.esValida());
    }
    @Test
    void testEstudianteConstructorCompleto() {
        Estudiante estudiante = new Estudiante(1, "juan.perez", "hash123",
                "juan.perez@mail.escuelaing.edu.co", 2001, "Juan Carlos Pérez", "2020100123",
                "Ingeniería de Sistemas", 6);
        assertEquals("Juan Carlos Pérez", estudiante.getNombre());
        assertEquals("2020100123", estudiante.getCodigo());
        assertEquals(6, estudiante.getSemestre());
        assertEquals(Rol.ESTUDIANTE, estudiante.getRol());
    }
    @Test
    void testEstudianteEsValido() {
        Estudiante estudiante = new Estudiante(2, "maria.garcia", "pass456",
                "maria.garcia@mail.escuelaing.edu.co", 2002, "María García López", "2019200456", "Ingeniería Civil", 4);
        assertTrue(estudiante.esValido());
        estudiante.setCodigo("");
        assertFalse(estudiante.esValido());
    }
    @Test
    void testEstudianteListas() {
        Estudiante estudiante = new Estudiante();
        estudiante.setSolicitudesIds(Arrays.asList("sol1", "sol2"));
        estudiante.setHorariosIds(Arrays.asList("hor1"));
        assertEquals(2, estudiante.getSolicitudesIds().size());
        assertEquals(1, estudiante.getHorariosIds().size());
    }
    @Test
    void testGrupoConstructorCompleto() {
        Grupo grupo = new Grupo(1, 30, "materia123", "profesor456");
        assertEquals(1, grupo.getIdGrupo());
        assertEquals(30, grupo.getCupoMaximo());
        assertTrue(grupo.esValido());
    }
    @Test
    void testGrupoCantidadInscritos() {
        Grupo grupo = new Grupo();
        grupo.setEstudiantesInscritosIds(Arrays.asList("est1", "est2", "est3"));
        assertEquals(3, grupo.getCantidadInscritos());
    }
    @Test
    void testGrupoValidacion() {
        Grupo grupo = new Grupo(-1, 60, "mat123", "prof456");
        assertFalse(grupo.esValido());
    }
    @Test
    void testHorarioConstructorCompleto() {
        Time inicio = Time.valueOf("08:00:00");
        Time fin = Time.valueOf("10:00:00");
        Horario horario = new Horario(1, "Lunes", inicio, fin, "Aula 101");
        assertEquals("Lunes", horario.getDiaSemana());
        assertEquals("Aula 101", horario.getSalon());
        assertTrue(horario.esValido());
    }
    @Test
    void testHorarioEquals() {
        Horario h1 = new Horario();
        h1.setIdHorario(1);
        Horario h2 = new Horario();
        h2.setIdHorario(1);

        assertEquals(h1, h2);
    }
    @Test
    void testHorarioValidacion() {
        Horario horario = new Horario();
        horario.setDiaSemana("");
        assertFalse(horario.esValido());
    }
    @Test
    void testMateriaConstructorCompleto() {
        Materia materia = new Materia(1, "Programación I", "PROG101", 3, "Ingeniería", true);
        assertEquals("Programación I", materia.getNombre());
        assertEquals("PROG101", materia.getCodigo());
        assertEquals(3, materia.getCreditos());
        assertTrue(materia.isEsObligatoria());
        assertTrue(materia.esValida());
    }
    @Test
    void testMateriaValidacion() {
        Materia materia = new Materia();
        materia.setCodigo("");
        materia.setCreditos(8);
        assertFalse(materia.esValida());
    }
    @Test
    void testMateriaListas() {
        Materia materia = new Materia();
        materia.setPrerrequisitosIds(Arrays.asList("mat1", "mat2"));
        materia.setGruposIds(Arrays.asList("grupo1"));
        assertEquals(2, materia.getPrerrequisitosIds().size());
        assertEquals(1, materia.getGruposIds().size());
    }
    @Test
    void testPeriodoCambioConstructorCompleto() {
        Date inicio = new Date();
        Date fin = new Date(inicio.getTime() + 86400000L);
        PeriodoCambio periodo = new PeriodoCambio(1, "Periodo 2024-1", inicio, fin, "ORDINARIO");
        assertEquals("Periodo 2024-1", periodo.getNombre());
        assertEquals("ORDINARIO", periodo.getTipo());
        assertTrue(periodo.esValido());
    }
    @Test
    void testPeriodoCambioValidacion() {
        PeriodoCambio periodo = new PeriodoCambio();
        periodo.setIdPeriodo(-1);
        assertFalse(periodo.esValido());
    }
    @Test
    void testPeriodoCambioEstadoActivo() {
        PeriodoCambio periodo = new PeriodoCambio();
        assertFalse(periodo.isActivo());
        periodo.setActivo(true);
        assertTrue(periodo.isActivo());
    }
    @Test
    void testPlanAcademicoConstructorCompleto() {
        PlanAcademico plan = new PlanAcademico(1, "Ingeniería de Sistemas", "PREGRADO", 160);
        assertEquals("Ingeniería de Sistemas", plan.getNombre());
        assertEquals("PREGRADO", plan.getGrado());
        assertEquals(160, plan.getCreditosTotales());
        assertTrue(plan.esValido());
    }
    @Test
    void testPlanAcademicoValidacion() {
        PlanAcademico plan = new PlanAcademico();
        plan.setGrado("DOCTORADO");
        assertFalse(plan.esValido());
    }
    @Test
    void testPlanAcademicoMaterias() {
        PlanAcademico plan = new PlanAcademico();
        plan.setMateriasObligatoriasIds(Arrays.asList("mat1", "mat2"));
        plan.setMateriasElectivasIds(Arrays.asList("elec1"));
        assertEquals(2, plan.getMateriasObligatoriasIds().size());
        assertEquals(1, plan.getMateriasElectivasIds().size());
    }
    @Test
    void testProfesorConstructorCompleto() {
        Profesor profesor = new Profesor(1, "Dr. Roberto Martínez", "roberto.martinez@escuelaing.edu.co");
        assertEquals("Dr. Roberto Martínez", profesor.getNombre());
        assertEquals("roberto.martinez@escuelaing.edu.co", profesor.getCorreoInstitucional());
        assertTrue(profesor.esValido());
    }
    @Test
    void testProfesorValidacion() {
        Profesor profesor = new Profesor();
        profesor.setIdProfesor(-1);
        profesor.setNombre("");
        assertFalse(profesor.esValido());
    }
    @Test
    void testProfesorAsignaciones() {
        Profesor profesor = new Profesor();
        profesor.setMateriasAsignadasIds(Arrays.asList("mat1", "mat2"));
        profesor.setGruposAsignadosIds(Arrays.asList("grupo1"));

        assertEquals(2, profesor.getMateriasAsignadasIds().size());
        assertEquals(1, profesor.getGruposAsignadosIds().size());
    }
    @Test
    void testSemaforoAcademicoConstructorCompleto() {
        SemaforoAcademico semaforo = new SemaforoAcademico("est123", "PREGRADO", "plan456");
        assertEquals("est123", semaforo.getEstudianteId());
        assertEquals("PREGRADO", semaforo.getGrado());
        assertFalse(semaforo.isCambioDePlan());
        assertTrue(semaforo.esValido());
    }
    @Test
    void testSemaforoAcademicoValidacion() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setGrado("DOCTORADO");
        semaforo.setCreditosAprobados(-5);
        assertFalse(semaforo.esValido());
    }
    @Test
    void testSemaforoAcademicoHistorial() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("mat1", EstadoMateria.APROBADA);
        semaforo.setHistorialMaterias(historial);
        assertEquals(1, semaforo.getHistorialMaterias().size());
    }
    @Test
    void testSemaforoAcademicoCambioPlan() {
        SemaforoAcademico semaforo = new SemaforoAcademico();
        semaforo.setCambioDePlan(true);
        semaforo.setPlanAnteriorId("planAnterior123");
        semaforo.setPromedioAcumulado(4.2f);
        assertTrue(semaforo.isCambioDePlan());
        assertEquals("planAnterior123", semaforo.getPlanAnteriorId());
        assertEquals(4.2f, semaforo.getPromedioAcumulado());
    }
    @Test
    void testSolicitudCambioConstructorCompleto() {
        SolicitudCambio solicitud = new SolicitudCambio("est123", "matOrigen456", "grupoOrigen789","matDestino012", "grupoDestino345");
        assertEquals("est123", solicitud.getEstudianteId());
        assertEquals("matDestino012", solicitud.getMateriaDestinoId());
        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
        assertTrue(solicitud.esValida());
    }
    @Test
    void testSolicitudCambioValidacion() {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setEstudianteId("");
        assertFalse(solicitud.esValida());
    }
    @Test
    void testSolicitudCambioEstados() {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud.setFechaRespuesta(new Date());
        solicitud.setRespuesta("Solicitud aprobada");
        assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstado());
        assertNotNull(solicitud.getFechaRespuesta());
        assertEquals("Solicitud aprobada", solicitud.getRespuesta());
    }
    @Test
    void testUsuarioConstructorCompleto() {
        Usuario usuario = new Usuario(1, "carlos.mendez", "hash456", "carlos.mendez@escuelaing.edu.co", Rol.ESTUDIANTE);
        assertEquals("carlos.mendez", usuario.getUsername());
        assertEquals(Rol.ESTUDIANTE, usuario.getRol());
        assertTrue(usuario.isActivo());
        assertTrue(usuario.esValido());
    }
    @Test
    void testUsuarioValidacion() {
        Usuario usuario = new Usuario();
        usuario.setUsername("");
        usuario.setCorreoInstitucional("correo_invalido");
        assertFalse(usuario.esValido());
    }
    @Test
    void testUsuarioEquals() {
        Usuario u1 = new Usuario(1, "user1", "pass1", "user1@mail.com", Rol.ESTUDIANTE);
        Usuario u2 = new Usuario(1, "user1", "pass2", "user2@mail.com", Rol.ADMIN);
        Usuario u3 = new Usuario(2, "user2", "pass1", "user1@mail.com", Rol.ESTUDIANTE);
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
    }
    @Test
    void testUsuarioActivo() {
        Usuario usuario = new Usuario();
        assertTrue(usuario.isActivo());
        usuario.setActivo(false);
        assertFalse(usuario.isActivo());
    }
    @Test
    void testEstadoMateria() {
        assertEquals(5, EstadoMateria.values().length);
        assertEquals(EstadoMateria.APROBADA, EstadoMateria.valueOf("APROBADA"));
    }
    @Test
    void testEstadoSolicitud() {
        assertEquals(4, EstadoSolicitud.values().length);
        assertEquals(EstadoSolicitud.PENDIENTE, EstadoSolicitud.valueOf("PENDIENTE"));
    }
    @Test
    void testRol() {
        assertEquals(3, Rol.values().length);
        assertEquals(Rol.ESTUDIANTE, Rol.valueOf("ESTUDIANTE"));
    }
    @Test
    void testEstadoSemaforo() {
        assertEquals(3, EstadoSemaforo.values().length);
        assertEquals(EstadoSemaforo.VERDE, EstadoSemaforo.valueOf("VERDE"));
    }
}