package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.Constructor;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.Visitor;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

class ConstructorImpl implements Constructor {

  private final ProjectImpl project;
  private final JsonNode node;

  ConstructorImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String name() {
    return node.path("Name").textValue();
  }

  @Override
  public Stream<Parameter> parameters() {
    return Util.streamOf(node.path("Parameters"), p -> null);
  }

  @Override
  public Stream<Statement> statements() {
    throw new UnsupportedOperationException(); /* TODO */
  }

  @Override
  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), a -> null);
  }

  @Override
  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(project, node.get("DocumentationComments"));
  }

  @Override
  public void accept(Visitor v) {
    v.visitConstructor(this);
  }
}
