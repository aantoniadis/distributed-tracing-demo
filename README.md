# distributed-tracing-demo

Distributed tracing demo for Spring applications using Brave and Zipkin.

How to build

    $ cd demo
    $ ./mvnw clean package
    $ java -jar target/demo-0.0.1-SNAPSHOT.jar

    $ cd demo1
    $ ./mvnw clean package
    $ java -jar target/demo1-0.0.1-SNAPSHOT.jar

    $ cd demo2
    $ ./mvnw clean package
    $ java -jar target/demo2-0.0.1-SNAPSHOT.jar

    $ git clone https://github.com/ervis/dockerfile
    $ cd dockerfiles/openzipkin/
    $ docker-compose -f zipkin.yml up -d

Go to to `http://localhost:8080` to trigger a request.

Check the console logs or go to zipkin (http://localhost:9411) to view the trace.

Check that the same trace id is generated in every console.

Note:

This works in every Spring application, it doesn't have to be be spring-boot.

More info:

- https://github.com/openzipkin/brave
- http://opentracing.io/
