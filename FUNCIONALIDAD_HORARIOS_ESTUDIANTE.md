# Funcionalidad de Consulta de Horarios del Estudiante - SIRHA

## 📋 Resumen de Implementación

Se ha implementado completamente la funcionalidad de consulta de horarios del estudiante para el sistema SIRHA, incluyendo:

- ✅ Consulta de horario del semestre actual
- ✅ Consulta de materias de semestres anteriores
- ✅ Uso correcto de MongoDB para todas las consultas
- ✅ Endpoints REST completos
- ✅ Validaciones y manejo de errores

## 🚀 Nuevos Endpoints Implementados

### 1. Consultar Horario del Semestre Actual
```
GET /api/estudiantes/{estudianteId}/horario-semestre-actual
```
**Descripción**: Obtiene los grupos (materias) que el estudiante está cursando en el semestre actual.

**Respuesta**:
```json
[
  {
    "id": "grupo_id_1",
    "idGrupo": 101,
    "cupoMaximo": 30,
    "estudiantesInscritosIds": ["estudiante_1", "estudiante_2"],
    "materiaId": "materia_id_1",
    "profesorId": "profesor_id_1",
    "horarioIds": ["horario_id_1", "horario_id_2"]
  }
]
```

### 2. Consultar Materias de Semestres Anteriores
```
GET /api/estudiantes/{estudianteId}/materias-semestres-anteriores
```
**Descripción**: Obtiene las materias que el estudiante ha cursado en semestres anteriores (aprobadas o reprobadas).

**Respuesta**:
```json
[
  {
    "id": "materia_id_1",
    "idMateria": 1001,
    "nombre": "Cálculo I",
    "codigo": "CALC1001",
    "creditos": 4,
    "facultad": "Ingeniería",
    "esObligatoria": true
  }
]
```

### 3. Consultar Horario Detallado del Semestre Actual
```
GET /api/estudiantes/{estudianteId}/horario-detallado-semestre-actual
```
**Descripción**: Obtiene información completa del horario incluyendo materias, horarios específicos y detalles.

**Respuesta**:
```json
{
  "estudianteId": "estudiante_123",
  "nombre": "Juan Pérez",
  "codigo": "2023123456",
  "semestre": 3,
  "carrera": "Ingeniería de Sistemas",
  "grupos": [
    {
      "grupoId": "grupo_123",
      "idGrupo": 101,
      "cupoMaximo": 30,
      "estudiantesInscritos": 25,
      "materia": {
        "materiaId": "materia_123",
        "nombre": "Cálculo II",
        "codigo": "CALC2001",
        "creditos": 4,
        "facultad": "Ingeniería",
        "esObligatoria": true
      },
      "horarios": [
        {
          "horarioId": "horario_123",
          "diaSemana": "LUNES",
          "horaInicio": "08:00:00",
          "horaFin": "10:00:00",
          "salon": "A-101"
        }
      ]
    }
  ],
  "totalGrupos": 5
}
```

### 4. Consultar Resumen Académico Completo
```
GET /api/estudiantes/{estudianteId}/resumen-academico-completo
```
**Descripción**: Obtiene un resumen completo de toda la información académica del estudiante.

**Respuesta**:
```json
{
  "estudiante": {
    "id": "estudiante_123",
    "nombre": "Juan Pérez",
    "codigo": "2023123456",
    "carrera": "Ingeniería de Sistemas",
    "semestre": 3,
    "correoInstitucional": "juan.perez@estudiantes.escuelaing.edu.co"
  },
  "horarioSemestreActual": [...],
  "materiasSemestresAnteriores": [...],
  "semaforoAcademico": {
    "creditosAprobados": 45,
    "totalCreditosPlan": 150,
    "materiasVistas": 12,
    "promedioAcumulado": 4.2,
    "grado": "PREGRADO"
  },
  "solicitudesCambio": [...],
  "avancePlanEstudios": {...}
}
```

## 🔧 Implementación Técnica

### Uso Correcto de MongoDB

