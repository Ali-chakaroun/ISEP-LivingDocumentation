package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Visitor;

class MethodImpl implements Method {

  private final ProjectImpl project;

  private final JsonNode node;

  public MethodImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String name() {
    return node.get("Name").textValue();
  }

  @Override
  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(project, node.get("DocumentationComments"));
  }

  @Override
  public void accept(Visitor v) {
    v.visitMethod(this);
  }
}
