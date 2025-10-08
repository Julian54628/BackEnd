package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Administrador;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.repository.RepositorioAdministrador;
import edu.escuelaing.sirha.repository.RepositorioDecanatura;
import edu.escuelaing.sirha.repository.RepositorioSemaforoAcademico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final Map<String, Administrador> administradores = new HashMap<>();
    private final Map<String, Grupo> grupos = new HashMap<>();
    private final List<PeriodoCambio> periodos = new ArrayList<>();
    private final List<SolicitudCambio> solicitudes = new ArrayList<>();

    @Override
    public Optional<SemaforoAcademico> modificarEstadoMateriaSemaforo(String estudianteId, String materiaId, EstadoMateria nuevoEstado) {
        return Optional.empty();
    }

    @Override
    public Administrador crear(Administrador administrador) {
        administradores.put(administrador.getId(), administrador);
        return administrador;
    }

    @Override
    public Optional<Administrador> buscarPorId(String id) {
        return Optional.ofNullable(administradores.get(id));
    }

    @Override
    public List<Administrador> listarTodos() {
        return new ArrayList<>(administradores.values());
    }

    @Override
    public Grupo modificarCupoGrupo(String grupoId, int nuevoCupo) {
        Grupo grupo = grupos.get(grupoId);
        if (grupo != null && nuevoCupo > 0 && nuevoCupo <= 50) {
            grupo.setCupoMaximo(nuevoCupo);
            return grupo;
        }
        return null;
    }

    @Override
    public PeriodoCambio configurarPeriodo(PeriodoCambio periodo) {
        periodos.forEach(p -> p.setActivo(false));
        periodo.setActivo(true);
        periodos.add(periodo);
        periodos.sort(Comparator.comparing(PeriodoCambio::getFechaInicio));
        return periodo;
    }

    @Override
    public List<SolicitudCambio> generarReportes() {
        return new ArrayList<>(solicitudes);
    }

    @Override
    public Administrador crearDesdeDecanatura(String decanaturaId) {
        int mockIdUsuario = administradores.size() + 1;
        String id = "ADMIN_" + mockIdUsuario;
        String username = "admin_" + decanaturaId.toLowerCase().replaceAll("[^a-z0-9]", "");
        String passwordHash = UUID.randomUUID().toString();
        String correo = decanaturaId + "@mockadmin.edu.co";

        Administrador newAdmin = new Administrador(mockIdUsuario, username, passwordHash, correo);
        newAdmin.setId(id);
        administradores.put(id, newAdmin);

        return newAdmin;
    }

    @Override
    public Optional<PeriodoCambio> getSemestreActual() {
        return periodos.stream().filter(PeriodoCambio::isActivo).findFirst();
    }

    @Override
    public Optional<PeriodoCambio> getSemestreAnterior() {
        Optional<PeriodoCambio> actual = getSemestreActual();

        if (actual.isPresent()) {
            int indexActual = periodos.indexOf(actual.get());

            if (indexActual > 0) {
                return Optional.of(periodos.get(indexActual - 1));
            }
        }
        return Optional.empty();
    }
}