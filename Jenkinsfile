pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // This tells Jenkins to check out the repo defined in the job config
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests=false'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("my-springboot-app:${env.BUILD_NUMBER}")
                }
            }
        }

        // Optional: Push to Docker Hub (we’ll set up credentials later)
        /*
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-creds') {
                        docker.image("my-springboot-app:${env.BUILD_NUMBER}").push()
                    }
                }
            }
        }
        */
    }
}
