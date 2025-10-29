package edu.escuelaing.sirha.proyecto_exodo_backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.escuelaing.sirha.model.*;

public class ProyectoExodoBackendModelTests {

    @Test
    public void testAdministradorCompleto() {
        Administrador admin = new Administrador(1, "admin", "password", "admin@escuelaing.edu.co");

        assertEquals(1, admin.getIdUsuario());
        assertEquals("admin", admin.getUsername());
        assertEquals(Rol.ADMIN, admin.getRol());
        assertTrue(admin.isActivo());
    }

    @Test
    public void testEstudianteBasico() {
        Estudiante est = new Estudiante(1, "juan", "pass123", "juan@escuelaing.edu.co",
                1001, "Juan Perez", "20231001", "Ingeniería", 3);

        assertEquals("Juan Perez", est.getNombre());
        assertEquals("20231001", est.getCodigo());
        assertEquals(3, est.getSemestre());
        assertTrue(est.esValido());
    }

    @Test
    public void testEstudianteInvalido() {
        Estudiante estMal = new Estudiante(1, "juan", "pass123", "juan@escuelaing.edu.co",
                1001, "Juan Perez", "", "Ingeniería", 3);

        assertFalse(estMal.esValido());

        Estudiante estSemestreMal = new Estudiante(1, "juan", "pass123", "juan@escuelaing.edu.co",
                1001, "Juan Perez", "20231001", "Ingeniería", 0);
        assertFalse(estSemestreMal.esValido());
    }

    @Test
    public void testMateriaSemaforoColores() {
        MateriaSemaforo materia = new MateriaSemaforo("MAT101", "Matemáticas", "MAT101", 4,
                "Ingeniería", true, EstadoMateria.PENDIENTE);

        assertEquals("AZUL", materia.getColor());

        materia.setEstado(EstadoMateria.APROBADA);
        assertEquals("VERDE", materia.getColor());

        materia.setEstado(EstadoMateria.REPROBADA);
        assertEquals("ROJO", materia.getColor());

        materia.setEstado(EstadoMateria.CANCELADA);
        assertEquals("ROJO", materia.getColor());

        materia.setEstado(EstadoMateria.INSCRITA);
        assertEquals("AZUL", materia.getColor());

        MateriaSemaforo materiaVacia = new MateriaSemaforo();
        assertNull(materiaVacia.getMateriaId());
    }

    @Test
    public void testDecanaturaPermisos() {
        Decanatura decano = new Decanatura(1, "decano", "pass", "decano@escuelaing.edu.co",
                1, "Decano Carlos", "Ingeniería");

        assertFalse(decano.tienePermisosAdministrador());

        decano.convertirEnAdministrador();
        assertTrue(decano.tienePermisosAdministrador());

        decano.removerPermisosAdministrador();
        assertFalse(decano.tienePermisosAdministrador());

        Decanatura decanoConPermisos = new Decanatura(2, "decano2", "pass2", "decano2@escuelaing.edu.co",
                2, "Decano Ana", "Medicina", true);
        assertTrue(decanoConPermisos.tienePermisosAdministrador());

        Decanatura decanoMal = new Decanatura(3, "mal", "pass", "correo@escuelaing.edu.co",
                0, "", "");
        assertFalse(decanoMal.esValida());
    }

    @Test
    public void testGrupoCupos() {
        Grupo grupo = new Grupo(1, 30, "MAT101", "PROF001", "PER001");

        assertEquals(0, grupo.getCantidadInscritos());

        grupo.getEstudiantesInscritosIds().add("EST001");
        grupo.getEstudiantesInscritosIds().add("EST002");

        assertEquals(2, grupo.getCantidadInscritos());

        Grupo grupoMal = new Grupo(0, 0, "", "", "");
        assertFalse(grupoMal.esValido());

        Grupo grupoCupoGrande = new Grupo(2, 60, "MAT102", "PROF002", "PER002");
        assertFalse(grupoCupoGrande.esValido());
    }

    @Test
    public void testHorarioValido() {
        Horario horario = new Horario(1, "Lunes", Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), "A101");

        assertTrue(horario.esValido());
        assertEquals("Lunes", horario.getDiaSemana());
        assertEquals("A101", horario.getSalon());

