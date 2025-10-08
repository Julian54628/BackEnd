package edu.escuelaing.sirha.service;

import edu.escuelaing.sirha.model.Estudiante;
import edu.escuelaing.sirha.model.Grupo;
import edu.escuelaing.sirha.model.Materia;
import edu.escuelaing.sirha.model.SolicitudCambio;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EstudianteServiceImpl implements EstudianteService {

    private final Map<String, Estudiante> estudiantes = new HashMap<>();
    private final Map<String, Materia> materias = new HashMap<>();
    private final Map<String, Grupo> grupos = new HashMap<>();
    private final List<SolicitudCambio> solicitudes = new ArrayList<>();

    @Override
    public Estudiante crear(Estudiante estudiante) {
        estudiantes.put(estudiante.getId(), estudiante);
        return estudiante;
    }

    @Override
    public Optional<Estudiante> buscarPorCodigo(String codigo) {
        return estudiantes.values().stream().filter(e -> codigo.equals(e.getCodigo())).findFirst();
    }

    @Override
    public Optional<Estudiante> buscarPorId(String id) {
        return Optional.ofNullable(estudiantes.get(id));
    }

    @Override
    public List<Estudiante> listarTodos() {
        return new ArrayList<>(estudiantes.values());
    }

    @Override
    public void eliminarPorId(String id) {
        estudiantes.remove(id);
    }

    @Override
    public Estudiante actualizar(String id, Estudiante estudiante) {
        estudiantes.put(id, estudiante);
        return estudiante;
    }

    @Override
    public SolicitudCambio crearSolicitudCambio(String estudianteId, String materiaOrigenId,
                                                String grupoOrigenId, String materiaDestinoId, String grupoDestinoId) {
        SolicitudCambio solicitud = new SolicitudCambio();
        solicitud.setId(UUID.randomUUID().toString());
        solicitud.setEstudianteId(estudianteId);
        solicitud.setMateriaOrigenId(materiaOrigenId);
        solicitud.setGrupoOrigenId(grupoOrigenId);
        solicitud.setMateriaDestinoId(materiaDestinoId);
        solicitud.setGrupoDestinoId(grupoDestinoId);
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public List<SolicitudCambio> consultarSolicitudes(String estudianteId) {
        List<SolicitudCambio> resultado = new ArrayList<>();
        for (SolicitudCambio s : solicitudes) {
            if (estudianteId.equals(s.getEstudianteId())) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    @Override
    public List<Grupo> consultarHorarioSemestreActual(String estudianteId) {
        Optional<Estudiante> estudianteOpt = buscarPorId(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return List.of();
        }
        Estudiante estudiante = estudianteOpt.get();
        List<Grupo> horarioActual = new ArrayList<>();
        for (String grupoId : estudiante.getHorariosIds()) {
            Grupo grupo = grupos.get(grupoId);
            if (grupo != null) {
                horarioActual.add(grupo);
            }
        }
        return horarioActual;
    }

    @Override
    public List<Materia> consultarMateriasSemestreAnterior(String estudianteId) {
        Optional<Estudiante> estudianteOpt = buscarPorId(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return List.of();
        }
        return new ArrayList<>(materias.values());
    }
}