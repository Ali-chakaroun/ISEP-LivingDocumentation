package com.infosupport.ldoc.support.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.support.Class;
import com.infosupport.ldoc.support.Enum;
import com.infosupport.ldoc.support.Interface;
import com.infosupport.ldoc.support.Project;
import com.infosupport.ldoc.support.Struct;
import com.infosupport.ldoc.support.Type;
import com.infosupport.ldoc.support.Visitor;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ProjectImpl implements Project {

  private final JsonNode node;

  public ProjectImpl(JsonNode node) {
    assert node != null;
    assert node.isArray();

    this.node = node;
  }

  @Override
  public Stream<Type> allTypes() {
    return StreamSupport.stream(node.spliterator(), false).map(n -> TypeImpl.fromNode(this, n));
  }

  private <T> Stream<T> allOfType(java.lang.Class<T> type) {
    return allTypes().filter(type::isInstance).map(type::cast);
  }

  @Override
  public Stream<Class> classes() {
    return allOfType(Class.class);
  }

  @Override
  public Stream<Interface> interfaces() {
    return allOfType(Interface.class);
  }

  @Override
  public Stream<Enum> enums() {
    return allOfType(Enum.class);
  }

  @Override
  public Stream<Struct> structs() {
    return allOfType(Struct.class);
  }

  @Override
  public void accept(Visitor v) {
    v.visitProject(this);
  }
}
