pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Obteniendo código del repositorio...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Compilando la aplicación...'
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo 'Ejecutando tests automáticos...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Empaquetando la aplicación...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Desplegando la aplicación...'
                script {
                    // Detener aplicación si está corriendo
                    sh '''
                        if pgrep -f "playlist-musica" > /dev/null; then
                            echo "Deteniendo aplicación existente..."
                            pkill -f "playlist-musica"
                            sleep 2
                        fi
                    '''
                    
                    // Copiar JAR a directorio de deploy
                    sh '''
                        mkdir -p /tmp/playlist-deploy
                        cp target/playlist-musica-*.jar /tmp/playlist-deploy/playlist-musica.jar
                        echo "Aplicación copiada a /tmp/playlist-deploy/"
                    '''
                    
                    // Iniciar aplicación en background
                    sh '''
                        cd /tmp/playlist-deploy
                        nohup java -jar playlist-musica.jar > app.log 2>&1 &
                        echo $! > app.pid
                        sleep 5
                    '''
                    
                    // Verificar que la aplicación está corriendo
                    sh '''
                        if pgrep -f "playlist-musica" > /dev/null; then
                            echo "✅ Aplicación desplegada correctamente"
                            echo "Aplicación disponible en: http://localhost:8080"
                        else
                            echo "❌ Error: La aplicación no se inició correctamente"
                            exit 1
                        fi
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline ejecutado exitosamente'
        }
        failure {
            echo '❌ Pipeline falló'
        }
        always {
            echo 'Pipeline finalizado'
        }
    }
}

