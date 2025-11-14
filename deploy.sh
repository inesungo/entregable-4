#!/bin/bash

# Script de deployment para Mac/Linux
# Uso: ./deploy.sh

set -e  # Salir si hay algún error

echo "🚀 Iniciando deployment de Playlist de Música..."
echo ""

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para verificar si un comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Verificar que Maven esté instalado
if ! command_exists mvn; then
    echo -e "${RED}❌ Error: Maven no está instalado${NC}"
    echo "Instala Maven con: brew install maven"
    exit 1
fi

# Verificar que Java esté instalado
if ! command_exists java; then
    echo -e "${RED}❌ Error: Java no está instalado${NC}"
    echo "Instala Java con: brew install openjdk@17"
    exit 1
fi

echo -e "${YELLOW}📦 Paso 1: Deteniendo aplicación si está corriendo...${NC}"
if pgrep -f "playlist-musica" > /dev/null; then
    echo "Aplicación encontrada, deteniendo..."
    pkill -f "playlist-musica"
    sleep 2
    echo -e "${GREEN}✅ Aplicación detenida${NC}"
else
    echo "No hay aplicación corriendo"
fi

echo ""
echo -e "${YELLOW}🔨 Paso 2: Compilando proyecto...${NC}"
mvn clean compile
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
else
    echo -e "${RED}❌ Error en la compilación${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}🧪 Paso 3: Ejecutando tests...${NC}"
mvn test
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Tests pasaron correctamente${NC}"
else
    echo -e "${RED}❌ Error: Los tests fallaron${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}📦 Paso 4: Empaquetando aplicación...${NC}"
mvn clean package -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Empaquetado exitoso${NC}"
else
    echo -e "${RED}❌ Error en el empaquetado${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}📁 Paso 5: Preparando directorio de deploy...${NC}"
DEPLOY_DIR="./deploy"
mkdir -p "$DEPLOY_DIR"
JAR_FILE=$(find target -name "playlist-musica-*.jar" -not -name "*-sources.jar" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo -e "${RED}❌ Error: No se encontró el archivo JAR${NC}"
    exit 1
fi

cp "$JAR_FILE" "$DEPLOY_DIR/playlist-musica.jar"
echo -e "${GREEN}✅ Archivo JAR copiado a $DEPLOY_DIR/${NC}"

echo ""
echo -e "${YELLOW}🚀 Paso 6: Iniciando aplicación...${NC}"
cd "$DEPLOY_DIR"
nohup java -jar playlist-musica.jar > app.log 2>&1 &
APP_PID=$!
echo $APP_PID > app.pid
sleep 5

# Verificar que la aplicación está corriendo
if ps -p $APP_PID > /dev/null; then
    echo -e "${GREEN}✅ Aplicación iniciada correctamente (PID: $APP_PID)${NC}"
    echo ""
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}✅ Deployment completado exitosamente!${NC}"
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    echo "🌐 Aplicación disponible en: http://localhost:8080"
    echo "📋 Logs disponibles en: $DEPLOY_DIR/app.log"
    echo "🛑 Para detener la aplicación: kill $APP_PID"
    echo ""
else
    echo -e "${RED}❌ Error: La aplicación no se inició correctamente${NC}"
    echo "Revisa los logs en: $DEPLOY_DIR/app.log"
    exit 1
fi

