# Sirha BackEnd
A continuación, el desarrollo de los puntos propuestos en la documentación técnica:

---

## Diseño de los Diagramas
### Diagrama de clases
#### Descripción
El diagrama de clases representa la estructura estática del sistema SIRHA, mostrando las clases del paquete model, sus atributos, métodos y las relaciones entre ellas. Este diagrama refleja fielmente la implementación Java proporcionada.
#### Clases
- Clase Base:
    - Usuario: Clase abstracta que representa un usuario del sistema con atributos comunes (id, username, passwordHash, correoInstitucional, rol, activo). Relaciones de herencia con Administrador, Decanatura y Estudiante. 
- Clases Especializada:
    - Administrador: Extiende Usuario, representa usuarios con privilegios administrativos.
    - Decanatura: Extiende Usuario, incluye atributos específicos (idDecanatura, nombre, facultad).
    - Estudiante: Extiende Usuario, contiene información académica (idEstudiante, nombre, codigo, carrera, semestre) y referencias a otras entidades.
- Entidades Académicas:
    - Materia: Representa las materias académicas con sus propiedades (idMateria, nombre, codigo, creditos, facultad, esObligatoria).
    - Grupo: Modela los grupos de clase con cupos y referencias a estudiantes, profesor y horarios.
    - Horario: Define los horarios de clase con día, hora inicio/fin, salon y referencias.
- Gestión de Solicitudes:
    - SolicitudCambio: Gestiona las solicitudes de cambio con estados, prioridad y referencias a estudiante, materias y grupos.
    - PeriodoCambio: Controla los periodos habilitados para cambios.
- Planificación Académica:
    - PlanAcademico: Define los planes de estudio con materias obligatorias y electivas.
    - SemaforoAcademico: Representa el estado académico del estudiante (avance, créditos, promedio).
- Entidades de Soporte:
    - Profesor: Información de profesores y sus asignaciones.
    - Enumeraciones: EstadoSolicitud, EstadoMateria, EstadoSemaforo, Rol.
#### Justificación de Diseño
- Herencia para Usuarios: Se utiliza herencia para evitar duplicación de código en atributos comunes de usuarios.
- Referencias por ID: En lugar de relaciones complejas con @DBRef, se usan referencias por ID para mejor performance en MongoDB.
- Separación de Responsabilidades: Cada clase tiene una responsabilidad única y clara.
- Validaciones Incorporadas: Métodos esValido() en cada clase para mantener la integridad de datos.
- Enumeraciones para Estados: Uso de enums para garantizar consistencia en estados del sistema.
#### Imagen del Diagrama
![diagramaClases.png](img%2Fdiagramas%2FdiagramaClases.png)
### Diagrama de Casos de Uso
#### Descripción
Representa las funcionalidades del sistema desde la perspectiva de los usuarios, mostrando qué actores pueden realizar qué acciones en el sistema.
#### Actores
- Estudiante
  - Descripción: Usuario principal que realiza solicitudes de cambio de horario
  - Responsabilidades: Gestionar su perfil, crear solicitudes, consultar estados
  - Cantidad: Múltiples (todos los estudiantes de la institución)

    ![estudianteUso.png](img%2Fdiagramas%2FestudianteUso.png)
- Decanatura
  - Descripción: Usuario administrativo que gestiona y aprueba solicitudes
  - Responsabilidades: Revisar solicitudes, tomar decisiones, generar reportes
  - Restricciones: Solo puede gestionar materias de su facultad

    ![decanaturaUso.png](img%2Fdiagramas%2FdecanaturaUso.png)
- Administrador
  - Descripción: Usuario con privilegios completos del sistema
  - Responsabilidades: Configuración global, gestión de usuarios, supervisión
  - Cantidad: Limitada (personal administrativo especializado)

    ![administradorUso.png](img%2Fdiagramas%2FadministradorUso.png)