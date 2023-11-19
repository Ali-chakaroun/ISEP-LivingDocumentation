package org.example.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.events.OrderPaid;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * InventoryCounter would keep track of how many sandwiches are ready to be sold, and perhaps cause
 * the preparation of more sandwiches if the inventory drops below some level. This implementation
 * does not do this, and only logs.
 */
@Service
public final class InventoryCounter {

  private final Log log = LogFactory.getLog(getClass());

  /**
   * Called when an order is paid, and the current inventory has to be updated to reflect this.
   */
  @EventListener
  public void handleOrderPaid(OrderPaid event) {
    log.info("Would now update inventory for order " + event.order);
  }
}
