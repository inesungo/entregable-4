# Análisis de Requerimientos - Webapp de Playlist de Videos

## Resumen del Proyecto

Desarrollo de una webapp en Java para gestionar una playlist de videos de **música** con funcionalidades de gestión, visualización, persistencia y sistema de likes/favoritos. Incluye configuración completa de CI/CD con Jenkins.

### Decisiones Técnicas Tomadas

- **Tema**: Música
- **Framework**: Spring Boot (recomendado - facilita desarrollo y despliegue)
- **Persistencia**: H2 Database (embebida, fácil de configurar, no requiere instalación externa)
- **Plataforma de videos**: Solo YouTube
- **Servidor**: Tomcat embebido (incluido en Spring Boot)

---

## Parte 1: Desarrollo - Requerimientos Funcionales

### Prioridad 1: Funcionalidades Core

#### 1.1. Gestión de Videos (ALTA PRIORIDAD)
- **Agregar videos**: El usuario debe poder agregar videos a la playlist
  - Campos requeridos: nombre y link del video
  - Validación: verificar que el link sea válido (formato URL)
  
- **Quitar videos**: El usuario debe poder eliminar videos de la playlist
  - Confirmación de eliminación (opcional pero recomendado)
  - Actualización inmediata de la UI

#### 1.2. Visualización Embebida (ALTA PRIORIDAD)
- **Reproductor embebido**: Los videos deben poder reproducirse directamente en la webapp
  - **Plataforma**: Solo YouTube
  - Usar iframe embebido de YouTube
  - Extraer ID del video desde la URL de YouTube (diferentes formatos: youtube.com/watch?v=, youtu.be/, etc.)
  - Construir URL embebida: `https://www.youtube.com/embed/{VIDEO_ID}`
  - Manejo de errores si el link no es válido o el video no está disponible
  - Validar que la URL sea de YouTube antes de procesar

### Prioridad 2: Persistencia y UI

#### 1.3. Persistencia de Datos (MEDIA PRIORIDAD)
- **Almacenamiento persistente**: Los datos deben persistir entre ejecuciones
  - **Decisión**: Base de datos H2 (embebida, fácil de configurar)
  - **Ventajas de H2**:
    - No requiere instalación externa
    - Se guarda en archivo local (persistente)
    - Compatible con JPA/Hibernate
    - Fácil de configurar con Spring Boot
  - **Datos a persistir**:
    - ID (auto-generado)
    - Nombre del video
    - Link/URL del video
    - ID de YouTube extraído
    - Cantidad de likes (inicial: 0)
    - Estado de favorito (boolean)
    - Fecha de creación (opcional pero recomendado)

#### 1.4. Interfaz de Usuario Atractiva (MEDIA PRIORIDAD)
- **Herramientas sugeridas**:
  - Bootstrap (CSS framework)
  - Material Design
  - Thymeleaf (si se usa Spring Boot) con estilos modernos
  - CSS moderno con Flexbox/Grid
- **Características**:
  - Diseño responsive
  - Interfaz intuitiva y moderna
  - Feedback visual para acciones del usuario

### Prioridad 3: Funcionalidades Adicionales

#### 1.5. Sistema de Likes (BAJA PRIORIDAD)
- **Funcionalidad**: Cada video puede tener likes
  - Botón/interacción para dar like
  - Contador visual de likes
  - Persistencia de likes (guardar en base de datos/archivo)
  - Posible mejora: evitar likes duplicados del mismo usuario (requiere autenticación)

#### 1.6. Sistema de Favoritos (BAJA PRIORIDAD)
- **Funcionalidad**: Marcar/desmarcar videos como favoritos
  - Toggle para marcar/desmarcar
  - Indicador visual (estrella, corazón, etc.)
  - Filtro para ver solo favoritos (opcional pero recomendado)
  - Persistencia del estado de favorito

---

## Parte 2: CI/CD - Requerimientos Técnicos

### 2.1. Control de Versiones
- **Herramienta**: Git (o similar)
- **Requisitos**:
  - Repositorio inicializado
  - Commits regulares y descriptivos
  - Estructura de branches (opcional pero recomendado: main, develop)
  - Archivo `.gitignore` apropiado para Java

### 2.2. Jenkins - Pipeline de CI/CD

