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
    void testAdministradorCompleto() {
        Administrador admin = new Administrador(1, "admin.lopez", "123", "admin.lopez@escuelaing.edu.co");
        assertEquals("admin.lopez", admin.getUsername());
        assertEquals("123", admin.getPasswordHash());
        assertEquals(Rol.ADMIN, admin.getRol());
        assertTrue(admin.isActivo());
        admin.setId("799439011");
        assertEquals("799439011", admin.getId());
    }

    @Test
    void testDecanaturaCompleta() {
        Decanatura decanatura = new Decanatura(1, "maria.decana", "456",
                "maria.rodriguez@escuelaing.edu.co", 101, "Decanatura Ingeniería", "Ingeniería");
        assertEquals(101, decanatura.getIdDecanatura());
        assertEquals("Decanatura Ingeniería", decanatura.getNombre());
        assertEquals("Ingeniería", decanatura.getFacultad());
        assertEquals(Rol.DECANATURA, decanatura.getRol());
        assertTrue(decanatura.esValida());
        decanatura.setNombre("");
        assertFalse(decanatura.esValida());
    }

    @Test
    void testEstudianteCompleto() {
        Estudiante estudiante = new Estudiante(1, "juan.perez", "123",
                "juan.perez@mail.escuelaing.edu.co", 2001, "Juan Carlos", "2020100123",
                "Ingeniería de Sistemas", 6);
        assertEquals("Juan Carlos", estudiante.getNombre());
        assertEquals("2020100123", estudiante.getCodigo());
        assertEquals(6, estudiante.getSemestre());
        assertEquals(Rol.ESTUDIANTE, estudiante.getRol());
        assertTrue(estudiante.esValido());
        estudiante.setSolicitudesIds(Arrays.asList("sol1", "sol2"));
        estudiante.setHorariosIds(Arrays.asList("hor1"));
        assertEquals(2, estudiante.getSolicitudesIds().size());
        assertEquals(1, estudiante.getHorariosIds().size());
    }

    @Test
    void testHorarioCompleto() {
        Time inicio = Time.valueOf("08:00:00");
        Time fin = Time.valueOf("10:00:00");
        Horario horario = new Horario(1, "Lunes", inicio, fin, "f 101");
        assertEquals("Lunes", horario.getDiaSemana());
        assertEquals("f 101", horario.getSalon());
        assertTrue(horario.esValido());
        Horario h1 = new Horario();
        h1.setIdHorario(1);
        Horario h2 = new Horario();
        h2.setIdHorario(1);
        assertEquals(h1, h2);
        horario.setDiaSemana("");
        assertFalse(horario.esValido());
    }

    @Test
    void testMateriaCompleta() {
        Materia materia = new Materia(1, "Programación I", "PROG101", 3, "Ingeniería", true);
        assertEquals("Programación I", materia.getNombre());
        assertEquals("PROG101", materia.getCodigo());
        assertEquals(3, materia.getCreditos());
        assertTrue(materia.isEsObligatoria());
        assertTrue(materia.esValida());
        materia.setPrerrequisitosIds(Arrays.asList("mat1", "mat2"));
        materia.setGruposIds(Arrays.asList("grupo1"));
        assertEquals(2, materia.getPrerrequisitosIds().size());
        assertEquals(1, materia.getGruposIds().size());
        Materia materiaInvalida = new Materia();
        materiaInvalida.setCodigo("");
        materiaInvalida.setCreditos(8);
        assertFalse(materiaInvalida.esValida());
    }

    @Test
    void testPeriodoCambioCompleto() {
        Date inicio = new Date();
        Date fin = new Date(inicio.getTime() + 86400000L);
        PeriodoCambio periodo = new PeriodoCambio(1, "Periodo 2024-1", inicio, fin, "ORDINARIO");

        assertEquals("Periodo 2024-1", periodo.getNombre());
        assertEquals("ORDINARIO", periodo.getTipo());
        assertTrue(periodo.esValido());
        assertFalse(periodo.isActivo());
        periodo.setActivo(true);
        assertTrue(periodo.isActivo());
        PeriodoCambio periodoInvalido = new PeriodoCambio();
        periodoInvalido.setIdPeriodo(-1);
        assertFalse(periodoInvalido.esValido());
    }

    @Test
    void testPlanAcademicoCompleto() {
        PlanAcademico plan = new PlanAcademico(1, "Ingeniería de Sistemas", "PREGRADO", 160);
        assertEquals("Ingeniería de Sistemas", plan.getNombre());
        assertEquals("PREGRADO", plan.getGrado());
        assertEquals(160, plan.getCreditosTotales());
        assertTrue(plan.esValido());
        plan.setMateriasObligatoriasIds(Arrays.asList("mat1", "mat2"));
        plan.setMateriasElectivasIds(Arrays.asList("elec1"));
        assertEquals(2, plan.getMateriasObligatoriasIds().size());
        assertEquals(1, plan.getMateriasElectivasIds().size());
        PlanAcademico planInvalido = new PlanAcademico();
        planInvalido.setGrado("DOCTORADO");
        assertFalse(planInvalido.esValido());
    }

    @Test
    void testProfesorCompleto() {
        Profesor profesor = new Profesor(1, "Dr. Roberto Martínez", "roberto.martinez@escuelaing.edu.co");
        assertEquals("Dr. Roberto Martínez", profesor.getNombre());
        assertEquals("roberto.martinez@escuelaing.edu.co", profesor.getCorreoInstitucional());
        assertTrue(profesor.esValido());
        profesor.setMateriasAsignadasIds(Arrays.asList("cald", "cali"));
        profesor.setGruposAsignadosIds(Arrays.asList("grupo1"));
        assertEquals(2, profesor.getMateriasAsignadasIds().size());
        assertEquals(1, profesor.getGruposAsignadosIds().size());
        Profesor profesorInvalido = new Profesor();
        profesorInvalido.setIdProfesor(-1);
        profesorInvalido.setNombre("");
        assertFalse(profesorInvalido.esValido());
    }

    @Test
    void testSemaforoAcademicoCompleto() {
        SemaforoAcademico semaforo = new SemaforoAcademico("estudiante", "PREGRADO", "plan15");
        assertEquals("estudiante", semaforo.getEstudianteId());
        assertEquals("PREGRADO", semaforo.getGrado());
        assertFalse(semaforo.isCambioDePlan());
        assertTrue(semaforo.esValido());
        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("calv", EstadoMateria.APROBADA);
        semaforo.setHistorialMaterias(historial);
        assertEquals(1, semaforo.getHistorialMaterias().size());
        semaforo.setCambioDePlan(true);
        semaforo.setPlanAnteriorId("planAnterior14");
        semaforo.setPromedioAcumulado(4.2f);
        assertTrue(semaforo.isCambioDePlan());
        assertEquals("planAnterior14", semaforo.getPlanAnteriorId());
        assertEquals(4.2f, semaforo.getPromedioAcumulado());
        SemaforoAcademico semaforoInvalido = new SemaforoAcademico();
        semaforoInvalido.setGrado("DOCTORADO");
        semaforoInvalido.setCreditosAprobados(-5);
        assertFalse(semaforoInvalido.esValido());
    }

    @Test
    void testSolicitudCambioCompleta() {
        SolicitudCambio solicitud = new SolicitudCambio("estudiante", "dopo", "grupo1","dosw", "grupo2");
        assertEquals("estudiante", solicitud.getEstudianteId());
        assertEquals("dosw", solicitud.getMateriaDestinoId());
        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
        assertTrue(solicitud.esValida());
        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitud.setFechaRespuesta(new Date());
        solicitud.setRespuesta("Solicitud aprobada");
        assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstado());
        assertNotNull(solicitud.getFechaRespuesta());
        assertEquals("Solicitud aprobada", solicitud.getRespuesta());
        SolicitudCambio solicitudInvalida = new SolicitudCambio();
        solicitudInvalida.setEstudianteId("");
        assertFalse(solicitudInvalida.esValida());
    }

    @Test
    void testUsuarioCompleto() {
        Usuario usuario = new Usuario(1, "carlos.mendez", "456", "carlos.mendez@escuelaing.edu.co", Rol.ESTUDIANTE);
        assertEquals("carlos.mendez", usuario.getUsername());
        assertEquals(Rol.ESTUDIANTE, usuario.getRol());
        assertTrue(usuario.isActivo());
        assertTrue(usuario.esValido());
        Usuario u1 = new Usuario(1, "user1", "pass1", "user1@mail.com", Rol.ESTUDIANTE);
        Usuario u2 = new Usuario(1, "user1", "pass2", "user2@mail.com", Rol.ADMIN);
        Usuario u3 = new Usuario(2, "user2", "pass1", "user1@mail.com", Rol.ESTUDIANTE);
        assertEquals(u1.getIdUsuario(), u2.getIdUsuario());
        assertEquals(u1.getUsername(), u2.getUsername());
        assertNotEquals(u1.getPasswordHash(), u2.getPasswordHash());
        assertNotEquals(u1.getCorreoInstitucional(), u2.getCorreoInstitucional());
        assertNotEquals(u1.getRol(), u2.getRol());

        usuario.setActivo(false);
        assertFalse(usuario.isActivo());

        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setUsername("");
        usuarioInvalido.setCorreoInstitucional("correo_invalido");
        assertFalse(usuarioInvalido.esValido());
    }

    @Test
    void testEnums() {
        assertEquals(5, EstadoMateria.values().length);
        assertEquals(EstadoMateria.APROBADA, EstadoMateria.valueOf("APROBADA"));
        assertEquals(4, EstadoSolicitud.values().length);
        assertEquals(EstadoSolicitud.PENDIENTE, EstadoSolicitud.valueOf("PENDIENTE"));
        assertEquals(4, Rol.values().length);
        assertEquals(Rol.ESTUDIANTE, Rol.valueOf("ESTUDIANTE"));
        assertEquals(3, EstadoSemaforo.values().length);
        assertEquals(EstadoSemaforo.VERDE, EstadoSemaforo.valueOf("VERDE"));
    }
}