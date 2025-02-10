### Websocket TCK using arquillian and test Jetty 12.0.x

Currently, it's really on work in progress only and need to build locally few projects

### Current status result 

https://jenkins.webtide.net/job/tck/job/tck-servlet-arquillian-experiment/
or 
gh action of this project 

#### TCK Servlet install

This will install websocket tck artifacts
```shell
wget -O jakarta-websocket-tck.zip https://download.eclipse.org/jakartaee/websocket/2.2/jakarta-websocket-tck-2.2.0.zip
unzip -j jakarta-websocket-tck.zip websocket-tck/artifacts/websocket-tck-common-2.2.0.pom websocket-tck/artifacts/websocket-tck-spec-tests-2.2.0.pom websocket-tck/artifacts/websocket-tck-common-2.2.0.jar websocket-tck/artifacts/websocket-tck-spec-tests-2.2.0.jar websocket-tck/artifacts/websocket-tck-2.2.0.pom
mvn -ntp install:install-file -Dfile=./websocket-tck-2.2.0.pom -DgroupId=jakarta.tck -DartifactId=websocket-tck -Dversion=2.2.0 -Dpackaging=pom
mvn -ntp install:install-file -Dfile=./websocket-tck-common-2.2.0.pom -DgroupId=jakarta.tck -DartifactId=websocket-tck-common -Dversion=2.2.0 -Dpackaging=pom
mvn -ntp install:install-file -Dfile=./websocket-tck-common-2.2.0.jar -DgroupId=jakarta.tck -DartifactId=websocket-tck-common -Dversion=2.2.0 -Dpackaging=jar
mvn -ntp install:install-file -Dfile=./websocket-tck-spec-tests-2.2.0.pom -DgroupId=jakarta.tck -DartifactId=websocket-tck-spec-tests -Dversion=2.2.0 -Dpackaging=pom
mvn -ntp install:install-file -Dfile=./websocket-tck-spec-tests-2.2.0.jar -DgroupId=jakarta.tck -DartifactId=websocket-tck-spec-tests -Dversion=2.2.0 -Dpackaging=jar
```

#### Arquillian Jetty 11.0.x support 

```shell
git clone https://github.com/arquillian/arquillian-container-jetty
cd arquillian-container-jetty
mvn install 
```

#### TCK Run with Jetty-11.0.x

This project
```shell
git clone https://github.com/jetty-project/websocket-tck-run
cd servlet-tck-run
mvn verify
```

