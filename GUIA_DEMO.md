# 🎯 Guía Completa para la Demo - Entrega 4

## 📋 Estructura de la Demo

La demo consta de 3 partes:
1. **Mostrar la aplicación web desarrollada**
2. **Refactoring de code smell + CI/CD con Jenkins**
3. **Explicación técnica**

---

## 🎬 PARTE 1: Mostrar la Aplicación Web

### Preparación (5 minutos antes de la demo)
```bash
# 1. Asegurarse de que la aplicación esté corriendo
cd "/Users/user/Desktop/pro avanzada entrega 4"
mvn spring-boot:run

# 2. Verificar que esté funcionando
# Abrir navegador en: http://localhost:8080
```

### Durante la Demo (5-7 minutos)

#### 1. **Página Principal**
- Mostrar la interfaz moderna y responsive
- Explicar el header con logo y título

#### 2. **Estadísticas en Tiempo Real**
- Mostrar las 3 tarjetas de estadísticas:
  - Total de videos
  - Favoritos
  - Likes totales
- Explicar que se actualizan automáticamente

#### 3. **Formulario de Agregar Video**
- **Demostrar agregando un video real:**
  - Nombre: "Canción Demo"
  - URL: `https://www.youtube.com/watch?v=dQw4w9WgXcQ`
  - Género: Seleccionar uno (ej: Pop)
- Mostrar mensaje de éxito
- Explicar validación de URLs de YouTube

#### 4. **Funcionalidades de Videos**
- **Búsqueda:** Escribir algo en el buscador
- **Filtros:**
  - Tabs: Todos / Favoritos / Populares
  - Filtro por género
  - Ordenamiento (Recientes, Antiguos, Likes, A-Z)
- **Acciones en cada video:**
  - Ver en pantalla completa (modal)
  - Copiar link
  - Dar like (mostrar contador)
  - Marcar como favorito
  - Abrir en YouTube
  - Eliminar (con confirmación)

#### 5. **Características Técnicas**
- Mencionar que usa:
  - Spring Boot (backend)
  - H2 Database (persistencia)
  - Thymeleaf (templates)
  - Bootstrap 5 (UI)
  - JavaScript (interactividad)

---

## 🔧 PARTE 2: Refactoring + CI/CD

### Code Smell Identificado: Duplicación de Código

**Problema:** En `VideoService.java`, los métodos `darLike()` y `toggleFavorito()` tienen código duplicado para buscar un video y lanzar excepción si no existe.

**Solución:** Extraer el código común a un método privado `obtenerVideoPorId()` usando la técnica de refactoring **"Extract Method"**.

### Pasos para la Demo (10-12 minutos)

#### Paso 1: Mostrar el Code Smell (2 min)
```bash
# Abrir el archivo en el editor
code src/main/java/com/playlist/musica/service/VideoService.java
```

**Señalar:**
- Líneas 72-73: `darLike()` busca el video
- Líneas 82-83: `toggleFavorito()` hace lo mismo
- **Code smell:** Duplicación de lógica

#### Paso 2: Realizar el Refactoring (3 min)

**Antes:**
```java
public Video darLike(Long id) {
    Video video = videoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Video no encontrado"));
    video.setLikes(video.getLikes() + 1);
    return videoRepository.save(video);
}

public Video toggleFavorito(Long id) {
    Video video = videoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Video no encontrado"));
    video.setEsFavorito(!video.getEsFavorito());
    return videoRepository.save(video);
}
```

**Después (refactorizado):**
```java
/**
 * Obtiene un video por ID o lanza excepción si no existe
 * Método privado extraído para eliminar duplicación de código
 */
private Video obtenerVideoPorId(Long id) {
    return videoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Video no encontrado"));
}

public Video darLike(Long id) {
    Video video = obtenerVideoPorId(id);
    video.setLikes(video.getLikes() + 1);
    return videoRepository.save(video);
}

public Video toggleFavorito(Long id) {
    Video video = obtenerVideoPorId(id);
    video.setEsFavorito(!video.getEsFavorito());
    return videoRepository.save(video);
}
```

