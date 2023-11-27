package com.infosupport.ldoc.springexample.ampq;

import java.util.ArrayList;
import java.util.List;

public record QueueInteraction(
    String actor,
    QueueInteractionKind kind,
    String queue,
    String messageType,

    // Hacky: assumes this is Read, and contains solely post interactions that happen within this read context
    List<QueueInteraction> reactionToRead
) {

  static List<QueueInteraction> getAllPosts(List<QueueInteraction> queueInteractions, String actor) {
    return getAllPosts(queueInteractions).stream().filter(i -> i.actor().equals(actor)).toList();
  }

  static List<QueueInteraction> getAllPosts(List<QueueInteraction> queueInteractions) {
    return queueInteractions.stream().filter(i -> i.kind() == QueueInteractionKind.POST).toList();
  }

  /**
   * Filters a list to get all Queue Interactions that get triggered after a queue post
   * @param queueInteractions
   * @return
   */
  List<QueueInteraction> filterReadsReactingToThis(List<QueueInteraction> queueInteractions) {
    if (this.kind() != QueueInteractionKind.POST) {
      throw new RuntimeException("No other actors can read from a non-post interaction");
    }
    return queueInteractions.stream().filter(i -> i.isReadOnPost(this)).toList();
  }

  /**
   * Actors may be different, queues and message types must be equal
   * @param other
   * @return
   */
  boolean isReadOnPost(QueueInteraction other) {
    return this.kind == QueueInteractionKind.READ && other.kind == QueueInteractionKind.POST
        && this.queue.equals(other.queue) && this.messageType.equals(other.messageType);
  }

}
