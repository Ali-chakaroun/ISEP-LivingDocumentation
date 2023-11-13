package org.example;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Example Spring Boot application that uses Spring AMQP to decouple different bits of a fictional
 * sandwich delivery business. The web frontend, 'kitchen' and 'delivery' components run in the same
 * process in this example, but communicate using a message broker.
 */
@SpringBootApplication
@EnableRabbit
public class ExampleAmqpApp {

  /**
   * Add the 'kitchen' queue to the context so that it is automatically declared if needed.
   */
  @Bean
  public Queue kitchenQueue() {
    return new Queue("kitchen");
  }

  /**
   * Add the 'delivery' queue to the context so that it is automatically declared if needed.
   */
  @Bean
  public Queue deliveryQueue() {
    return new Queue("delivery");
  }

  /**
   * Command-line entry point.
   */
  public static void main(String[] args) {
    SpringApplication.run(ExampleAmqpApp.class, args);
  }
}
