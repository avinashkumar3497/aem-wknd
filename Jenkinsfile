pipeline {
    agent any

    parameters {
        choice(name: 'ENVIRONMENT', choices: ['dev', 'staging', 'prod'], description: 'Select deployment environment')
    }

    stages {
        stage('Build') {
            steps {
                echo "Building the application for ${params.ENVIRONMENT} environment..."
                bat 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (params.ENVIRONMENT == 'dev') {
                        echo "Deploying to DEV server..."
                    } else if (params.ENVIRONMENT == 'staging') {
                        echo "Deploying to STAGING server..."
                    } else {
                        echo "Deploying to PRODUCTION server 🚀"
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline completed for environment: ${params.ENVIRONMENT}"
        }
    }
}
