# Funcionalidades de Solicitudes de Cambio - Sistema SIRHA

## 📋 Resumen de Implementación

Se ha implementado completamente el sistema de solicitudes de cambio con todas las funcionalidades solicitadas, incluyendo:

- ✅ Visualización del semáforo académico para administradores y decanos
- ✅ Creación de solicitudes de cambio de grupo y materia
- ✅ Sistema de prioridades (Normal, Especial, Excepcional)
- ✅ Gestión completa de estados de solicitudes
- ✅ Historial de solicitudes para todos los usuarios
- ✅ Filtros y consultas avanzadas
- ✅ CRUD completo para administradores y decanos

## 🎯 Tipos de Solicitudes Implementadas

### **1. Cambio de Grupo de Materia**
- **Descripción**: Permite cambiar de un grupo a otro de la misma materia
- **Campos requeridos**:
  - `materiaOrigenId`: ID de la materia actual
  - `grupoOrigenId`: ID del grupo actual
  - `grupoDestinoId`: ID del grupo destino
  - `descripcion`: Explicación del cambio
  - `tipoPrioridad`: Normal, Especial o Excepcional

### **2. Cambio de Materias en el Plan de Estudio**
- **Descripción**: Permite cambiar de una materia a otra diferente
- **Campos requeridos**:
  - `materiaOrigenId`: ID de la materia actual
  - `materiaDestinoId`: ID de la materia destino
  - `descripcion`: Explicación del cambio
  - `tipoPrioridad`: Normal, Especial o Excepcional

## 🎨 Sistema de Prioridades

### **NORMAL**
- Prioridad estándar para solicitudes regulares
- Tiempo de procesamiento: 3-5 días hábiles

### **ESPECIAL**
- Prioridad alta para casos justificados
- Tiempo de procesamiento: 1-2 días hábiles
- Requiere justificación adicional

### **EXCEPCIONAL**
- Prioridad máxima para casos urgentes
- Tiempo de procesamiento: Mismo día
- Requiere aprobación especial del decano

## 📊 Estados de Solicitudes

### **PENDIENTE**
- Estado inicial de todas las solicitudes
- Esperando revisión por parte del decano

### **EN_REVISION**
- Solicitud siendo evaluada por el decano
- Estado intermedio durante el proceso

### **APROBADA**
- Solicitud aprobada por el decano
- Se procede con el cambio solicitado

### **RECHAZADA**
- Solicitud rechazada por el decano
- Incluye justificación del rechazo

## 🚀 Endpoints Implementados

### **Para Estudiantes**

#### **Crear Solicitudes**
```
POST /api/estudiantes/{id}/solicitudes/cambio-grupo
POST /api/estudiantes/{id}/solicitudes/cambio-materia
```

**Ejemplo de solicitud de cambio de grupo**:
```json
{
  "materiaOrigenId": "materia_123",
  "grupoOrigenId": "grupo_456",
  "grupoDestinoId": "grupo_789",
  "descripcion": "Necesito cambiar de grupo por conflicto de horarios",
  "tipoPrioridad": "ESPECIAL"
}
```

**Ejemplo de solicitud de cambio de materia**:
```json
{
  "materiaOrigenId": "materia_123",
  "materiaDestinoId": "materia_456",
  "descripcion": "Cambio de carrera, necesito materia de mi nueva carrera",
  "tipoPrioridad": "NORMAL"
}
```

#### **Consultar Solicitudes**
```
GET /api/estudiantes/{id}/solicitudes
GET /api/estudiantes/{id}/solicitudes/historial
```

### **Para Administradores y Decanos**

#### **Gestión de Solicitudes**
```
GET /api/solicitudes
GET /api/solicitudes/{id}
PUT /api/solicitudes/{id}/estado
PUT /api/solicitudes/{id}/aprobar
PUT /api/solicitudes/{id}/rechazar
DELETE /api/solicitudes/{id}
```

#### **Filtros y Consultas**
```
GET /api/solicitudes/estado/{estado}
GET /api/solicitudes/estudiante/{estudianteId}
GET /api/solicitudes/decanatura/{decanaturaId}
GET /api/solicitudes/tipo/{tipo}
GET /api/solicitudes/prioridad/{prioridad}
GET /api/solicitudes/historial
GET /api/solicitudes/estadisticas
```

#### **Semáforo Académico**
```
GET /api/administradores/semaforo/estudiante/{estudianteId}/completo
GET /api/administradores/semaforo/estudiante/{estudianteId}/detallado
GET /api/decanaturas/semaforo/estudiante/{estudianteId}/completo
GET /api/decanaturas/semaforo/estudiante/{estudianteId}/detallado
```

## 🔧 Modelos de Datos

