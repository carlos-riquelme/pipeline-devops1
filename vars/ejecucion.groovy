/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
 pipeline {
    agent any
    environment {
        NEXUS_USER = credentials('NEXUS-USER')
        NEXUS_PASSWORD = credentials('NEXUS-PASS')
    }
    parameters {
        choice(
            name:'compileTool',
            choices: ['Maven', 'Gradle'],
            description: 'Seleccione herramienta de compilacion'
        )
    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                  switch(params.compileTool)
                    {
                        case 'Maven':
                            // def ejecucion = load 'maven.groovy'
                            // ejecucion.call()
                            maven.call();
                        break;
                        case 'Gradle':
                            // def ejecucion = load 'gradle.groovy'
                            // ejecucion.call()
                            gradle.call()
                        break;
                    }
                }
            }
            post{
                success{
                    slackSend color: 'good', message: "[Carlos  Riquelme] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: '09bb43a7-7b85-4368-8bcb-b6cc51f1010e'
                }
                failure{
                    slackSend color: 'danger', message: "[Carlos Riquelme] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: '09bb43a7-7b85-4368-8bcb-b6cc51f1010e'
                }
            }
        }
    }
}

}

return this;