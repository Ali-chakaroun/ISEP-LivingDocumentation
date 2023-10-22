package org.example;

import org.example.domain.DeliveryInstruction;
import org.example.domain.SandwichOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "kitchen")
public class KitchenListener {

  private final Logger logger = LoggerFactory.getLogger(KitchenListener.class);

  @RabbitHandler
  @SendTo("delivery")
  public DeliveryInstruction prepare(SandwichOrder order) {
    logger.atInfo().log("Would now prepare a {}", order);
    return new DeliveryInstruction(order);
  }
}
