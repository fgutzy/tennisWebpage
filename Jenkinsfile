pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
       stage('Build') {
           steps {
               script {
                   // Perform a clean install
                   def installResult = sh(returnStatus: true, script: 'mvn clean install')

                   // Check if 'mvn clean install' was successful
                   if (installResult == 0) {
                       // If it succeeded, build the package
                       sh 'mvn clean package'
                   } else {
                       // If 'mvn clean install' failed, stop the pipeline
                       error('Failed to install artifacts with mvn clean install')
                   }
               }
           }
       }

        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }
}
