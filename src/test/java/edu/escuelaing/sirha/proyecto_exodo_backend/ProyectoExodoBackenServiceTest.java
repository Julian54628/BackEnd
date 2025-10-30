package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProyectoExodoBackenServiceTest {

    @Mock private RepositorioHorario repositorioHorario;
    @Mock private RepositorioPeriodoCambio repositorioPeriodoCambio;
    @Mock private RepositorioSolicitudCambio repositorioSolicitudCambio;
    @Mock private RepositorioGrupo repositorioGrupo;
    @Mock private RepositorioMateria repositorioMateria;
    @Mock private RepositorioEstudiante repositorioEstudiante;
    @Mock private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    @Mock private RepositorioDecanatura repositorioDecanatura;

    @InjectMocks private HorarioServiceImpl horarioService;
    @InjectMocks private PeriodoCambioServiceImpl periodoService;
    @InjectMocks private SolicitudCambioServiceImpl solicitudService;
    @InjectMocks private GrupoServiceImpl grupoService;
    @InjectMocks private MateriaServiceImpl materiaService;

    private Horario horario;
    private PeriodoCambio periodo;
    private SolicitudCambio solicitud;
    private Grupo grupo;
    private Materia materia;

    @BeforeEach
    void setUp() {
        horario = new Horario();
        horario.setId("h1");
        horario.setIdHorario(1);
        horario.setDiaSemana("Lunes");
        horario.setHoraInicio(LocalTime.parse("08:00:00"));
        horario.setHoraFin(LocalTime.parse("10:00:00"));
        horario.setSalon("A101");
        horario.setMateriaId("mat1");
        horario.setGrupoId("grp1");

        periodo = new PeriodoCambio();
        periodo.setId("p1");
        periodo.setIdPeriodo(1);
        periodo.setNombre("Periodo 2024-1");
        periodo.setFechaInicio(new Date());
        periodo.setFechaFin(new Date(System.currentTimeMillis() + 86400000));
        periodo.setActivo(true);
        periodo.setTipo("CAMBIOS");

        solicitud = new SolicitudCambio();
        solicitud.setId("sol1");
        solicitud.setEstudianteId("est1");
        solicitud.setMateriaOrigenId("mat1");
        solicitud.setMateriaDestinoId("mat2");
        solicitud.setGrupoOrigenId("grp1");
        solicitud.setGrupoDestinoId("grp2");
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        solicitud.setTipoPrioridad(TipoPrioridad.NORMAL);

        grupo = new Grupo();
        grupo.setId("grp1");
        grupo.setIdGrupo(1);
        grupo.setCupoMaximo(30);
        grupo.setMateriaId("mat1");
        grupo.setEstudiantesInscritosIds(new ArrayList<>());

        materia = new Materia();
        materia.setId("mat1");
        materia.setCodigo("ISIS1001");
        materia.setNombre("Programacion");
        materia.setCreditos(4);
        materia.setFacultad("Ingenieria");
    }

    // ============ HORARIO SERVICE TESTS ============
    @Test
    void testHorarioServiceCrear() {
        when(repositorioHorario.save(any(Horario.class))).thenReturn(horario);
        Horario resultado = horarioService.crear(horario);
        assertNotNull(resultado);
        verify(repositorioHorario, times(1)).save(horario);
    }

    @Test
    void testHorarioServiceBuscarPorId() {
        when(repositorioHorario.findById("h1")).thenReturn(Optional.of(horario));
        Optional<Horario> resultado = horarioService.buscarPorId("h1");
        assertTrue(resultado.isPresent());
    }

    @Test
    void testHorarioServiceListarTodos() {
        when(repositorioHorario.findAll()).thenReturn(Arrays.asList(horario));
        List<Horario> resultado = horarioService.listarTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    void testHorarioServiceActualizar() {
        when(repositorioHorario.existsById("h1")).thenReturn(true);
        when(repositorioHorario.save(any(Horario.class))).thenReturn(horario);
        Horario resultado = horarioService.actualizar("h1", horario);
        assertNotNull(resultado);
    }

    @Test
    void testHorarioServiceActualizarNoExiste() {
        when(repositorioHorario.existsById("h99")).thenReturn(false);
        assertThrows(IllegalArgumentException.class,
                () -> horarioService.actualizar("h99", horario));
    }

    @Test
    void testHorarioServiceEliminar() {
        doNothing().when(repositorioHorario).deleteById("h1");
        horarioService.eliminarPorId("h1");
        verify(repositorioHorario, times(1)).deleteById("h1");
    }

    @Test
    void testHorarioServiceConsultarPorGrupo() {
        when(repositorioHorario.findByGrupoId("grp1")).thenReturn(Arrays.asList(horario));
        List<Horario> resultado = horarioService.consultarHorariosPorGrupo("grp1");
        assertEquals(1, resultado.size());
    }

    @Test
    void testHorarioServiceConsultarPorDia() {
        when(repositorioHorario.findByDiaSemanaIgnoreCase("Lunes")).thenReturn(Arrays.asList(horario));
        List<Horario> resultado = horarioService.consultarHorariosPorDia("Lunes");
        assertEquals(1, resultado.size());
    }

    @Test
    void testHorarioServiceConsultarPorSalon() {
        when(repositorioHorario.findBySalon("A101")).thenReturn(Arrays.asList(horario));
        List<Horario> resultado = horarioService.consultarHorariosPorSalon("A101");
        assertEquals(1, resultado.size());
    }

    // ============ PERIODO SERVICE TESTS ============
    @Test
    void testPeriodoServiceCrear() {
        when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodo);
        PeriodoCambio resultado = periodoService.crear(periodo);
        assertNotNull(resultado);
    }

    @Test
    void testPeriodoServiceBuscarPorId() {
        when(repositorioPeriodoCambio.findById("p1")).thenReturn(Optional.of(periodo));
        Optional<PeriodoCambio> resultado = periodoService.buscarPorId("p1");
        assertTrue(resultado.isPresent());
    }

    @Test
    void testPeriodoServiceListarTodos() {
        when(repositorioPeriodoCambio.findAll()).thenReturn(Arrays.asList(periodo));
        List<PeriodoCambio> resultado = periodoService.listarTodos();
        assertEquals(1, resultado.size());
    }

    @Test
    void testPeriodoServiceActualizar() {
        when(repositorioPeriodoCambio.existsById("p1")).thenReturn(true);
        when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodo);
        PeriodoCambio resultado = periodoService.actualizar("p1", periodo);
        assertNotNull(resultado);
    }

    @Test
    void testPeriodoServiceEliminar() {
        doNothing().when(repositorioPeriodoCambio).deleteById("p1");
        periodoService.eliminarPorId("p1");
        verify(repositorioPeriodoCambio, times(1)).deleteById("p1");
    }

    // ============ SOLICITUD SERVICE TESTS ============
    @Test
    void testSolicitudServiceRechazar() {
        when(repositorioSolicitudCambio.findById("sol1")).thenReturn(Optional.of(solicitud));
        when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitud);
        SolicitudCambio resultado = solicitudService.rechazarSolicitud("sol1", "Rechazada");
        assertEquals(EstadoSolicitud.RECHAZADA, resultado.getEstado());
    }

    @Test
    void testSolicitudServiceObtenerPorEstudiante() {
        when(repositorioSolicitudCambio.findByEstudianteId("est1")).thenReturn(Arrays.asList(solicitud));
        List<SolicitudCambio> resultado = solicitudService.obtenerSolicitudesPorEstudiante("est1");
        assertEquals(1, resultado.size());
    }

    @Test
    void testSolicitudServiceObtenerPorDecanatura() {
        when(repositorioSolicitudCambio.findByDecanaturaId("dec1")).thenReturn(Arrays.asList(solicitud));
        List<SolicitudCambio> resultado = solicitudService.obtenerSolicitudesPorDecanatura("dec1");
        assertEquals(1, resultado.size());
    }

    @Test
    void testSolicitudServiceObtenerPorPrioridad() {
        when(repositorioSolicitudCambio.findByTipoPrioridad(TipoPrioridad.NORMAL))
                .thenReturn(Arrays.asList(solicitud));
        List<SolicitudCambio> resultado = solicitudService.obtenerSolicitudesPorPrioridad(TipoPrioridad.NORMAL);
        assertEquals(1, resultado.size());
    }

    @Test
    void testSolicitudServiceHistorial() {
        when(repositorioSolicitudCambio.findAllByOrderByFechaCreacionDesc())
                .thenReturn(Arrays.asList(solicitud));
        List<SolicitudCambio> resultado = solicitudService.obtenerHistorialSolicitudes();
        assertEquals(1, resultado.size());
    }

    @Test
    void testSolicitudServiceEstadisticas() {
        when(repositorioSolicitudCambio.findAll()).thenReturn(Arrays.asList(solicitud));
        Map<String, Object> resultado = solicitudService.obtenerEstadisticasSolicitudes();
        assertNotNull(resultado);
        assertTrue(resultado.containsKey("totalSolicitudes"));
    }

    @Test
    void testSolicitudServiceValidarInvalido() {
        SolicitudCambio invalida = new SolicitudCambio();
        boolean resultado = solicitudService.validarSolicitud(invalida);
        assertFalse(resultado);
    }

    @Test
    void testSolicitudServicePuedeCrearSolicitud() {
        when(repositorioSolicitudCambio.findByEstudianteIdAndMateriaDestinoIdAndEstadoIn(
                anyString(), anyString(), anyList())).thenReturn(Collections.emptyList());
        boolean resultado = solicitudService.puedeCrearSolicitud("est1", "mat1");
        assertTrue(resultado);
    }

    // ============ GRUPO SERVICE TESTS ============
    @Test
    void testGrupoServiceActualizarCupo() {
        when(repositorioGrupo.findById("grp1")).thenReturn(Optional.of(grupo));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);
        Grupo resultado = grupoService.actualizarCupo("grp1", 35);
        assertEquals(35, resultado.getCupoMaximo());
    }

    @Test
    void testGrupoServiceActualizarCupoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> grupoService.actualizarCupo("grp1", 0));
    }

    @Test
    void testGrupoServiceConsultarEstudiantesInscritos() {
        grupo.getEstudiantesInscritosIds().add("est1");
        when(repositorioGrupo.findById("grp1")).thenReturn(Optional.of(grupo));
        when(repositorioEstudiante.findById("est1")).thenReturn(Optional.of(new Estudiante()));
        List<Estudiante> resultado = grupoService.consultarEstudiantesInscritos("grp1");
        assertEquals(1, resultado.size());
    }

    @Test
    void testGrupoServiceBuscarPorMateria() {
        when(repositorioGrupo.findByMateriaId("mat1")).thenReturn(Arrays.asList(grupo));
        List<Grupo> resultado = grupoService.buscarPorMateria("mat1");
        assertEquals(1, resultado.size());
    }

    @Test
    void testGrupoServiceBuscarPorProfesor() {
        when(repositorioGrupo.findByProfesorId("prof1")).thenReturn(Arrays.asList(grupo));
        List<Grupo> resultado = grupoService.buscarPorProfesor("prof1");
        assertEquals(1, resultado.size());
    }

    @Test
    void testGrupoServiceEliminar() {
        doNothing().when(repositorioGrupo).deleteById("grp1");
        grupoService.eliminarPorId("grp1");
        verify(repositorioGrupo, times(1)).deleteById("grp1");
    }

    @Test
    void testGrupoServiceObtenerGruposConAlerta() {
        grupo.getEstudiantesInscritosIds().addAll(Arrays.asList("e1", "e2", "e3"));
        grupo.setCupoMaximo(3);
        when(repositorioGrupo.findAll()).thenReturn(Arrays.asList(grupo));
        List<Grupo> resultado = grupoService.obtenerGruposConAlertaCapacidad(90.0);
        assertEquals(1, resultado.size());
    }

    @Test
    void testMateriaServiceEliminar() {
        doNothing().when(repositorioMateria).deleteById("mat1");
        materiaService.eliminarPorId("mat1");
        verify(repositorioMateria, times(1)).deleteById("mat1");
    }

    @Test
    void testMateriaServiceVerificarDisponibilidad() {
        when(repositorioGrupo.findByMateriaId("mat1")).thenReturn(Arrays.asList(grupo));
        boolean resultado = materiaService.verificarDisponibilidad("mat1");
        assertTrue(resultado);
    }

    @Test
    void testMateriaServiceConsultarTotalInscritos() {
        grupo.getEstudiantesInscritosIds().addAll(Arrays.asList("e1", "e2"));
        when(repositorioGrupo.findByMateriaId("mat1")).thenReturn(Arrays.asList(grupo));
        int resultado = materiaService.consultarTotalInscritosPorMateria("mat1");
        assertEquals(2, resultado);
    }

    @Test
    void testMateriaServiceModificarCupos() {
        when(repositorioGrupo.findByMateriaId("mat1")).thenReturn(Arrays.asList(grupo));
        when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);
        materiaService.modificarCuposMateria("mat1", 40);
        verify(repositorioGrupo, times(1)).save(any(Grupo.class));
    }

    @Test
    void testMateriaServiceInscribirCupoLleno() {
        grupo.setCupoMaximo(2);
        grupo.getEstudiantesInscritosIds().addAll(Arrays.asList("e1", "e2"));
        when(repositorioGrupo.findById("grp1")).thenReturn(Optional.of(grupo));
        assertThrows(IllegalArgumentException.class,
                () -> materiaService.inscribirEstudianteEnGrupo("grp1", "e3"));
    }
}