package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosupport.ldoc.reader.Class;
import com.infosupport.ldoc.reader.Enum;
import com.infosupport.ldoc.reader.Interface;
import com.infosupport.ldoc.reader.Project;
import com.infosupport.ldoc.reader.Struct;
import com.infosupport.ldoc.reader.Type;
import com.infosupport.ldoc.reader.Visitor;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ProjectImpl implements Project {

  private final ObjectMapper objectMapper;

  private final JsonNode node;

  public ProjectImpl(ObjectMapper objectMapper, JsonNode node) {
    this.objectMapper = objectMapper;
    assert node != null;
    assert node.isArray();

    this.node = node;
  }

  public ObjectMapper objectMapper() {
    return objectMapper;
  }

  @Override
  public Stream<Type> allTypes() {
    return StreamSupport.stream(node.spliterator(), false).map(n -> switch (n.path("type").asInt(0)) {
      case 0 -> new ClassImpl(this, n);
      case 1 -> new UnknownTypeImpl(); /* TODO: should be interface */
      case 2 -> new UnknownTypeImpl(); /* TODO: should be struct */
      case 3 -> new UnknownTypeImpl(); /* TODO: should be enum */
      default -> new UnknownTypeImpl();
    });
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
