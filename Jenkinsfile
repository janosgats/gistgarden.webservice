#!groovy


String RELEASE_IMAGE_NAME = 'gistgarden-webservice'

String DOCKER_HUB_USERNAME
withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDS', passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USER')]) {
    DOCKER_HUB_USERNAME = "$DOCKER_HUB_USER"
}

BRANCH_TO_DEPLOY = 'master'

//Stages (because enum is disabled in the runtime)
STAGE_BUILD_DOCKER_IMAGE = 'STAGE_BUILD_DOCKER_IMAGE'
STAGE_UNIT_TESTS = 'STAGE_UNIT_TESTS'
STAGE_DEPLOY_TO_K8SC1 = 'STAGE_DEPLOY_TO_K8SC1'

def boolean shouldStageBeExecutedByDefault(stage) {
    switch (stage) {
        case STAGE_BUILD_DOCKER_IMAGE:
        case STAGE_UNIT_TESTS:
            return true;
        case STAGE_DEPLOY_TO_K8SC1:
            return env.BRANCH_NAME == BRANCH_TO_DEPLOY;
    }

    error "Unrecognized stage in shouldStageBeExecutedByDefault: ${stage}"
}

def boolean shouldStageBeExecutedByCustomization(stage) {
    switch (stage) {
        case STAGE_BUILD_DOCKER_IMAGE:
            return params.CUSTOM_EXECUTE_STAGE_BUILD_IMAGE;
        case STAGE_UNIT_TESTS:
            return params.CUSTOM_EXECUTE_STAGE_UNIT_TESTS;
        case STAGE_DEPLOY_TO_K8SC1:
            return params.CUSTOM_EXECUTE_STAGE_DEPLOY_TO_K8SC1;
    }

    error "Unrecognized Stage in shouldStageBeExecutedByCustomization: ${stage}"
}

def boolean shouldStageBeExecuted(stage) {
    boolean result;

    if (params.CUSTOMIZE_EXECUTED_BUILD_STAGES) {
        result = shouldStageBeExecutedByCustomization(stage);
    } else {
        result = shouldStageBeExecutedByDefault(stage);
    }

    echo 'Result of shouldStageBeExecuted(' + stage + ') is ' + result + '. params.CUSTOMIZE_EXECUTED_BUILD_STAGES: ' + params.CUSTOMIZE_EXECUTED_BUILD_STAGES
    return result;
}

def isReleaseBuild = shouldStageBeExecuted(STAGE_DEPLOY_TO_K8SC1)

pipeline {
    parameters {
        booleanParam(
                defaultValue: false,
                description: 'Check this to override if build stages should be executed or not',
                name: 'CUSTOMIZE_EXECUTED_BUILD_STAGES'
        )
        booleanParam(
                defaultValue: true,
                description: 'Build docker image',
                name: 'CUSTOM_EXECUTE_STAGE_BUILD_IMAGE'
        )
        booleanParam(
                defaultValue: false,
                description: 'Run unit tests',
                name: 'CUSTOM_EXECUTE_STAGE_UNIT_TESTS'
        )
        booleanParam(
                defaultValue: false,
                description: 'Deploy to k8sc1',
                name: 'CUSTOM_EXECUTE_STAGE_DEPLOY_TO_K8SC1'
        )
    }

    environment {
        SHORT_COMMIT_HASH = "${env.GIT_COMMIT.substring(0, 10)}"
        DOCKER_COMPATIBLE_BRANCH_NAME = "${env.BRANCH_NAME.replace('/', '-')}"

        IMAGE_NAME_COMMIT = "${DOCKER_HUB_USERNAME}/${RELEASE_IMAGE_NAME}:${env.DOCKER_COMPATIBLE_BRANCH_NAME}_${env.SHORT_COMMIT_HASH}"
        IMAGE_NAME_LATEST = "${DOCKER_HUB_USERNAME}/${RELEASE_IMAGE_NAME}:latest"
    }

    stages {

        stage('Build Release Image') {
            when { expression { return shouldStageBeExecuted(STAGE_BUILD_DOCKER_IMAGE) } }
            steps {
                script {
                    sh "docker build" +
                            " --target release" +
                            " -f .\\docker\\Dockerfile-k8s" +
                            " -t ${IMAGE_NAME_COMMIT}" +
                            " ."
                }
            }
        }


        stage('Run Unit Tests') {
            when { expression { return shouldStageBeExecuted(STAGE_UNIT_TESTS) } }
            steps {
                script {
                    sh 'docker build' +
                            ' --target tester' +
                            ' -f .\\docker\\Dockerfile-k8s' +
                            ' .'
                }
            }
        }


        stage('Publish Docker Image') {
            when { expression { return shouldStageBeExecuted(STAGE_BUILD_DOCKER_IMAGE) && isReleaseBuild } }
            steps {
                script {
                    echo "TODO: publisher stage here"
                }
            }
        }
    } // stages

    post {
        cleanup {
            cleanWs()
        }
    }
}
