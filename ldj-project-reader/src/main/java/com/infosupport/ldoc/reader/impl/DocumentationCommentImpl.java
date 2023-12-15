package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Visitor;
import java.util.Map;

class DocumentationCommentImpl implements DocumentationComment {

  private final ProjectImpl project;
  private final JsonNode node;

  DocumentationCommentImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  private Map<String, String> asMap(String path) {
    return project.objectMapper().convertValue(node.path(path), new TypeReference<>() {
    });
  }

  @Override
  public String example() {
    return node.path("Example").textValue();
  }

  @Override
  public String remarks() {
    return node.path("Remarks").textValue();
  }

  @Override
  public String returns() {
    return node.path("Returns").textValue();
  }

  @Override
  public String summary() {
    return node.path("Summary").textValue();
  }

  @Override
  public String value() {
    return node.path("Value").textValue();
  }

  @Override
  public Map<String, String> exceptions() {
    return asMap("Exceptions");
  }

  @Override
  public Map<String, String> permissions() {
    return asMap("Permissions");
  }

  @Override
  public Map<String, String> seeAlsos() {
    return asMap("SeeAlsos");
  }

  @Override
  public Map<String, String> typeParams() {
    return asMap("TypeParams");
  }

  @Override
  public Map<String, String> params() {
    return asMap("Params");
  }

  @Override
  public void accept(Visitor v) {
    v.visitDocumentationComment(this);
  }
}
