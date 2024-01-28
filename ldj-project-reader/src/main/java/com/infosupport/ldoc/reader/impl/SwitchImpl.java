package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.SwitchSection;
import com.infosupport.ldoc.reader.SwitchStatement;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class SwitchImpl implements SwitchStatement {

  private final ProjectImpl project;
  private final JsonNode node;

  SwitchImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String expression() {
    return node.path("Expression").textValue();
  }

  @Override
  public Stream<SwitchSection> sections() {
    return Util.streamOf(node.path("Sections"), s -> new SwitchSectionImpl(project, s));
  }

  @Override
  public void accept(Visitor v) {
    v.visitSwitch(this);
  }
}
