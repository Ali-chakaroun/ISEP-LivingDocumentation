package com.infosupport.ldoc.reader;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with Invocation statement nodes.
 */
public interface Invocation extends Statement {

  /**
   * Retrieve the class that the invoked method belongs to.
   *
   * @return String with containing type.
   */
  String containingType();

  /**
   * Retrieve the name of the invoked method.
   *
   * @return String with name.
   */
  String name();

  /**
   * Retrieve the arguments given to the invoked method.
   *
   * @return Stream of Arguments.
   */
  Stream<Argument> arguments();

  /**
   * Find and retrieve the Method that is invoked here, if that method was part of the analyzed
   * project.
   *
   * @return Optional invoked Method
   */
  Optional<Method> getInvokedMethod();
}
