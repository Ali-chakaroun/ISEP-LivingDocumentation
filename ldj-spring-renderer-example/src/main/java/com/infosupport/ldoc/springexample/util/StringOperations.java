package com.infosupport.ldoc.springexample.util;

/**
 * Class that extracts common String operations for both the Spring Event and AMQP renderer.
 */
public class StringOperations {

  /**
   *  Private constructor to hide the implicit public one.
   */
  private StringOperations() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Converts a fully-qualified class name into a plain class name.
   */
  public static String stripName(String fqdn) {
    return fqdn.replaceAll("^.*\\.", "");
  }

  /**
   * Converts a fully-qualified class to a label that seems more human, with space-separated words.
   */
  public static String humanizeName(String fqdn) {
    return String.join(" ", stripName(fqdn).split("(?<=\\p{Ll})(?=\\p{Lu})"));
  }
}
