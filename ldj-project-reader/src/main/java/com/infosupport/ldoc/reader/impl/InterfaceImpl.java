package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Interface;
import com.infosupport.ldoc.reader.Visitor;

class InterfaceImpl extends TypeImpl implements Interface {

  InterfaceImpl(ProjectImpl project, JsonNode node) {
    super(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitInterface(this);
  }
}