#### Configuración Local
- **Instalación**: Jenkins corriendo localmente
- **Plugins necesarios**:
  - Git plugin
  - Pipeline plugin
  - JUnit plugin (para reportes de tests)

#### Pipeline Automatizado
El pipeline debe ejecutar los siguientes pasos en orden:

**a. Checkout del código**
- Clonar/actualizar código desde el repositorio Git
- Usar credenciales si es necesario

**b. Build de la aplicación**
- Compilar el proyecto Java
- Si usa Maven: `mvn clean compile` o `mvn clean package`
- Si usa Gradle: `./gradlew build`
- Manejo de dependencias

**c. Ejecución de Tests Automáticos**
- Ejecutar tests unitarios con JUnit
- Generar reportes de tests
- Pipeline debe fallar si los tests fallan (fail-fast)
- Cobertura de código (opcional pero recomendado)

**d. Deploy**
- Desplegar la aplicación
- Si es webapp: copiar archivos WAR/JAR a directorio de servidor
- Reiniciar servicio si es necesario
- Verificación post-deploy

### 2.3. Scripts de Deployment

#### Requisitos
- **Script para Mac** (bash/shell script)
  - Extension: `.sh`
  - Ejecutable: `chmod +x deploy.sh`
  - Debe manejar rutas de Mac
  - Instrucciones de uso incluidas

- **Script para Windows** (batch/PowerShell)
  - Extension: `.bat` o `.ps1`
  - Si es PowerShell: incluir instrucciones de ejecución
  - Debe manejar rutas de Windows
  - Instrucciones de uso incluidas

#### Funcionalidad de los Scripts
- Detener aplicación si está corriendo
- Compilar proyecto
- Ejecutar tests
- Copiar archivos necesarios
Iniciar aplicación
- Verificar que la aplicación esté corriendo
- Logs de deployment

---

## Sugerencias y Recomendaciones

### Enfoque Incremental (CRÍTICO)
1. **Fase 1: "Hola Mundo" + CI/CD**
   - Crear webapp básica que muestre "Hola Mundo"
   - Configurar Git
   - Configurar Jenkins completo (checkout, build, test, deploy)
   - Crear scripts de deployment
   - **Validar que todo funciona antes de continuar**

2. **Fase 2: Funcionalidades Core**
   - Agregar/quitar videos (sin persistencia)
   - Visualización embebida básica

3. **Fase 3: Persistencia**
   - Implementar almacenamiento de datos
   - Verificar que persiste entre ejecuciones

4. **Fase 4: UI Mejorada**
   - Aplicar framework CSS
   - Mejorar diseño y UX

5. **Fase 5: Funcionalidades Adicionales**
   - Sistema de likes
   - Sistema de favoritos

### Stack Tecnológico Definido

#### Backend
- **Java 11+** (o Java 17 recomendado)
- **Framework web**: **Spring Boot 2.7+ o 3.x**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Boot DevTools (para desarrollo)
- **Build tool**: **Maven** (más común y estándar)
- **Servidor**: **Tomcat embebido** (incluido en Spring Boot)

#### Frontend
- **Templates**: **Thymeleaf** (integración perfecta con Spring Boot)
- **CSS Framework**: **Bootstrap 5** (moderno, fácil de usar, buena documentación)
- **JavaScript**: Vanilla JS (sin dependencias adicionales)

#### Persistencia
- **Base de datos**: **H2 Database** (embebida, archivo local)
- **ORM**: **Spring Data JPA / Hibernate**
- **Configuración**: 
  - Modo file (persistente): `jdbc:h2:file:./data/playlist`
  - O modo memoria para tests: `jdbc:h2:mem:testdb`

#### Testing
- **JUnit 5** (Jupiter)
- **Spring Boot Test** (para tests de integración)
- **Mockito** (para mocks, opcional pero útil)
- **H2 en memoria** para tests

#### CI/CD
- **Git**: Control de versiones
- **Jenkins**: Servidor CI/CD local
- **Maven**: Para build y tests (`mvn clean package`)

### Estructura de Proyecto Definida

