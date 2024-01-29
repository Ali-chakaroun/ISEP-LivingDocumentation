package com.infosupport.ldoc.reader.impl;

import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.Visitor;

class UnknownStatementImpl implements Statement {

  @Override
  public void accept(Visitor v) {
    /* Do nothing. */
  }
}
