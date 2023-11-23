# Spring Boot AMQP example

This is a Spring Boot application that serves as an example of a project using Spring AMQP that could be analyzed using the Living Documentation Java analyzer.

A web interface collects an order for a sandwich, which is sent to the "kitchen".
Once the sandwich is done, it is sent out for "delivery".
Of course, neither of these things actually happen - instead, the relevant code just logs what it otherwise would have done.

The example app requires a RabbitMQ message broker to be running.
If Docker is available, the following command suffices:

```shell
docker run -p 5672:5672 --hostname rabbit rabbitmq
```

Once the app is successfully started, "orders" can be submitted on [http://localhost:8080](http://localhost:8080/).
