package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Type;
import com.infosupport.ldoc.reader.Visitor;

class UnknownTypeImpl extends TypeImpl implements Type {

  UnknownTypeImpl(ProjectImpl project, JsonNode node) {
    super(project, node);
  }

  @Override
  public void accept(Visitor v) {
    /* Do nothing. */
  }
}
