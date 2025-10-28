package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.repository.*;
import edu.escuelaing.sirha.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdministradorServiceImpl implements AdministradorService {

    private final RepositorioSemaforoAcademico repositorioSemaforoAcademico;
    private final RepositorioAdministrador repositorioAdministrador;
    private final RepositorioDecanatura repositorioDecanatura;
    private final RepositorioGrupo repositorioGrupo;
    private final RepositorioPeriodoCambio repositorioPeriodoCambio;
    private final RepositorioSolicitudCambio repositorioSolicitudCambio;

    @Autowired
    public AdministradorServiceImpl(
            RepositorioSemaforoAcademico repositorioSemaforoAcademico,
            RepositorioAdministrador repositorioAdministrador,
            RepositorioDecanatura repositorioDecanatura,
            RepositorioGrupo repositorioGrupo,
            RepositorioPeriodoCambio repositorioPeriodoCambio,
            RepositorioSolicitudCambio repositorioSolicitudCambio) {
        this.repositorioSemaforoAcademico = repositorioSemaforoAcademico;
        this.repositorioAdministrador = repositorioAdministrador;
        this.repositorioDecanatura = repositorioDecanatura;
        this.repositorioGrupo = repositorioGrupo;
        this.repositorioPeriodoCambio = repositorioPeriodoCambio;
        this.repositorioSolicitudCambio = repositorioSolicitudCambio;
    }

    @Override
    public Optional<SemaforoAcademico> modificarEstadoMateriaSemaforo(String estudianteId, String materiaId, EstadoMateria nuevoEstado) {
        return repositorioSemaforoAcademico.findByEstudianteId(estudianteId)
                .map(semaforo -> {
                    semaforo.getHistorialMaterias().put(materiaId, nuevoEstado);
                    return repositorioSemaforoAcademico.save(semaforo);
                });
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
        if (nuevoCupo <= 0 || nuevoCupo > 50) {
            throw new IllegalArgumentException("El cupo debe estar entre 1 y 50");
        }

        return repositorioGrupo.findById(grupoId)
                .map(grupo -> {
                    grupo.setCupoMaximo(nuevoCupo);
                    return repositorioGrupo.save(grupo);
                })
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado: " + grupoId));
    }

    @Override
    public PeriodoCambio configurarPeriodo(PeriodoCambio periodo) {
        return repositorioPeriodoCambio.save(periodo);
    }

    @Override
    public List<SolicitudCambio> generarReportes() {
        return repositorioSolicitudCambio.findAll();
    }

    @Override
    public Administrador crearDesdeDecanatura(String decanaturaId) {
        return repositorioDecanatura.findById(decanaturaId)
                .map(decanatura -> {
                    Administrador admin = new Administrador();
                    admin.setUsername(decanatura.getUsername());
                    admin.setPasswordHash(decanatura.getPasswordHash());
                    admin.setCorreoInstitucional(decanatura.getCorreoInstitucional());
                    admin.setActivo(true);
                    return repositorioAdministrador.save(admin);
                })
                .orElseThrow(() -> new IllegalArgumentException("Decanatura no encontrada: " + decanaturaId));
    }

    @Override
    public boolean eliminarAdministrador(String id) {
        if (repositorioAdministrador.existsById(id)) {
            repositorioAdministrador.deleteById(id);
            return true;
        }
        return false;
    }
}