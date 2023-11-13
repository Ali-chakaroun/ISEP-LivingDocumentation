package org.example;

import org.example.domain.DeliveryInstruction;
import org.example.domain.SandwichOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * Kitchen component. Listens on the 'kitchen' queue for new sandwiches to prepare. Once a sandwich
 * is prepared, it is sent out for delivery on the 'delivery' queue.
 */
@Component
@RabbitListener(queues = "kitchen")
public class KitchenListener {

  private final Logger logger = LoggerFactory.getLogger(KitchenListener.class);

  /**
   * Called when a new sandwich order arrives.
   */
  @RabbitHandler
  @SendTo("delivery")
  public DeliveryInstruction prepare(SandwichOrder order) {
    logger.atInfo().log("Would now prepare a {}", order);
    return new DeliveryInstruction(order);
  }
}
