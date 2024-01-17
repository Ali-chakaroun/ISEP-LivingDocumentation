package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with Property nodes.
 */
public interface Property extends HasName, HasType, HasModifiers, HasComment, HasAttributes, Node {

  String initializer();
}
