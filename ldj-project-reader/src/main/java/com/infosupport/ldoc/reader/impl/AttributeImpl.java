package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.AttributeArgument;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class AttributeImpl implements Attribute {
  private final ProjectImpl project;
  private final JsonNode node;

  AttributeImpl(ProjectImpl project, JsonNode node) {
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
  public Stream<AttributeArgument> arguments() {
    return Util.streamOf(node.path("Arguments"), a -> null);
  }

  @Override
  public void accept(Visitor v) {
    v.visitAttribute(this);
  }
}
