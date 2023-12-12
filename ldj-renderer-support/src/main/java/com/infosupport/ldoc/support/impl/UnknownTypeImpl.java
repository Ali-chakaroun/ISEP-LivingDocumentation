package com.infosupport.ldoc.support.impl;

import com.infosupport.ldoc.support.Type;
import com.infosupport.ldoc.support.Visitor;

class UnknownTypeImpl implements Type {

  @Override
  public void accept(Visitor v) {
    /* Do nothing. */
  }
}
