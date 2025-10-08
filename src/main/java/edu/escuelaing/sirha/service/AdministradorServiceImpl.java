package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Administrador;
import edu.escuelaing.sirha.model.EstadoMateria;
import edu.escuelaing.sirha.model.SemaforoAcademico;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Decanatura;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.model.SolicitudCambio;
import edu.escuelaing.sirha.repository.RepositorioSemaforoAcademico;
import org.springframework.beans.factory.annotation.Autowired;
import edu.escuelaing.sirha.repository.RepositorioAdministrador;
import edu.escuelaing.sirha.repository.RepositorioDecanatura;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final Map<String, Administrador> administradores = new HashMap<>();
    private final Map<String, Grupo> grupos = new HashMap<>();
    private final List<PeriodoCambio> periodos = new ArrayList<>();
    private final List<SolicitudCambio> solicitudes = new ArrayList<>();
    private final RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    private final RepositorioAdministrador repositorioAdministrador;
    private final RepositorioDecanatura repositorioDecanatura;

    @Autowired
    public AdministradorServiceImpl(RepositorioSemaforoAcademico repositorioSemaforoAcademico,
                                    RepositorioAdministrador repositorioAdministrador,
                                    RepositorioDecanatura repositorioDecanatura) {
        this.repositorioSemaforoAcademico = repositorioSemaforoAcademico;
        this.repositorioAdministrador = repositorioAdministrador;
        this.repositorioDecanatura = repositorioDecanatura;
    }

    public AdministradorServiceImpl() {
        this.repositorioSemaforoAcademico = null;
        this.repositorioAdministrador = null;
        this.repositorioDecanatura = null;
    }
    @Override
    public Administrador crear(Administrador administrador) {
        if (repositorioAdministrador != null) {
            Administrador saved = repositorioAdministrador.save(administrador);
            administradores.put(saved.getId(), saved);
            return saved;
        }
        administradores.put(administrador.getId(), administrador);
        return administrador;
    }
    @Override
    public Optional<Administrador> buscarPorId(String id) {
        if (repositorioAdministrador != null) {
            return repositorioAdministrador.findById(id);
        }
        return Optional.ofNullable(administradores.get(id));
    }
    @Override
    public List<Administrador> listarTodos() {
        if (repositorioAdministrador != null) {
            return repositorioAdministrador.findAll();
        }
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
        periodos.add(periodo);
        return periodo;
    }
    @Override
    public List<SolicitudCambio> generarReportes() {
        return new ArrayList<>(solicitudes);
    }
    @Override
    public Optional<SemaforoAcademico> modificarEstadoMateriaSemaforo(String estudianteId, String materiaId, EstadoMateria nuevoEstado) {
        if (repositorioSemaforoAcademico == null) {
            return Optional.empty();
        }
        Optional<SemaforoAcademico> optSemaforo = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);
        if (optSemaforo.isPresent()) {
            SemaforoAcademico semaforo = optSemaforo.get();
            semaforo.getHistorialMaterias().put(materiaId, nuevoEstado);
            SemaforoAcademico actualizado = repositorioSemaforoAcademico.save(semaforo);
            return Optional.of(actualizado);
        }
        return Optional.empty();
    }

    @Override
    public Administrador crearDesdeDecanatura(String decanaturaId) {
        if (repositorioDecanatura == null || repositorioAdministrador == null) {
            return null;
        }
        Optional<Decanatura> decOpt = repositorioDecanatura.findById(decanaturaId);
        if (decOpt.isEmpty()) {
            return null;
        }
        Decanatura d = decOpt.get();
        Administrador admin = new Administrador(
                d.getIdUsuario(),
                d.getUsername(),
                d.getPasswordHash(),
                d.getCorreoInstitucional()
        );
        Administrador saved = repositorioAdministrador.save(admin);
        administradores.put(saved.getId(), saved);
        return saved;
    }
}