package org.example.domain;

import java.io.Serializable;

public class Receipt implements Serializable {

  public final SandwichOrder sandwichOrder;

  public final double price;

  /**
   * Creates a receipt from a SandwichOrder. Always sets the price to the fixed value of 8.75.
   */
  public Receipt(SandwichOrder sandwichOrder) {
    this.sandwichOrder = sandwichOrder;
    this.price = 8.75; // All sandwiches are created equal
  }

  /**
   * Represents a receipt as a String. Fully displaying the order along with the price.
   */
  @Override
  public String toString() {
    return "Receipt for " + this.sandwichOrder + " with price = " + this.price;
  }
}
