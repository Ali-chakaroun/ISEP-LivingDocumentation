package com.infosupport.ldoc.reader;

import java.util.stream.Stream;

public interface Attribute extends HasName, HasType, Node {

  Stream<AttributeArgument> arguments();

}
