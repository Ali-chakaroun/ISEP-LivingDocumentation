package org.example;

import org.example.domain.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "payments")
public class PaymentsListener {

  private final Logger logger = LoggerFactory.getLogger(PaymentsListener.class);

  @RabbitHandler
  public void handleReceipt(Receipt receipt) {
    logger.atInfo().log("Would now send receipt to customer: {}", receipt);
  }
}
