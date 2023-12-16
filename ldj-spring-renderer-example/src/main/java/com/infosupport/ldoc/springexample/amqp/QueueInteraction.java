package com.infosupport.ldoc.springexample.amqp;

import java.util.List;

public record QueueInteraction(
    // The actor (Java classes in this case) that either reads or posts a message in a queue
    String actor,
    // Whether actor reads or posts a message from/to a queue
    QueueInteractionKind kind,
    // The queue a message is either read/posted from/on
    String queue,
    // Class name of the POJO that is the message
    //  NOTE: In order to support message type matching across multiple applications, in this
    //    sample, the plain class name is used (as opposed to the fqdn)
    String messageType,

    // TODO note: Hacky: assumes this is Read, and contains solely post interactions that happen
    //   within this read context (advocates for polymorphism is read/post interactions
    // A list of Post interactions that are instantiated from this read interaction
    List<QueueInteraction> reactionToRead
) {

  /**
   * Filters a list to get all Queue Interactions that get triggered after a queue post.
   *
   * @param queueInteractions list to filter
   * @return a list of read interactions that react to a post interaction
   */
  List<QueueInteraction> filterReadsReactingToThis(List<QueueInteraction> queueInteractions) {
    if (this.kind() != QueueInteractionKind.POST) {
      throw new RuntimeException("No other actors can read from a non-post interaction");
    }
    return queueInteractions.stream().filter(i -> i.isReadOnPost(this)).toList();
  }

  /**
   * Check whether this would react on another post reaction. Only makes sense if this is a read
   * Actors may be different, queues and message types must be equal
   *
   * @param other interaction to check
   * @return whether this would react on other
   */
  boolean isReadOnPost(QueueInteraction other) {
    return this.kind == QueueInteractionKind.READ && other.kind == QueueInteractionKind.POST
        && this.queue.equals(other.queue) && this.messageType.equals(other.messageType);
  }

}
