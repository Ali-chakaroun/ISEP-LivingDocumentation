package org.example.domain;

import java.io.Serializable;

public class Receipt implements Serializable {

  public final SandwichOrder sandwichOrder;

  public final double price;

  public Receipt(SandwichOrder sandwichOrder) {
    this.sandwichOrder = sandwichOrder;
    this.price = 8.75; // All sandwiches are created equal
  }

  @Override
  public String toString() {
    return "Receipt for " + this.sandwichOrder + " with price = " + this.price;
  }
}
