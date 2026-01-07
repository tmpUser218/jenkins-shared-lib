def call() {
    pipeline {
        agent any

        stages {

            stage('Lint') {
                steps {
                    echo 'Running lint step'
                }
            }

            stage('Build') {
                steps {
                    echo 'Running build step'
                }
            }

            stage('Test') {
                steps {
                    echo 'Running test step'
                }
            }

            stage('Build Docker Image') {
                steps {
                    echo 'Building Docker image'
                }
            }

            stage('Push Docker Image') {
                steps {
                    echo 'Pushing Docker image'
                }
            }
        }

        post {
            success {
                echo 'Pipeline completed successfully'
            }
            failure {
                echo 'Pipeline failed'
            }
        }
    }
}
