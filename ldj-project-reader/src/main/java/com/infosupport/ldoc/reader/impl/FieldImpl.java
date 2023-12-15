package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Field;
import com.infosupport.ldoc.reader.Visitor;

class FieldImpl implements Field {
  private final ProjectImpl project;
  private final JsonNode node;

  FieldImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String name() {
    return node.path("Name").textValue();
  }

  @Override
  public String type() {
    return node.path("Type").textValue();
  }

  @Override
  public String initializer() {
    return node.path("Initializer").textValue();
  }

  @Override
  public void accept(Visitor v) {
    v.visitField(this);
  }
}
