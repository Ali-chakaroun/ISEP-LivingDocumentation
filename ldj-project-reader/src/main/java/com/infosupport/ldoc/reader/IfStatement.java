package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface IfStatement extends Statement {

  Stream<IfElseSection> sections();

}
