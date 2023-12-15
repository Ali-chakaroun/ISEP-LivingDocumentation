package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.Visitor;

class UnknownStatementImpl implements Statement {
  private ProjectImpl project;
  private JsonNode node;

  UnknownStatementImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public void accept(Visitor v) {
    /* Do nothing. */
  }
}
