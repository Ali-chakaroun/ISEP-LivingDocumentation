package com.infosupport.ldoc.reader;

/**
 * Interface for class of useful methods when dealing with Parameter nodes.
 */
public interface Parameter extends HasName, HasType, HasAttributes, Node {

  boolean hasDefaultValue();
}
