package org.example.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.example.events.InvoiceCreated;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * InvoiceSender would take a generated invoice and send it to the customer, perhaps using e-mail or
 * postal mail. This implementation only logs.
 */
@Service
public final class InvoiceSender {

  private final Log log = LogFactory.getLog(getClass());

  /**
   * Called when an invoice is created and (thus) ready to be sent.
   */
  @EventListener
  public void handleInvoiceCreated(InvoiceCreated event) {
    log.info("Would now send invoice " + event.hashCode());
  }
}
