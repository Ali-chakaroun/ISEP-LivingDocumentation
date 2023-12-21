package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Parameter;
import com.infosupport.ldoc.reader.Statement;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class MethodImpl implements Method {

  private final ProjectImpl project;

  private final JsonNode node;

  MethodImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String name() {
    return node.path("Name").textValue();
  }

  @Override
  public long modifiers() {
    return node.path("Modifiers").longValue();
  }

  @Override
  public String returnType() {
    return node.path("ReturnType").textValue();
  }

  @Override
  public Stream<Parameter> parameters() {
    return Util.streamOf(node.path("Parameters"), ParameterImpl::new);
  }

  @Override
  public Stream<Statement> statements() {
    return Util.statements(project, node);
  }

  @Override
  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), AttributeImpl::new);
  }

  @Override
  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(project, node.path("DocumentationComments"));
  }

  @Override
  public void accept(Visitor v) {
    v.visitMethod(this);
  }
}
