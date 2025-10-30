package edu.escuelaing.sirha.proyecto_exodo_backend;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.service.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Servicios - Proyecto Exodo Backend")
class ProyectoExodoBackenServiceTest {

    @Nested
    @DisplayName("AdministradorService Tests")
    class AdministradorServiceTests {

        @Mock private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
        @Mock private RepositorioAdministrador repositorioAdministrador;
        @Mock private RepositorioDecanatura repositorioDecanatura;
        @Mock private RepositorioGrupo repositorioGrupo;
        @Mock private RepositorioPeriodoCambio repositorioPeriodoCambio;
        @Mock private RepositorioSolicitudCambio repositorioSolicitudCambio;

        @InjectMocks
        private AdministradorServiceImpl administradorService;

        @Test
        @DisplayName("Debe modificar estado de materia en semáforo")
        void debeModificarEstadoMateriaSemaforo() {
            String estudianteId = "est1";
            String materiaId = "mat1";
            EstadoMateria nuevoEstado = EstadoMateria.APROBADA;

            SemaforoAcademico semaforo = new SemaforoAcademico();
            semaforo.setEstudianteId(estudianteId);
            semaforo.setHistorialMaterias(new HashMap<>());

            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));
            when(repositorioSemaforoAcademico.save(any(SemaforoAcademico.class)))
                    .thenReturn(semaforo);

            Optional<SemaforoAcademico> resultado = administradorService
                    .modificarEstadoMateriaSemaforo(estudianteId, materiaId, nuevoEstado);

