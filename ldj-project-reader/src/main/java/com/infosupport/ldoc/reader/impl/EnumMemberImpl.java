package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Argument;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.EnumMember;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;

class EnumMemberImpl implements EnumMember {

  private ProjectImpl project;
  private JsonNode node;

  EnumMemberImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String name() {
    return node.path("Name").textValue();
  }

  @Override
  public String value() {
    return node.path("Value").textValue();
  }

  @Override
  public Stream<Argument> arguments() {
    return Util.streamOf(node.path("Arguments"), a -> new ArgumentImpl(project, a));
  }

  @Override
  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), a -> new AttributeImpl(project, a));
  }

  @Override
  public DocumentationComment documentationComment() {
    return new DocumentationCommentImpl(project, node.path("DocumentationComments"));
  }

  @Override
  public void accept(Visitor v) {
    v.visitEnumMember(this);
  }
}
