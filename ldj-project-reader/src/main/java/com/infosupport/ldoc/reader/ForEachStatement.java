package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface ForEachStatement extends Statement {

  String expression();

  Stream<Statement> statements();

}
