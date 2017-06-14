# distributed-tracing-demo

Distributed tracing demo for Spring applications using Brave and Zipkin.

How to build

    $ git clone https://github.com/ervis/distributed-tracing-demo
    $ cd distributed-tracing-demo
    $ ./run-zipkin.sh

    $ ./mvnw clean package
    $ java -jar target/demo-0.0.1-SNAPSHOT.jar

    $ cd demo1
    $ ./mvnw clean package
    $ java -jar target/demo1-0.0.1-SNAPSHOT.jar

    $ cd demo2
    $ ./mvnw clean package
    $ java -jar target/demo2-0.0.1-SNAPSHOT.jar

    $ git clone https://github.com/ervis/dockerfiles

Go to to `http://localhost:8080` to trigger a request.

Check the console logs or go to zipkin (http://localhost:9411) to view the trace and check information about the HTTP requests.

Check that the same trace id is generated in every console.

The logback pattern is:

    "%d [%X{traceId}/%X{spanId}] [%thread] %-5level %logger{36} - %msg%n"

Note:

This works in every Spring application, it doesn't have to be be spring-boot.

More info:

- https://github.com/openzipkin/brave
- http://opentracing.io/
