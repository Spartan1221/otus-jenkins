timeout(180) {
  node('gradle') {
    timestamps {
      wrap([$class: 'BuildUser']) {
        summary = """|<b>Owner:</b> ${env.BUILD_USER}
                            |<b>Branch:</b> ${BRANCH}""".stripMargin()
        currentBuild.description = summary.replaceAll("\n", "<br>")
        owner_user_id = "${env.BUILD_USER_ID}"
      }
      stage('Checkout') {
        checkout scm
      }K
      stage('Run tests') {
        tests_exit_code = sh(
            script: "gradle test -Dbrowser=$BROWSER_NAME -DbrowserVersion=$BROWSER_VERSION -DwebDriverRemoteUrl=$GRID_URL",
        )
        if (tests_exit_code != 0) {
          currentBuild.result = 'UNSTABLE'
        }
      }
      stage('Publish artifacts') {
        allure([
            includeProperties: false,
            jdk: '',
            properties: [],
            reportBuildPolicy: 'ALWAYS',
            results: [[path: 'build/reports/allure-results']]
        ])
      }
    }
  }
}