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
      }
      stage('Run tests') {
        tests_exit_code = sh(
            script: "gradle test -DbaseUrl=$BASE_URL",
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
//      stage('Publish notification in telegram') {
//        lines = readFile 'target/allure-results/export/influxDbData.txt'
//        def message = "============= UI REORT ============"
//        for (line in lines) {
//          def matcher = line =~ /.*(\w+=\d+).*/
//          message += line
//        }
//
//        def connection = null
//
//        connection = new URL(stringBuilder.toString()).openConnection() as HttpURLConnection
//        withCredentials([sring(name: 'telegram_token', envVar: TELEGRAM_TOKEN)]) {
//          connection.setProperty("token", $TELEGRAM_TOKEN)
//          connection.setProperty("text", message)
//          connection.setProperty('chat_id': '-1111111')
//        }
//        connection.setRequestMethod('POST')
//        connection.setDoOutput(true)
//      }
    }
  }
}