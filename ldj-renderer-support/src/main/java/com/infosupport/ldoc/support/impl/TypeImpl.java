package com.infosupport.ldoc.support.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.support.Project;
import com.infosupport.ldoc.support.Type;
import com.infosupport.ldoc.support.Visitor;

class TypeImpl implements Type {

  public static Type fromNode(Project project, JsonNode node) {
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
