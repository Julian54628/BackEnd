package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Administrador;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.PeriodoCambio;
import edu.escuelaing.sirha.model.SolicitudCambio;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdministradorServiceImpl implements AdministradorService {

    private final Map<String, Administrador> administradores = new HashMap<>();
    private final Map<String, Grupo> grupos = new HashMap<>();
    private final List<PeriodoCambio> periodos = new ArrayList<>();
    private final List<SolicitudCambio> solicitudes = new ArrayList<>();

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
        periodos.add(periodo);
        return periodo;
    }

    @Override
    public List<SolicitudCambio> generarReportes() {
        return new ArrayList<>(solicitudes);
    }
}
