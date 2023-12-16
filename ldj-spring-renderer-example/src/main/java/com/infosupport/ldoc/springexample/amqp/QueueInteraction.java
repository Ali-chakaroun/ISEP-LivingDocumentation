package com.infosupport.ldoc.springexample.amqp;

import java.util.List;

public record QueueInteraction(
    // The actor (Java classes in this case) that either receives or sends a message in a queue
    String actor,
    // Whether actor receives or sends a message from/to a queue
    QueueInteractionKind kind,
    // The queue a message is either receive/sent from/on
    String queue,
    // Class name of the POJO that is the message
    //  NOTE: In order to support message type matching across multiple applications, in this
    //    sample, the plain class name is used (as opposed to the fqdn)
    String messageType,

    // TODO note: Hacky: assumes this is Read, and contains solely post interactions that happen
    //   within this read context (advocates for polymorphism is read/post interactions
    // A list of Send interactions that are instantiated from this receive interaction
    List<QueueInteraction> reactionToReceive
) {

  /**
   * Filters a list to get all Queue Interactions that get triggered after a queue send.
   *
   * @param queueInteractions list to filter
   * @return a list of receive interactions that react to a send interaction
   */
  List<QueueInteraction> filterReceivesReactingToThis(List<QueueInteraction> queueInteractions) {
    if (this.kind() != QueueInteractionKind.SEND) {
      throw new RuntimeException("No other actors can receive from a non-send interaction");
    }
    return queueInteractions.stream().filter(i -> i.isReceiveOnSend(this)).toList();
  }

  /**
   * Check whether this would react on another post reaction. Only makes sense if this is a read
   * Actors may be different, queues and message types must be equal
   *
   * @param other interaction to check
   * @return whether this would react on other
   */
  boolean isReceiveOnSend(QueueInteraction other) {
    return this.kind == QueueInteractionKind.RECEIVE && other.kind == QueueInteractionKind.SEND
        && this.queue.equals(other.queue) && this.messageType.equals(other.messageType);
  }

}
