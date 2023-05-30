### Websocket TCK using arquillian and test Jetty 12.0.x

Currently, it's really on work in progress only and need to build locally few projects

### Current status result 

https://jenkins.webtide.net/job/tck/job/tck-servlet-arquillian-experiment/
or 
gh action of this project 

#### TCK Servlet build

This will build only the servlet TCK module
```shell
git clone https://github.com/olamy/jakartaee-tck.git
cd jakartaee-tck
git checkout tck-refactor-websocket-2-arquillian-url
clean install -pl :websocket-tck -am
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

