package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Class;
import com.infosupport.ldoc.reader.Method;
import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ClassImpl implements Class {

  private final Project project;
  private final JsonNode node;

  public ClassImpl(Project project, JsonNode node) {
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
    return StreamSupport.stream(node.path("Methods").spliterator(), false).map(MethodImpl::new);
  }

  @Override
  public void accept(Visitor v) {
    v.visitClass(this);
  }
}
