package com.infosupport.ldoc.springexample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.springexample.SpringEventRenderer.Interaction;
import com.infosupport.ldoc.springexample.SpringEventRenderer.Kind;
import com.infosupport.ldoc.springexample.util.PlantUMLBuilder;
import com.infosupport.ldoc.springexample.util.StringOperations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.junit.jupiter.api.Test;

class SpringEventRendererTest {

  private final StringWriter sw = new StringWriter();
  private final PrintWriter pw = new PrintWriter(sw);

  @Test
  void interaction() {
    Interaction pub = new Interaction(Kind.PUBLISHES, "org.example.Example", "Reaction", "Action");
    assertTrue(pub.isPublish("org.example.Example", "Action"));
    assertFalse(pub.isPublish("org.example.SomeOtherClass", "Action"));
    assertFalse(pub.isPublish("org.example.Example", null));
    assertFalse(pub.isListen("org.example.Example", "Reaction"));

    Interaction listen = new Interaction(Kind.LISTENS, "org.example.Example", "Action", null);
    assertTrue(listen.isListen("org.example.Example", "Action"));
    assertFalse(listen.isListen("org.example.SomeOtherClass", "Action"));
    assertFalse(listen.isListen("org.example.Example", "SomeOtherEvent"));
    assertFalse(listen.isPublish("org.example.Example", "Action"));
  }

  @Test
  void findInteractions() throws IOException {
    JsonNode jsonNode = new ObjectMapper().readTree(getClass().getResource("example.json"));

    List<Interaction> expected = List.of(
        new Interaction(
            Kind.PUBLISHES, "org.example.App", "org.example.Action", null),
        new Interaction(
            Kind.LISTENS, "org.example.Interact", "org.example.Action", null),
        new Interaction(
            Kind.PUBLISHES, "org.example.Interact", "org.example.Reaction", "org.example.Action"),
        new Interaction(
            Kind.LISTENS, "org.example.OnlyListen", "org.example.Action", null),
        new Interaction(
            Kind.LISTENS, "org.example.OnlyListen", "org.example.Reaction", null));
    List<Interaction> actual = SpringEventRenderer.findInteractions(jsonNode);

    assertIterableEquals(expected, actual);
  }

  @Test
  void stripName() {
    assertEquals("", StringOperations.stripName(""));
    assertEquals("SimpleName", StringOperations.stripName("SimpleName"));
    assertEquals("SomeClassName", StringOperations.stripName("org.example.SomeClassName"));
  }

  @Test
  void humanizeName() {
    assertEquals("", StringOperations.humanizeName(""));
    assertEquals("Simple Name", StringOperations.humanizeName("SimpleName"));
    assertEquals("Some Class Name", StringOperations.humanizeName("org.example.SomeClassName"));
  }

  @Test
  void renderParticipant() {
    PlantUMLBuilder.renderParticipant(pw, "org.example.ExampleParticipant");

    assertEquals(
        "participant \"Example Participant\" as org.example.ExampleParticipant\n", sw.toString());
  }

  @Test
  void renderInteraction() {
    PlantUMLBuilder.renderInteraction(
        pw, "org.example.SomeSender", "org.example.SomeReceiver", "org.example.ExampleEvent");

    assertEquals(
        "org.example.SomeSender -[#ForestGreen]> org.example.SomeReceiver : ExampleEvent\n",
        sw.toString());
  }

  @Test
  void renderInteractions() {
    List<String> classes = List.of("org.example.App", "org.example.Listener");
    List<Interaction> interactions = List.of(
        new Interaction(Kind.PUBLISHES, "org.example.App", "org.example.SomeEvent", null),
        new Interaction(Kind.LISTENS, "org.example.Listener", "org.example.SomeEvent", null));

    SpringEventRenderer.renderInteractions(pw, "org.example.App", interactions, classes, null);

    String expected = """
        org.example.App -[#ForestGreen]> org.example.Listener : SomeEvent
        activate org.example.Listener
        deactivate org.example.Listener
        """;

    assertEquals(expected, sw.toString());
  }

  @Test
  void render() throws IOException {
    String expected = """
        participant "App" as org.example.App
        participant "Interact" as org.example.Interact
        participant "Only Listen" as org.example.OnlyListen
        org.example.App -[#ForestGreen]> org.example.Interact : Action
        activate org.example.Interact
        org.example.Interact -[#ForestGreen]> org.example.OnlyListen : Reaction
        activate org.example.OnlyListen
        deactivate org.example.OnlyListen
        deactivate org.example.Interact
        org.example.App -[#ForestGreen]> org.example.OnlyListen : Action
        activate org.example.OnlyListen
        deactivate org.example.OnlyListen
        """;

    try (InputStream input = getClass().getResourceAsStream("example.json");
        ByteArrayOutputStream output = new ByteArrayOutputStream()) {
      SpringEventRenderer.render(input, output, "' diagram\n");
      assertEquals(expected.strip(), output.toString().strip());
    }
  }
}
