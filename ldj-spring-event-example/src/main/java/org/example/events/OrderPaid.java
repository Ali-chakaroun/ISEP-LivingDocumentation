package org.example.events;

import org.example.models.Order;

/**
 * OrderPaid happening indicates that an order was paid in full.
 */
public final class OrderPaid {

  /**
   * The order that was paid.
   */
  public final Order order;

  /**
   * Constructs (but do not yet publish) an OrderPaid event.
   */
  public OrderPaid(Order order) {
    this.order = order;
  }
}
