package com.infosupport.ldoc.reader;

import java.util.List;
import java.util.stream.Stream;

/**
 * Interface for class of useful methods when dealing with SwitchSection nodes.
 */
public interface SwitchSection extends Node {

  List<String> labels();

  Stream<Statement> statements();

}
