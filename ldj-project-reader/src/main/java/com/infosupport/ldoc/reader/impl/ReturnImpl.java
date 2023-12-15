package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.ReturnStatement;
import com.infosupport.ldoc.reader.Visitor;

class ReturnImpl implements ReturnStatement {

  private ProjectImpl project;
  private JsonNode node;

  ReturnImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String expression() {
    return node.path("Expression").textValue();
  }

  @Override
  public void accept(Visitor v) {
    v.visitReturn(this);
  }
}
