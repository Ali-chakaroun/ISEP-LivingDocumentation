package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface EnumMember extends Named, Node {

  String value();

  Stream<Argument> arguments();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();
}
