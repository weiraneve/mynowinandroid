pipeline
{
    environment {
        ANDROID_HOME = "/opt/android-sdk"
    }
    agent any
    stages {
        stage('Build')
        {
         steps {
            sh './gradlew clean'
            sh './gradlew assembleRelease'
            }
        }
    }
}