# Playlist de Música - Webapp

Webapp desarrollada en Java con Spring Boot para gestionar una playlist de videos de música de YouTube.

## 📋 Estado del Proyecto

### ✅ Proyecto Completado

Todas las fases del desarrollo han sido completadas:

#### ✅ Fase 1: Hola Mundo + CI/CD
- Aplicación Spring Boot básica funcionando
- Tests con JUnit configurados
- Pipeline de Jenkins configurado
- Scripts de deployment para Mac y Windows

#### ✅ Fase 2: Funcionalidades Core
- Agregar videos a la playlist (nombre + URL de YouTube)
- Eliminar videos de la playlist
- Visualización embebida de videos de YouTube
- Validación de URLs de YouTube

#### ✅ Fase 3: Persistencia
- Base de datos H2 embebida configurada
- Persistencia de datos entre ejecuciones
- Entidades JPA para videos

#### ✅ Fase 4: UI Mejorada
- Interfaz moderna con Bootstrap 5
- Diseño responsive y atractivo
- Feedback visual para acciones del usuario

#### ✅ Fase 5: Funcionalidades Adicionales
- Sistema de likes (contador y botón)
- Sistema de favoritos (marcar/desmarcar videos)
- Persistencia de likes y favoritos

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** (gestión de dependencias)
- **Thymeleaf** (templates)
- **Bootstrap 5** (framework CSS)
- **H2 Database** (base de datos embebida)
- **Spring Data JPA / Hibernate** (ORM)
- **JUnit 5** (testing)
- **Jenkins** (CI/CD)

## 📦 Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- Git (para control de versiones)
- Jenkins (para CI/CD, opcional para desarrollo local)

## 🚀 Instalación y Ejecución

### Opción 1: Ejecutar directamente con Maven

```bash
# Compilar y ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en: http://localhost:8080

### Opción 2: Compilar y ejecutar JAR

```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/playlist-musica-1.0.0.jar
```

### Opción 3: Usar scripts de deployment

#### Mac/Linux:
```bash
chmod +x deploy.sh
./deploy.sh
```

#### Windows:
```cmd
deploy.bat
```

Los scripts automáticamente:
1. Detienen la aplicación si está corriendo
2. Compilan el proyecto
3. Ejecutan los tests
4. Empaquetan la aplicación
5. Inician la aplicación en background

## 🧪 Ejecutar Tests

```bash
mvn test
```

## 🔧 Configuración de Jenkins

### Instalación de Jenkins (Mac)

```bash
# Con Homebrew
brew install jenkins-lts

# Iniciar Jenkins
brew services start jenkins-lts
```

**Nota**: Si tu aplicación Spring Boot está corriendo en el puerto 8080, Jenkins se iniciará en el puerto 8081.

Acceder a: http://localhost:8081 (o el puerto configurado)

### Configuración del Pipeline

1. Crear un nuevo Pipeline en Jenkins (nombre sugerido: `playlist-musica-pipeline`)
2. Seleccionar "Pipeline script from SCM"
3. Configurar:
   - **SCM**: Git
   - **Repository URL**: `https://github.com/inesungo/entregable-4.git`
   - **Branch**: `*/main`
   - **Script Path**: `Jenkinsfile`
4. Guardar y ejecutar "Build Now"

#### Pipeline Automatizado

El pipeline ejecuta automáticamente los siguientes pasos:

1. **Checkout**: Obtiene el código del repositorio Git
2. **Build**: Compila la aplicación con `mvn clean compile`
3. **Test**: Ejecuta tests automáticos con JUnit (`mvn test`)
4. **Package**: Empaqueta la aplicación en JAR (`mvn clean package`)
5. **Deploy**: 
   - Detiene la aplicación si está corriendo
   - Copia el JAR a `/tmp/playlist-deploy/`
   - Inicia la aplicación en background
   - Verifica que la aplicación esté corriendo

**Nota**: Si tu aplicación Spring Boot está corriendo en el puerto 8080, Jenkins se configuró para usar el puerto 8081.

## 📁 Estructura del Proyecto

```
playlist-musica/
├── src/
│   ├── main/
│   │   ├── java/com/playlist/musica/
│   │   │   ├── PlaylistMusicaApplication.java
│   │   │   ├── controller/
│   │   │   │   └── HomeController.java
│   │   │   ├── model/
│   │   │   │   └── Video.java
│   │   │   ├── repository/
│   │   │   │   └── VideoRepository.java
│   │   │   └── service/
│   │   │       ├── VideoService.java
│   │   │       └── YouTubeService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           └── index.html
│   └── test/
│       └── java/com/playlist/musica/
│           ├── PlaylistMusicaApplicationTest.java
│           ├── controller/
│           │   └── HomeControllerTest.java
│           └── service/
│               ├── VideoServiceTest.java
│               └── YouTubeServiceTest.java
├── data/ (generado por H2 Database)
│   ├── playlist.mv.db
│   └── playlist.trace.db
├── Jenkinsfile
├── deploy.sh
├── deploy.bat
├── pom.xml
├── .gitignore
├── README.md
└── ANALISIS_REQUERIMIENTOS.md
```

## 🛑 Detener la Aplicación

### Mac/Linux:
```bash
# Si usaste el script deploy.sh
kill $(cat deploy/app.pid)

# O buscar y matar el proceso
pkill -f "playlist-musica"
```

### Windows:
- Cerrar la ventana de comandos donde se ejecutó
- O usar el Administrador de Tareas para finalizar el proceso Java

## ✨ Funcionalidades Implementadas

### Gestión de Videos
- ✅ Agregar videos con nombre y URL de YouTube
- ✅ Eliminar videos de la playlist
- ✅ Validación de URLs de YouTube (soporta múltiples formatos)
- ✅ Extracción automática del ID del video

### Visualización
- ✅ Reproductor embebido de YouTube
- ✅ Lista de videos con información completa
- ✅ Interfaz responsive y moderna

### Persistencia
- ✅ Base de datos H2 embebida
- ✅ Datos persisten entre ejecuciones
- ✅ Almacenamiento de: nombre, URL, likes, favoritos, fecha de creación

### Interacciones
- ✅ Sistema de likes (botón y contador)
- ✅ Sistema de favoritos (marcar/desmarcar con estrella)
- ✅ Feedback visual para todas las acciones

## 📝 Notas de Desarrollo

- La aplicación usa el puerto **8080** por defecto
- Jenkins está configurado para usar el puerto **8081** (para evitar conflictos)
- Los logs se guardan en `deploy/app.log` cuando se usa el script de deployment
- La base de datos H2 se guarda en `data/playlist.mv.db`
- Para desarrollo, se recomienda usar `mvn spring-boot:run` para hot-reload
- La consola H2 está disponible en: http://localhost:8080/h2-console

## 🔗 Repositorio

El código está disponible en: https://github.com/inesungo/entregable-4

## 📄 Licencia

Este proyecto es parte de un trabajo académico.

---

**Desarrollado con ❤️ usando Spring Boot**

