package org.example;

import org.example.domain.DeliveryInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Delivery component. Listens on the 'delivery' queue for delivery instructions.
 */
@Component
@RabbitListener(queues = "delivery")
public class DeliveryListener {

  private final Logger logger = LoggerFactory.getLogger(DeliveryListener.class);

  /**
   * Called when a delivery instruction arrives.
   */
  @RabbitHandler
  public void deliver(DeliveryInstruction delivery) {
    logger.atInfo().log("Would now deliver a {}", delivery.order);
  }
}
