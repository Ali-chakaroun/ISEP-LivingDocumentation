package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface IfElseSection {

  String condition();

  Stream<Statement> statements();

}
