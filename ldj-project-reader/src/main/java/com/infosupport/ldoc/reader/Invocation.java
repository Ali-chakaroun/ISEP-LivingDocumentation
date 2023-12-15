package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Invocation extends Statement {

  String containingType();

  String name();

  Stream<Argument> arguments();

}
