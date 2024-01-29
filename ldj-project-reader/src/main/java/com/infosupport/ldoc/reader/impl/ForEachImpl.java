package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.ForEachStatement;
import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class ForEachImpl implements ForEachStatement {

  private final ProjectImpl project;
  private final JsonNode node;

  ForEachImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String expression() {
    return node.path("Expression").textValue();
  }

  @Override
  public Stream<Statement> statements() {
    return Util.statements(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitForEach(this);
  }
}
