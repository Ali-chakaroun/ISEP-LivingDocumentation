package com.infosupport.ldoc.reader.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.infosupport.ldoc.reader.Struct;
import com.infosupport.ldoc.reader.Visitor;

class StructImpl extends TypeImpl implements Struct {


  StructImpl(ProjectImpl project, JsonNode node) {
    super(project, node);
  }

  @Override
  public void accept(Visitor v) {
    v.visitStruct(this);
  }
}
