package org.example.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.events.InvoiceCreated;
import org.example.events.OrderPaid;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * InvoiceGenerator would, given a fully paid order, generate an invoice (perhaps in PDF format) for
 * the sandwich consumer to keep for their records, perhaps in case a warranty claim comes up. This
 * implementation does not generate an invoice, but does emit an event that is has done so.
 */
@Service
public final class InvoiceGenerator {

  private final Log log = LogFactory.getLog(getClass());

  /**
   * Called when an order is fully paid up, which necessitates the generation of an invoice.
   */
  @EventListener
  public InvoiceCreated handleOrderPaid(OrderPaid event) {
    InvoiceCreated reaction = new InvoiceCreated(event.order.hashCode());
    log.info("Order %s paid, created invoice %s".formatted(event.order, reaction.hashCode()));
    return reaction;
  }
}
