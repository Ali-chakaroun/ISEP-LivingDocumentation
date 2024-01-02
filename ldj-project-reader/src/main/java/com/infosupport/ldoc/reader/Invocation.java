package com.infosupport.ldoc.reader;

import java.util.Optional;
import java.util.stream.Stream;

public interface Invocation extends Statement {

  String containingType();

  String name();

  Stream<Argument> arguments();

  /** Find the Method that is invoked here, if that method was part of the analyzed project. */
  Optional<Method> getInvokedMethod();
}
