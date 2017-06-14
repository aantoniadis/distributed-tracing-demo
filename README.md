# distributed-tracing-demo

Distributed tracing demo for Spring applications using Brave and Zipkin.

How to build

    $ git clone https://github.com/ervis/distributed-tracing-demo
    $ cd distributed-tracing-demo
    $ ./run-zipkin.sh

`cd` in every project and run:

    $ ./mvnw clean package

Start the servers:

    $ java -jar demo/target/demo-0.0.1-SNAPSHOT.jar
    $ java -jar demo1/target/demo1-0.0.1-SNAPSHOT.jar
    $ java -jar demo2/target/demo2-0.0.1-SNAPSHOT.jar

Go to to `http://localhost:8080` to trigger a request.

Check the console logs or go to zipkin (http://localhost:9411) to view the trace and check information about the HTTP requests.

Check that the same trace id is generated in every console.

The logback pattern is:

    "%d [%X{traceId}/%X{spanId}] [%thread] %-5level %logger{36} - %msg%n"

Note:

This works in every Spring application, it doesn't have to be spring-boot.

More info:

- https://github.com/openzipkin/brave
- http://opentracing.io/
