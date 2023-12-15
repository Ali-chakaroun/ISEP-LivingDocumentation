package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Attribute extends Node {

  String name();

  String type();

  Stream<AttributeArgument> arguments();

}