```
playlist-musica/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/playlist/musica/
│   │   │       ├── PlaylistMusicaApplication.java
│   │   │       ├── controller/
│   │   │       │   └── VideoController.java
│   │   │       ├── model/
│   │   │       │   └── Video.java (entidad JPA)
│   │   │       ├── service/
│   │   │       │   ├── VideoService.java
│   │   │       │   └── YouTubeService.java (extracción de ID)
│   │   │       └── repository/
│   │   │           └── VideoRepository.java (JPA Repository)
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css
│   │       │   └── js/
│   │       │       └── main.js
│   │       └── templates/
│   │           ├── index.html
│   │           └── fragments/ (opcional)
│   └── test/
│       └── java/
│           └── com/playlist/musica/
│               ├── controller/
│               ├── service/
│               └── repository/
├── data/ (generado automáticamente por H2)
│   └── playlist.mv.db
├── Jenkinsfile
├── deploy.sh (Mac)
├── deploy.bat (Windows)
├── pom.xml
├── .gitignore
└── README.md
```

### Configuración application.properties

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:file:./data/playlist
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console (para desarrollo)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Server Configuration
server.port=8080

# Thymeleaf Configuration
spring.thymeleaf.cache=false
```

### Consideraciones Adicionales

1. **Validación de Links de Video de YouTube**
   - Validar que la URL sea de YouTube
   - Formatos soportados:
     - `https://www.youtube.com/watch?v=VIDEO_ID`
     - `https://youtu.be/VIDEO_ID`
     - `https://www.youtube.com/embed/VIDEO_ID`
   - Extraer ID del video desde cualquier formato
   - Validar que el ID tenga el formato correcto (11 caracteres alfanuméricos)
   - Construir URL embebida: `https://www.youtube.com/embed/{VIDEO_ID}`

2. **Manejo de Errores**
   - Validación de inputs
   - Manejo de excepciones
   - Mensajes de error amigables

3. **Seguridad Básica**
   - Validar inputs del usuario
   - Prevenir XSS (si se permite HTML)
   - Sanitizar URLs

4. **Documentación**
   - README con instrucciones de instalación
   - Comentarios en código
   - Documentación del pipeline de Jenkins

5. **Mejoras Opcionales (si hay tiempo)**
   - Búsqueda de videos
   - Ordenamiento (por nombre, fecha, likes)
   - Paginación si hay muchos videos
   - Categorías/tags
   - Comentarios

---

## Checklist de Entregables

### Parte 1: Desarrollo
- [ ] Webapp funcional en Java
- [ ] Agregar videos (nombre + link)
- [ ] Quitar videos
- [ ] Visualización embebida de videos
- [ ] Persistencia de datos (funciona entre ejecuciones)
- [ ] UI atractiva (framework CSS aplicado)
- [ ] Sistema de likes
- [ ] Sistema de favoritos (marcar/desmarcar)

### Parte 2: CI/CD
- [ ] Repositorio Git configurado
- [ ] Jenkins instalado y configurado localmente
- [ ] Pipeline Jenkins que:
  - [ ] Hace checkout del código
  - [ ] Compila la aplicación
  - [ ] Ejecuta tests automáticos (JUnit)
  - [ ] Realiza deploy
- [ ] Script de deployment para Mac (`.sh`)
- [ ] Script de deployment para Windows (`.bat` o `.ps1`)
- [ ] Documentación de cómo ejecutar los scripts

### Documentación
- [ ] README.md con instrucciones
- [ ] Comentarios en código
- [ ] Documentación del pipeline

---

## Notas Finales

- **Trabajo incremental es clave**: No intentar hacer todo de una vez
- **CI/CD primero**: Configurar Jenkins con "Hola Mundo" antes de agregar complejidad
- **Tests desde el inicio**: Escribir tests mientras se desarrolla, no al final
- **Commits frecuentes**: Hacer commits pequeños y descriptivos
- **Validar cada paso**: Asegurarse de que cada funcionalidad funciona antes de agregar la siguiente

---

## Decisiones Técnicas Finales

✅ **Tema de la playlist**: Música  
✅ **Framework**: Spring Boot (con Tomcat embebido)  
✅ **Persistencia**: H2 Database (embebida, archivo local)  
✅ **Plataforma de videos**: Solo YouTube  
✅ **Servidor**: Tomcat embebido (incluido en Spring Boot)  

### Dependencias Maven Principales

```xml
<!-- Spring Boot Starter Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot Starter Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- Spring Boot DevTools -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

*Análisis creado: [Fecha]*
*Última actualización: [Fecha]*

