package com.infosupport.ldoc.springexample.ampq;

public record QueueInteraction(
    String actor,
    QueueInteractionKind kind,
    String queue,
    String messageType
) {
}
