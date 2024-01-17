package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with ForEachStatement nodes.
 */
public interface ForEachStatement extends Statement {

  String expression();

  Stream<Statement> statements();

}
