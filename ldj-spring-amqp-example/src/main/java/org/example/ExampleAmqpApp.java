package org.example;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableRabbit
public class ExampleAmqpApp {

  @Bean
  public Queue kitchenQueue() {
    return new Queue("kitchen");
  }

  @Bean
  public Queue deliveryQueue() {
    return new Queue("delivery");
  }

  public static void main(String[] args) {
    SpringApplication.run(ExampleAmqpApp.class, args);
  }
}