**Beneficios:**
- ✅ Elimina duplicación (DRY - Don't Repeat Yourself)
- ✅ Facilita mantenimiento (cambios en un solo lugar)
- ✅ Mejora legibilidad
- ✅ Facilita testing

#### Paso 3: Verificar que Funciona (2 min)
```bash
# Compilar y ejecutar tests
mvn clean test

# Verificar que todos los tests pasan
```

#### Paso 4: Commit y Push a GitHub (1 min)
```bash
# Agregar cambios
git add src/main/java/com/playlist/musica/service/VideoService.java

# Commit con mensaje descriptivo
git commit -m "refactor: Extraer método obtenerVideoPorId() para eliminar duplicación de código"

# Push a GitHub
git push origin main
```

#### Paso 5: Ejecutar Pipeline en Jenkins (3-4 min)

**A. Abrir Jenkins:**
- URL: `http://localhost:8081`
- Iniciar sesión

**B. Ejecutar el Pipeline:**
1. Ir al proyecto "playlist-musica" (o el nombre que tenga)
2. Click en **"Build Now"** o **"Construir ahora"**
3. Observar la ejecución en tiempo real:
   - ✅ Stage: Checkout
   - ✅ Stage: Build
   - ✅ Stage: Test (debe pasar todos los tests)
   - ✅ Stage: Package
   - ✅ Stage: Deploy

**C. Verificar el Deploy:**
- El pipeline debe mostrar: "✅ Aplicación desplegada correctamente"
- Verificar en: `http://localhost:8080`
- La aplicación debe estar funcionando con los cambios

---

## 📚 PARTE 3: Explicación Técnica (5-7 minutos)

### 3.1. Implementación de Requerimientos (Parte 1)

#### **Arquitectura:**
- **Backend:** Spring Boot (framework Java)
- **Base de Datos:** H2 (embebida, file-based)
- **ORM:** Spring Data JPA (simplifica acceso a datos)
- **Frontend:** Thymeleaf (templates server-side) + Bootstrap 5 + JavaScript

#### **Funcionalidades Principales:**

1. **Gestión de Videos:**
   - Modelo: `Video.java` (entidad JPA)
   - Repository: `VideoRepository.java` (Spring Data JPA)
   - Service: `VideoService.java` (lógica de negocio)
   - Controller: `HomeController.java` (endpoints HTTP)

2. **Validación de YouTube:**
   - Service: `YouTubeService.java`
   - Extrae ID de video de diferentes formatos de URL
   - Genera URL embebida automáticamente

3. **Persistencia:**
   - H2 Database (archivo: `data/playlist.mv.db`)
   - Datos persisten entre ejecuciones
   - Configuración en `application.properties`

4. **Interfaz de Usuario:**
   - Diseño responsive (Bootstrap)
   - Búsqueda y filtros (JavaScript)
   - Modales para confirmación y visualización
   - Estadísticas en tiempo real

#### **Estructura del Proyecto:**
```
src/main/java/com/playlist/musica/
├── controller/     # Controladores MVC
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos
└── model/          # Entidades JPA

src/main/resources/
├── templates/      # Templates Thymeleaf
└── application.properties  # Configuración
```

### 3.2. Pipeline de CI/CD (Parte 2)

#### **Jenkinsfile - Pipeline as Code:**

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') { ... }    // Obtiene código de GitHub
        stage('Build') { ... }       // Compila con Maven
        stage('Test') { ... }        // Ejecuta tests JUnit
        stage('Package') { ... }     // Genera JAR
        stage('Deploy') { ... }      // Despliega automáticamente
    }
}
```

#### **Stages del Pipeline:**

1. **Checkout:**
   - Clona el repositorio desde GitHub
   - Obtiene la última versión del código

2. **Build:**
   - `mvn clean compile`
   - Compila el código Java
   - Verifica que no haya errores de compilación

3. **Test:**
   - `mvn test`
   - Ejecuta todos los tests JUnit
   - Genera reportes (JUnit XML)
   - Si falla, el pipeline se detiene

4. **Package:**
   - `mvn clean package`
   - Genera el JAR ejecutable
   - Incluye todas las dependencias

5. **Deploy:**
   - Detiene la aplicación anterior (si existe)
   - Copia el nuevo JAR
   - Inicia la aplicación en background
   - Verifica que esté corriendo

#### **Configuración en Jenkins:**

- **Tipo:** Pipeline
- **SCM:** Git
- **Repository URL:** `https://github.com/inesungo/entregable-4.git`
- **Branch:** `main`
- **Script Path:** `Jenkinsfile`

#### **Beneficios del CI/CD:**

- ✅ **Automatización:** Cada push ejecuta el pipeline
- ✅ **Calidad:** Tests automáticos antes de deploy
- ✅ **Rapidez:** Deploy automático sin intervención manual
- ✅ **Trazabilidad:** Historial de builds y deploys
- ✅ **Rollback:** Fácil volver a versiones anteriores

---

## ✅ Checklist Pre-Demo

- [ ] Aplicación corriendo en `http://localhost:8080`
- [ ] Jenkins corriendo en `http://localhost:8081`
- [ ] Repositorio GitHub actualizado
- [ ] Pipeline configurado en Jenkins
- [ ] Tests pasando localmente
- [ ] Al menos 2-3 videos en la base de datos para la demo
- [ ] Navegador abierto y listo
- [ ] Terminal abierta para comandos

---

## 🎤 Tips para la Presentación

1. **Parte 1 (Aplicación):**
   - Habla mientras navegas
   - Muestra las funcionalidades más importantes
   - Responde preguntas sobre la tecnología

2. **Parte 2 (Refactoring):**
   - Explica el code smell claramente
   - Muestra el código antes y después
   - Ejecuta el pipeline y espera a que termine
   - Verifica que la app funciona después del deploy

3. **Parte 3 (Explicación):**
   - Sé conciso pero completo
   - Menciona tecnologías y decisiones de diseño
   - Explica el flujo del pipeline
   - Destaca los beneficios del CI/CD

---

## 🆘 Troubleshooting

### Si la aplicación no inicia:
```bash
# Verificar que el puerto 8080 esté libre
lsof -i :8080

# Si está ocupado, matar el proceso
pkill -f "playlist-musica"
```

### Si Jenkins no funciona:
```bash
# Verificar que esté corriendo
brew services list | grep jenkins

# Reiniciar si es necesario
brew services restart jenkins-lts
```

### Si el pipeline falla:
- Revisar los logs en Jenkins
- Verificar que los tests pasen localmente: `mvn test`
- Verificar que el JAR se genere: `mvn package`

---

## 📝 Notas Finales

- **Tiempo total estimado:** 20-25 minutos
- **Preparación:** 5 minutos antes
- **Demo:** 20 minutos
- **Preguntas:** 5 minutos

¡Éxito en tu demo! 🚀

