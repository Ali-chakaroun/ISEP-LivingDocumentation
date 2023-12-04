package com.infosupport.ldoc.springexample.amqp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class QueueInteractionTest {

  @Test
  void filter_non_empty_post_list() {
    List<QueueInteraction> interactions = Arrays.asList(
        new QueueInteraction("test1", QueueInteractionKind.READ, "queue1", "msg1", null),
        new QueueInteraction("test2", QueueInteractionKind.READ, "queue2", "msg2", null),
        new QueueInteraction("test3", QueueInteractionKind.POST, "queue3", "msg3", null),
        new QueueInteraction("test4", QueueInteractionKind.POST, "queue4", "msg4", null)
    );

    List<QueueInteraction> expected = Arrays.asList(
        new QueueInteraction("test3", QueueInteractionKind.POST, "queue3", "msg3", null),
        new QueueInteraction("test4", QueueInteractionKind.POST, "queue4", "msg4", null)
    );

    assertIterableEquals(expected, QueueInteraction.getAllPosts(interactions));
  }

  @Test
  void filter_empty_post_list() {
    List<QueueInteraction> interactions = new ArrayList<>();
    assertIterableEquals(new ArrayList<QueueInteraction>(),
        QueueInteraction.getAllPosts(interactions));
  }

  @Test
  void filterReadsReactingToThis() {
    QueueInteraction post = new QueueInteraction("Kees", QueueInteractionKind.POST, "q1", "msg1",
        null);
    QueueInteraction read = new QueueInteraction("Henk", QueueInteractionKind.READ, "q1", "msg1",
        null);
    QueueInteraction read2 = new QueueInteraction("Henks alterego", QueueInteractionKind.READ, "q1",
        "msg1", null);
    QueueInteraction readDecoy1 = new QueueInteraction("Pieter", QueueInteractionKind.READ, "q1",
        "otherMsg", null);
    QueueInteraction readDecoy2 = new QueueInteraction("Ingrid", QueueInteractionKind.READ,
        "otherQueue", "msg1", null);
    List<QueueInteraction> readInteractions = Arrays.asList(readDecoy1, read, readDecoy2, read2);

    List<QueueInteraction> expected = Arrays.asList(read, read2);

    assertIterableEquals(expected, post.filterReadsReactingToThis(readInteractions));
  }

  @Test
  void is_read_on_post() {
    QueueInteraction read = new QueueInteraction("henk", QueueInteractionKind.READ, "q1", "msg1",
        null);
    QueueInteraction post = new QueueInteraction("kees", QueueInteractionKind.POST, "q1", "msg1",
        null);
    assertTrue(read.isReadOnPost(post));
    assertFalse(post.isReadOnPost(read));

    QueueInteraction postDecoy1 = new QueueInteraction("Pieter", QueueInteractionKind.POST, "q1",
        "otherMsg", null);
    assertFalse(read.isReadOnPost(postDecoy1));

    QueueInteraction postDecoy2 = new QueueInteraction("Ingrid", QueueInteractionKind.POST,
        "otherQueue", "msg1", null);
    assertFalse(read.isReadOnPost(postDecoy2));
  }
}