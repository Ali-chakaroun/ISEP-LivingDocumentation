package com.infosupport.ldoc.springexample.amqp;

import static com.infosupport.ldoc.springexample.util.StringOperations.stripName;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.springexample.util.StringOperations;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SpringAmqpRenderer {

  public static final String RABBIT_LISTENER_ANNOTATION = "org.springframework.amqp.rabbit."
      + "annotation.RabbitListener";
  public static final String RABBIT_TEMPLATE_CLASS = "org.springframework.amqp.rabbit.core."
      + "RabbitTemplate";
  public static final String RABBIT_SEND_TO_ANNOTATION = "org.springframework.messaging.handler."
      + "annotation.SendTo";
  public static final String RABBIT_HANDLER_ANNOTATION = "org.springframework.amqp.rabbit."
      + "annotation.RabbitHandler";

  /**
   * Jackson JsonNode helper function.
   * Check if contains attribute with specified type and return that attribute node.
   *
   * @return The attribute JsonNode with attributeType, null if does not exist
   */
  public static JsonNode getAttributeWithType(JsonNode node, String attributeType) {
    for (JsonNode att : node.path("Attributes")) {
      if (att.path("Type").asText("").equals(attributeType)) {
        return att;
      }
    }
    return null;
  }

  /**
   * Jackson JsonNode helper function. Presumes node is array type.
   *
   * @param nodes JsonNode (presumed to be of Json Array type)
   * @param key   the requested key of the JsonNode
   * @param value the requested value of the JsonNode
   * @return Json node (object) which contains value associated to key
   */
  public static JsonNode getNodeWithKeyAndValue(JsonNode nodes, String key, String value) {
    if (!nodes.isArray()) {
      return null;
    }

    for (JsonNode node : nodes) {
      if (node.path(key).asText().equals(value)) {
        return node;
      }
    }
    return null;
  }

  /**
   * Whether a node is a method node that handles the receiving of a queue message.
   * Pragmatically checks whether a presumed method node contains a @RabbitHandler annotation
   */
  public static boolean isQueueReceiveMethod(JsonNode methodNode) {
    return getAttributeWithType(methodNode, RABBIT_HANDLER_ANNOTATION) != null;
  }

  /**
   * Tries to extract a send queue interaction. This can either be from: 1. a convertAndSend(queue,
   * msg) method invocation 2. Rabbit listener with the @SendTo annotation.
   *
   * @param node      JsonNode to check
   * @param className class name that is attached to the interaction IF  such interaction is found
   * @return Extracted QueueInteraction from node. Null if not found
   */
  public static QueueInteraction extractQueueSend(JsonNode node, String className) {
    // convertAndSend statement type
    if (node.path("$type").asText("").contains("InvocationDescription")
        && node.path("Name").asText("").equals("convertAndSend")
        && node.path("ContainingType").asText("").equals(RABBIT_TEMPLATE_CLASS)) {
      // Extract the queue from the convertAndSend statement
      String queue = node.path("Arguments").path(0).path("Text").asText();
      String msgType = stripName(
          node.path("Arguments").path(1).path("Type").asText()); // Full qualified name
      return new QueueInteraction(className, QueueInteractionKind.SEND, queue, msgType,
          null); // Convert to basic name to allow cross application queue communications
    }

    // Listen method with return type
    JsonNode sendToAttribute = getAttributeWithType(node, RABBIT_SEND_TO_ANNOTATION);
    if (isQueueReceiveMethod(node) && sendToAttribute != null) {
      // So node is both a method and contains the SendTo annotation
      String queue = sendToAttribute.path("Arguments").path(0).path("Value").asText();
      String msgType = stripName(node.path("ReturnType")
          .asText()); // Convert to basic name to allow cross application queue communications
      return new QueueInteraction(className, QueueInteractionKind.SEND, queue, msgType, null);
    }
    return null;
  }

  /**
   * Recursive method to retrieve all sends to a queue from a node and all its children.
   *
   * @param node      Node to check
   * @param className class name that is attached to interactions that are found
   * @return A list of Send Queue interactions that were found
   */
  public static List<QueueInteraction> findQueueSends(JsonNode node, String className) {
    List<QueueInteraction> interactions = new ArrayList<>(0);

    QueueInteraction found = extractQueueSend(node, className);
    if (found != null) {
      interactions.add(found);
    }

    for (JsonNode child : node) {
      interactions.addAll(findQueueSends(child, className));
    }
    return interactions;
  }

  /**
   * From a JsonNode, gather all QueueInteractions.
   *
   * @param root root Node of the Json tree corresponding to a LivingDocumentation analysis Json
   *             file
   * @return a list of all top-level interactions (note that send interactions that happen in a
   *             receive-interaction are nested)
   */
  public static List<QueueInteraction> findAllQueueInteractions(JsonNode root) {
    List<QueueInteraction> interactions = new ArrayList<>();

    for (JsonNode typeDesc : root) {
      String className = typeDesc.path("FullName").textValue();

      //check if class is listener
      JsonNode rabbitListenerAnnotation = getAttributeWithType(typeDesc, RABBIT_LISTENER_ANNOTATION);
      if (rabbitListenerAnnotation != null) { // I.e., this class is a RabbitListener
        // Note that the queue is the same for all methods within a RabbitListener
        JsonNode queueArgument = getNodeWithKeyAndValue(rabbitListenerAnnotation.path("Arguments"),
            "Name", "queues");
        assert queueArgument
            != null; // TODO This should not happen, how nicely do we want to handle this?
        String queue = queueArgument.path("Value").asText();

        //Check for receive/listen methods
        for (JsonNode method : typeDesc.path("Methods")) {
          if (isQueueReceiveMethod(method)) {
            // Note the type is stripped to support messages across different applications
            String messageType = stripName(
                method.path("Parameters").path(0).path("Type").textValue());
            // Create and add receive interaction
            QueueInteraction newReceive = new QueueInteraction(className, QueueInteractionKind.RECEIVE,
                queue, messageType, new ArrayList<>(0));
            // Add all sends that happen within this receive
            newReceive.reactionToReceive().addAll(findQueueSends(method, className));

            interactions.add(newReceive);
          } else {
            // Check for non-listening methods whether messages are sent to a queue
            interactions.addAll(findQueueSends(typeDesc, className));
          }
        }
      } else {
        // Check for non-receiving/listener classes
        interactions.addAll(findQueueSends(typeDesc, className));
      }

    }
    return interactions;
  }


  /**
   * Recursive method to create all PlantUML code for all interactions.
   *
   * @param out                             writer where PlantUML is written to
   * @param allInteractions                 all interactions associated with the source
   *                                        applications
   * @param relevantSendInteractionsToReply list of Send Queue interactions that should be rendered
   *                                        for this cycle
   */
  static void renderInteractions(PrintWriter out, List<QueueInteraction> allInteractions,
      List<QueueInteraction> relevantSendInteractionsToReply) {
    // Loop over all top-level send interactions
    for (QueueInteraction sendInteraction : relevantSendInteractionsToReply) {
      // Loop over all read reactions that get triggered after the sendInteraction
      for (QueueInteraction receiveInteraction : sendInteraction.filterReceivesReactingToThis(
          allInteractions)) {
        out.printf("%s -[#ForestGreen]> %s : %s\n", sendInteraction.actor(),
            receiveInteraction.actor(),
            stripName(receiveInteraction.messageType()));

        out.printf("activate %s\n", receiveInteraction.actor());
        // render all send queue interactions that are instantiated from this receive interaction
        renderInteractions(out, allInteractions, receiveInteraction.reactionToReceive());
        out.printf("deactivate %s\n", receiveInteraction.actor());
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

    List<QueueInteraction> interactions = findAllQueueInteractions(json);
    List<String> classes = interactions.stream().map(QueueInteraction::actor).distinct().toList();

    for (String participant : classes) {
      bufferWriter.printf("participant \"%s\" as %s\n", StringOperations.humanizeName(participant),
          participant);
    }

    // Note: requires there to be at least one queue send outside a queue read context
    List<QueueInteraction> onlySendInteractions = interactions.stream()
        .filter(i -> i.kind() == QueueInteractionKind.SEND).toList();
    renderInteractions(bufferWriter, interactions, onlySendInteractions);

    out.write(template.replace("' diagram", buffer.toString()).getBytes());
  }


  public static void main(String[] args) throws IOException {
    try (InputStream tplFile = SpringAmqpRenderer.class.getResourceAsStream("template.adoc")) {
      String template = new String(Objects.requireNonNull(tplFile).readAllBytes());
      SpringAmqpRenderer.render(System.in, System.out, template);
    }
  }

}
