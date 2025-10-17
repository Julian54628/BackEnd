# Semáforo Académico - Sistema SIRHA

## 📊 Descripción General

El Semáforo Académico es una funcionalidad que permite a los estudiantes visualizar su progreso académico de manera intuitiva mediante un sistema de colores y estadísticas detalladas. Esta funcionalidad proporciona una vista completa del estado académico del estudiante.

## 🎨 Sistema de Colores

### **AZUL** - Materias Cursando Actualmente
- **Estado**: `INSCRITA`, `PENDIENTE`
- **Descripción**: Materias que el estudiante está cursando en el semestre actual
- **Información incluida**: Nombre, código, créditos, facultad, horarios

### **VERDE** - Materias Aprobadas
- **Estado**: `APROBADA`
- **Descripción**: Materias que el estudiante ha cursado y aprobado exitosamente
- **Información incluida**: Nombre, código, créditos, nota obtenida, semestre cursado

### **ROJO** - Materias Reprobadas
- **Estado**: `REPROBADA`, `CANCELADA`
- **Descripción**: Materias que el estudiante ha cursado pero no ha aprobado
- **Información incluida**: Nombre, código, créditos, motivo de reprobación

### **BLANCO** - Materias No Vistas
- **Estado**: No registradas en el historial
- **Descripción**: Materias del plan académico que el estudiante aún no ha cursado
- **Información incluida**: Nombre, código, créditos, prerrequisitos

## 📈 Estadísticas de Créditos

### **Créditos Actuales**
- **Definición**: Créditos que el estudiante está cursando en el semestre actual
- **Cálculo**: Suma de créditos de todas las materias inscritas
- **Máximo**: 18 créditos por semestre

### **Créditos Completados**
- **Definición**: Suma total de créditos de materias aprobadas
- **Cálculo**: Suma de créditos de todas las materias con estado `APROBADA`

### **Créditos Faltantes**
- **Definición**: Créditos restantes para completar el plan académico
- **Cálculo**: `Total Créditos Plan - Créditos Completados`

### **Porcentaje de Progreso**
- **Definición**: Porcentaje de avance en el plan académico
- **Cálculo**: `(Créditos Completados / Total Créditos Plan) * 100`

## 📊 Estadísticas de Materias

### **Materias Aprobadas**
- Cantidad de materias con estado `APROBADA`
- Contribuyen a los créditos completados

### **Materias Reprobadas**
- Cantidad de materias con estado `REPROBADA` o `CANCELADA`
- Requieren ser cursadas nuevamente

### **Materias Cursando**
- Cantidad de materias con estado `INSCRITA` o `PENDIENTE`
- Representan la carga académica actual

### **Materias Pendientes**
- Cantidad de materias del plan que no han sido cursadas
- Representan el trabajo académico futuro

## 🎯 Información Adicional

### **Estado Académico**
- **EXCELENTE**: Promedio ≥ 4.0
- **REGULAR**: Promedio ≥ 3.0 y < 4.0
- **EN_RIESGO**: Promedio < 3.0

### **Semestres Restantes**
- Estimación basada en créditos faltantes
- Asume 15 créditos por semestre en promedio

### **Elegibilidad de Graduación**
- Verifica si el estudiante cumple los requisitos para graduarse
- Requisitos: Créditos completados ≥ Total créditos plan Y Promedio ≥ 3.0

## 🚀 Endpoints Disponibles

### 1. Semáforo Completo
```
GET /api/estudiantes/{id}/semaforo-completo
```
**Descripción**: Obtiene la visualización completa del semáforo académico.

