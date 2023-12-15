package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface IfElseSection extends Node {

  String condition();

  Stream<Statement> statements();

}
