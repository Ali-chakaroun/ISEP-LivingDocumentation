package com.infosupport.ldoc.support.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.support.DocumentationComment;
import com.infosupport.ldoc.support.Method;
import com.infosupport.ldoc.support.Visitor;

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