#### 1. **Consulta de Horario Semestre Actual**
```java
// Busca el estudiante por ID
Optional<Estudiante> estudianteOpt = repositorioEstudiante.findById(estudianteId);

// Obtiene los grupos usando los IDs almacenados en el estudiante
for (String grupoId : estudiante.getHorariosIds()) {
    Optional<Grupo> grupoOpt = repositorioGrupo.findById(grupoId);
    // Procesa cada grupo encontrado
}
```

#### 2. **Consulta de Materias Semestres Anteriores**
```java
// Busca el semáforo académico del estudiante
Optional<SemaforoAcademico> semaforoOpt = repositorioSemaforoAcademico.findByEstudianteId(estudianteId);

// Filtra materias aprobadas o reprobadas del historial
List<String> materiasIds = semaforo.getHistorialMaterias().entrySet().stream()
    .filter(entry -> entry.getValue() == EstadoMateria.APROBADA || 
                    entry.getValue() == EstadoMateria.REPROBADA)
    .map(Map.Entry::getKey)
    .collect(Collectors.toList());

// Busca las materias en MongoDB usando los IDs
return repositorioMateria.findAllById(materiasIds);
```

#### 3. **Consultas Optimizadas**
- Uso de `findById()` para búsquedas por ID
- Uso de `findAllById()` para búsquedas múltiples
- Filtrado en memoria para lógica de negocio compleja
- Validaciones de existencia antes de procesar

### Estructura de Datos

#### **Estudiante**
```java
@Document(collection = "Estudiante")
public class Estudiante extends Usuario {
    private List<String> horariosIds; // IDs de grupos del semestre actual
    private String semaforoAcademicoId; // Referencia al semáforo académico
    // ... otros campos
}
```

#### **SemaforoAcademico**
```java
@Document(collection = "SemaforoAcademico")
public class SemaforoAcademico {
    private Map<String, EstadoMateria> historialMaterias; // Materias cursadas
    // ... otros campos
}
```

#### **Grupo**
```java
@Document(collection = "Grupo")
public class Grupo {
    private String materiaId; // Referencia a la materia
    private List<String> horarioIds; // Referencias a horarios específicos
    // ... otros campos
}
```

## ✅ Validaciones Implementadas

1. **Estudiante Existe**: Verifica que el estudiante exista antes de procesar
2. **Semáforo Académico**: Verifica que exista el semáforo para materias anteriores
3. **Listas Vacías**: Retorna listas vacías cuando no hay datos
4. **Manejo de Errores**: Retorna mensajes de error apropiados
5. **Validación de IDs**: Verifica que los IDs de referencia existan

## 🎯 Funcionalidades Principales

### **Semestre Actual**
- Consulta grupos inscritos
- Información detallada de materias
- Horarios específicos (día, hora, salón)
- Información de cupos y profesores

### **Semestres Anteriores**
- Materias aprobadas y reprobadas
- Información completa de cada materia
- Basado en el historial del semáforo académico

### **Información Integrada**
- Resumen académico completo
- Estado del semáforo académico
- Solicitudes de cambio
- Avance en el plan de estudios

## 🔍 Casos de Uso Cubiertos

1. **Estudiante consulta su horario actual** → Endpoint 1
2. **Estudiante revisa materias cursadas anteriormente** → Endpoint 2
3. **Estudiante necesita información detallada de horarios** → Endpoint 3
4. **Sistema genera reporte académico completo** → Endpoint 4

## 📊 Rendimiento y Optimización

- **Consultas Eficientes**: Uso de índices de MongoDB para búsquedas por ID
- **Filtrado Inteligente**: Filtrado en memoria para lógica compleja
- **Carga Lazy**: Solo carga datos cuando es necesario
- **Validaciones Tempranas**: Evita procesamiento innecesario

## 🚀 Próximos Pasos

1. **Testing**: Implementar pruebas unitarias para cada endpoint
2. **Caching**: Considerar cache para consultas frecuentes
3. **Paginación**: Para listas grandes de materias
4. **Filtros**: Filtros por semestre específico
5. **Exportación**: Exportar horarios a PDF/Excel

---

**Estado**: ✅ **IMPLEMENTACIÓN COMPLETA Y FUNCIONAL**
**Fecha**: Diciembre 2024
**Versión**: 1.0
