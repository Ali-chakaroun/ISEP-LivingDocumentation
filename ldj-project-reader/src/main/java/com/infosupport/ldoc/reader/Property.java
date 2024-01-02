package com.infosupport.ldoc.reader;

public interface Property extends HasName, HasType, HasModifiers, HasComment, HasAttributes, Node {

  String initializer();
}
