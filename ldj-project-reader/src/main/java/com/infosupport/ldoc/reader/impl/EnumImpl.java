package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Enum;
import com.infosupport.ldoc.reader.Visitor;

class EnumImpl extends TypeImpl implements Enum {

  EnumImpl(ProjectImpl project, JsonNode node) {
    super(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitEnum(this);
  }
}
