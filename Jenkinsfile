#!groovy

pipeline {
  agent { node { label 'linux' } }

  options {
    buildDiscarder logRotator( numToKeepStr: '50' )
    disableRestartFromStage()
    disableConcurrentBuilds(abortPrevious: true)
  }
  parameters {

    choice(
            description: 'TCK Github org',
            name: 'GITHUB_ORG_TCK',
            choices: ['jakartaee', 'olamy']
    )

    string( defaultValue: 'main', description: 'GIT branch name to build TCK (main/tckrefactor)',
            name: 'TCK_BRANCH' )

    choice(
            description: 'Arquillian Github org',
            name: 'GITHUB_ORG_ARQUILLIAN',
            choices: ['arquillian','olamy'] )

    string( defaultValue: 'master', description: 'GIT branch name to build arquillian Jetty (master/tck-all-changes)',
            name: 'ARQUILLIAN_JETTY_BRANCH' )

    string( defaultValue: 'jetty-12.1.x', description: 'GIT branch name to build Jetty (jetty-12.0.x)',
            name: 'JETTY_BRANCH' )

    string( defaultValue: 'jetty', description: 'GitHub org to clone Jetty from  (jetty)',
        name: 'JETTY_ORG' )

    string( defaultValue: 'SNAPSHOT', description: 'Jetty Version',
            name: 'JETTY_VERSION' )


    string( defaultValue: 'jdk21', description: 'JDK to build Jetty', name: 'JDKBUILD' )
  }

  stages {

    //stage('Build External') {
      //parallel {
        stage("Checkout Build Jetty 12.0.x") {
          steps {
            ws('jetty') {
              deleteDir()
              checkout([$class: 'GitSCM',
                        branches: [[name: "*/$JETTY_BRANCH"]],
                        extensions: [[$class: 'CloneOption', depth: 1, noTags: true, shallow: true, reference: "/home/jenkins/jetty.project.git"]],
                        userRemoteConfigs: [[url: "https://github.com/$JETTY_ORG/jetty.project.git"]]])
              timeout(time: 45, unit: 'MINUTES') {
                withEnv(["JAVA_HOME=${tool "$JDKBUILD"}",
                         "PATH+MAVEN=${env.JAVA_HOME}/bin:${tool 'maven3'}/bin",
                         "MAVEN_OPTS=-Xms2g -Xmx4g -Djava.awt.headless=true"]) {
                  configFileProvider([configFile(fileId: 'oss-settings.xml', variable: 'GLOBAL_MVN_SETTINGS')]) {
                    sh "mvn -ntp -s $GLOBAL_MVN_SETTINGS -V -B -U clean install -T2 -e -DskipTests -Dmaven.build.cache.restoreGeneratedSources=false -Dmaven.build.cache.remote.url=http://nexus-service.nexus.svc.cluster.local:8081/repository/maven-build-cache -Dmaven.build.cache.remote.enabled=true -Dmaven.build.cache.remote.save.enabled=true -Dmaven.build.cache.remote.server.id=nexus-cred"
                    script {
                      if (JETTY_VERSION == "SNAPSHOT") {
                        JETTY_VERSION = sh(script: "mvn -N help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                      }
                      sh "echo Jetty Version is ${JETTY_VERSION}"
                    }
                  }
                }
              }
            }
          }
        }
        stage("Checkout Build Arquillian Jetty") {
          steps {
            ws('arquillian') {
              deleteDir()
              checkout([$class: 'GitSCM',
                        branches: [[name: "*/$ARQUILLIAN_JETTY_BRANCH"]],
                        extensions: [[$class: 'CloneOption', depth: 1, noTags: true, shallow: true]],
                        userRemoteConfigs: [[url: 'https://github.com/${GITHUB_ORG_ARQUILLIAN}/arquillian-container-jetty']]])
              timeout(time: 30, unit: 'MINUTES') {
                withEnv(["JAVA_HOME=${tool "$JDKBUILD"}",
                         "PATH+MAVEN=${env.JAVA_HOME}/bin:${tool "maven3"}/bin",
                         "MAVEN_OPTS=-Xms2g -Xmx4g -Djava.awt.headless=true"]) {
                  configFileProvider([configFile(fileId: 'oss-settings.xml', variable: 'GLOBAL_MVN_SETTINGS')]) {
                    sh "mvn -ntp -s $GLOBAL_MVN_SETTINGS -V -B -U clean install -DskipTests -T3 -e -Denforcer.skip=true"
                  }
                }
              }
            }
          }
        }

      //}
    //}
    stage("Checkout Build TCK Sources") {
      steps {
        ws('arquillian') {
          deleteDir()
          checkout([$class: 'GitSCM',
                    branches: [[name: "*/$TCK_BRANCH"]],
                    extensions: [[$class: 'CloneOption', depth: 1, noTags: true, shallow: true]],
                    userRemoteConfigs: [[url: 'https://github.com/${GITHUB_ORG_TCK}/websocket']]])
          timeout(time: 30, unit: 'MINUTES') {
            withEnv(["JAVA_HOME=${tool "$JDKBUILD"}",
                     "PATH+MAVEN=${env.JAVA_HOME}/bin:${tool "maven3"}/bin",
                     "MAVEN_OPTS=-Xms2g -Xmx4g -Djava.awt.headless=true"]) {
              configFileProvider([configFile(fileId: 'oss-settings.xml', variable: 'GLOBAL_MVN_SETTINGS')]) {
                sh "mvn -ntp -s $GLOBAL_MVN_SETTINGS -V -B -U -am clean install -DskipTests -e -Dmaven.build.cache.remote.url=http://nexus-service.nexus.svc.cluster.local:8081/repository/maven-build-cache -Dmaven.build.cache.remote.enabled=true -Dmaven.build.cache.remote.save.enabled=true -Dmaven.build.cache.remote.server.id=nexus-cred"
              }
            }
          }
        }
      }
    }

    stage("Run TCK") {
      steps {
        timeout(time: 90, unit: 'MINUTES') {
          withEnv(["JAVA_HOME=${tool "$JDKBUILD"}",
                   "PATH+MAVEN=${env.JAVA_HOME}/bin:${tool "maven3"}/bin",
                   "MAVEN_OPTS=-Xms4g -Xmx8g -Djava.awt.headless=true"]) {
            configFileProvider([configFile(fileId: 'oss-settings.xml', variable: 'GLOBAL_MVN_SETTINGS')]) {
              sh "mvn -nsu -ntp -s $GLOBAL_MVN_SETTINGS -Dmaven.test.failure.ignore=true -V -B -U clean verify -e -Djetty.version=$JETTY_VERSION"
            }
          }
        }
      }
      post {
        always {
          junit testResults: '**/failsafe-reports/TEST-**.xml'
        }
      }
    }
  }
}
