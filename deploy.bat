@echo off
REM Script de deployment para Windows
REM Uso: deploy.bat

setlocal enabledelayedexpansion

echo 🚀 Iniciando deployment de Playlist de Musica...
echo.

REM Verificar que Maven esté instalado
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error: Maven no está instalado
    echo Instala Maven desde: https://maven.apache.org/download.cgi
    exit /b 1
)

REM Verificar que Java esté instalado
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error: Java no está instalado
    echo Instala Java desde: https://adoptium.net/
    exit /b 1
)

echo 📦 Paso 1: Deteniendo aplicación si está corriendo...
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST ^| findstr /I "PID"') do (
    set PID=%%a
    for /f "tokens=2" %%b in ('wmic process where "ProcessId=!PID!" get CommandLine /format:list ^| findstr /I "playlist-musica"') do (
        echo Aplicación encontrada, deteniendo...
        taskkill /F /PID !PID! >nul 2>&1
        timeout /t 2 /nobreak >nul
        echo ✅ Aplicación detenida
    )
)

echo.
echo 🔨 Paso 2: Compilando proyecto...
call mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error en la compilación
    exit /b 1
)
echo ✅ Compilación exitosa

echo.
echo 🧪 Paso 3: Ejecutando tests...
call mvn test
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error: Los tests fallaron
    exit /b 1
)
echo ✅ Tests pasaron correctamente

echo.
echo 📦 Paso 4: Empaquetando aplicación...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Error en el empaquetado
    exit /b 1
)
echo ✅ Empaquetado exitoso

echo.
echo 📁 Paso 5: Preparando directorio de deploy...
if not exist "deploy" mkdir deploy

REM Buscar el archivo JAR
for %%f in (target\playlist-musica-*.jar) do (
    set JAR_FILE=%%f
    goto :found_jar
)
:found_jar

if not defined JAR_FILE (
    echo ❌ Error: No se encontró el archivo JAR
    exit /b 1
)

copy "!JAR_FILE!" "deploy\playlist-musica.jar" >nul
echo ✅ Archivo JAR copiado a deploy\

echo.
echo 🚀 Paso 6: Iniciando aplicación...
cd deploy
start /B java -jar playlist-musica.jar > app.log 2>&1
timeout /t 5 /nobreak >nul

REM Verificar que la aplicación está corriendo
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /FO LIST ^| findstr /I "PID"') do (
    set PID=%%a
    for /f "tokens=2" %%b in ('wmic process where "ProcessId=!PID!" get CommandLine /format:list ^| findstr /I "playlist-musica"') do (
        echo ✅ Aplicación iniciada correctamente (PID: !PID!)
        echo !PID! > app.pid
        goto :app_started
    )
)

:app_started
echo.
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo ✅ Deployment completado exitosamente!
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
echo.
echo 🌐 Aplicación disponible en: http://localhost:8080
echo 📋 Logs disponibles en: deploy\app.log
echo 🛑 Para detener la aplicación, cierra la ventana o usa el Administrador de Tareas
echo.

cd ..
endlocal

