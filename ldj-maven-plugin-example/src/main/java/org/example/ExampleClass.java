package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** ExampleClass prints a friendly greeting at INFO level. */
public class ExampleClass implements ExampleInterface {

  private static final Logger logger = LoggerFactory.getLogger(ExampleClass.class);

  /** Command-line entry point. */
  public static void main(String[] args) {
    logger.atInfo().log("Hello, world!");
  }
}
