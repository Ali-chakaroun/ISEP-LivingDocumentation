package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.SwitchSection;
import com.infosupport.ldoc.reader.Visitor;
import java.util.List;
import java.util.stream.Stream;

class SwitchSectionImpl implements SwitchSection {

  private final ProjectImpl project;
  private final JsonNode node;

  SwitchSectionImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public List<String> labels() {
    return project.objectMapper().convertValue(node.path("Labels"), new TypeReference<>() {
    });
  }

  @Override
  public Stream<Statement> statements() {
    return Util.statements(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitSwitchSection(this);
  }
}
