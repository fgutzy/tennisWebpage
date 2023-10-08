pipeline {
    agent any
   
   tools {
        maven '3.9.5'
    }

    stages {
        stage('credentials'){
            steps{
                script{ 
                    echo "Database engine is ${DB_ENGINE}"
                    echo "DATABASE_USERNAME: ${env.DATABASE_USERNAME}"  
                    echo "DATABASE_PASSWORD: ${env.DATABASE_PASSWORD}" 
                }
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
                sh 'mvn --version'
            }
        }
       stage('Build') {
           steps {
               script {
                    echo "innerhalb if"
                   
                   // Perform a clean install
                   def installResult = sh(returnStatus: true, script: 'mvn clean install')

                   // Check if 'mvn clean install' was successful
                   if (installResult == 0) {
                       // If it succeeded, build the package
                       //sh 'mvn clean package'
                   } else {
                       // If 'mvn clean install' failed, stop the pipeline
                       error('Failed to install artifacts with mvn clean install')
                   }
                   
               }
           }
       }
        stage('Test') {
            steps {
               // sh 'mvn test'
                echo "tests"
            }
        }
    }
}
