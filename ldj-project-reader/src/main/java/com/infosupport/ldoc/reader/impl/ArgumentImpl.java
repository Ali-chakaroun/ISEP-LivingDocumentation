package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Argument;
import com.infosupport.ldoc.reader.Visitor;

class ArgumentImpl implements Argument {

  private final JsonNode node;

  ArgumentImpl(JsonNode node) {
    this.node = node;
  }

  @Override
  public String type() {
    return node.path("Type").textValue();
  }

  @Override
  public String text() {
    return node.path("Text").textValue();
  }

  @Override
  public void accept(Visitor v) {
    v.visitArgument(this);
  }
}
