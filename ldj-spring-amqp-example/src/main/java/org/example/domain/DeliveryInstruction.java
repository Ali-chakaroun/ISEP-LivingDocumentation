package org.example.domain;

import java.io.Serializable;

/**
 * An instruction to deliver a prepared sandwich order somewhere.
 */
public final class DeliveryInstruction implements Serializable {

  /**
   * The order that has been prepared and is now ready for delivery.
   */
  public final SandwichOrder order;

  /**
   * Constructs a delivery instruction for the given order.
   */
  public DeliveryInstruction(SandwichOrder order) {
    this.order = order;
  }
}
