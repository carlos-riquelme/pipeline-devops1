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
                choice choices: ['maven', 'gradle'], description: 'Seleccione una herramienta para preceder a compilar', name: 'compileTool'
                text description: 'Enviar los stages separados por ";"... Vacío si necesita todos los stages', name: 'stages'
                choice (choices: ['feature-cicd', 'develop', 'release'], description: 'Seleccione la rama', name: 'REQUESTED_ACTION')
            }
            stages {
                stage("Pipeline"){
                        steps {
                        echo "develop o feature"
                        script{
                            sh "env"
                            env.TAREA = ""
                            if(params.compileTool == 'maven'){
                                maven.call(params.stages);
                            }else{
                                if(params.REQUESTED_ACTION == 'feature-cicd') {
                                    gradle.call(params.stages)
                                } else if (params.REQUESTED_ACTION == 'develop') {
                                    gradle.call(params.stages)
                                } else {
                                    echo "hola"
                                }
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