package com.infosupport.ldoc.support.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.support.DocumentationComment;

public class DocumentationCommentImpl implements DocumentationComment {
  private final JsonNode node;

  public DocumentationCommentImpl(JsonNode node) {
    this.node = node;
  }

  @Override
  public String summary() {
    return node.get("Summary").textValue();
  }
}
