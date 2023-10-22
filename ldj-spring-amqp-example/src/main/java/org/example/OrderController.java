package org.example;

import org.example.domain.SandwichOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

  private final RabbitTemplate rabbitmq;

  public OrderController(RabbitTemplate rabbitmq) {
    this.rabbitmq = rabbitmq;
  }

  @PostMapping(value = "/order", produces = MediaType.TEXT_PLAIN_VALUE)
  public String acceptOrder(@ModelAttribute SandwichOrder sandwichOrder) {
    rabbitmq.convertAndSend("kitchen", sandwichOrder);
    return "Thank you for your order! (Check the logs.)";
  }
}
