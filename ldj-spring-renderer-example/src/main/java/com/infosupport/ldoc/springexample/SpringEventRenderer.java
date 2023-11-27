package com.infosupport.ldoc.springexample;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.springexample.util.PlantUmlBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * SpringRenderer is an example renderer that makes Asciidoc with PlantUML diagrams from analyzed
 * Spring projects that use application events.
 */
public class SpringEventRenderer {

  private static final String EV_LISTENER = "org.springframework.context.event.EventListener";
  private static final String CONTEXT_PACKAGE = "org.springframework.context.";

  enum Kind {
    LISTENS,
    PUBLISHES
  }

  /**
   * Some interaction with Spring application events: a listener or publisher. Listens are never a
   * reply to anything.
   */
  record Interaction(Kind kind, String className, String event, String reply) {

    boolean isListen(String klass, String ev) {
      return kind == Kind.LISTENS && className.equals(klass) && event.equals(ev);
    }

    boolean isPublish(String klass, String scope) {
      return kind == Kind.PUBLISHES && className.equals(klass) && Objects.equals(reply, scope);
    }
  }

  /**
   * Recursively walks the given JSON node finding invocations of publishEvent on Context classes.
   */
  static List<Interaction> findPublishCalls(String sendingClass, String eventName, JsonNode node) {
    List<Interaction> interactions = new ArrayList<>(0);

    if (node.path("$type").asText("").contains("InvocationDescription")
        && node.path("ContainingType").asText("").startsWith(CONTEXT_PACKAGE)
        && node.path("Name").asText("").equals("publishEvent")) {
      // This is an explicit call to some Spring Context 'publishEvent' method.
      String eventType = node.get("Arguments").get(0).get("Type").textValue();
      interactions.add(new Interaction(Kind.PUBLISHES, sendingClass, eventType, eventName));
    }

    for (JsonNode child : node) {
      interactions.addAll(findPublishCalls(sendingClass, eventName, child));
    }

    return interactions;
  }

  static List<Interaction> findInteractions(JsonNode root) {
    List<Interaction> interactions = new ArrayList<>(0);

    for (JsonNode type : root) {
      String fullName = type.path("FullName").asText("");

      for (JsonNode method : type.path("Methods")) {
        String returnType = method.get("ReturnType").asText();
        String eventType = null;

        for (JsonNode annotation : method.path("Attributes")) {
          eventType = method.path("Parameters").path(0).path("Type").asText(null);

          if (annotation.get("Type").asText().equals(EV_LISTENER)) {
            // This method is annotated @EventListener, so it is a listener.
            interactions.add(new Interaction(Kind.LISTENS, fullName, eventType, null));

            if (!returnType.equals("void")) {
              // It returns something, which is published as a new event, so it's also a publisher.
              interactions.add(new Interaction(Kind.PUBLISHES, fullName, returnType, eventType));
            }
          }
        }

        // Look through the method for calls to publishEvent, also if the method is not a listener.
        interactions.addAll(findPublishCalls(fullName, eventType, method.path("Statements")));
      }
    }

    interactions.sort(Comparator.comparing(Interaction::className));

    return interactions;
  }

  static void renderInteractions(PrintWriter out, String sender, List<Interaction> interactions,
      List<String> classes, String scope) {
    // This is O(spicy) but seems fast enough in our example.
    for (Interaction send : interactions) {
      if (send.isPublish(sender, scope)) {
        for (String receiver : classes) {
          for (Interaction receive : interactions) {
            if (receive.isListen(receiver, send.event())) {
              PlantUmlBuilder.renderInteraction(out, sender, receiver, receive.event());

              out.println("activate %s".formatted(receiver));
              renderInteractions(out, receiver, interactions, classes, receive.event());
              out.println("deactivate %s".formatted(receiver));
            }
          }
        }
      }
    }
  }

  /**
   * Reads LivingDocumentation JSON from the input stream and writes Asciidoc to the output stream.
   */
  public static void render(InputStream in, OutputStream out, String template) throws IOException {
    JsonNode json = new ObjectMapper().readTree(in);

    StringWriter buffer = new StringWriter();
    PrintWriter bufferWriter = new PrintWriter(buffer, true);

    List<Interaction> interactions = findInteractions(json);
    List<String> classes = interactions.stream().map(Interaction::className).distinct().toList();

    for (String participant : classes) {
      PlantUmlBuilder.renderParticipant(bufferWriter, participant);
    }

    for (String sender : classes) {
      renderInteractions(bufferWriter, sender, interactions, classes, null);
    }

    out.write(template.replace("' diagram", buffer.toString()).getBytes());
  }

  /**
   * Command-line entry point. Reads JSON from standard input and writes Asciidoc to standard out.
   */
  public static void main(String[] args) throws IOException {
    try (InputStream tplFile = SpringEventRenderer.class.getResourceAsStream("template.adoc")) {
      String template = new String(Objects.requireNonNull(tplFile).readAllBytes());
      SpringEventRenderer.render(System.in, System.out, template);
    }
  }
}
