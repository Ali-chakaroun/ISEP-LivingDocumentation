package com.infosupport.ldoc.reader;

/**
 * General node interface.
 */
public interface Node {

  /**
   * Accept method to visit node.
   *
   * @param v Visitor
   */
  void accept(Visitor v);

}
