package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.Parameter;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class ParameterImpl implements Parameter {

  private final JsonNode node;

  ParameterImpl(JsonNode node) {
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
  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), AttributeImpl::new);
  }

  @Override
  public boolean hasDefaultValue() {
    return node.path("HasDefaultValue").asBoolean();
  }

  @Override
  public void accept(Visitor v) {
    v.visitParameter(this);
  }
}
