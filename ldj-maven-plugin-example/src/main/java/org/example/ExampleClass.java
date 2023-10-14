package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleClass implements ExampleInterface {

  private static final Logger logger = LoggerFactory.getLogger(ExampleClass.class);

  public static void main(String[] args) {
    logger.atInfo().log("Hello, world!");
  }
}
