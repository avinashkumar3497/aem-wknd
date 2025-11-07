pipeline {
    agent any

    triggers {
        cron('H/5 * * * *')  // Runs every 5 minutes
    }

    stages {
        stage('Build') {
            steps {
                echo "Pipeline triggered automatically on schedule!"
            }
        }
    }
}
