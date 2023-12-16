package org.example.domain;

import java.io.Serializable;

/**
 * Message indicating that an order has been delivered.
 */
public final class DeliveryConfirmation implements Serializable {

  // Order that was delivered
  public final SandwichOrder sandwichOrder;

  /**
   * Creates a delivery confirmation.
   */
  public DeliveryConfirmation(SandwichOrder order) {
    this.sandwichOrder = order;
  }

}
