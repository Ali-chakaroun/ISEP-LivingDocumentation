package com.infosupport.ldoc.reader.impl;

import com.infosupport.ldoc.reader.Type;
import com.infosupport.ldoc.reader.Visitor;

class UnknownTypeImpl implements Type {

  @Override
  public void accept(Visitor v) {
    /* Do nothing. */
  }
}
