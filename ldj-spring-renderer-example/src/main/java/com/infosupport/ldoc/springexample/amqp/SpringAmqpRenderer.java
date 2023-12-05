package com.infosupport.ldoc.springexample.amqp;

import static com.infosupport.ldoc.springexample.util.StringOperations.stripName;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.springexample.util.PlantUmlBuilder;
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
   * Check if contains attribute with specified type and return that attribute node.
   *
   * @return The attribute JsonNode with attributeType, null if does not exist
   */
  public static JsonNode containsAttribute(JsonNode node, String attributeType) {
    for (JsonNode att : node.path("Attributes")) {
      if (att.path("Type").asText("").equals(attributeType)) {
        return att;
      }
    }
    return null;
  }

  /**
   * Whether a node is a method node that handles the reading of a queue message.
   * Pragmatically checks whether a presumed method node contains a @RabbitHandler annotation
   */
  public static boolean isQueueReadMethod(JsonNode methodNode) {
    return containsAttribute(methodNode, RABBIT_HANDLER_ANNOTATION) != null;
  }

  /**
   * Tries to extract a post queue interaction. This can either be from: 1. a convertAndSend(queue,
   * msg) method invocation 2. Rabbit listener with the @SendTo annotation.
   *
   * @param node      JsonNode to check
   * @param className class name that is attached to the interaction IF  such interaction is found
   * @return Extracted QueueInteraction from node. Null if not found
   */
  public static QueueInteraction extractQueuePost(JsonNode node, String className) {
    // convertAndSend statement type
    if (node.path("$type").asText("").contains("InvocationDescription")
        && node.path("Name").asText("").equals("convertAndSend")
        && node.path("ContainingType").asText("").equals(RABBIT_TEMPLATE_CLASS)) {
      String queue = node.path("Arguments").path(0).path("Text").asText();
      String msgType = stripName(
          node.path("Arguments").path(1).path("Type").asText()); // Full qualified name
      return new QueueInteraction(className, QueueInteractionKind.POST, queue, msgType,
          null); // Convert to basic name to allow cross application queue communications
    }

    // Listen method with return type
    JsonNode sendToAttribute = containsAttribute(node, RABBIT_SEND_TO_ANNOTATION);
    if (isQueueReadMethod(node) && sendToAttribute != null) {
      // So node is both a method and contains the SendTo annotation
      String queue = sendToAttribute.path("Arguments").path(0).path("Value").asText();
      String msgType = stripName(node.path("ReturnType")
          .asText()); // Convert to basic name to allow cross application queue communications
      return new QueueInteraction(className, QueueInteractionKind.POST, queue, msgType, null);
    }
    return null;
  }

  /**
   * Recursive method to retrieve all posts to a queue from a node and all its children.
   *
   * @param node      Node to check
   * @param className class name that is attached to interactions that are found
   * @return A list of Post Queue interactions that were found
   */
  public static List<QueueInteraction> findQueuePosts(JsonNode node, String className) {
    List<QueueInteraction> interactions = new ArrayList<>(0);

    QueueInteraction found = extractQueuePost(node, className);
    if (found != null) {
      interactions.add(found);
    }

    for (JsonNode child : node) {
      interactions.addAll(findQueuePosts(child, className));
    }
    return interactions;
  }

  /**
   * Presumes node is array type.
   *
   * @param nodes JsonNode (presumed to be of Json Array type
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
   * From a JsonNode, gather all QueueInteractions.
   *
   * @param root root Node of the Json tree corresponding to a LivingDocumentation analysis Json
   *             file
   * @return a list of all top-level interactions (note that post interactions that happen in a read
   *             interaction are nested)
   */
  public static List<QueueInteraction> findAllQueueInteractions(JsonNode root) {
    List<QueueInteraction> interactions = new ArrayList<>();

    for (JsonNode typeDesc : root) {
      String className = typeDesc.path("FullName").textValue();

      //check if class is listener
      JsonNode rabbitListenerAnnotation = containsAttribute(typeDesc, RABBIT_LISTENER_ANNOTATION);
      if (rabbitListenerAnnotation != null) { // I.e., this class is a RabbitListener
        // Note that the queue is the same for all methods within a RabbitListener
        JsonNode queueArgument = getNodeWithKeyAndValue(rabbitListenerAnnotation.path("Arguments"),
            "Name", "queues");
        assert queueArgument
            != null; // TODO This should not happen, how nicely do we want to handle this?
        String queue = queueArgument.path("Value").asText();

        //Check for listen methods
        for (JsonNode method : typeDesc.path("Methods")) {
          if (isQueueReadMethod(method)) {
            // Note the type is stripped to support messages across different applications
            String messageType = stripName(
                method.path("Parameters").path(0).path("Type").textValue());
            // Create and add read interaction
            QueueInteraction newRead = new QueueInteraction(className, QueueInteractionKind.READ,
                queue, messageType, new ArrayList<>(0));
            // Add all posts that happen within this read
            newRead.reactionToRead().addAll(findQueuePosts(method, className));

            interactions.add(newRead);
          } else {
            // Check for non-listening methods whether posts are made
            interactions.addAll(findQueuePosts(typeDesc, className));
          }
        }
      } else {
        // Check for non-listener classes
        interactions.addAll(findQueuePosts(typeDesc, className));
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
   * @param relevantPostInteractionsToReply list of Post Queue interactions that should be rendered
   *                                        for this cycle
   */
  static void renderInteractions(PrintWriter out, List<QueueInteraction> allInteractions,
      List<QueueInteraction> relevantPostInteractionsToReply) {
    // Loop over all top-level post interactions
    for (QueueInteraction postInteraction : relevantPostInteractionsToReply) {
      // Loop over all read reactions that get triggered after the postInteraction
      for (QueueInteraction readInteraction : postInteraction.filterReadsReactingToThis(
          allInteractions)) {
        PlantUmlBuilder.renderInteraction(out, postInteraction.actor(), readInteraction.actor(),
            readInteraction.messageType());

        out.printf("activate %s\n", readInteraction.actor());
        // render all post queue interactions that are instantiated from this read interaction
        renderInteractions(out, allInteractions, readInteraction.reactionToRead());
        out.printf("deactivate %s\n", readInteraction.actor());
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
      PlantUmlBuilder.renderParticipant(bufferWriter, participant);
    }

    // Note: requires there to be at least one queue post outside a queue read context
    renderInteractions(bufferWriter, interactions, QueueInteraction.getAllPosts(interactions));

    out.write(template.replace("' diagram", buffer.toString()).getBytes());
  }


  public static void main(String[] args) throws IOException {
    try (InputStream tplFile = SpringAmqpRenderer.class.getResourceAsStream("template.adoc")) {
      String template = new String(Objects.requireNonNull(tplFile).readAllBytes());
      SpringAmqpRenderer.render(System.in, System.out, template);
    }
  }

}
