package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Visitor;

public class MethodImpl implements Method {

  private final JsonNode node;

  public MethodImpl(JsonNode node) {
    this.node = node;
  }

  @Override
  public String name() {
    return node.get("Name").textValue();
  }

  @Override
  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(node.get("DocumentationComments"));
  }

  @Override
  public void accept(Visitor v) {
    v.visitMethod(this);
  }
}
