node {
    stage('Clone code') {
        git 'https://github.com/Cengiz-Cinar/APC_Assignment.git'
    }

    stage('Run tests'){
        if(isUnix()){
            sh "mvn clean test"
        } else {
            bat "mvn clean test"
        }
    }

    stage('Generate report'){
           cucumber failedFeaturesNumber: -1, failedScenariosNumber: -1, failedStepsNumber: -1, fileIncludePattern: '**/cucumber.json', pendingStepsNumber: -1, skippedStepsNumber: -1, sortingMethod: 'ALPHABETICAL', undefinedStepsNumber: -1
    }
}