**Respuesta**:
```json
{
  "estudianteId": "estudiante_123",
  "nombreEstudiante": "Juan Pérez",
  "codigoEstudiante": "2023123456",
  "carrera": "Ingeniería de Sistemas",
  "semestreActual": 5,
  "grado": "PREGRADO",
  "creditosActuales": 15,
  "creditosCompletados": 45,
  "creditosFaltantes": 75,
  "creditosMaximosSemestre": 18,
  "totalCreditosPlan": 120,
  "materiasAprobadas": 12,
  "materiasReprobadas": 2,
  "materiasCursando": 5,
  "materiasPendientes": 8,
  "totalMateriasPlan": 27,
  "promedioAcumulado": 3.8,
  "porcentajeProgreso": 37.5,
  "materiasAzules": [...],
  "materiasVerdes": [...],
  "materiasRojas": [...],
  "materiasBlancas": [...]
}
```

### 2. Semáforo Detallado
```
GET /api/estudiantes/{id}/semaforo-detallado
```
**Descripción**: Obtiene la visualización detallada con información adicional.

**Respuesta**: Incluye toda la información del semáforo completo más:
```json
{
  "estadoAcademico": "REGULAR",
  "semestresRestantes": 5,
  "puedeGraduarse": false
}
```

## 🔧 Implementación Técnica

### **Modelos Creados**

#### `SemaforoVisualizacion`
- Contiene toda la información del semáforo académico
- Incluye estadísticas, colores y información del estudiante

#### `MateriaSemaforo`
- Representa una materia en el contexto del semáforo
- Incluye información de la materia y su estado/color

### **Lógica de Colores**
```java
private String determinarColor(EstadoMateria estado) {
    switch (estado) {
        case INSCRITA:
        case PENDIENTE:
            return "AZUL";
        case APROBADA:
            return "VERDE";
        case REPROBADA:
        case CANCELADA:
            return "ROJO";
        default:
            return "BLANCO";
    }
}
```

### **Cálculo de Créditos Actuales**
```java
private int calcularCreditosSemestreActual(Estudiante estudiante) {
    int creditosActuales = 0;
    for (String grupoId : estudiante.getHorariosIds()) {
        // Buscar grupo y materia, sumar créditos
    }
    return creditosActuales;
}
```

### **Procesamiento de Materias por Colores**
1. Obtener todas las materias del plan académico
2. Procesar materias del historial según su estado
3. Identificar materias no vistas como blancas
4. Clasificar por colores según la lógica definida

## 📋 Casos de Uso

### **Estudiante Consulta su Progreso**
1. Accede al endpoint de semáforo completo
2. Ve sus materias organizadas por colores
3. Revisa sus estadísticas de créditos
4. Identifica materias pendientes

### **Estudiante Planifica su Graduación**
1. Accede al endpoint de semáforo detallado
2. Ve semestres restantes estimados
3. Verifica elegibilidad de graduación
4. Planifica materias futuras

### **Sistema Genera Reportes**
1. Utiliza los endpoints para obtener datos
2. Genera visualizaciones gráficas
3. Crea reportes de progreso académico

## 🎯 Beneficios

### **Para el Estudiante**
- Vista clara y organizada de su progreso
- Identificación fácil de materias pendientes
- Motivación visual para continuar estudios
- Planificación académica informada

### **Para la Institución**
- Monitoreo del progreso estudiantil
- Identificación de estudiantes en riesgo
- Datos para toma de decisiones académicas
- Mejora en la retención estudiantil

## 🔍 Validaciones Implementadas

1. **Estudiante Existe**: Verifica que el estudiante exista
2. **Semáforo Académico**: Verifica que exista el semáforo
3. **Plan Académico**: Verifica que exista el plan de estudios
4. **Materias Válidas**: Verifica que las materias existan
5. **Cálculos Correctos**: Valida que los cálculos sean precisos

## 🚀 Próximas Mejoras

1. **Visualización Gráfica**: Implementar gráficos y diagramas
2. **Alertas**: Notificaciones para estudiantes en riesgo
3. **Comparativas**: Comparar con otros estudiantes
4. **Predicciones**: IA para predecir tiempo de graduación
5. **Exportación**: Exportar semáforo a PDF/Excel

---

**Estado**: ✅ **IMPLEMENTACIÓN COMPLETA Y FUNCIONAL**
**Fecha**: Diciembre 2024
**Versión**: 1.0
