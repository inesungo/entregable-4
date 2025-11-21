# 🔧 Refactoring: Eliminación de Código Duplicado

## 📋 Code Smell Identificado

**Archivo:** `src/main/java/com/playlist/musica/service/VideoService.java`

**Problema:** Duplicación de código (DRY violation)

Los métodos `darLike()` y `toggleFavorito()` contienen la misma lógica para buscar un video por ID y lanzar una excepción si no existe.

### Código Actual (ANTES del refactoring):

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

**Problemas:**
- ❌ Código duplicado (viola principio DRY)
- ❌ Si cambia la lógica de búsqueda, hay que modificar 2 lugares
- ❌ Dificulta el mantenimiento
- ❌ Mensaje de error duplicado

---

## ✅ Solución: Extract Method

**Técnica de Refactoring:** Extract Method (Extraer Método)

**Descripción:** Extraer el código común a un método privado reutilizable.

### Código Refactorizado (DESPUÉS):

```java
/**
 * Obtiene un video por ID o lanza excepción si no existe.
 * Método privado extraído para eliminar duplicación de código.
 * 
 * @param id ID del video a buscar
 * @return Video encontrado
 * @throws IllegalArgumentException si el video no existe
 */
private Video obtenerVideoPorId(Long id) {
    return videoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Video no encontrado"));
}

/**
 * Incrementa los likes de un video
 */
public Video darLike(Long id) {
    Video video = obtenerVideoPorId(id);
    video.setLikes(video.getLikes() + 1);
    return videoRepository.save(video);
}

/**
 * Marca/desmarca un video como favorito
 */
public Video toggleFavorito(Long id) {
    Video video = obtenerVideoPorId(id);
    video.setEsFavorito(!video.getEsFavorito());
    return videoRepository.save(video);
}
```

---

## 🎯 Beneficios del Refactoring

1. **✅ Elimina duplicación (DRY)**
   - El código común está en un solo lugar

2. **✅ Facilita mantenimiento**
   - Si cambia la lógica de búsqueda, solo se modifica un método

3. **✅ Mejora legibilidad**
   - Los métodos públicos son más claros y concisos

4. **✅ Facilita testing**
   - Se puede testear la lógica de búsqueda de forma aislada

5. **✅ Consistencia**
   - Garantiza que todos los métodos usen la misma lógica de búsqueda

---

## 🧪 Verificación

### Tests que deben pasar:

```bash
# Ejecutar todos los tests
mvn test

# Tests específicos que validan el refactoring:
# - VideoServiceTest.testDarLike()
# - VideoServiceTest.testToggleFavorito()
```

### Verificación manual:

1. Compilar: `mvn clean compile` ✅
2. Tests: `mvn test` ✅
3. Ejecutar app: `mvn spring-boot:run` ✅
4. Probar funcionalidad:
   - Dar like a un video ✅
   - Marcar/desmarcar favorito ✅

---

## 📝 Notas Técnicas

- **Técnica de Refactoring:** Extract Method
- **Principio SOLID:** Single Responsibility (cada método tiene una responsabilidad)
- **Principio DRY:** Don't Repeat Yourself
- **Nivel de complejidad:** Bajo (refactoring simple y seguro)
- **Riesgo:** Bajo (no cambia comportamiento, solo estructura)

---

## 🔄 Pasos para Aplicar el Refactoring

1. **Identificar el code smell** ✅
2. **Crear el método privado** `obtenerVideoPorId()`
3. **Reemplazar código duplicado** en `darLike()` y `toggleFavorito()`
4. **Compilar y verificar** que no hay errores
5. **Ejecutar tests** para asegurar que todo funciona
6. **Commit y push** a GitHub
7. **Ejecutar pipeline** en Jenkins para CI/CD

---

## 📚 Referencias

- **Refactoring Catalog:** Extract Method
- **Code Smell:** Duplicated Code
- **Principio:** DRY (Don't Repeat Yourself)

