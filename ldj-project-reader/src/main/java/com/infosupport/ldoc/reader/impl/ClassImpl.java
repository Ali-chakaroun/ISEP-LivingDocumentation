package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Class;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ClassImpl implements Class {

  private final ProjectImpl project;
  private final JsonNode node;

  public ClassImpl(ProjectImpl project, JsonNode node) {
    assert project != null;
    assert node != null;
    assert node.isObject();

    this.project = project;
    this.node = node;
  }

  @Override
  public String fullName() {
    return node.get("FullName").textValue();
  }

  @Override
  public Stream<Method> methods() {
    return StreamSupport.stream(node.path("Methods").spliterator(), false).map(m -> new MethodImpl(project, m));
  }

  @Override
  public void accept(Visitor v) {
    v.visitClass(this);
  }
}
