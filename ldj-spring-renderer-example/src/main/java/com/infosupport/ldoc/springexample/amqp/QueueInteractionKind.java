package com.infosupport.ldoc.springexample.amqp;

/**
 * Represents types of communication/interaction with a queue.
 * Receive a message from a queue, or, send a message to a queue.
 */
public enum QueueInteractionKind {
  RECEIVE,
  SEND
}
