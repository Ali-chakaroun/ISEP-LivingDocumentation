package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Attribute extends Named, Typed, Node {

  Stream<AttributeArgument> arguments();

}
