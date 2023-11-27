package org.example.domain;

import java.io.Serializable;

public final class DeliveryConfirmation implements Serializable {

  // Order that was delivered
  public final SandwichOrder sandwichOrder;

  public DeliveryConfirmation(SandwichOrder order) {
    this.sandwichOrder = order;
  }

}
