package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface EnumMember extends Node {

  String name();

  String value();

  Stream<Argument> arguments();

  Stream<Attribute> attributes();

  DocumentationComment documentationComment();
}
