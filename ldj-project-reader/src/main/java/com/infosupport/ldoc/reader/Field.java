package com.infosupport.ldoc.reader;

public interface Field extends HasName, HasType, HasModifiers, HasComment, Node {

  String initializer();
}