            assertTrue(resultado.isPresent());
            assertEquals(nuevoEstado, resultado.get().getHistorialMaterias().get(materiaId));
        }

        @Test
        @DisplayName("Debe crear administrador")
        void debeCrearAdministrador() {
            Administrador admin = new Administrador();
            admin.setId("admin1");

            when(repositorioAdministrador.save(any(Administrador.class))).thenReturn(admin);

            Administrador resultado = administradorService.crear(admin);

            assertNotNull(resultado);
            assertEquals("admin1", resultado.getId());
        }

        @Test
        @DisplayName("Debe buscar administrador por ID")
        void debeBuscarAdministradorPorId() {
            String id = "admin1";
            Administrador admin = new Administrador();
            admin.setId(id);

            when(repositorioAdministrador.findById(id)).thenReturn(Optional.of(admin));

            Optional<Administrador> resultado = administradorService.buscarPorId(id);

            assertTrue(resultado.isPresent());
            assertEquals(id, resultado.get().getId());
        }

        @Test
        @DisplayName("Debe listar todos los administradores")
        void debeListarTodosAdministradores() {
            List<Administrador> admins = Arrays.asList(new Administrador(), new Administrador());
            when(repositorioAdministrador.findAll()).thenReturn(admins);

            List<Administrador> resultado = administradorService.listarTodos();

            assertEquals(2, resultado.size());
        }

        @Test
        @DisplayName("Debe modificar cupo de grupo")
        void debeModificarCupoGrupo() {
            String grupoId = "grupo1";
            int nuevoCupo = 30;

            Grupo grupo = new Grupo();
            grupo.setId(grupoId);
            grupo.setCupoMaximo(25);
            grupo.setEstudiantesInscritosIds(new ArrayList<>());

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = administradorService.modificarCupoGrupo(grupoId, nuevoCupo);

            assertEquals(nuevoCupo, resultado.getCupoMaximo());
        }

        @Test
        @DisplayName("Debe lanzar excepción si cupo inválido")
        void debeLanzarExcepcionCupoInvalido() {
            assertThrows(IllegalArgumentException.class,
                    () -> administradorService.modificarCupoGrupo("grupo1", 0));
            assertThrows(IllegalArgumentException.class,
                    () -> administradorService.modificarCupoGrupo("grupo1", 51));
        }

        @Test
        @DisplayName("Debe configurar período")
        void debeConfigurarPeriodo() {
            PeriodoCambio periodo = new PeriodoCambio();
            when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodo);

            PeriodoCambio resultado = administradorService.configurarPeriodo(periodo);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe generar reportes")
        void debeGenerarReportes() {
            List<SolicitudCambio> solicitudes = Arrays.asList(new SolicitudCambio());
            when(repositorioSolicitudCambio.findAll()).thenReturn(solicitudes);

            List<SolicitudCambio> resultado = administradorService.generarReportes();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe listar casos excepcionales")
        void debeListarCasosExcepcionales() {
            List<SolicitudCambio> solicitudes = Arrays.asList(new SolicitudCambio());
            when(repositorioSolicitudCambio.findAll()).thenReturn(solicitudes);

            List<SolicitudCambio> resultado = administradorService.listCasosExcepcionales();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe aprobar caso especial")
        void debeAprobarCasoEspecial() {
            Long id = 1L;
            SolicitudCambio solicitud = new SolicitudCambio();

            when(repositorioSolicitudCambio.findById("1")).thenReturn(Optional.of(solicitud));
            when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitud);

            Object resultado = administradorService.aprobarCasoEspecial(id, new HashMap<>());

            assertNotNull(resultado);
        }
    }

    @Nested
    @DisplayName("DecanaturaService Tests")
    class DecanaturaServiceTests {

        @Mock private RepositorioDecanatura repositorioDecanatura;
        @Mock private RepositorioSolicitudCambio repositorioSolicitudCambio;

        @InjectMocks
        private DecanaturaServiceImpl decanaturaService;

        @Test
        @DisplayName("Debe crear decanatura")
        void debeCrearDecanatura() {
            Decanatura decanatura = new Decanatura();
            decanatura.setId("dec1");

            when(repositorioDecanatura.save(any(Decanatura.class))).thenReturn(decanatura);

            Decanatura resultado = decanaturaService.crear(decanatura);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar decanatura por ID")
        void debeBuscarPorId() {
            String id = "dec1";
            Decanatura decanatura = new Decanatura();

            when(repositorioDecanatura.findById(id)).thenReturn(Optional.of(decanatura));

            Optional<Decanatura> resultado = decanaturaService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todas las decanaturas")
        void debeListarTodos() {
            List<Decanatura> decanaturas = Arrays.asList(new Decanatura());
            when(repositorioDecanatura.findAll()).thenReturn(decanaturas);

            List<Decanatura> resultado = decanaturaService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe actualizar decanatura")
        void debeActualizar() {
            String id = "dec1";
            Decanatura decanatura = new Decanatura();

            when(repositorioDecanatura.existsById(id)).thenReturn(true);
            when(repositorioDecanatura.save(any(Decanatura.class))).thenReturn(decanatura);

            Decanatura resultado = decanaturaService.actualizar(id, decanatura);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe eliminar decanatura")
        void debeEliminar() {
            String id = "dec1";

            assertDoesNotThrow(() -> decanaturaService.eliminarPorId(id));
            verify(repositorioDecanatura).deleteById(id);
        }

        @Test
        @DisplayName("Debe consultar solicitudes pendientes")
        void debeConsultarSolicitudesPendientes() {
            List<SolicitudCambio> solicitudes = Arrays.asList(new SolicitudCambio());
            when(repositorioSolicitudCambio.findByEstado(EstadoSolicitud.PENDIENTE))
                    .thenReturn(solicitudes);

            List<SolicitudCambio> resultado = decanaturaService.consultarSolicitudesPendientes();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe revisar solicitud")
        void debeRevisarSolicitud() {
            String solicitudId = "sol1";
            SolicitudCambio solicitud = new SolicitudCambio();

            when(repositorioSolicitudCambio.findById(solicitudId)).thenReturn(Optional.of(solicitud));
            when(repositorioSolicitudCambio.save(any(SolicitudCambio.class))).thenReturn(solicitud);

            SolicitudCambio resultado = decanaturaService.revisarSolicitud(
                    solicitudId, EstadoSolicitud.APROBADA, "OK");

            assertEquals(EstadoSolicitud.APROBADA, resultado.getEstado());
        }

        @Test
        @DisplayName("Debe aprobar solicitud especial")
        void debeAprobarSolicitudEspecial() {
            String solicitudId = "sol1";
            SolicitudCambio solicitud = new SolicitudCambio();

            when(repositorioSolicitudCambio.findById(solicitudId)).thenReturn(Optional.of(solicitud));

            assertDoesNotThrow(() -> decanaturaService.aprobarSolicitudEspecial(solicitudId));
        }

        @Test
        @DisplayName("Debe otorgar permisos administrador")
        void debeOtorgarPermisos() {
            String decanaturaId = "dec1";
            Decanatura decanatura = new Decanatura();

            when(repositorioDecanatura.findById(decanaturaId)).thenReturn(Optional.of(decanatura));
            when(repositorioDecanatura.save(any(Decanatura.class))).thenReturn(decanatura);

            Decanatura resultado = decanaturaService.otorgarPermisosAdministrador(decanaturaId);

            assertTrue(resultado.isEsAdministrador());
        }

        @Test
        @DisplayName("Debe revocar permisos administrador")
        void debeRevocarPermisos() {
            String decanaturaId = "dec1";
            Decanatura decanatura = new Decanatura();

            when(repositorioDecanatura.findById(decanaturaId)).thenReturn(Optional.of(decanatura));
            when(repositorioDecanatura.save(any(Decanatura.class))).thenReturn(decanatura);

            Decanatura resultado = decanaturaService.revocarPermisosAdministrador(decanaturaId);

            assertFalse(resultado.isEsAdministrador());
        }

        @Test
        @DisplayName("Debe consultar tasa aprobación/rechazo")
        void debeConsultarTasas() {
            String decanaturaId = "dec1";
            SolicitudCambio sol1 = new SolicitudCambio();
            sol1.setEstado(EstadoSolicitud.APROBADA);

            when(repositorioSolicitudCambio.findByDecanaturaId(decanaturaId))
                    .thenReturn(Arrays.asList(sol1));

            Map<String, Object> resultado = decanaturaService.consultarTasaAprobacionRechazo(decanaturaId);

            assertNotNull(resultado);
            assertEquals(1L, resultado.get("totalSolicitudes"));
        }
    }

    @Nested
    @DisplayName("EstudianteService Tests")
    class EstudianteServiceTests {

        @Mock private RepositorioEstudiante repositorioEstudiante;
        @Mock private RepositorioSolicitudCambio repositorioSolicitudCambio;
        @Mock private SemaforoAcademicoService semaforoAcademicoService;

        @InjectMocks
        private EstudianteServiceImpl estudianteService;

        @Test
        @DisplayName("Debe crear estudiante")
        void debeCrearEstudiante() {
            Estudiante estudiante = new Estudiante();
            estudiante.setCodigo("EST001");

            when(repositorioEstudiante.existsByCodigo(anyString())).thenReturn(false);
            when(repositorioEstudiante.save(any(Estudiante.class))).thenReturn(estudiante);

            Estudiante resultado = estudianteService.crear(estudiante);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por código")
        void debeBuscarPorCodigo() {
            String codigo = "EST001";
            Estudiante estudiante = new Estudiante();

            when(repositorioEstudiante.findByCodigo(codigo)).thenReturn(Optional.of(estudiante));

            Optional<Estudiante> resultado = estudianteService.buscarPorCodigo(codigo);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "est1";
            Estudiante estudiante = new Estudiante();

            when(repositorioEstudiante.findById(id)).thenReturn(Optional.of(estudiante));

            Optional<Estudiante> resultado = estudianteService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todos")
        void debeListarTodos() {
            List<Estudiante> estudiantes = Arrays.asList(new Estudiante());
            when(repositorioEstudiante.findAll()).thenReturn(estudiantes);

            List<Estudiante> resultado = estudianteService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe eliminar estudiante")
        void debeEliminar() {
            String id = "est1";

            assertDoesNotThrow(() -> estudianteService.eliminarPorId(id));
            verify(repositorioEstudiante).deleteById(id);
        }

        @Test
        @DisplayName("Debe actualizar estudiante")
        void debeActualizar() {
            String id = "est1";
            Estudiante estudiante = new Estudiante();

            when(repositorioEstudiante.existsById(id)).thenReturn(true);
            when(repositorioEstudiante.save(any(Estudiante.class))).thenReturn(estudiante);

            Estudiante resultado = estudianteService.actualizar(id, estudiante);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe crear solicitud de cambio")
        void debeCrearSolicitudCambio() {
            String estudianteId = "est1";

            when(repositorioEstudiante.existsById(estudianteId)).thenReturn(true);
            when(repositorioSolicitudCambio.save(any(SolicitudCambio.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            SolicitudCambio resultado = estudianteService.crearSolicitudCambio(
                    estudianteId, "mat1", "grp1", "mat2", "grp2");

            assertNotNull(resultado);
            assertEquals(EstadoSolicitud.PENDIENTE, resultado.getEstado());
        }

        @Test
        @DisplayName("Debe consultar solicitudes")
        void debeConsultarSolicitudes() {
            String estudianteId = "est1";
            List<SolicitudCambio> solicitudes = Arrays.asList(new SolicitudCambio());

            when(repositorioSolicitudCambio.findByEstudianteId(estudianteId))
                    .thenReturn(solicitudes);

            List<SolicitudCambio> resultado = estudianteService.consultarSolicitudes(estudianteId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe buscar por carrera")
        void debeBuscarPorCarrera() {
            String carrera = "Ingeniería";
            List<Estudiante> estudiantes = Arrays.asList(new Estudiante());

            when(repositorioEstudiante.findByCarrera(carrera)).thenReturn(estudiantes);

            List<Estudiante> resultado = estudianteService.buscarPorCarrera(carrera);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe buscar por semestre")
        void debeBuscarPorSemestre() {
            int semestre = 5;
            List<Estudiante> estudiantes = Arrays.asList(new Estudiante());

            when(repositorioEstudiante.findBySemestre(semestre)).thenReturn(estudiantes);

            List<Estudiante> resultado = estudianteService.buscarPorSemestre(semestre);

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("GrupoService Tests")
    class GrupoServiceTests {

        @Mock private RepositorioGrupo repositorioGrupo;
        @Mock private RepositorioEstudiante repositorioEstudiante;

        @InjectMocks
        private GrupoServiceImpl grupoService;

        @Test
        @DisplayName("Debe crear grupo")
        void debeCrearGrupo() {
            Grupo grupo = new Grupo();
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = grupoService.crear(grupo);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "grp1";
            Grupo grupo = new Grupo();

            when(repositorioGrupo.findById(id)).thenReturn(Optional.of(grupo));

            Optional<Grupo> resultado = grupoService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todos")
        void debeListarTodos() {
            List<Grupo> grupos = Arrays.asList(new Grupo());
            when(repositorioGrupo.findAll()).thenReturn(grupos);

            List<Grupo> resultado = grupoService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe eliminar grupo")
        void debeEliminar() {
            String id = "grp1";

            assertDoesNotThrow(() -> grupoService.eliminarPorId(id));
            verify(repositorioGrupo).deleteById(id);
        }

        @Test
        @DisplayName("Debe actualizar cupo")
        void debeActualizarCupo() {
            String grupoId = "grp1";
            int nuevoCupo = 30;

            Grupo grupo = new Grupo();
            grupo.setEstudiantesInscritosIds(new ArrayList<>());

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = grupoService.actualizarCupo(grupoId, nuevoCupo);

            assertEquals(nuevoCupo, resultado.getCupoMaximo());
        }

        @Test
        @DisplayName("Debe verificar cupo disponible")
        void debeVerificarCupo() {
            String grupoId = "grp1";
            Grupo grupo = new Grupo();
            grupo.setCupoMaximo(30);
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1"));

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));

            boolean resultado = grupoService.verificarCupoDisponible(grupoId);

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe consultar carga académica")
        void debeConsultarCarga() {
            String grupoId = "grp1";
            Grupo grupo = new Grupo();
            grupo.setCupoMaximo(30);
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1", "est2", "est3"));

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));

            float carga = grupoService.consultarCargaAcademica(grupoId);

            assertEquals(10.0f, carga, 0.01);
        }

        @Test
        @DisplayName("Debe consultar estudiantes inscritos")
        void debeConsultarEstudiantes() {
            String grupoId = "grp1";
            Grupo grupo = new Grupo();
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1"));

            Estudiante est = new Estudiante();

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioEstudiante.findById("est1")).thenReturn(Optional.of(est));

            List<Estudiante> resultado = grupoService.consultarEstudiantesInscritos(grupoId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe buscar por materia")
        void debeBuscarPorMateria() {
            String materiaId = "mat1";
            List<Grupo> grupos = Arrays.asList(new Grupo());

            when(repositorioGrupo.findByMateriaId(materiaId)).thenReturn(grupos);

            List<Grupo> resultado = grupoService.buscarPorMateria(materiaId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe buscar por profesor")
        void debeBuscarPorProfesor() {
            String profesorId = "prof1";
            List<Grupo> grupos = Arrays.asList(new Grupo());

            when(repositorioGrupo.findByProfesorId(profesorId)).thenReturn(grupos);

            List<Grupo> resultado = grupoService.buscarPorProfesor(profesorId);

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("HorarioService Tests")
    class HorarioServiceTests {

        @Mock private RepositorioHorario repositorioHorario;

        @InjectMocks
        private HorarioServiceImpl horarioService;

        @Test
        @DisplayName("Debe crear horario")
        void debeCrear() {
            Horario horario = new Horario();
            when(repositorioHorario.save(any(Horario.class))).thenReturn(horario);

            Horario resultado = horarioService.crear(horario);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "hor1";
            Horario horario = new Horario();

            when(repositorioHorario.findById(id)).thenReturn(Optional.of(horario));

            Optional<Horario> resultado = horarioService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todos")
        void debeListarTodos() {
            List<Horario> horarios = Arrays.asList(new Horario());
            when(repositorioHorario.findAll()).thenReturn(horarios);

            List<Horario> resultado = horarioService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe actualizar horario")
        void debeActualizar() {
            String id = "hor1";
            Horario horario = new Horario();

            when(repositorioHorario.existsById(id)).thenReturn(true);
            when(repositorioHorario.save(any(Horario.class))).thenReturn(horario);

            Horario resultado = horarioService.actualizar(id, horario);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe eliminar horario")
        void debeEliminar() {
            String id = "hor1";

            assertDoesNotThrow(() -> horarioService.eliminarPorId(id));
            verify(repositorioHorario).deleteById(id);
        }

        @Test
        @DisplayName("Debe consultar por grupo")
        void debeConsultarPorGrupo() {
            String grupoId = "grp1";
            List<Horario> horarios = Arrays.asList(new Horario());

            when(repositorioHorario.findByGrupoId(grupoId)).thenReturn(horarios);

            List<Horario> resultado = horarioService.consultarHorariosPorGrupo(grupoId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe consultar por día")
        void debeConsultarPorDia() {
            String dia = "Lunes";
            List<Horario> horarios = Arrays.asList(new Horario());

            when(repositorioHorario.findByDiaSemanaIgnoreCase(dia)).thenReturn(horarios);

            List<Horario> resultado = horarioService.consultarHorariosPorDia(dia);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe consultar por salón")
        void debeConsultarPorSalon() {
            String salon = "A101";
            List<Horario> horarios = Arrays.asList(new Horario());

            when(repositorioHorario.findBySalon(salon)).thenReturn(horarios);

            List<Horario> resultado = horarioService.consultarHorariosPorSalon(salon);

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("MateriaService Tests")
    class MateriaServiceTests {

        @Mock private RepositorioMateria repositorioMateria;
        @Mock private RepositorioGrupo repositorioGrupo;
        @Mock private RepositorioEstudiante repositorioEstudiante;

        @InjectMocks
        private MateriaServiceImpl materiaService;

        @Test
        @DisplayName("Debe crear materia")
        void debeCrear() {
            Materia materia = new Materia();
            materia.setCodigo("MAT001");

            when(repositorioMateria.existsByCodigo(anyString())).thenReturn(false);
            when(repositorioMateria.save(any(Materia.class))).thenReturn(materia);

            Materia resultado = materiaService.crear(materia);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "mat1";
            Materia materia = new Materia();

            when(repositorioMateria.findById(id)).thenReturn(Optional.of(materia));

            Optional<Materia> resultado = materiaService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe buscar por código")
        void debeBuscarPorCodigo() {
            String codigo = "MAT001";
            Materia materia = new Materia();

            when(repositorioMateria.findByCodigo(codigo)).thenReturn(Optional.of(materia));

            Optional<Materia> resultado = materiaService.buscarPorCodigo(codigo);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todas")
        void debeListarTodas() {
            List<Materia> materias = Arrays.asList(new Materia());
            when(repositorioMateria.findAll()).thenReturn(materias);

            List<Materia> resultado = materiaService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe eliminar materia")
        void debeEliminar() {
            String id = "mat1";

            assertDoesNotThrow(() -> materiaService.eliminarPorId(id));
            verify(repositorioMateria).deleteById(id);
        }

        @Test
        @DisplayName("Debe actualizar materia")
        void debeActualizar() {
            String id = "mat1";
            Materia materia = new Materia();

            when(repositorioMateria.existsById(id)).thenReturn(true);
            when(repositorioMateria.save(any(Materia.class))).thenReturn(materia);

            Materia resultado = materiaService.actualizar(id, materia);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe consultar grupos disponibles")
        void debeConsultarGruposDisponibles() {
            String materiaId = "mat1";
            Grupo grupo = new Grupo();
            grupo.setCupoMaximo(30);
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1"));

            when(repositorioGrupo.findByMateriaId(materiaId)).thenReturn(Arrays.asList(grupo));

            List<Grupo> resultado = materiaService.consultarGruposDisponibles(materiaId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe verificar disponibilidad")
        void debeVerificarDisponibilidad() {
            String materiaId = "mat1";
            Grupo grupo = new Grupo();
            grupo.setCupoMaximo(30);
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1"));

            when(repositorioGrupo.findByMateriaId(materiaId)).thenReturn(Arrays.asList(grupo));

            boolean resultado = materiaService.verificarDisponibilidad(materiaId);

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe modificar cupos de materia")
        void debeModificarCupos() {
            String materiaId = "mat1";
            Grupo grupo = new Grupo();
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1"));

            when(repositorioGrupo.findByMateriaId(materiaId)).thenReturn(Arrays.asList(grupo));

            assertDoesNotThrow(() -> materiaService.modificarCuposMateria(materiaId, 35));
        }

        @Test
        @DisplayName("Debe registrar materia con grupos")
        void debeRegistrarConGrupos() {
            Materia materia = new Materia();
            materia.setGruposIds(new ArrayList<>());
            Grupo grupo = new Grupo();
            grupo.setId("grp1");

            when(repositorioMateria.save(any(Materia.class))).thenReturn(materia);
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Materia resultado = materiaService.registrarMateriaConGrupos(materia, Arrays.asList(grupo));

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe consultar total inscritos")
        void debeConsultarTotalInscritos() {
            String materiaId = "mat1";
            Grupo grupo = new Grupo();
            grupo.setEstudiantesInscritosIds(Arrays.asList("est1", "est2"));

            when(repositorioGrupo.findByMateriaId(materiaId)).thenReturn(Arrays.asList(grupo));

            int total = materiaService.consultarTotalInscritosPorMateria(materiaId);

            assertEquals(2, total);
        }

        @Test
        @DisplayName("Debe inscribir estudiante en grupo")
        void debeInscribirEstudiante() {
            String grupoId = "grp1";
            String estudianteId = "est1";

            Grupo grupo = new Grupo();
            grupo.setCupoMaximo(30);
            grupo.setEstudiantesInscritosIds(new ArrayList<>());
            Estudiante estudiante = new Estudiante();

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioEstudiante.findById(estudianteId)).thenReturn(Optional.of(estudiante));
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = materiaService.inscribirEstudianteEnGrupo(grupoId, estudianteId);

            assertTrue(resultado.getEstudiantesInscritosIds().contains(estudianteId));
        }

        @Test
        @DisplayName("Debe retirar estudiante de grupo")
        void debeRetirarEstudiante() {
            String grupoId = "grp1";
            String estudianteId = "est1";

            Grupo grupo = new Grupo();
            grupo.setEstudiantesInscritosIds(new ArrayList<>(Arrays.asList(estudianteId)));

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = materiaService.retirarEstudianteDeGrupo(grupoId, estudianteId);

            assertFalse(resultado.getEstudiantesInscritosIds().contains(estudianteId));
        }

        @Test
        @DisplayName("Debe asignar materia a estudiante")
        void debeAsignarMateria() {
            String materiaId = "mat1";
            String estudianteId = "est1";

            when(repositorioMateria.existsById(materiaId)).thenReturn(true);
            when(repositorioEstudiante.existsById(estudianteId)).thenReturn(true);

            boolean resultado = materiaService.asignarMateriaAEstudiante(materiaId, estudianteId);

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe buscar por facultad")
        void debeBuscarPorFacultad() {
            String facultad = "Ingeniería";
            List<Materia> materias = Arrays.asList(new Materia());

            when(repositorioMateria.findByFacultad(facultad)).thenReturn(materias);

            List<Materia> resultado = materiaService.buscarPorFacultad(facultad);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe buscar por créditos")
        void debeBuscarPorCreditos() {
            int creditos = 3;
            List<Materia> materias = Arrays.asList(new Materia());

            when(repositorioMateria.findByCreditos(creditos)).thenReturn(materias);

            List<Materia> resultado = materiaService.buscarPorCreditos(creditos);

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("PeriodoCambioService Tests")
    class PeriodoCambioServiceTests {

        @Mock private RepositorioPeriodoCambio repositorioPeriodoCambio;

        @InjectMocks
        private PeriodoCambioServiceImpl periodoService;

        @Test
        @DisplayName("Debe crear período")
        void debeCrear() {
            PeriodoCambio periodo = new PeriodoCambio();
            periodo.setFechaInicio(new Date());
            periodo.setFechaFin(new Date(System.currentTimeMillis() + 86400000L));

            when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodo);

            PeriodoCambio resultado = periodoService.crear(periodo);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "per1";
            PeriodoCambio periodo = new PeriodoCambio();

            when(repositorioPeriodoCambio.findById(id)).thenReturn(Optional.of(periodo));

            Optional<PeriodoCambio> resultado = periodoService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todos")
        void debeListarTodos() {
            List<PeriodoCambio> periodos = Arrays.asList(new PeriodoCambio());
            when(repositorioPeriodoCambio.findAll()).thenReturn(periodos);

            List<PeriodoCambio> resultado = periodoService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe actualizar período")
        void debeActualizar() {
            String id = "per1";
            PeriodoCambio periodo = new PeriodoCambio();
            periodo.setFechaInicio(new Date());
            periodo.setFechaFin(new Date(System.currentTimeMillis() + 86400000L));

            when(repositorioPeriodoCambio.existsById(id)).thenReturn(true);
            when(repositorioPeriodoCambio.save(any(PeriodoCambio.class))).thenReturn(periodo);

            PeriodoCambio resultado = periodoService.actualizar(id, periodo);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe eliminar período")
        void debeEliminar() {
            String id = "per1";

            assertDoesNotThrow(() -> periodoService.eliminarPorId(id));
            verify(repositorioPeriodoCambio).deleteById(id);
        }

        @Test
        @DisplayName("Debe verificar período activo")
        void debeVerificarActivo() {
            when(repositorioPeriodoCambio.existsByActivoTrue()).thenReturn(true);

            boolean resultado = periodoService.estaPeriodoActivo();

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe obtener período activo")
        void debeObtenerActivo() {
            PeriodoCambio periodo = new PeriodoCambio();

            when(repositorioPeriodoCambio.findByActivoTrue()).thenReturn(Arrays.asList(periodo));

            Optional<PeriodoCambio> resultado = periodoService.obtenerPeriodoActivo();

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe obtener períodos vigentes")
        void debeObtenerVigentes() {
            List<PeriodoCambio> periodos = Arrays.asList(new PeriodoCambio());

            when(repositorioPeriodoCambio.findPeriodosVigentesEnFecha(any(Date.class)))
                    .thenReturn(periodos);

            List<PeriodoCambio> resultado = periodoService.obtenerPeriodosVigentes();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe obtener período activo actual")
        void debeObtenerActivoActual() {
            PeriodoCambio periodo = new PeriodoCambio();

            when(repositorioPeriodoCambio.findPeriodoActivoEnFecha(any(Date.class)))
                    .thenReturn(Optional.of(periodo));

            Optional<PeriodoCambio> resultado = periodoService.obtenerPeriodoActivoActual();

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe obtener períodos futuros")
        void debeObtenerFuturos() {
            List<PeriodoCambio> periodos = Arrays.asList(new PeriodoCambio());

            when(repositorioPeriodoCambio.findPeriodosFuturos(any(Date.class)))
                    .thenReturn(periodos);

            List<PeriodoCambio> resultado = periodoService.obtenerPeriodosFuturos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe obtener por tipo")
        void debeObtenerPorTipo() {
            String tipo = "REGULAR";
            List<PeriodoCambio> periodos = Arrays.asList(new PeriodoCambio());

            when(repositorioPeriodoCambio.findByTipo(tipo)).thenReturn(periodos);

            List<PeriodoCambio> resultado = periodoService.obtenerPeriodosPorTipo(tipo);

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("ProfesorService Tests")
    class ProfesorServiceTests {

        @Mock private RepositorioProfesor repositorioProfesor;
        @Mock private RepositorioGrupo repositorioGrupo;

        @InjectMocks
        private ProfesorServiceImpl profesorService;

        @Test
        @DisplayName("Debe crear profesor")
        void debeCrear() {
            Profesor profesor = new Profesor();
            profesor.setIdProfesor(1001);

            when(repositorioProfesor.existsByIdProfesor(anyInt())).thenReturn(false);
            when(repositorioProfesor.save(any(Profesor.class))).thenReturn(profesor);

            Profesor resultado = profesorService.crear(profesor);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "prof1";
            Profesor profesor = new Profesor();

            when(repositorioProfesor.findById(id)).thenReturn(Optional.of(profesor));

            Optional<Profesor> resultado = profesorService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe buscar por código")
        void debeBuscarPorCodigo() {
            String codigo = "1001";
            Profesor profesor = new Profesor();

            when(repositorioProfesor.findByIdProfesor(1001)).thenReturn(Optional.of(profesor));

            Optional<Profesor> resultado = profesorService.buscarPorCodigo(codigo);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe listar todos")
        void debeListarTodos() {
            List<Profesor> profesores = Arrays.asList(new Profesor());
            when(repositorioProfesor.findAll()).thenReturn(profesores);

            List<Profesor> resultado = profesorService.listarTodos();

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe actualizar profesor")
        void debeActualizar() {
            String id = "prof1";
            Profesor profesor = new Profesor();

            when(repositorioProfesor.existsById(id)).thenReturn(true);
            when(repositorioProfesor.save(any(Profesor.class))).thenReturn(profesor);

            Profesor resultado = profesorService.actualizar(id, profesor);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe eliminar profesor")
        void debeEliminar() {
            String id = "prof1";

            assertDoesNotThrow(() -> profesorService.eliminarPorId(id));
            verify(repositorioProfesor).deleteById(id);
        }

        @Test
        @DisplayName("Debe consultar grupos asignados")
        void debeConsultarGrupos() {
            String profesorId = "prof1";
            List<Grupo> grupos = Arrays.asList(new Grupo());

            when(repositorioGrupo.findByProfesorId(profesorId)).thenReturn(grupos);

            List<Grupo> resultado = profesorService.consultarGruposAsignados(profesorId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe asignar profesor a grupo")
        void debeAsignar() {
            String profesorId = "prof1";
            String grupoId = "grp1";

            Profesor profesor = new Profesor();
            profesor.setGruposAsignadosIds(new ArrayList<>());
            Grupo grupo = new Grupo();

            when(repositorioProfesor.findById(profesorId)).thenReturn(Optional.of(profesor));
            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = profesorService.asignarProfesorAGrupo(profesorId, grupoId);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe retirar profesor de grupo")
        void debeRetirar() {
            String grupoId = "grp1";
            String profesorId = "prof1";

            Profesor profesor = new Profesor();
            profesor.setGruposAsignadosIds(new ArrayList<>(Arrays.asList(grupoId)));
            Grupo grupo = new Grupo();
            grupo.setProfesorId(profesorId);

            when(repositorioGrupo.findById(grupoId)).thenReturn(Optional.of(grupo));
            when(repositorioProfesor.findById(profesorId)).thenReturn(Optional.of(profesor));
            when(repositorioGrupo.save(any(Grupo.class))).thenReturn(grupo);

            Grupo resultado = profesorService.retirarProfesorDeGrupo(grupoId);

            assertNull(resultado.getProfesorId());
        }

        @Test
        @DisplayName("Debe buscar por departamento")
        void debeBuscarPorDepartamento() {
            String departamento = "Matemáticas";
            List<Profesor> profesores = Arrays.asList(new Profesor());

            when(repositorioProfesor.findByDepartamento(departamento)).thenReturn(profesores);

            List<Profesor> resultado = profesorService.buscarPorDepartamento(departamento);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe buscar activos")
        void debeBuscarActivos() {
            List<Profesor> profesores = Arrays.asList(new Profesor());

            when(repositorioProfesor.findByActivoTrue()).thenReturn(profesores);

            List<Profesor> resultado = profesorService.buscarActivos();

            assertEquals(1, resultado.size());
        }
    }

    @Nested
    @DisplayName("SemaforoAcademicoService Tests")
    class SemaforoAcademicoServiceTests {

        @Mock private RepositorioSemaforoAcademico repositorioSemaforoAcademico;
        @Mock private RepositorioEstudiante repositorioEstudiante;

        @InjectMocks
        private SemaforoAcademicoServiceImpl semaforoService;

        @Test
        @DisplayName("Debe visualizar semáforo")
        void debeVisualizar() {
            String estudianteId = "est1";
            SemaforoAcademico semaforo = new SemaforoAcademico();
            Map<String, EstadoMateria> historial = new HashMap<>();
            historial.put("mat1", EstadoMateria.APROBADA);
            semaforo.setHistorialMaterias(historial);

            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));

            Map<String, EstadoSemaforo> resultado = semaforoService.visualizarSemaforoEstudiante(estudianteId);

            assertEquals(1, resultado.size());
        }

        @Test
        @DisplayName("Debe consultar semáforo de materia")
        void debeConsultarMateria() {
            String estudianteId = "est1";
            String materiaId = "mat1";

            SemaforoAcademico semaforo = new SemaforoAcademico();
            Map<String, EstadoMateria> historial = new HashMap<>();
            historial.put(materiaId, EstadoMateria.APROBADA);
            semaforo.setHistorialMaterias(historial);

            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));

            Optional<EstadoSemaforo> resultado = semaforoService.consultarSemaforoMateria(estudianteId, materiaId);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe obtener semestre actual")
        void debeObtenerSemestre() {
            String estudianteId = "est1";
            Estudiante estudiante = new Estudiante();
            estudiante.setSemestre(5);

            when(repositorioEstudiante.findById(estudianteId)).thenReturn(Optional.of(estudiante));

            int semestre = semaforoService.getSemestreActual(estudianteId);

            assertEquals(5, semestre);
        }

        @Test
        @DisplayName("Debe obtener foráneo estudiante")
        void debeObtenerForaneo() {
            String estudianteId = "est1";
            Estudiante estudiante = new Estudiante();
            estudiante.setSemestre(3);

            SemaforoAcademico semaforo = new SemaforoAcademico();
            Map<String, EstadoMateria> historial = new HashMap<>();
            historial.put("mat1", EstadoMateria.APROBADA);
            semaforo.setHistorialMaterias(historial);

            when(repositorioEstudiante.findById(estudianteId)).thenReturn(Optional.of(estudiante));
            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));

            Map<String, Object> resultado = semaforoService.getForaneoEstudiante(estudianteId);

            assertNotNull(resultado);
            assertEquals(3, resultado.get("semestreActual"));
        }

        @Test
        @DisplayName("Debe obtener semáforo completo")
        void debeObtenerCompleto() {
            String estudianteId = "est1";
            Estudiante estudiante = new Estudiante();
            estudiante.setSemestre(4);

            SemaforoAcademico semaforo = new SemaforoAcademico();
            semaforo.setHistorialMaterias(new HashMap<>());

            when(repositorioEstudiante.findById(estudianteId)).thenReturn(Optional.of(estudiante));
            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));

            SemaforoVisualizacion resultado = semaforoService.obtenerSemaforoCompleto(estudianteId);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe obtener semáforo detallado")
        void debeObtenerDetallado() {
            String estudianteId = "est1";
            Estudiante estudiante = new Estudiante();
            estudiante.setSemestre(4);

            SemaforoAcademico semaforo = new SemaforoAcademico();
            semaforo.setHistorialMaterias(new HashMap<>());

            when(repositorioEstudiante.findById(estudianteId)).thenReturn(Optional.of(estudiante));
            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));

            SemaforoVisualizacion resultado = semaforoService.obtenerSemaforoDetallado(estudianteId);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe detectar problemas académicos")
        void debeDetectarProblemas() {
            String estudianteId = "est1";
            Estudiante estudiante = new Estudiante();

            SemaforoAcademico semaforo = new SemaforoAcademico();
            Map<String, EstadoMateria> historial = new HashMap<>();
            historial.put("mat1", EstadoMateria.REPROBADA);
            historial.put("mat2", EstadoMateria.REPROBADA);
            historial.put("mat3", EstadoMateria.REPROBADA);
            historial.put("mat4", EstadoMateria.REPROBADA);
            semaforo.setHistorialMaterias(historial);
            semaforo.setPromedioAcumulado(2.5F);

            when(repositorioEstudiante.findById(estudianteId)).thenReturn(Optional.of(estudiante));
            when(repositorioSemaforoAcademico.findByEstudianteId(estudianteId))
                    .thenReturn(Optional.of(semaforo));

            boolean resultado = semaforoService.tieneProblemasAcademicos(estudianteId);

            assertTrue(resultado);
        }
    }

    @Nested
    @DisplayName("UsuarioService Tests")
    class UsuarioServiceTests {

        @Mock private RepositorioUsuario repositorioUsuario;

        @InjectMocks
        private UsuarioServiceImpl usuarioService;

        @Test
        @DisplayName("Debe autenticar usuario")
        void debeAutenticar() {
            String username = "admin";
            String password = "pass123";

            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPasswordHash(password);
            usuario.setActivo(true);

            when(repositorioUsuario.findByUsername(username)).thenReturn(Optional.of(usuario));

            Optional<Usuario> resultado = usuarioService.autenticar(username, password);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe buscar por username")
        void debeBuscarPorUsername() {
            String username = "admin";
            Usuario usuario = new Usuario();

            when(repositorioUsuario.findByUsername(username)).thenReturn(Optional.of(usuario));

            Optional<Usuario> resultado = usuarioService.buscarPorUsername(username);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe verificar permisos")
        void debeVerificarPermisos() {
            String usuarioId = "usr1";
            Usuario usuario = new Usuario();
            usuario.setRol(Rol.valueOf("ADMIN"));
            usuario.setActivo(true);

            when(repositorioUsuario.findById(usuarioId)).thenReturn(Optional.of(usuario));

            boolean resultado = usuarioService.tienePermiso(usuarioId, "ACCION");

            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe cambiar contraseña")
        void debeCambiarPassword() {
            String usuarioId = "usr1";
            Usuario usuario = new Usuario();

            when(repositorioUsuario.findById(usuarioId)).thenReturn(Optional.of(usuario));

            assertDoesNotThrow(() -> usuarioService.cambiarPassword(usuarioId, "newpass"));
        }

        @Test
        @DisplayName("Debe crear usuario")
        void debeCrear() {
            Usuario usuario = new Usuario();
            usuario.setUsername("newuser");

            when(repositorioUsuario.existsByUsername("newuser")).thenReturn(false);
            when(repositorioUsuario.save(any(Usuario.class))).thenReturn(usuario);

            Usuario resultado = usuarioService.crearUsuario(usuario);

            assertNotNull(resultado);
        }

        @Test
        @DisplayName("Debe buscar por ID")
        void debeBuscarPorId() {
            String id = "usr1";
            Usuario usuario = new Usuario();

            when(repositorioUsuario.findById(id)).thenReturn(Optional.of(usuario));

            Optional<Usuario> resultado = usuarioService.buscarPorId(id);

            assertTrue(resultado.isPresent());
        }

        @Test
        @DisplayName("Debe desactivar usuario")
        void debeDesactivar() {
            String usuarioId = "usr1";
            Usuario usuario = new Usuario();

            when(repositorioUsuario.findById(usuarioId)).thenReturn(Optional.of(usuario));

            assertDoesNotThrow(() -> usuarioService.desactivarUsuario(usuarioId));
        }

        @Test
        @DisplayName("Debe activar usuario")
        void debeActivar() {
            String usuarioId = "usr1";
            Usuario usuario = new Usuario();

            when(repositorioUsuario.findById(usuarioId)).thenReturn(Optional.of(usuario));

            assertDoesNotThrow(() -> usuarioService.activarUsuario(usuarioId));
        }
    }
}