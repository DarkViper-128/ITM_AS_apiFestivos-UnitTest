pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/sebaszapatar/ITM_AS_apiFestivos.git' // Actualizado para apifestivos
        BRANCH = 'UnitTest' // Actualizado para la rama UnitTest
        DOCKER_IMAGE = 'apifestivos:latest' // Nombre de la imagen actualizado
        DOCKER_CONTAINER = 'dockerapifestivos' // Nombre del contenedor actualizado
    }

    stages {
        stage('Clonar Repositorio') {
            steps {
                git branch: "${BRANCH}", credentialsId: '100', url: "${REPO_URL}"
            }
        }
        stage('Construir Imagen Docker') {
            steps {
                script {
                    bat 'docker rm -f %DOCKER_IMAGE% || true'
                    bat 'docker build -t %DOCKER_IMAGE% .'
                }
            }
        }
        stage('Detener Contenedor Anterior') {
            steps {
                script {
                    bat "docker rm -f %DOCKER_CONTAINER% || true"
                }
            }
        }
        stage('Desplegar Contenedor Docker') {
            steps {
                script {
                    bat 'docker run --name %DOCKER_CONTAINER% -p 7070:5235 -d %DOCKER_IMAGE%'
                }
            }
        }
    }

    post {
        success {
            echo 'Despliegue exitoso.'
        }
        failure {
            echo 'Despliegue fallido.'
        }
    }
}
