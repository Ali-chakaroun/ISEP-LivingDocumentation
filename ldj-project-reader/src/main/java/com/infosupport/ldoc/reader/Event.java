package com.infosupport.ldoc.reader;

public interface Event extends HasName, HasType, HasModifiers, HasComment, HasAttributes, Node {

  String initializer();
}
