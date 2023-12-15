package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.IfElseSection;
import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class IfElseSectionImpl implements IfElseSection {

  private final ProjectImpl project;
  private final JsonNode node;

  IfElseSectionImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String condition() {
    return node.path("Condition").textValue();
  }

  @Override
  public Stream<Statement> statements() {
    return Util.statements(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitIfElseSection(this);
  }
}
