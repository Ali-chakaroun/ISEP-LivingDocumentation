package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.Parameter;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class ParameterImpl implements Parameter {
  private ProjectImpl project;
  private JsonNode node;

  ParameterImpl(ProjectImpl project, JsonNode node) {
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
  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), a -> new AttributeImpl(project, a));
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
