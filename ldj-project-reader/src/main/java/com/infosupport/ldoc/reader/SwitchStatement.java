package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with SwitchStatement nodes.
 */
public interface SwitchStatement extends Statement {

  String expression();

  Stream<SwitchSection> sections();

}
