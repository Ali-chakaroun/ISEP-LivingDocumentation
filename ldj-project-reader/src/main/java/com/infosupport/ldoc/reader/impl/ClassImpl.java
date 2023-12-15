package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Class;
import com.infosupport.ldoc.reader.Visitor;

class ClassImpl extends TypeImpl implements Class {

  ClassImpl(ProjectImpl project, JsonNode node) {
    super(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitClass(this);
  }
}
