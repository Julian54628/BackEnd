package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.*;
import edu.escuelaing.sirha.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    @Autowired
    private RepositorioAdministrador repositorioAdministrador;

    @Autowired
    private RepositorioGrupo repositorioGrupo;

    @Autowired
    private RepositorioPeriodoCambio repositorioPeriodoCambio;

    @Autowired
    private RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    private RepositorioSemaforoAcademico repositorioSemaforoAcademico;

    @Override
    public Optional<SemaforoAcademico> modificarEstadoMateriaSemaforo(String estudianteId, String materiaId, EstadoMateria nuevoEstado) {
        Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (semaforoOpt.isPresent()) {
            SemaforoAcademico semaforo = semaforoOpt.get();
            semaforo.getHistorialMaterias().put(materiaId, nuevoEstado);
            return Optional.of(repositorioSemaforoAcademico.save(semaforo));
        }
        return Optional.empty();
    }

    @Override
    public Administrador crear(Administrador administrador) {
        return repositorioAdministrador.save(administrador);
    }

    @Override
    public Optional<Administrador> buscarPorId(String id) {
        return repositorioAdministrador.findById(id);
    }

    @Override
    public List<Administrador> listarTodos() {
        return repositorioAdministrador.findAll();
    }

    @Override
    public Grupo modificarCupoGrupo(String grupoId, int nuevoCupo) {
        Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
        if (grupoOpt.isPresent() && nuevoCupo > 0 && nuevoCupo <= 50) {
            Grupo grupo = grupoOpt.get();
            grupo.setCupoMaximo(nuevoCupo);
            return repositorioGrupo.save(grupo);
        }
        return null;
    }

    @Override
    public PeriodoCambio configurarPeriodo(PeriodoCambio periodo) {
        // Desactivar todos los periodos existentes
        List<PeriodoCambio> periodosActivos = repositorioPeriodoCambio.findByActivoTrue();
        for (PeriodoCambio p : periodosActivos) {
            p.setActivo(false);
            repositorioPeriodoCambio.save(p);
        }

        periodo.setActivo(true);
        return repositorioPeriodoCambio.save(periodo);
    }

    @Override
    public List<SolicitudCambio> generarReportes() {
        return repositorioSolicitudCambio.findAll();
    }

    @Override
    public Administrador crearDesdeDecanatura(String decanaturaId) {
        long count = repositorioAdministrador.count();
        int mockIdUsuario = (int) (count + 1);
        String username = "admin_" + decanaturaId.toLowerCase().replaceAll("[^a-z0-9]", "");
        String passwordHash = UUID.randomUUID().toString();
        String correo = username + "@admin.edu.co";
        Administrador newAdmin = new Administrador(mockIdUsuario, username, passwordHash, correo);
        return repositorioAdministrador.save(newAdmin);
    }

    @Override
    public Optional<PeriodoCambio> getSemestreActual() {
        return repositorioPeriodoCambio.findByActivoTrue().stream().findFirst();
    }

    @Override
    public Optional<PeriodoCambio> getSemestreAnterior() {
        List<PeriodoCambio> periodosOrdenados = repositorioPeriodoCambio.findAll().stream()
                .sorted(Comparator.comparing(PeriodoCambio::getFechaInicio).reversed()).toList();
        if (periodosOrdenados.size() > 1) {
            return Optional.of(periodosOrdenados.get(1));
        }
        return Optional.empty();
    }

    @Override
    public List<Grupo> obtenerGruposConAlertaCarga() {
        return repositorioGrupo.findAll().stream().filter(grupo -> {
                    double porcentajeOcupacion = (double) grupo.getEstudiantesInscritosIds().size() / grupo.getCupoMaximo() * 100;
                    return porcentajeOcupacion >= 90.0;}).toList();
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudesGlobalesPorPrioridad() {
        return repositorioSolicitudCambio.findByOrderByPrioridadDesc();
    }

    @Override
    public Map<String, Object> generarReporteGruposMasSolicitados() {
        List<SolicitudCambio> todasSolicitudes = repositorioSolicitudCambio.findAll();
        Map<String, Integer> conteoSolicitudesPorGrupo = new HashMap<>();
        for (SolicitudCambio solicitud : todasSolicitudes) {
            String grupoDestinoId = solicitud.getGrupoDestinoId();
            conteoSolicitudesPorGrupo.put(grupoDestinoId, conteoSolicitudesPorGrupo.getOrDefault(grupoDestinoId, 0) + 1);
        }
        Map<String, Object> reporte = new HashMap<>();
        reporte.put("conteoPorGrupo", conteoSolicitudesPorGrupo);
        reporte.put("totalSolicitudes", todasSolicitudes.size());
        reporte.put("gruposMasSolicitados",
                conteoSolicitudesPorGrupo.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(10).collect(Collectors.toList()));
        return reporte;
    }

    @Override
    public Map<String, Object> generarReporteEstadisticasReasignacion() {
        List<SolicitudCambio> todasSolicitudes = repositorioSolicitudCambio.findAll();
        long totalSolicitudes = todasSolicitudes.size();
        long aprobadas = todasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.APROBADA).count();
        long rechazadas = todasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.RECHAZADA).count();
        long pendientes = todasSolicitudes.stream().filter(s -> s.getEstado() == EstadoSolicitud.PENDIENTE).count();
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalSolicitudes", totalSolicitudes);
        estadisticas.put("aprobadas", aprobadas);
        estadisticas.put("rechazadas", rechazadas);
        estadisticas.put("pendientes", pendientes);
        estadisticas.put("tasaAprobacion", totalSolicitudes > 0 ? (double) aprobadas / totalSolicitudes * 100 : 0);
        estadisticas.put("tasaRechazo", totalSolicitudes > 0 ? (double) rechazadas / totalSolicitudes * 100 : 0);
        return estadisticas;
    }
}