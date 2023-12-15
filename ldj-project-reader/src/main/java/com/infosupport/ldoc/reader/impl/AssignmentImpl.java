package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Assignment;
import com.infosupport.ldoc.reader.Visitor;

class AssignmentImpl implements Assignment {

  private final JsonNode node;

  AssignmentImpl(JsonNode node) {
    this.node = node;
  }

  @Override
  public String left() {
    return node.path("Left").textValue();
  }

  @Override
  public String operator() {
    return node.path("Operator").textValue();
  }

  @Override
  public String right() {
    return node.path("Right").textValue();
  }

  @Override
  public void accept(Visitor v) {
    v.visitAssignment(this);
  }
}
