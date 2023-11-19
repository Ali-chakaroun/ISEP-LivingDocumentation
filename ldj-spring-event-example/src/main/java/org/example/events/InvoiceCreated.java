package org.example.events;

/**
 * InvoiceCreated occurs when an invoice for a sandwich order has been generated. A more realistic
 * event would have fields representing the generated invoice.
 */
public final class InvoiceCreated {

  /**
   * The number (hash code) of the order that this invoice is for.
   */
  public final long orderNumber;

  /**
   * Constructs (but does not yet publish) an InvoiceCreated event.
   */
  public InvoiceCreated(long orderNumber) {
    this.orderNumber = orderNumber;
  }
}