### **SolicitudCambio (Actualizado)**
```java
public class SolicitudCambio {
    private String id;
    private int idSolicitud;
    private Date fechaCreacion;
    private EstadoSolicitud estado;
    private int prioridad;
    private String observaciones;
    private Date fechaRespuesta;
    private String respuesta;
    
    // Información de la solicitud
    private String estudianteId;
    private String materiaOrigenId;
    private String grupoOrigenId;
    private String materiaDestinoId;
    private String grupoDestinoId;
    
    // Nuevos campos
    private String decanaturaId;
    private TipoSolicitud tipoSolicitud;
    private TipoPrioridad tipoPrioridad;
    private String descripcion;
    private String justificacion;
    private String administradorId;
}
```

### **TipoSolicitud (Nuevo)**
```java
public enum TipoSolicitud {
    CAMBIO_GRUPO,
    CAMBIO_MATERIA
}
```

### **TipoPrioridad (Nuevo)**
```java
public enum TipoPrioridad {
    NORMAL,
    ESPECIAL,
    EXCEPCIONAL
}
```

## 📈 Funcionalidades Avanzadas

### **Asignación Automática de Decanatura**
- Las solicitudes se asignan automáticamente a la decanatura correspondiente
- Basado en la facultad de la materia destino
- Permite distribución automática de carga de trabajo

### **Validaciones Inteligentes**
- Verificación de existencia de estudiantes, materias y grupos
- Validación de campos obligatorios según el tipo de solicitud
- Prevención de solicitudes duplicadas activas

### **Historial Completo**
- Todas las solicitudes se mantienen en el historial
- Ordenadas por fecha de creación (más recientes primero)
- Incluye información completa del proceso

### **Estadísticas Detalladas**
- Total de solicitudes por estado
- Distribución por tipo de solicitud
- Distribución por prioridad
- Métricas de rendimiento del sistema

## 🎯 Casos de Uso Implementados

### **1. Estudiante Crea Solicitud de Cambio de Grupo**
1. Estudiante accede al endpoint de creación
2. Completa la información requerida
3. Sistema valida la solicitud
4. Se asigna automáticamente a la decanatura
5. Solicitud queda en estado PENDIENTE

### **2. Decano Revisa Solicitudes**
1. Decano consulta solicitudes pendientes
2. Revisa la información y justificación
3. Aprueba o rechaza la solicitud
4. Proporciona justificación de la decisión
5. Sistema actualiza el estado

### **3. Administrador Supervisa el Sistema**
1. Administrador consulta estadísticas generales
2. Revisa solicitudes por decanatura
3. Puede aprobar solicitudes especiales
4. Genera reportes de gestión

### **4. Visualización del Semáforo Académico**
1. Administrador/Decano consulta semáforo de estudiante
2. Ve progreso académico completo
3. Identifica estudiantes en riesgo
4. Toma decisiones informadas

## 🔍 Filtros y Consultas Disponibles

### **Por Estado**
- PENDIENTE: Solicitudes esperando revisión
- EN_REVISION: Solicitudes siendo evaluadas
- APROBADA: Solicitudes aprobadas
- RECHAZADA: Solicitudes rechazadas

### **Por Tipo**
- CAMBIO_GRUPO: Cambios de grupo de materia
- CAMBIO_MATERIA: Cambios de materia

### **Por Prioridad**
- NORMAL: Prioridad estándar
- ESPECIAL: Prioridad alta
- EXCEPCIONAL: Prioridad máxima

### **Por Usuario**
- Por estudiante: Todas las solicitudes de un estudiante
- Por decanatura: Solicitudes asignadas a una decanatura
- Por administrador: Solicitudes gestionadas por un administrador

## 📊 Información Adicional en Solicitudes

### **Campos Obligatorios**
- `estudianteId`: ID del estudiante que hace la solicitud
- `descripcion`: Explicación detallada del cambio
- `tipoSolicitud`: Tipo de solicitud (cambio grupo/materia)
- `tipoPrioridad`: Prioridad de la solicitud

### **Campos Opcionales**
- `justificacion`: Justificación adicional del cambio
- `observaciones`: Observaciones adicionales
- `administradorId`: ID del administrador que gestiona

### **Campos Automáticos**
- `id`: ID único generado automáticamente
- `fechaCreacion`: Fecha de creación automática
- `estado`: Estado inicial PENDIENTE
- `decanaturaId`: Asignado automáticamente por facultad

## 🚀 Próximas Mejoras

1. **Notificaciones**: Sistema de notificaciones en tiempo real
2. **Workflow**: Flujo de aprobación multi-nivel
3. **Reportes**: Generación de reportes en PDF/Excel
4. **Dashboard**: Panel de control visual
5. **Integración**: Integración con sistemas externos

---

**Estado**: ✅ **IMPLEMENTACIÓN COMPLETA Y FUNCIONAL**
**Fecha**: Diciembre 2024
**Versión**: 1.0
