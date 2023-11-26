package com.infosupport.ldoc.springexample.ampq;

import static com.infosupport.ldoc.springexample.util.StringOperations.stripName;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class SpringAmpqRenderer {

  public static final String RABBIT_LISTENER_ANNOTATION = "org.springframework.amqp.rabbit.annotation.RabbitListener";
  public static final String RABBIT_TEMPLATE_CLASS = "org.springframework.amqp.rabbit.core.RabbitTemplate";
  public static final String RABBIT_SEND_TO_ANNOTATION = "org.springframework.messaging.handler.annotation.SendTo";
  public static final String RABBIT_HANDLER_ANNOTATION = "org.springframework.amqp.rabbit.annotation.RabbitHandler";

  /**
   * check if contains attribute with specified type and return that attribute node
   * @param node
   * @param attributeType
   * @return
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
   * Whether a node is a class node that is a queue Listener
   * Pragmatically checks whether a presumed class node has a @RabbitListener annotation
   * @param typeDescriptionNode
   * @return
   */
  public static boolean isRabbitListener(JsonNode typeDescriptionNode) {
    return containsAttribute(typeDescriptionNode, RABBIT_LISTENER_ANNOTATION) != null;
  }

  /**
   * Whether a node is a method node that handles the reading of a queue message
   * Pragmatically checks whether a presumed method node contains a @RabbitHandler annotation
   * @param methodNode
   * @return
   */
  public static boolean isQueueReadMethod(JsonNode methodNode) {
    return containsAttribute(methodNode, RABBIT_HANDLER_ANNOTATION) != null;
  }

  /**
   * Tries to extract a post queue interaction.
   *  This can either be from:
   *    1. a convertAndSend(queue, msg) method invocation
   *    2. Rabbit listener with the @SendTo annotation
   * @param node
   * @param className
   * @return Extracted QueueInteraction from node. Null if could not find one
   */
  public static QueueInteraction extractQueuePost(JsonNode node, String className) {
    // convertAndSend statement type
    if (node.path("$type").asText("").contains("InvocationDescription")
        && node.path("Name").asText("").equals("convertAndSend")
        && node.path("ContainingType").asText("").equals(RABBIT_TEMPLATE_CLASS)) {
      String queue = node.path("Arguments").path(0).path("Text").asText();
      String msgType = stripName(node.path("Arguments").path(1).path("Text").asText()); // Full qualified name
      return new QueueInteraction(className, QueueInteractionKind.POST, queue, msgType); // Convert to basic name to allow cross application queue communications
    }

    // Listen method with return type
    JsonNode sendToAttribute = containsAttribute(node, RABBIT_SEND_TO_ANNOTATION);
    if (isQueueReadMethod(node) && sendToAttribute != null) {
      // So node is both a method and contains the SendTo annotation
      String queue = sendToAttribute.path("Arguments").path(0).path("Value").asText();
      String msgType = stripName(node.path("ReturnType").asText()); // Convert to basic name to allow cross application queue communications
      return new QueueInteraction(className, QueueInteractionKind.POST, queue, msgType);
    }
    return null;
  }

  /**
   * Recursive method to retrieve all posts to a queue from a node and all its children
   * @param node
   * @param className
   * @return
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
   * Presumes node is array type
   * @param nodes
   * @param key
   * @param value
   * @return
   */
  public static JsonNode getNodeWithKeyAndValue(JsonNode nodes, String key, String value) {
    if (!nodes.isArray()) return null;

    for (JsonNode node : nodes) {
      if(node.path(key).asText().equals(value)) {
        return node;
      }
    }
    return null;
  }

  public List<QueueInteraction> findAllQueueInteractions(JsonNode root) {
    List<QueueInteraction> interactions = new ArrayList<>();

    for (JsonNode typeDesc : root) {
      String className = typeDesc.path("Name").textValue();

      //check if class is listener
      JsonNode rabbitListenerAnnotation = containsAttribute(typeDesc, RABBIT_LISTENER_ANNOTATION);
      if (rabbitListenerAnnotation != null) { // I.e., this class is a RabbitListener
        // Note that the queue is the same for all methods within a RabbitListener
        JsonNode queueArgument = getNodeWithKeyAndValue(rabbitListenerAnnotation.path("Arguments"),"Name","queues");
        assert queueArgument != null; // TODO This should not happen, how nicely do we want to handle this?
        String queue = queueArgument.path("Value").asText();

        //Check for listen methods
        for (JsonNode method : typeDesc.path("Methods")) {
          if (isQueueReadMethod(method)) {
            // Note the type is stripped to support messages across different applications
            String messageType = stripName(method.path("Parameters").path(0).path("Type").textValue());
            // Create and add ReadInteraction
            interactions.add(new QueueInteraction(className, QueueInteractionKind.READ, queue, messageType));
          }
        }
      }

      // Check all statements whether a queue is called
      interactions.addAll(findQueuePosts(typeDesc, className));
    }
    return interactions;
  }


  public static void main(String[] args) {

  }

}