        Horario horario2 = new Horario(1, "Martes", Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), "A102");
        assertEquals(horario, horario2);
        assertEquals(horario.hashCode(), horario2.hashCode());
    }

    @Test
    public void testSolicitudCambioEstados() {
        SolicitudCambio solicitud = new SolicitudCambio("EST001", "MAT101", "GRP001", "MAT102", "GRP002");

        assertEquals(EstadoSolicitud.PENDIENTE, solicitud.getEstado());
        assertTrue(solicitud.esValida());

        solicitud.addHistorialEstado("PENDIENTE");
        solicitud.addHistorialEstado("EN_REVISION");

        assertEquals(2, solicitud.getHistorialEstados().size());

        SolicitudCambio solicitudConDesc = new SolicitudCambio("EST002", "MAT101", "GRP001",
                "MAT102", "GRP002", "Conflicto de horario", TipoPrioridad.ESPECIAL);
        assertEquals("Conflicto de horario", solicitudConDesc.getDescripcion());
        assertEquals(TipoPrioridad.ESPECIAL, solicitudConDesc.getTipoPrioridad());

        SolicitudCambio solicitudMal = new SolicitudCambio();
        assertFalse(solicitudMal.esValida());
    }

    @Test
    public void testMateriaConPrerrequisitos() {
        List<String> prerrequisitos = new ArrayList<>();
        prerrequisitos.add("CAL101");
        prerrequisitos.add("MAT101");

        List<String> grupos = new ArrayList<>();

        Materia materia = new Materia("materia123", 101, "Cálculo II", "CAL102", 4, "Ingeniería", true, prerrequisitos, grupos);

        assertEquals(2, materia.getPrerrequisitosIds().size());
        assertTrue(materia.esValida());

        Materia materiaSimple = new Materia(102, "Física", "FIS101", 3, "Ingeniería", false);
        if (materiaSimple.getPrerrequisitosIds() != null) {
            materiaSimple.getPrerrequisitosIds().add("MAT101");
            assertEquals(1, materiaSimple.getPrerrequisitosIds().size());
        }
    }

    @Test
    public void testProfesorSimple() {
        Profesor profesor = new Profesor(1001, "Profesor Ana", "ana@escuelaing.edu.co");

        assertEquals("Profesor Ana", profesor.getNombre());
        assertEquals(Rol.PROFESOR, profesor.getRol());
        assertTrue(profesor.esValido());

        profesor.getMateriasAsignadasIds().add("MAT101");
        profesor.getMateriasAsignadasIds().add("MAT102");
        assertEquals(2, profesor.getMateriasAsignadasIds().size());

        Profesor profesorMal = new Profesor(0, "", "correoInvalido");
        assertFalse(profesorMal.esValido());

        profesor.setFacultad("Ingeniería");
    }

    @Test
    public void testPlanAcademico() {
        PlanAcademico plan = new PlanAcademico(1, "Plan Ingeniería", "PREGRADO", 160);

        plan.getMateriasObligatoriasIds().add("MAT101");
        plan.getMateriasObligatoriasIds().add("MAT102");
        plan.getMateriasElectivasIds().add("ELE101");

        assertEquals(2, plan.getMateriasObligatoriasIds().size());
        assertEquals(1, plan.getMateriasElectivasIds().size());
        assertTrue(plan.esValido());

        PlanAcademico planMal = new PlanAcademico(0, "", "INVALIDO", 0);
        assertFalse(planMal.esValido());

        PlanAcademico planMaestria = new PlanAcademico(2, "Plan Maestría", "MAESTRIA", 60);
        assertTrue(planMaestria.esValido());
    }

    @Test
    public void testUsuarioNormal() {
        Usuario usuario = new Usuario(1, "usuario1", "password123", "usuario@escuelaing.edu.co", Rol.ESTUDIANTE);

        assertEquals("usuario1", usuario.getUsername());
        assertEquals("usuario@escuelaing.edu.co", usuario.getCorreoInstitucional());
        assertTrue(usuario.esValido());

        Usuario usuarioMal = new Usuario(0, "", "", "correoInvalido", null);
        assertFalse(usuarioMal.esValido());

        Usuario usuarioCompleto = new Usuario("user123", 2, "user2", "pass2", "user2@escuelaing.edu.co", Rol.PROFESOR, false);
        assertEquals("user123", usuarioCompleto.getId());
        assertFalse(usuarioCompleto.isActivo());
    }

    @Test
    public void testSemaforoAcademico() {
        SemaforoAcademico semaforo = new SemaforoAcademico("EST001", "PREGRADO", "PLAN001");

        semaforo.setCreditosAprobados(45);
        semaforo.setMateriasVistas(15);
        semaforo.setPromedioAcumulado(4.0f);

        assertEquals(45, semaforo.getCreditosAprobados());
        assertEquals(15, semaforo.getMateriasVistas());
        assertEquals(4.0f, semaforo.getPromedioAcumulado(), 0.01f);
        assertTrue(semaforo.esValido());

        SemaforoAcademico semaforoMal = new SemaforoAcademico("", "INVALIDO", "");
        assertFalse(semaforoMal.esValido());

        Map<String, EstadoMateria> historial = new HashMap<>();
        historial.put("MAT101", EstadoMateria.APROBADA);
        semaforo.setHistorialMaterias(historial);
        assertEquals(1, semaforo.getHistorialMaterias().size());

        SemaforoAcademico semaforoMaestria = new SemaforoAcademico("EST002", "MAESTRIA", "PLAN002");
        assertTrue(semaforoMaestria.esValido());
    }

    @Test
    public void testPeriodoCambio() {
        java.util.Date hoy = new java.util.Date();
        java.util.Date manana = new java.util.Date(hoy.getTime() + 86400000);

        PeriodoCambio periodo = new PeriodoCambio(1, "Periodo 2023-2", hoy, manana, "ACADEMICO");

        assertEquals("Periodo 2023-2", periodo.getNombre());
        assertEquals("ACADEMICO", periodo.getTipo());
        assertTrue(periodo.esValido());

        periodo.setActivo(true);
        periodo.setDescripcion("Periodo regular");
        assertTrue(periodo.isActivo());
        assertEquals("Periodo regular", periodo.getDescripcion());

        PeriodoCambio periodoMal = new PeriodoCambio(0, "", hoy, hoy, "");
        assertFalse(periodoMal.esValido());
    }

    @Test
    public void testFactoryUsuarios() {
        Estudiante est = UsuarioFactory.crearEstudiante(1, "est", "pass", "est@escuelaing.edu.co",
                1001, "Maria", "20231002", "Medicina", 2);

        Decanatura decano = UsuarioFactory.crearDecanatura(2, "decano", "pass",
                "decano@escuelaing.edu.co", 1, "Decano Luis", "Medicina");

        Decanatura decanoAdmin = UsuarioFactory.crearDecanaturaConPermisos(3, "decanoAdmin", "pass",
                "decanoAdmin@escuelaing.edu.co", 2, "Decano Admin", "Ingeniería", true);

        Profesor profesor = UsuarioFactory.crearProfesor(1002, "Profesor Carlos", "carlos@escuelaing.edu.co");

        assertEquals(Rol.ESTUDIANTE, est.getRol());
        assertEquals(Rol.DECANATURA, decano.getRol());
        assertEquals(Rol.DECANATURA, decanoAdmin.getRol());
        assertEquals(Rol.PROFESOR, profesor.getRol());
        assertEquals("Maria", est.getNombre());
        assertEquals("Decano Luis", decano.getNombre());
        assertTrue(decanoAdmin.tienePermisosAdministrador());
    }

    @Test
    public void testEnums() {
        assertNotNull(EstadoMateria.APROBADA);
        assertNotNull(EstadoSolicitud.PENDIENTE);
        assertNotNull(Rol.ESTUDIANTE);
        assertNotNull(TipoPrioridad.NORMAL);

        assertEquals(5, EstadoMateria.values().length);
        assertEquals(4, EstadoSolicitud.values().length);
        assertEquals(4, Rol.values().length);
        assertEquals(3, TipoPrioridad.values().length);
        assertEquals(3, EstadoSemaforo.values().length);
    }

    @Test
    public void testValidadoresMensajes() {
        assertEquals("Decanatura inválida: nombre, facultad e ID son requeridos", DecanaturaValidator.getErrorMessage());
        assertEquals("Materia inválida: código, nombre, créditos (1-6) y facultad son requeridos", MateriaValidator.getErrorMessage());
        assertEquals("Estudiante inválido: código, nombre, carrera y semestre válido son requeridos", EstudianteValidator.getErrorMessage());
        assertEquals("Horario inválido: día, horas válidas, salón e ID son requeridos", HorarioValidator.getErrorMessage());
        assertEquals("Usuario inválido: username, password, correo y rol son requeridos", UsuarioValidator.getErrorMessage());
    }

    @Test
    public void testSemaforoVisualizacionBuilder() {
        SemaforoVisualizacionBuilder builder = new SemaforoVisualizacionBuilder();

        List<MateriaSemaforo> azules = new ArrayList<>();
        azules.add(new MateriaSemaforo("MAT101", "Cálculo I", "CAL101", 4, "Ingeniería", true, EstadoMateria.INSCRITA));

        List<MateriaSemaforo> verdes = new ArrayList<>();
        verdes.add(new MateriaSemaforo("MAT102", "Cálculo II", "CAL102", 4, "Ingeniería", true, EstadoMateria.APROBADA));
        verdes.add(new MateriaSemaforo("MAT103", "Física", "FIS101", 3, "Ingeniería", true, EstadoMateria.APROBADA));

        List<MateriaSemaforo> rojas = new ArrayList<>();
        rojas.add(new MateriaSemaforo("MAT104", "Química", "QUI101", 3, "Ingeniería", true, EstadoMateria.REPROBADA));

        SemaforoVisualizacion semaforo = builder
                .withStudentInfo("EST001", "Juan Perez", "20231001", "Ingeniería", 4, "PREGRADO")
                .withCreditsInfo(16, 64, 96, 160, 20)
                .withSubjectsByColor(azules, verdes, rojas, new ArrayList<>())
                .withAcademicStatus(3.8f, false, "BUENO", 4)
                .build();

        assertEquals("EST001", semaforo.getEstudianteId());
        assertEquals("Juan Perez", semaforo.getNombreEstudiante());
        assertEquals(1, semaforo.getMateriasCursando());
        assertEquals(2, semaforo.getMateriasAprobadas());
        assertEquals(1, semaforo.getMateriasReprobadas());
        assertEquals(40.0f, semaforo.getPorcentajeProgreso(), 0.01f);
        assertEquals(3.8f, semaforo.getPromedioAcumulado(), 0.01f);
        assertFalse(semaforo.isPuedeGraduarse());
    }
}