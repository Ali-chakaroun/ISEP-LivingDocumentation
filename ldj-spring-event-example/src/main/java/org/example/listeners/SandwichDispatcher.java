package org.example.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.events.OrderPaid;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * SandwichDispatcher would, if it existed, send out a courier to deliver the requested number of
 * sandwiches whenever an order is fully paid. The current implementation does not do this.
 */
@Service
public final class SandwichDispatcher {

  private final Log log = LogFactory.getLog(getClass());

  /**
   * Called when an order has been paid, and sandwiches need to be sent out to hungry customers.
   */
  @EventListener
  public void handleOrderPaid(OrderPaid event) {
    log.info("Would now dispatch order " + event.order);
  }
}
