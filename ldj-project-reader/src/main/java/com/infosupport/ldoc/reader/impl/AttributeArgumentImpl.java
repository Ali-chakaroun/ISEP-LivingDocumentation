package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.AttributeArgument;
import com.infosupport.ldoc.reader.Visitor;

class AttributeArgumentImpl implements AttributeArgument {
  private ProjectImpl project;
  private JsonNode node;

  AttributeArgumentImpl(ProjectImpl project, JsonNode node) {
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
  public String value() {
    return node.path("Value").textValue();
  }

  @Override
  public void accept(Visitor v) {
    v.visitAttributeArgument(this);
  }
}
