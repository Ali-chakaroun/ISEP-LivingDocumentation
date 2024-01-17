package com.infosupport.ldoc.springexample.amqp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class SpringAmqpRendererTest {



  @Test
  void render() throws IOException {
    String expected = """
        participant "Queue Listener Class" as org.example.QueueListenerClass
        participant "First Message Publish" as org.example.FirstMessagePublish
        participant "Queue Listener Class2" as org.example.QueueListenerClass2
        org.example.FirstMessagePublish -[#ForestGreen]> org.example.QueueListenerClass : MsgType
        activate org.example.QueueListenerClass
        org.example.QueueListenerClass -[#ForestGreen]> org.example.QueueListenerClass2 : MsgType2
        activate org.example.QueueListenerClass2
        deactivate org.example.QueueListenerClass2
        deactivate org.example.QueueListenerClass
        """;

    try (InputStream input = getClass().getResourceAsStream("amqp-example.json");
        ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      SpringAmqpRenderer.render(input, output, "' diagram\n");
      assertEquals(expected.strip(), output.toString().strip());
    }
  }
}