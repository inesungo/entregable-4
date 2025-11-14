# Playlist de Música - Webapp

Webapp desarrollada en Java con Spring Boot para gestionar una playlist de videos de música de YouTube.

## 📋 Estado del Proyecto

### ✅ Fase 1: Hola Mundo + CI/CD (Completada)
- Aplicación Spring Boot básica funcionando
- Tests con JUnit configurados
- Pipeline de Jenkins configurado
- Scripts de deployment para Mac y Windows

### 🔄 Próximas Fases
- Fase 2: Funcionalidades Core (agregar/quitar videos, visualización embebida)
- Fase 3: Persistencia con H2 Database
- Fase 4: UI mejorada con Bootstrap
- Fase 5: Sistema de likes y favoritos

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** (gestión de dependencias)
- **Thymeleaf** (templates)
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

Acceder a: http://localhost:8080 (o el puerto configurado)

### Configuración del Pipeline

1. Crear un nuevo Pipeline en Jenkins
2. Seleccionar "Pipeline script from SCM"
3. Configurar el repositorio Git
4. Especificar el path del Jenkinsfile: `Jenkinsfile`

El pipeline ejecutará automáticamente:
- **Checkout**: Obtiene el código del repositorio
- **Build**: Compila la aplicación
- **Test**: Ejecuta tests automáticos
- **Package**: Empaqueta la aplicación
- **Deploy**: Despliega la aplicación

## 📁 Estructura del Proyecto

```
playlist-musica/
├── src/
│   ├── main/
│   │   ├── java/com/playlist/musica/
│   │   │   ├── PlaylistMusicaApplication.java
│   │   │   └── controller/
│   │   │       └── HomeController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           └── index.html
│   └── test/
│       └── java/com/playlist/musica/
│           ├── PlaylistMusicaApplicationTest.java
│           └── controller/
│               └── HomeControllerTest.java
├── deploy/ (generado por scripts)
├── Jenkinsfile
├── deploy.sh
├── deploy.bat
├── pom.xml
└── README.md
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

## 📝 Notas de Desarrollo

- La aplicación usa el puerto **8080** por defecto
- Los logs se guardan en `deploy/app.log` cuando se usa el script de deployment
- Para desarrollo, se recomienda usar `mvn spring-boot:run` para hot-reload

## 🔄 Próximos Pasos

1. Agregar funcionalidad para agregar/quitar videos
2. Implementar visualización embebida de YouTube
3. Configurar persistencia con H2 Database
4. Mejorar UI con Bootstrap
5. Agregar sistema de likes y favoritos

## 📄 Licencia

Este proyecto es parte de un trabajo académico.

---

**Desarrollado con ❤️ usando Spring Boot**

