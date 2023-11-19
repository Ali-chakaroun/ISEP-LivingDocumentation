package org.example.models;

/**
 * A request for some quantity of sandwiches (which are assumed to be indistinguishable from each
 * other) to be shipped to some address. The object's hashCode stands in for an order number.
 */
public final class Order {
  private final int quantity;

  private final String address;

  /**
   * Constructs a new sandwich order.
   */
  public Order(int quantity, String address) {
    if (quantity < 1) {
      throw new IllegalArgumentException("Quantity must be strictly positive.");
    }
    this.quantity = quantity;
    this.address = address;
  }

  /**
   * Returns a human-readable string representation of this order.
   */
  @Override
  public String toString() {
    return "%d (%d sandwiches to %s)".formatted(hashCode(), quantity, address);
  }
}
