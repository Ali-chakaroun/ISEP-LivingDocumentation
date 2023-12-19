package org.example;

import org.example.domain.DeliveryConfirmation;
import org.example.domain.DeliveryInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.SendTo;
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
  @SendTo("kitchen")
  public DeliveryConfirmation deliver(DeliveryInstruction delivery) {
    logger.atInfo().log("Would now deliver a {}", delivery.order);
    return new DeliveryConfirmation(delivery.order);
  }
}
