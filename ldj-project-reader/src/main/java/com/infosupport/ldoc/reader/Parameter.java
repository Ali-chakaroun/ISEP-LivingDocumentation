package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Parameter extends Node {

  String name();

  String type();

  Stream<Attribute> attributes();

  boolean hasDefaultValue();
}
