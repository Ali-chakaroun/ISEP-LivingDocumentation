package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Attribute;
import com.infosupport.ldoc.reader.DocumentationComment;
import com.infosupport.ldoc.reader.Event;
import com.infosupport.ldoc.reader.Visitor;
import java.util.Optional;
import java.util.stream.Stream;

class EventImpl implements Event {

  private final ProjectImpl project;
  private final JsonNode node;

  EventImpl(ProjectImpl project, JsonNode node) {
    this.project = project;
    this.node = node;
  }

  @Override
  public String name() {
    return node.path("Name").textValue();
  }

  @Override
  public String type() {
    return node.path("Type").textValue();
  }

  @Override
  public long modifiers() {
    return node.path("Modifiers").longValue();
  }

  @Override
  public String initializer() {
    return node.path("Initializer").textValue();
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
    v.visitEvent(this);
  }
}
