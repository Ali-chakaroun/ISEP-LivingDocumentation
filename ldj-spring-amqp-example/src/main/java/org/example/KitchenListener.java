package org.example;

import org.example.domain.DeliveryConfirmation;
import org.example.domain.DeliveryInstruction;
import org.example.domain.Receipt;
import org.example.domain.SandwichOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

  private final RabbitTemplate rabbitmq;

  /**
   * Initialises a KitchenListener along with a RabbitTemplate which is attached via dependency
   * injection.
   */
  public KitchenListener(RabbitTemplate rabbitTemplate) {
    this.rabbitmq = rabbitTemplate;
  }


  /**
   * Called when a new sandwich order arrives.
   */
  @RabbitHandler
  @SendTo("delivery")
  public DeliveryInstruction prepare(SandwichOrder order) {
    logger.atInfo().log("Would now prepare a {}", order);
    // Make sure payment is handled
    rabbitmq.convertAndSend("payments", new Receipt(order));
    return new DeliveryInstruction(order);
  }

  /**
   * Called when an order is delivered.
   */
  @RabbitHandler
  public void receiveDeliveryConfirmation(DeliveryConfirmation confirmation) {
    logger.atInfo().log("Received confirmation of delivery at kitchen of order {}",
        confirmation.sandwichOrder);
  }
}
