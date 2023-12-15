package com.infosupport.ldoc.reader;

public interface Node {
  void accept(Visitor v);

}
