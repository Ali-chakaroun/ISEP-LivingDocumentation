package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Argument;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.EnumMember;
import com.infosupport.ldoc.reader.Visitor;
import java.util.Optional;
import java.util.stream.Stream;

class EnumMemberImpl implements EnumMember {

  private final ProjectImpl project;
  private final JsonNode node;

  EnumMemberImpl(ProjectImpl project, JsonNode node) {
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
  public String value() {
    return node.path("Value").textValue();
  }

  @Override
  public Stream<Argument> arguments() {
    return Util.streamOf(node.path("Arguments"), ArgumentImpl::new);
  }

  @Override
  public Stream<Attribute> attributes() {
    return Util.streamOf(node.path("Attributes"), AttributeImpl::new);
  }

  @Override
  public Optional<DocumentationComment> documentationComment() {
    return Util.commentOf(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitEnumMember(this);
  }
}
