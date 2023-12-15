package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.IfElseSection;
import com.infosupport.ldoc.reader.IfStatement;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class IfImpl implements IfStatement {

  private final ProjectImpl project;
  private final JsonNode node;

  IfImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public Stream<IfElseSection> sections() {
    return Util.streamOf(node.path("Sections"), s -> new IfElseSectionImpl(project, node));
  }

  @Override
  public void accept(Visitor v) {
    v.visitIf(this);
  }
}
