# distributed-tracing-demo

Distributed tracing demo for Spring applications using Brave and Zipkin.

Installation:

You need to [docker](https://www.docker.com) and [docker-compose](https://github.com/docker/compose)
in order to run Zipkin.


How to build

    $ git clone https://github.com/ervis/distributed-tracing-demo
    $ cd distributed-tracing-demo
    
    # run Zipkin
    $ docker-compose up -d

Build the projects:

    $ ./gradlew clean build

Start the servers:

    $ java -jar webapp/build/libs/webapp.jar
    $ java -jar web-service-1/build/libs/web-service-1.jar
    $ java -jar web-service-2/build/libs/web-service-2.jar

Go to to `http://localhost:8080` to trigger a request.

The following chain of calls is made.

``
webapp --> web-service-1 --> web-service-2
``

Check the console logs or go to zipkin (http://localhost:9411) to view the trace and check information about the HTTP requests.

Check that the same trace id is generated in every console.

The logback pattern is:

    "%d [%X{traceId}/%X{spanId}] [%thread] %-5level %logger{36} - %msg%n"

Note:

This works on every Spring application, it does not have to be spring-boot.

More info:

- https://github.com/openzipkin/brave
- http://opentracing.io/
