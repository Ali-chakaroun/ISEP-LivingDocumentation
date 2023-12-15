package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Type;
import com.infosupport.ldoc.reader.Visitor;

class TypeImpl implements Type {

  public static Type fromNode(ProjectImpl project, JsonNode node) {
    return switch (node.path("type").asInt(0)) {
      case 0 -> new ClassImpl(project, node);
      case 1 -> new UnknownTypeImpl(); /* TODO: should be interface */
      case 2 -> new UnknownTypeImpl(); /* TODO: should be struct */
      case 3 -> new UnknownTypeImpl(); /* TODO: should be enum */
      default -> new UnknownTypeImpl();
    };
  }

  @Override
  public void accept(Visitor v) {
    v.visitType(this);
  }
}
