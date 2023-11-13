package org.example.domain;

import java.io.Serializable;

/**
 * SandwichOrder represents both a sandwich and a destination address for that sandwich.
 */
public final class SandwichOrder implements Serializable {

  private final Bread bread;
  private final Protein protein;
  private final String address;

  /**
   * Constructs (but does not yet transmit) a sandwich order.
   */
  public SandwichOrder(Bread bread, Protein protein, String address) {
    this.bread = bread;
    this.protein = protein;
    this.address = address;
  }

  /**
   * Returns a programmer-readable representation of this order.
   */
  @Override
  public String toString() {
    return "SandwichOrder{" + bread + ", " + protein + ", '" + address + '\'' + '}';
  }
}
