package org.example;

import org.example.events.OrderPaid;
import org.example.models.Order;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * An example application where we pretend 6 sandwiches were ordered by a hungry customer who lives
 * at 123 Fake Street.
 */
public class EventApp {

  /**
   * Command-line entry point.
   */
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext("org.example");

    context.publishEvent(new OrderPaid(new Order(6, "123 Fake Street")));
  }
}
